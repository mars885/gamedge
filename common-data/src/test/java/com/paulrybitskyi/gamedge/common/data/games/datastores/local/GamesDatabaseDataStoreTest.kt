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

package com.paulrybitskyi.gamedge.common.data.games.datastores.local

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.DATA_GAME
import com.paulrybitskyi.gamedge.common.testing.DATA_GAMES
import com.paulrybitskyi.gamedge.common.testing.DATA_PAGINATION
import com.paulrybitskyi.gamedge.common.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.common.data.DATA_COMPANY
import com.paulrybitskyi.gamedge.common.data.FakeDiscoveryGamesReleaseDatesProvider
import com.paulrybitskyi.gamedge.common.data.games.datastores.local.database.DbGameMapper
import com.paulrybitskyi.gamedge.common.data.games.datastores.local.database.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.common.data.games.datastores.local.database.mapToDatabaseGames
import com.paulrybitskyi.gamedge.database.games.tables.GamesTable
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class GamesDatabaseDataStoreTest {

    @MockK private lateinit var gamesTable: GamesTable

    private lateinit var dbGameMapper: DbGameMapper
    private lateinit var SUT: GamesDatabaseDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        dbGameMapper = DbGameMapper()
        SUT = GamesDatabaseDataStore(
            gamesTable = gamesTable,
            dispatcherProvider = FakeDispatcherProvider(),
            discoveryGamesReleaseDatesProvider = FakeDiscoveryGamesReleaseDatesProvider(),
            dbGameMapper = dbGameMapper,
        )
    }

    @Test
    fun `Saves games to table successfully`() {
        runTest {
            SUT.saveGames(DATA_GAMES)

            coVerify { gamesTable.saveGames(dbGameMapper.mapToDatabaseGames(DATA_GAMES)) }
        }
    }

    @Test
    fun `Retrieves game successfully`() {
        runTest {
            val dbGame = dbGameMapper.mapToDatabaseGame(DATA_GAME)

            coEvery { gamesTable.getGame(any()) } returns dbGame

            assertThat(SUT.getGame(DATA_GAME.id)).isEqualTo(DATA_GAME)
        }
    }

    @Test
    fun `Retrieves null instead of game`() {
        runTest {
            val gameId = DATA_GAME.id

            coEvery { gamesTable.getGame(gameId) } returns null

            assertThat(SUT.getGame(gameId)).isNull()
        }
    }

    @Test
    fun `Retrieves company developed games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.getGames(any(), any(), any()) } returns dbGames

            assertThat(SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION))
                .isEqualTo(DATA_GAMES)
        }
    }

    @Test
    fun `Retrieves similar games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.getGames(any(), any(), any()) } returns dbGames

            assertThat(SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION))
                .isEqualTo(DATA_GAMES)
        }
    }

    @Test
    fun `Searches games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DATA_GAMES)

            coEvery { gamesTable.searchGames(any(), any(), any()) } returns dbGames

            assertThat(SUT.searchGames("", DATA_PAGINATION)).isEqualTo(DATA_GAMES)
        }
    }

    @Test
    fun `Observes popular games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DATA_GAMES)

            every { gamesTable.observePopularGames(any(), any(), any()) } returns flowOf(dbGames)

            SUT.observePopularGames(DATA_PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DATA_GAMES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Observes recently released games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DATA_GAMES)

            every { gamesTable.observeRecentlyReleasedGames(any(), any(), any(), any()) } returns flowOf(dbGames)

            SUT.observeRecentlyReleasedGames(DATA_PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DATA_GAMES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Observes coming soon games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DATA_GAMES)

            every { gamesTable.observeComingSoonGames(any(), any(), any()) } returns flowOf(dbGames)

            SUT.observeComingSoonGames(DATA_PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DATA_GAMES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Observes most anticipated games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DATA_GAMES)

            every { gamesTable.observeMostAnticipatedGames(any(), any(), any()) } returns flowOf(dbGames)

            SUT.observeMostAnticipatedGames(DATA_PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DATA_GAMES)
                awaitComplete()
            }
        }
    }
}
