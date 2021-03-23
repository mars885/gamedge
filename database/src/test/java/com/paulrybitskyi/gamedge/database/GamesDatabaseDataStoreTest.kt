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
import com.paulrybitskyi.gamedge.commons.data.QueryTimestampProvider
import com.paulrybitskyi.gamedge.commons.testing.*
import com.paulrybitskyi.gamedge.database.games.datastores.GameMapper
import com.paulrybitskyi.gamedge.database.games.datastores.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.database.games.datastores.mapToDatabaseGames
import com.paulrybitskyi.gamedge.database.games.tables.GamesTable
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class GamesDatabaseDataStoreTest {


    @MockK private lateinit var gamesTable: GamesTable

    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: GamesDatabaseDataStore


    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        gameMapper = GameMapper()
        SUT = GamesDatabaseDataStore(
            gamesTable = gamesTable,
            dispatcherProvider = FakeDispatcherProvider(),
            queryTimestampProvider = FakeQueryTimestampProvider(),
            gameMapper = gameMapper
        )
    }


    @Test
    fun `Saves games to table successfully`() {
        runBlockingTest {
            SUT.saveGames(DATA_GAMES)

            coVerify { gamesTable.saveGames(gameMapper.mapToDatabaseGames(DATA_GAMES)) }
        }
    }


    @Test
    fun `Retrieves game successfully`() {
        runBlockingTest {
            val dbGame = gameMapper.mapToDatabaseGame(DATA_GAME)

            coEvery { gamesTable.getGame(any()) } returns dbGame

            assertThat(SUT.getGame(DATA_GAME.id)).isEqualTo(DATA_GAME)
        }
    }


    @Test
    fun `Retrieves null instead of game`() {
        runBlockingTest {
            val gameId = DATA_GAME.id

            coEvery { gamesTable.getGame(gameId) } returns null

            assertThat(SUT.getGame(gameId)).isNull()
        }
    }


    @Test
    fun `Retrieves company developed games successfully`() {
        runBlockingTest {
            val dbGames = gameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.getGames(any(), any(), any()) } returns dbGames

            assertThat(SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION))
                .isEqualTo(DATA_GAMES)
        }
    }


    @Test
    fun `Retrieves similar games successfully`() {
        runBlockingTest {
            val dbGames = gameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.getGames(any(), any(), any()) } returns dbGames

            assertThat(SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION))
                .isEqualTo(DATA_GAMES)
        }
    }


    @Test
    fun `Searches games successfully`() {
        runBlockingTest {
            val dbGames = gameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.searchGames(any(), any(), any()) } returns dbGames

            assertThat(SUT.searchGames("", DATA_PAGINATION)).isEqualTo(DATA_GAMES)
        }
    }


    @Test
    fun `Observes popular games successfully`() {
        runBlockingTest {
            val dbGames = gameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.observePopularGames(any(), any(), any()) } returns flowOf(dbGames)

            SUT.observePopularGames(DATA_PAGINATION).test {
                assertThat(expectItem()).isEqualTo(DATA_GAMES)
                expectComplete()
            }
        }
    }


    @Test
    fun `Observes recently released games successfully`() {
        runBlockingTest {
            val dbGames = gameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.observeRecentlyReleasedGames(any(), any(), any(), any()) } returns flowOf(dbGames)

            SUT.observeRecentlyReleasedGames(DATA_PAGINATION).test {
                assertThat(expectItem()).isEqualTo(DATA_GAMES)
                expectComplete()
            }
        }
    }


    @Test
    fun `Observes coming soon games successfully`() {
        runBlockingTest {
            val dbGames = gameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.observeComingSoonGames(any(), any(), any()) } returns flowOf(dbGames)

            SUT.observeComingSoonGames(DATA_PAGINATION).test {
                assertThat(expectItem()).isEqualTo(DATA_GAMES)
                expectComplete()
            }
        }
    }


    @Test
    fun `Observes most anticipated games successfully`() {
        runBlockingTest {
            val dbGames = gameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.observeMostAnticipatedGames(any(), any(), any()) } returns flowOf(dbGames)

            SUT.observeMostAnticipatedGames(DATA_PAGINATION).test {
                assertThat(expectItem()).isEqualTo(DATA_GAMES)
                expectComplete()
            }
        }
    }


    private class FakeQueryTimestampProvider : QueryTimestampProvider {

        override fun getPopularGamesMinReleaseDate(): Long = 500L
        override fun getRecentlyReleasedGamesMinReleaseDate(): Long = 500L
        override fun getRecentlyReleasedGamesMaxReleaseDate(): Long = 500L
        override fun getComingSoonGamesMinReleaseDate(): Long = 500L
        override fun getMostAnticipatedGamesMinReleaseDate(): Long = 500L

    }


}