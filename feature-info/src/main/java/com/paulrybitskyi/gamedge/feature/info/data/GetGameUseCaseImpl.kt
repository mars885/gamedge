/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.feature.info.data

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.common.data.games.datastores.local.GamesLocalDataStore
import com.paulrybitskyi.gamedge.common.data.games.usecases.common.GameMapper
import com.paulrybitskyi.gamedge.common.domain.common.DomainResult
import com.paulrybitskyi.gamedge.common.domain.common.entities.Error
import com.paulrybitskyi.gamedge.common.domain.games.DomainGame
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameUseCase
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty

@Singleton
@BindType
internal class GetGameUseCaseImpl @Inject constructor(
    private val gamesLocalDataStore: GamesLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val gameMapper: GameMapper
) : GetGameUseCase {

    override suspend fun execute(params: GetGameUseCase.Params): Flow<DomainResult<Game>> {
        return flow { gamesLocalDataStore.getGame(params.gameId)?.let { emit(it) } }
            .flowOn(dispatcherProvider.main)
            .map(gameMapper::mapToDomainGame)
            .flowOn(dispatcherProvider.computation)
            .map<DomainGame, DomainResult<Game>>(::Ok)
            .onEmpty { emit(Err(Error.NotFound("Could not find the game with ID = ${params.gameId}"))) }
    }
}
