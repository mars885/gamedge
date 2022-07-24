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

package com.paulrybitskyi.gamedge.feature.info.domain.likes

import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.common.domain.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.common.testing.domain.MainCoroutineRule
import com.paulrybitskyi.gamedge.feature.info.TOGGLE_GAME_LIKE_STATE_USE_CASE_PARAMS
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.likes.ToggleGameLikeStateUseCaseImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val GAME_ID = 100

private val USE_CASE_PARAMS = TOGGLE_GAME_LIKE_STATE_USE_CASE_PARAMS.copy(gameId = GAME_ID)

internal class ToggleGameLikeStateUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var likedGamesLocalDataStore: FakeLikedGamesLocalDataStore
    private lateinit var SUT: ToggleGameLikeStateUseCaseImpl

    @Before
    fun setup() {
        likedGamesLocalDataStore = FakeLikedGamesLocalDataStore()
        SUT = ToggleGameLikeStateUseCaseImpl(likedGamesLocalDataStore)
    }

    @Test
    fun `Toggles game from unliked to liked state`() {
        runTest {
            assertThat(likedGamesLocalDataStore.isGameLiked(GAME_ID)).isFalse()

            SUT.execute(USE_CASE_PARAMS)

            assertThat(likedGamesLocalDataStore.isGameLiked(GAME_ID)).isTrue()
        }
    }

    @Test
    fun `Toggles game from liked to unliked state`() {
        runTest {
            likedGamesLocalDataStore.likeGame(GAME_ID)

            assertThat(likedGamesLocalDataStore.isGameLiked(GAME_ID)).isTrue()

            SUT.execute(USE_CASE_PARAMS)

            assertThat(likedGamesLocalDataStore.isGameLiked(GAME_ID)).isFalse()
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

        override fun observeGameLikeState(gameId: Int): Flow<Boolean> {
            return flowOf() // no-op
        }

        override fun observeLikedGames(pagination: Pagination): Flow<List<Game>> {
            return flowOf() // no-op
        }
    }
}
