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

package com.paulrybitskyi.gamedge.data.games.usecases.refreshers

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.mapResult
import com.paulrybitskyi.gamedge.data.commons.utils.onEachSuccess
import com.paulrybitskyi.gamedge.data.games.datastores.commons.GamesDataStores
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.data.games.usecases.refreshers.commons.GamesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.data.games.usecases.refreshers.commons.RefreshGamesUseCaseMappers
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshCompanyDevelopedGamesUseCase.Params
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal class RefreshCompanyDevelopedGamesUseCaseImpl(
    private val gamesDataStores: GamesDataStores,
    private val dispatcherProvider: DispatcherProvider,
    private val throttlerTools: GamesRefreshingThrottlerTools,
    private val mappers: RefreshGamesUseCaseMappers
) : RefreshCompanyDevelopedGamesUseCase {


    override suspend fun execute(params: Params): Flow<DomainResult<List<Game>>> {
        val throttlerKey = withContext(dispatcherProvider.computation) {
            throttlerTools.keyBuilder.buildCompanyDevelopedGamesKey(
                params.company,
                params.pagination
            )
        }

        return flow {
            if(throttlerTools.throttler.canRefreshCompanyDevelopedGames(throttlerKey)) {
                val dataCompany = mappers.game.mapToDataCompany(params.company)
                val dataPagination = mappers.pagination.mapToDataPagination(params.pagination)

                emit(gamesDataStores.remote.getCompanyDevelopedGames(dataCompany, dataPagination))
            }
        }
        .onEachSuccess {
            gamesDataStores.local.saveGames(it)
            throttlerTools.throttler.updateGamesLastRefreshTime(throttlerKey)
        }
        .flowOn(dispatcherProvider.main)
        .mapResult(mappers.game::mapToDomainGames, mappers.error::mapToDomainError)
        .flowOn(dispatcherProvider.computation)
    }


}