/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.common.data.games.database

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.data.DOMAIN_COMPANY
import com.paulrybitskyi.gamedge.common.data.FakeDiscoveryGamesReleaseDatesProvider
import com.paulrybitskyi.gamedge.common.data.games.datastores.database.DbGameMapper
import com.paulrybitskyi.gamedge.common.data.games.datastores.database.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.common.data.games.datastores.database.mapToDatabaseGames
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAME
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.common.testing.domain.MainCoroutineRule
import com.paulrybitskyi.gamedge.common.testing.domain.PAGINATION
import com.paulrybitskyi.gamedge.database.games.tables.GamesTable
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class GamesDatabaseDataStoreTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK private lateinit var gamesTable: GamesTable

    private lateinit var dbGameMapper: DbGameMapper
    private lateinit var SUT: GamesDatabaseDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        dbGameMapper = DbGameMapper()
        SUT = GamesDatabaseDataStore(
            gamesTable = gamesTable,
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
            discoveryGamesReleaseDatesProvider = FakeDiscoveryGamesReleaseDatesProvider(),
            dbGameMapper = dbGameMapper,
        )
    }

    @Test
    fun `Saves games to table successfully`() {
        runTest {
            SUT.saveGames(DOMAIN_GAMES)

            coVerify { gamesTable.saveGames(dbGameMapper.mapToDatabaseGames(DOMAIN_GAMES)) }
        }
    }

    @Test
    fun `Retrieves game successfully`() {
        runTest {
            val dbGame = dbGameMapper.mapToDatabaseGame(DOMAIN_GAME)

            coEvery { gamesTable.getGame(any()) } returns dbGame

            assertThat(SUT.getGame(DOMAIN_GAME.id)).isEqualTo(DOMAIN_GAME)
        }
    }

    @Test
    fun `Retrieves null instead of game`() {
        runTest {
            val gameId = DOMAIN_GAME.id

            coEvery { gamesTable.getGame(gameId) } returns null

            assertThat(SUT.getGame(gameId)).isNull()
        }
    }

    @Test
    fun `Retrieves company developed games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DOMAIN_GAMES)

            coEvery { gamesTable.getGames(any(), any(), any()) } returns dbGames

            assertThat(SUT.getCompanyDevelopedGames(DOMAIN_COMPANY, PAGINATION))
                .isEqualTo(DOMAIN_GAMES)
        }
    }

    @Test
    fun `Retrieves similar games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DOMAIN_GAMES)

            coEvery { gamesTable.getGames(any(), any(), any()) } returns dbGames

            assertThat(SUT.getSimilarGames(DOMAIN_GAME, PAGINATION))
                .isEqualTo(DOMAIN_GAMES)
        }
    }

    @Test
    fun `Searches games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DOMAIN_GAMES)

            coEvery { gamesTable.searchGames(any(), any(), any()) } returns dbGames

            assertThat(SUT.searchGames("", PAGINATION)).isEqualTo(DOMAIN_GAMES)
        }
    }

    @Test
    fun `Observes popular games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DOMAIN_GAMES)

            every { gamesTable.observePopularGames(any(), any(), any()) } returns flowOf(dbGames)

            SUT.observePopularGames(PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DOMAIN_GAMES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Observes recently released games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DOMAIN_GAMES)

            every { gamesTable.observeRecentlyReleasedGames(any(), any(), any(), any()) } returns flowOf(dbGames)

            SUT.observeRecentlyReleasedGames(PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DOMAIN_GAMES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Observes coming soon games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DOMAIN_GAMES)

            every { gamesTable.observeComingSoonGames(any(), any(), any()) } returns flowOf(dbGames)

            SUT.observeComingSoonGames(PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DOMAIN_GAMES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Observes most anticipated games successfully`() {
        runTest {
            val dbGames = dbGameMapper.mapToDatabaseGames(DOMAIN_GAMES)

            every { gamesTable.observeMostAnticipatedGames(any(), any(), any()) } returns flowOf(dbGames)

            SUT.observeMostAnticipatedGames(PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DOMAIN_GAMES)
                awaitComplete()
            }
        }
    }
}
