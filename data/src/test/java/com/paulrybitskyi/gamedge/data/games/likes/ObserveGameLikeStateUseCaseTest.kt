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

import com.paulrybitskyi.gamedge.data.commons.Pagination
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.entities.Game
import com.paulrybitskyi.gamedge.data.games.usecases.likes.ObserveGameLikeStateUseCaseImpl
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveGameLikeStateUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val USE_CASE_PARAMS = ObserveGameLikeStateUseCase.Params(gameId = 10)


internal class ObserveGameLikeStateUseCaseTest {


    private lateinit var likedGamesLocalDataStore: FakeLikedGamesLocalDataStore
    private lateinit var SUT: ObserveGameLikeStateUseCaseImpl


    @Before
    fun setup() {
        likedGamesLocalDataStore = FakeLikedGamesLocalDataStore()
        SUT = ObserveGameLikeStateUseCaseImpl(likedGamesLocalDataStore)
    }


    @Test
    fun `Emits game like state successfully`() = runBlockingTest {
        likedGamesLocalDataStore.gameLikeState = true
        assertTrue(SUT.execute(USE_CASE_PARAMS).first())

        likedGamesLocalDataStore.gameLikeState = false
        assertFalse(SUT.execute(USE_CASE_PARAMS).first())
    }


    private class FakeLikedGamesLocalDataStore : LikedGamesLocalDataStore {

        var gameLikeState = false

        override suspend fun likeGame(gameId: Int) {
            // no-op
        }

        override suspend fun unlikeGame(gameId: Int) {
            // no-op
        }

        override suspend fun isGamedLiked(gameId: Int): Boolean {
            return false // no-op
        }

        override suspend fun observeGameLikeState(gameId: Int): Flow<Boolean> {
            return flowOf(gameLikeState)
        }

        override suspend fun observeLikedGames(pagination: Pagination): Flow<List<Game>> {
            return flowOf() // no-op
        }

    }


}