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

package com.paulrybitskyi.gamedge.data.games.likes

import com.paulrybitskyi.gamedge.commons.testing.TOGGLE_GAME_LIKE_STATE_USE_CASE_PARAMS
import com.paulrybitskyi.gamedge.data.commons.Pagination
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.entities.Game
import com.paulrybitskyi.gamedge.data.games.usecases.likes.ToggleGameLikeStateUseCaseImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

private const val GAME_ID = 100

private val USE_CASE_PARAMS = TOGGLE_GAME_LIKE_STATE_USE_CASE_PARAMS.copy(gameId = GAME_ID)

internal class ToggleGameLikeStateUseCaseImplTest {

    private lateinit var likedGamesLocalDataStore: FakeLikedGamesLocalDataStore
    private lateinit var SUT: ToggleGameLikeStateUseCaseImpl

    @Before
    fun setup() {
        likedGamesLocalDataStore = FakeLikedGamesLocalDataStore()
        SUT = ToggleGameLikeStateUseCaseImpl(likedGamesLocalDataStore)
    }

    @Test
    fun `Toggles game from unliked to liked state`() {
        runBlockingTest {
            assertThat(likedGamesLocalDataStore.isGameLiked(GAME_ID)).isFalse

            SUT.execute(USE_CASE_PARAMS)

            assertThat(likedGamesLocalDataStore.isGameLiked(GAME_ID)).isTrue
        }
    }

    @Test
    fun `Toggles game from liked to unliked state`() {
        runBlockingTest {
            likedGamesLocalDataStore.likeGame(GAME_ID)

            assertThat(likedGamesLocalDataStore.isGameLiked(GAME_ID)).isTrue

            SUT.execute(USE_CASE_PARAMS)

            assertThat(likedGamesLocalDataStore.isGameLiked(GAME_ID)).isFalse
        }
    }

    private class FakeLikedGamesLocalDataStore : LikedGamesLocalDataStore {

        private val likedGameIds = mutableSetOf<Int>()

        override suspend fun likeGame(gameId: Int) {
            likedGameIds.add(gameId)
        }

        override suspend fun unlikeGame(gameId: Int) {
            likedGameIds.remove(gameId)
        }

        override suspend fun isGameLiked(gameId: Int): Boolean {
            return likedGameIds.contains(gameId)
        }

        override suspend fun observeGameLikeState(gameId: Int): Flow<Boolean> {
            return flowOf() // no-op
        }

        override suspend fun observeLikedGames(pagination: Pagination): Flow<List<Game>> {
            return flowOf() // no-op
        }
    }
}
