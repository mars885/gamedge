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

package com.paulrybitskyi.gamedge.igdb.api

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import com.paulrybitskyi.gamedge.commons.api.ErrorMapper
import com.paulrybitskyi.gamedge.commons.testing.API_ERROR_HTTP
import com.paulrybitskyi.gamedge.commons.testing.API_ERROR_NETWORK
import com.paulrybitskyi.gamedge.commons.testing.API_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.commons.testing.DATA_COMPANY
import com.paulrybitskyi.gamedge.commons.testing.DATA_GAME
import com.paulrybitskyi.gamedge.commons.testing.DATA_PAGINATION
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.igdb.api.games.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.games.GamesEndpoint
import com.paulrybitskyi.gamedge.igdb.api.games.datastores.GameMapper
import com.paulrybitskyi.gamedge.igdb.api.games.datastores.GamesIgdbDataStore
import com.paulrybitskyi.gamedge.igdb.api.games.datastores.mapToDataGames
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

private val API_GAMES = listOf(
    ApiGame(id = 1),
    ApiGame(id = 2),
    ApiGame(id = 3)
)

internal class GamesIgdbDataStoreTest {

    @MockK private lateinit var gamesEndpoint: GamesEndpoint

    private lateinit var gameMapper: GameMapper
    private lateinit var errorMapper: ErrorMapper
    private lateinit var SUT: GamesIgdbDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        gameMapper = GameMapper()
        errorMapper = ErrorMapper()
        SUT = GamesIgdbDataStore(
            gamesEndpoint = gamesEndpoint,
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = gameMapper,
            errorMapper = errorMapper
        )
    }

    @Test
    fun `Returns searched games successfully`() {
        runBlockingTest {
            coEvery { gamesEndpoint.searchGames(any(), any(), any()) } returns Ok(API_GAMES)

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when searching games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.searchGames(any(), any(), any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when searching games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.searchGames(any(), any(), any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when searching games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.searchGames(any(), any(), any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns popular games successfully`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getPopularGames(any(), any()) } returns Ok(API_GAMES)

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching popular games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getPopularGames(any(), any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching popular games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getPopularGames(any(), any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching popular games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getPopularGames(any(), any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns recently released games successfully`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any(), any()) } returns Ok(API_GAMES)

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching recently released games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any(), any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching recently released games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any(), any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching recently released games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any(), any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns coming soon games successfully`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getComingSoonGames(any(), any()) } returns Ok(API_GAMES)

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching coming soon games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getComingSoonGames(any(), any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching coming soon games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getComingSoonGames(any(), any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching coming soon games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getComingSoonGames(any(), any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns most anticipated games successfully`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any(), any()) } returns Ok(API_GAMES)

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching most anticipated games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any(), any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching most anticipated games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any(), any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching most anticipated games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any(), any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns company developed games successfully`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getGames(any(), any(), any()) } returns Ok(API_GAMES)

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching company developed games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getGames(any(), any(), any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching company developed games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getGames(any(), any(), any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching company developed games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getGames(any(), any(), any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns similar games successfully`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getGames(any(), any(), any()) } returns Ok(API_GAMES)

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching similar games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getGames(any(), any(), any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching similar games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getGames(any(), any(), any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching similar games`() {
        runBlockingTest {
            coEvery { gamesEndpoint.getGames(any(), any(), any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }
}
