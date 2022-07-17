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

package com.paulrybitskyi.gamedge.common.data.games.datastores.remote

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.API_ERROR_HTTP
import com.paulrybitskyi.gamedge.common.testing.API_ERROR_NETWORK
import com.paulrybitskyi.gamedge.common.testing.API_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.common.testing.DATA_GAME
import com.paulrybitskyi.gamedge.common.testing.DATA_PAGINATION
import com.paulrybitskyi.gamedge.common.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.common.data.DATA_COMPANY
import com.paulrybitskyi.gamedge.common.data.FakeDiscoveryGamesReleaseDatesProvider
import com.paulrybitskyi.gamedge.common.data.common.ApiErrorMapper
import com.paulrybitskyi.gamedge.common.data.games.datastores.remote.igdb.GamesIgdbDataStore
import com.paulrybitskyi.gamedge.common.data.games.datastores.remote.igdb.IgdbGameMapper
import com.paulrybitskyi.gamedge.common.data.games.datastores.remote.igdb.mapToDataGames
import com.paulrybitskyi.gamedge.igdb.api.games.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.games.GamesEndpoint
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val API_GAMES = listOf(
    ApiGame(id = 1, name = "name1"),
    ApiGame(id = 2, name = "name2"),
    ApiGame(id = 3, name = "name3"),
)

internal class GamesIgdbDataStoreTest {

    @MockK private lateinit var gamesEndpoint: GamesEndpoint

    private lateinit var igdbGameMapper: IgdbGameMapper
    private lateinit var apiErrorMapper: ApiErrorMapper
    private lateinit var SUT: GamesIgdbDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        igdbGameMapper = IgdbGameMapper()
        apiErrorMapper = ApiErrorMapper()
        SUT = GamesIgdbDataStore(
            gamesEndpoint = gamesEndpoint,
            releaseDatesProvider = FakeDiscoveryGamesReleaseDatesProvider(),
            dispatcherProvider = FakeDispatcherProvider(),
            igdbGameMapper = igdbGameMapper,
            apiErrorMapper = apiErrorMapper,
        )
    }

    @Test
    fun `Returns searched games successfully`() {
        runTest {
            coEvery { gamesEndpoint.searchGames(any()) } returns Ok(API_GAMES)

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when searching games`() {
        runTest {
            coEvery { gamesEndpoint.searchGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when searching games`() {
        runTest {
            coEvery { gamesEndpoint.searchGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when searching games`() {
        runTest {
            coEvery { gamesEndpoint.searchGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns popular games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getPopularGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching popular games`() {
        runTest {
            coEvery { gamesEndpoint.getPopularGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching popular games`() {
        runTest {
            coEvery { gamesEndpoint.getPopularGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching popular games`() {
        runTest {
            coEvery { gamesEndpoint.getPopularGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns recently released games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching recently released games`() {
        runTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching recently released games`() {
        runTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching recently released games`() {
        runTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns coming soon games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getComingSoonGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching coming soon games`() {
        runTest {
            coEvery { gamesEndpoint.getComingSoonGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching coming soon games`() {
        runTest {
            coEvery { gamesEndpoint.getComingSoonGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching coming soon games`() {
        runTest {
            coEvery { gamesEndpoint.getComingSoonGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns most anticipated games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching most anticipated games`() {
        runTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching most anticipated games`() {
        runTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching most anticipated games`() {
        runTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns company developed games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching company developed games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching company developed games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching company developed games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns similar games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDataGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching similar games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching similar games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching similar games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }
}
