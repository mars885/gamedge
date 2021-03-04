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
import com.paulrybitskyi.gamedge.commons.api.ApiResult
import com.paulrybitskyi.gamedge.commons.api.Error
import com.paulrybitskyi.gamedge.commons.api.ErrorMapper
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.games.DataCategory
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.igdb.api.games.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.games.GamesEndpoint
import com.paulrybitskyi.gamedge.igdb.api.games.datastores.GameMapper
import com.paulrybitskyi.gamedge.igdb.api.games.datastores.GamesIgdbDataStore
import com.paulrybitskyi.gamedge.igdb.api.games.datastores.mapToDataGames
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test


private val API_GAMES = listOf(
    ApiGame(id = 1),
    ApiGame(id = 2),
    ApiGame(id = 3)
)

private val API_HTTP_ERROR = Error.HttpError(code = 2, message = "http_error")
private val API_NETWORK_ERROR = Error.NetworkError(Exception("network_error"))
private val API_UNKNOWN_ERROR = Error.NetworkError(Exception("unknown_error"))

private val DATA_PAGINATION = DataPagination(offset = 0, limit = 20)
private val DATA_COMPANY = DataCompany(
    id = 1,
    name = "",
    websiteUrl = "",
    logo = null,
    developedGames = listOf()
)
private val DATA_GAME = DataGame(
    id = 1,
    followerCount = null,
    hypeCount = null,
    releaseDate = null,
    criticsRating = null,
    usersRating = null,
    totalRating = null,
    name = "name",
    summary = null,
    storyline = null,
    category = DataCategory.UNKNOWN,
    cover = null,
    releaseDates = listOf(),
    ageRatings = listOf(),
    videos = listOf(),
    artworks = listOf(),
    screenshots = listOf(),
    genres = listOf(),
    platforms = listOf(),
    playerPerspectives = listOf(),
    themes = listOf(),
    modes = listOf(),
    keywords = listOf(),
    involvedCompanies = listOf(),
    websites = listOf(),
    similarGames = listOf()
)


internal class GamesIgdbDataStoreTest {


    private lateinit var gamesEndpoint: FakeGamesEndpoint
    private lateinit var gameMapper: GameMapper
    private lateinit var errorMapper: ErrorMapper
    private lateinit var SUT: GamesIgdbDataStore


    @Before
    fun setup() {
        gamesEndpoint = FakeGamesEndpoint()
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
            gamesEndpoint.shouldReturnGames = true

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }


    @Test
    fun `Returns http error when searching games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnHttpError = true

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_HTTP_ERROR))
        }
    }


    @Test
    fun `Returns network error when searching games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnNetworkError = true

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_NETWORK_ERROR))
        }
    }


    @Test
    fun `Returns unknown error when searching games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnUnknownError = true

            val result = SUT.searchGames("query", DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_UNKNOWN_ERROR))
        }
    }


    @Test
    fun `Returns popular games successfully`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnGames = true

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }


    @Test
    fun `Returns http error when fetching popular games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnHttpError = true

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_HTTP_ERROR))
        }
    }


    @Test
    fun `Returns network error when fetching popular games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnNetworkError = true

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_NETWORK_ERROR))
        }
    }


    @Test
    fun `Returns unknown error when fetching popular games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnUnknownError = true

            val result = SUT.getPopularGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_UNKNOWN_ERROR))
        }
    }


    @Test
    fun `Returns recently released games successfully`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnGames = true

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }


    @Test
    fun `Returns http error when fetching recently released games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnHttpError = true

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_HTTP_ERROR))
        }
    }


    @Test
    fun `Returns network error when fetching recently released games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnNetworkError = true

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_NETWORK_ERROR))
        }
    }


    @Test
    fun `Returns unknown error when fetching recently released games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnUnknownError = true

            val result = SUT.getRecentlyReleasedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_UNKNOWN_ERROR))
        }
    }


    @Test
    fun `Returns coming soon games successfully`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnGames = true

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }


    @Test
    fun `Returns http error when fetching coming soon games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnHttpError = true

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_HTTP_ERROR))
        }
    }


    @Test
    fun `Returns network error when fetching coming soon games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnNetworkError = true

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_NETWORK_ERROR))
        }
    }


    @Test
    fun `Returns unknown error when fetching coming soon games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnUnknownError = true

            val result = SUT.getComingSoonGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_UNKNOWN_ERROR))
        }
    }


    @Test
    fun `Returns most anticipated games successfully`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnGames = true

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }


    @Test
    fun `Returns http error when fetching most anticipated games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnHttpError = true

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_HTTP_ERROR))
        }
    }


    @Test
    fun `Returns network error when fetching most anticipated games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnNetworkError = true

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_NETWORK_ERROR))
        }
    }


    @Test
    fun `Returns unknown error when fetching most anticipated games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnUnknownError = true

            val result = SUT.getMostAnticipatedGames(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_UNKNOWN_ERROR))
        }
    }


    @Test
    fun `Returns company developed games successfully`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnGames = true

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }


    @Test
    fun `Returns http error when fetching company developed games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnHttpError = true

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_HTTP_ERROR))
        }
    }


    @Test
    fun `Returns network error when fetching company developed games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnNetworkError = true

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_NETWORK_ERROR))
        }
    }


    @Test
    fun `Returns unknown error when fetching company developed games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnUnknownError = true

            val result = SUT.getCompanyDevelopedGames(DATA_COMPANY, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_UNKNOWN_ERROR))
        }
    }


    @Test
    fun `Returns similar games successfully`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnGames = true

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(gameMapper.mapToDataGames(API_GAMES))
        }
    }


    @Test
    fun `Returns http error when fetching similar games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnHttpError = true

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_HTTP_ERROR))
        }
    }


    @Test
    fun `Returns network error when fetching similar games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnNetworkError = true

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_NETWORK_ERROR))
        }
    }


    @Test
    fun `Returns unknown error when fetching similar games`() {
        runBlockingTest {
            gamesEndpoint.shouldReturnUnknownError = true

            val result = SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_UNKNOWN_ERROR))
        }
    }


    private class FakeGamesEndpoint : GamesEndpoint {

        var shouldReturnGames = false
        var shouldReturnHttpError = false
        var shouldReturnNetworkError = false
        var shouldReturnUnknownError = false

        override suspend fun searchGames(searchQuery: String, offset: Int, limit: Int) = getTestResult()

        override suspend fun getPopularGames(offset: Int, limit: Int) = getTestResult()

        override suspend fun getRecentlyReleasedGames(offset: Int, limit: Int) = getTestResult()

        override suspend fun getComingSoonGames(offset: Int, limit: Int) = getTestResult()

        override suspend fun getMostAnticipatedGames(offset: Int, limit: Int) = getTestResult()

        override suspend fun getGames(gameIds: List<Int>, offset: Int, limit: Int) = getTestResult()

        private fun getTestResult(): ApiResult<List<ApiGame>> {
            return when {
                shouldReturnGames -> Ok(API_GAMES)
                shouldReturnHttpError -> Err(API_HTTP_ERROR)
                shouldReturnNetworkError -> Err(API_NETWORK_ERROR)
                shouldReturnUnknownError -> Err(API_UNKNOWN_ERROR)

                else -> throw IllegalStateException()
            }
        }

    }


    private class FakeDispatcherProvider(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
        override val main: CoroutineDispatcher = testDispatcher,
        override val io: CoroutineDispatcher = testDispatcher,
        override val computation: CoroutineDispatcher = testDispatcher
    ) : DispatcherProvider


}