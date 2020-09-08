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

package com.paulrybitskyi.gamedge.data.usecases.refreshers

import com.paulrybitskyi.gamedge.core.extensions.onSuccess
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.datastores.GamesRemoteDataStore
import com.paulrybitskyi.gamedge.domain.usecases.refreshers.RefreshComingSoonGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.refreshers.RefreshComingSoonGamesUseCase.Params
import kotlinx.coroutines.withContext

internal class RefreshComingSoonGamesUseCaseImpl(
    private val gamesRemoteDataStore: GamesRemoteDataStore,
    private val gamesLocalDataStore: GamesLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val networkStateProvider: NetworkStateProvider
) : RefreshComingSoonGamesUseCase {


    override suspend fun execute(params: Params) {
        if(!networkStateProvider.isNetworkAvailable) return

        withContext(dispatcherProvider.io) {
            val pagination = params.pagination

            gamesRemoteDataStore
                .getComingSoonGames(pagination.offset, pagination.limit)
                .onSuccess(gamesLocalDataStore::saveGames)
        }
    }


}