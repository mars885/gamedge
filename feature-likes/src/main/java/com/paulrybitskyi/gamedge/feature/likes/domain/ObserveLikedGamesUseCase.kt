/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.feature.likes.domain

import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.games.ObservableGamesUseCase
import com.paulrybitskyi.gamedge.common.domain.games.common.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.common.domain.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

internal interface ObserveLikedGamesUseCase : ObservableGamesUseCase

@Singleton
@BindType
internal class ObserveLikedGamesUseCaseImpl @Inject constructor(
    private val likedGamesLocalDataStore: LikedGamesLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
) : ObserveLikedGamesUseCase {

    override fun execute(params: ObserveGamesUseCaseParams): Flow<List<Game>> {
        return likedGamesLocalDataStore.observeLikedGames(params.pagination)
            .flowOn(dispatcherProvider.main)
    }
}
