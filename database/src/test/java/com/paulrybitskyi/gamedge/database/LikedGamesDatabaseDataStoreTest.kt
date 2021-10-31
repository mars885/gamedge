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

package com.paulrybitskyi.gamedge.database

import app.cash.turbine.test
import com.paulrybitskyi.gamedge.commons.testing.DATA_GAMES
import com.paulrybitskyi.gamedge.commons.testing.DATA_PAGINATION
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.database.games.DatabaseGame
import com.paulrybitskyi.gamedge.database.games.datastores.GameMapper
import com.paulrybitskyi.gamedge.database.games.datastores.LikedGameFactory
import com.paulrybitskyi.gamedge.database.games.datastores.LikedGamesDatabaseDataStore
import com.paulrybitskyi.gamedge.database.games.datastores.mapToDatabaseGames
import com.paulrybitskyi.gamedge.database.games.entities.LikedGame
import com.paulrybitskyi.gamedge.database.games.tables.LikedGamesTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

private const val GAME_ID = 100
private const val ANOTHER_GAME_ID = 110

internal class LikedGamesDatabaseDataStoreTest {

    private lateinit var likedGamesTable: FakeLikedGamesTable
    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: LikedGamesDatabaseDataStore

    @Before
    fun setup() {
        likedGamesTable = FakeLikedGamesTable()
        gameMapper = GameMapper()
        SUT = LikedGamesDatabaseDataStore(
            likedGamesTable = likedGamesTable,
            likedGameFactory = FakeLikedGameFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = gameMapper
        )
    }

    @Test
    fun `Likes game successfully`() {
        runBlockingTest {
            SUT.likeGame(GAME_ID)

            assertThat(SUT.isGameLiked(GAME_ID)).isTrue
        }
    }

    @Test
    fun `Unlikes game successfully`() {
        runBlockingTest {
            SUT.likeGame(GAME_ID)
            SUT.unlikeGame(GAME_ID)

            assertThat(SUT.isGameLiked(GAME_ID)).isFalse
        }
    }

    @Test
    fun `Validates that unliked game is unliked`() {
        runBlockingTest {
            assertThat(SUT.isGameLiked(gameId = ANOTHER_GAME_ID)).isFalse
        }
    }

    @Test
    fun `Observes game like state successfully`() {
        runBlockingTest {
            SUT.likeGame(GAME_ID)

            SUT.observeGameLikeState(GAME_ID).test {
                assertThat(awaitItem()).isTrue
                awaitComplete()
            }

            SUT.observeGameLikeState(ANOTHER_GAME_ID).test {
                assertThat(awaitItem()).isFalse
                awaitComplete()
            }
        }
    }

    @Test
    fun `Observes liked games successfully`() {
        runBlockingTest {
            val dbGames = gameMapper.mapToDatabaseGames(DATA_GAMES)

            likedGamesTable.dbGamesToObserve = dbGames

            SUT.observeLikedGames(DATA_PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DATA_GAMES)
                awaitComplete()
            }
        }
    }

    private class FakeLikedGamesTable : LikedGamesTable {

        var likedGamesMap = mutableMapOf<Int, LikedGame>()
        var dbGamesToObserve = listOf<DatabaseGame>()

        override suspend fun saveLikedGame(likedGame: LikedGame) {
            likedGamesMap[likedGame.gameId] = likedGame
        }

        override suspend fun deleteLikedGame(gameId: Int) {
            likedGamesMap.remove(gameId)
        }

        override suspend fun isGameLiked(gameId: Int): Boolean {
            return likedGamesMap.containsKey(gameId)
        }

        override fun observeGameLikeState(gameId: Int): Flow<Boolean> {
            return flowOf(likedGamesMap.contains(gameId))
        }

        override fun observeLikedGames(offset: Int, limit: Int): Flow<List<DatabaseGame>> {
            return flowOf(dbGamesToObserve)
        }
    }

    private class FakeLikedGameFactory : LikedGameFactory {

        override fun createLikedGame(gameId: Int): LikedGame {
            return LikedGame(
                id = 1,
                gameId = gameId,
                likeTimestamp = 500L
            )
        }
    }
}
