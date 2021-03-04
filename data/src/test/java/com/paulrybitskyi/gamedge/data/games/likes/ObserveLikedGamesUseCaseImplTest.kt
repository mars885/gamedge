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

import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.data.games.usecases.likes.ObserveLikedGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.utils.DATA_GAMES
import com.paulrybitskyi.gamedge.data.games.utils.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test


private val USE_CASE_PARAMS = ObserveGamesUseCaseParams()


internal class ObserveLikedGamesUseCaseImplTest {


    private lateinit var likedGamesLocalDataStore: FakeLikedGamesLocalDataStore
    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: ObserveLikedGamesUseCaseImpl


    @Before
    fun setup() {
        likedGamesLocalDataStore = FakeLikedGamesLocalDataStore()
        gameMapper = GameMapper()
        SUT = ObserveLikedGamesUseCaseImpl(
            likedGamesLocalDataStore = likedGamesLocalDataStore,
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = gameMapper
        )
    }


    @Test
    fun `Emits liked games successfully`() {
        runBlockingTest {
            assertThat(SUT.execute(USE_CASE_PARAMS).first())
                .isEqualTo(gameMapper.mapToDomainGames(DATA_GAMES))
        }
    }


    private class FakeLikedGamesLocalDataStore : LikedGamesLocalDataStore {

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
            return flowOf() // no-op
        }

        override suspend fun observeLikedGames(pagination: DataPagination): Flow<List<DataGame>> {
            return flowOf(DATA_GAMES)
        }

    }


}