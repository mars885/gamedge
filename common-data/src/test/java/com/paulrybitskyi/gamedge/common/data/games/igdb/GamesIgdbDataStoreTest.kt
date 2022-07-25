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

package com.paulrybitskyi.gamedge.common.data.games.igdb

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAME
import com.paulrybitskyi.gamedge.common.testing.domain.PAGINATION
import com.paulrybitskyi.gamedge.common.testing.API_ERROR_HTTP
import com.paulrybitskyi.gamedge.common.testing.API_ERROR_NETWORK
import com.paulrybitskyi.gamedge.common.testing.API_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.common.data.DOMAIN_COMPANY
import com.paulrybitskyi.gamedge.common.data.FakeDiscoveryGamesReleaseDatesProvider
import com.paulrybitskyi.gamedge.common.data.common.ApiErrorMapper
import com.paulrybitskyi.gamedge.common.data.games.datastores.igdb.GamesIgdbDataStore
import com.paulrybitskyi.gamedge.common.data.games.datastores.igdb.IgdbGameMapper
import com.paulrybitskyi.gamedge.common.data.games.datastores.igdb.mapToDomainGames
import com.paulrybitskyi.gamedge.common.testing.domain.FakeDispatcherProvider
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

            val result = SUT.searchGames("query", PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDomainGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when searching games`() {
        runTest {
            coEvery { gamesEndpoint.searchGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.searchGames("query", PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when searching games`() {
        runTest {
            coEvery { gamesEndpoint.searchGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.searchGames("query", PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when searching games`() {
        runTest {
            coEvery { gamesEndpoint.searchGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.searchGames("query", PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns popular games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getPopularGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getPopularGames(PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDomainGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching popular games`() {
        runTest {
            coEvery { gamesEndpoint.getPopularGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getPopularGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching popular games`() {
        runTest {
            coEvery { gamesEndpoint.getPopularGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getPopularGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching popular games`() {
        runTest {
            coEvery { gamesEndpoint.getPopularGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getPopularGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns recently released games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getRecentlyReleasedGames(PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDomainGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching recently released games`() {
        runTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getRecentlyReleasedGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching recently released games`() {
        runTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getRecentlyReleasedGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching recently released games`() {
        runTest {
            coEvery { gamesEndpoint.getRecentlyReleasedGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getRecentlyReleasedGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns coming soon games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getComingSoonGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getComingSoonGames(PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDomainGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching coming soon games`() {
        runTest {
            coEvery { gamesEndpoint.getComingSoonGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getComingSoonGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching coming soon games`() {
        runTest {
            coEvery { gamesEndpoint.getComingSoonGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getComingSoonGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching coming soon games`() {
        runTest {
            coEvery { gamesEndpoint.getComingSoonGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getComingSoonGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns most anticipated games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getMostAnticipatedGames(PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDomainGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching most anticipated games`() {
        runTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getMostAnticipatedGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching most anticipated games`() {
        runTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getMostAnticipatedGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching most anticipated games`() {
        runTest {
            coEvery { gamesEndpoint.getMostAnticipatedGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getMostAnticipatedGames(PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns company developed games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getCompanyDevelopedGames(DOMAIN_COMPANY, PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDomainGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching company developed games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getCompanyDevelopedGames(DOMAIN_COMPANY, PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching company developed games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getCompanyDevelopedGames(DOMAIN_COMPANY, PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching company developed games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getCompanyDevelopedGames(DOMAIN_COMPANY, PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_UNKNOWN))
        }
    }

    @Test
    fun `Returns similar games successfully`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Ok(API_GAMES)

            val result = SUT.getSimilarGames(DOMAIN_GAME, PAGINATION)

            assertThat(result.get())
                .isEqualTo(igdbGameMapper.mapToDomainGames(API_GAMES))
        }
    }

    @Test
    fun `Returns http error when fetching similar games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getSimilarGames(DOMAIN_GAME, PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching similar games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getSimilarGames(DOMAIN_GAME, PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching similar games`() {
        runTest {
            coEvery { gamesEndpoint.getGames(any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getSimilarGames(DOMAIN_GAME, PAGINATION)

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_UNKNOWN))
        }
    }
}
