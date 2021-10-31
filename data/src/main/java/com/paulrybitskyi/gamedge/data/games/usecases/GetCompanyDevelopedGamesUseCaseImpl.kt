/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.gamedge.data.games.usecases

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.data.commons.utils.toDataPagination
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.usecases.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.GetCompanyDevelopedGamesUseCase.Params
import com.paulrybitskyi.gamedge.domain.games.usecases.RefreshCompanyDevelopedGamesUseCase
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty

@Singleton
@BindType
internal class GetCompanyDevelopedGamesUseCaseImpl @Inject constructor(
    private val refreshCompanyDevelopedGamesUseCase: RefreshCompanyDevelopedGamesUseCase,
    private val gamesLocalDataStore: GamesLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val gameMapper: GameMapper
) : GetCompanyDevelopedGamesUseCase {

    override suspend fun execute(params: Params): Flow<List<Game>> {
        return refreshCompanyDevelopedGamesUseCase
            .execute(RefreshCompanyDevelopedGamesUseCase.Params(params.company, params.pagination))
            .resultOrError()
            .onEmpty {
                val localCompanyDevelopedGamesFlow = flow {
                    val dataCompany = gameMapper.mapToDataCompany(params.company)
                    val dataPagination = params.pagination.toDataPagination()

                    emit(gamesLocalDataStore.getCompanyDevelopedGames(dataCompany, dataPagination))
                }
                .flowOn(dispatcherProvider.main)
                .map(gameMapper::mapToDomainGames)
                .flowOn(dispatcherProvider.computation)

                emitAll(localCompanyDevelopedGamesFlow)
            }
    }
}
