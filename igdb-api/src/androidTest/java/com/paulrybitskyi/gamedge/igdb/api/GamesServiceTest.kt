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

import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.api.Error
import com.paulrybitskyi.gamedge.common.testing.startSafe
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.ApiOauthCredentials
import com.paulrybitskyi.gamedge.igdb.api.common.CredentialsStore
import com.paulrybitskyi.gamedge.igdb.api.games.GamesService
import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiCategory
import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiGame
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private val OAUTH_CREDENTIALS = ApiOauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 5000L,
)

@HiltAndroidTest
internal class GamesServiceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject lateinit var mockWebServer: MockWebServer
    @Inject lateinit var credentialsStore: CredentialsStore
    @Inject lateinit var gamesService: GamesService

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer.startSafe()

        runBlocking {
            credentialsStore.saveOauthCredentials(OAUTH_CREDENTIALS)
        }
    }

    @Test
    fun http_error_is_returned_when_games_endpoint_returns_bad_request_response() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(400))

            val error = gamesService.getGames("").getError()

            assertThat(error is Error.HttpError).isTrue()
        }
    }

    @Test
    fun http_error_with_400_code_is_returned_when_games_endpoint_returns_bad_request_response() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(400))

            val error = gamesService.getGames("").getError()

            assertThat(error is Error.HttpError).isTrue()
            assertThat((error as Error.HttpError).code).isEqualTo(400)
        }
    }

    @Test
    fun http_error_with_proper_error_message_is_returned_when_games_endpoint_returns_bad_request_response() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(400)
                    .setBody(
                        """
                        [
                          {
                            "title": "Syntax Error"
                          }
                        ]
                        """.trimIndent(),
                    ),
            )

            val error = gamesService.getGames("").getError()

            assertThat(error is Error.HttpError).isTrue()
            assertThat((error as Error.HttpError).message).isEqualTo("Syntax Error")
        }
    }

    @Test
    fun http_error_with_unknown_error_message_is_returned_when_games_endpoint_returns_bad_request_response() {
        runBlocking {
            val errorBody = "[{\"message\": \"Missing `;` at end of query\"}]"

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(400)
                    .setBody(errorBody),
            )

            val error = gamesService.getGames("").getError()

            assertThat(error is Error.HttpError).isTrue()
            assertThat((error as Error.HttpError).message).isEqualTo("Unknown Error: $errorBody")
        }
    }

    @Test
    fun parsed_credentials_are_returned_when_games_endpoint_returns_successful_response() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """
                        [
                            {
                                "id": 144133,
                                "category": 0,
                                "first_release_date": 1614556800,
                                "follows": 5,
                                "hypes": 1,
                                "name": "Gloam",
                                "rating": 99.1794871794872,
                                "total_rating": 99.1794871794872
                            },
                            {
                                "id": 126356,
                                "category": 0,
                                "first_release_date": 1613692800,
                                "name": "Survival Vacancy",
                                "rating": 70.0,
                                "total_rating": 70.0
                            }
                        ]
                        """.trimIndent(),
                    ),
            )

            val parsedGames = gamesService.getGames("").get()
            val expectedGames = listOf(
                ApiGame(
                    id = 144133,
                    category = ApiCategory.MAIN_GAME,
                    releaseDate = 1614556800L,
                    followerCount = 5,
                    hypeCount = 1,
                    name = "Gloam",
                    usersRating = 99.1794871794872,
                    totalRating = 99.1794871794872,
                ),
                ApiGame(
                    id = 126356,
                    category = ApiCategory.MAIN_GAME,
                    releaseDate = 1613692800L,
                    name = "Survival Vacancy",
                    usersRating = 70.0,
                    totalRating = 70.0,
                ),
            )

            assertThat(parsedGames).isEqualTo(expectedGames)
        }
    }

    @Test
    fun unknown_error_is_returned_when_games_endpoint_returns_successful_response_with_no_body() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(200))

            val error = gamesService.getGames("").getError()

            assertThat(error is Error.UnknownError).isTrue()
        }
    }

    @Test
    fun unknown_error_is_returned_when_games_endpoint_returns_successful_response_with_bad_json() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("{}"),
            )

            val error = gamesService.getGames("").getError()

            assertThat(error is Error.UnknownError).isTrue()
        }
    }

    @Test
    fun network_error_is_returned_when_network_is_disconnected_while_fetching_games() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START),
            )

            val error = gamesService.getGames("").getError()

            assertThat(error is Error.NetworkError).isTrue()
        }
    }

    @After
    fun cleanup() {
        mockWebServer.shutdown()
    }
}
