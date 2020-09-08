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

package com.paulrybitskyi.gamedge.data.usecases.observers

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.data.usecases.mapper.EntityMapper
import com.paulrybitskyi.gamedge.data.usecases.mapper.mapToDomainGames
import com.paulrybitskyi.gamedge.domain.usecases.games.commons.GamesObserverParams
import com.paulrybitskyi.gamedge.domain.usecases.games.commons.GamesRefresherParams
import com.paulrybitskyi.gamedge.domain.usecases.games.observers.ObserveMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.games.refreshers.RefreshMostAnticipatedGamesUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class ObserveMostAnticipatedGamesUseCaseImpl(
    private val refreshGamesUseCase: RefreshMostAnticipatedGamesUseCase,
    private val gamesDatabaseDataStore: GamesDatabaseDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val entityMapper: EntityMapper
) : ObserveMostAnticipatedGamesUseCase {


    override suspend fun execute(params: GamesObserverParams) = withContext(dispatcherProvider.io) {
        val pagination = params.pagination

        if(params.refresh) {
            refreshGamesUseCase.execute(GamesRefresherParams(pagination))
        }

        gamesDatabaseDataStore
            .observeMostAnticipatedGames(pagination.offset, pagination.limit)
            .map(entityMapper::mapToDomainGames)
    }


}