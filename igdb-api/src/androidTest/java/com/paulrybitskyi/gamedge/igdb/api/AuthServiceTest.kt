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
import com.paulrybitskyi.gamedge.commons.api.Error
import com.paulrybitskyi.gamedge.commons.testing.utils.startSafe
import com.paulrybitskyi.gamedge.igdb.api.auth.ApiOauthCredentials
import com.paulrybitskyi.gamedge.igdb.api.auth.AuthService
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

@HiltAndroidTest
internal class AuthServiceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject lateinit var mockWebServer: MockWebServer
    @Inject lateinit var authService: AuthService

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer.startSafe()
    }

    @Test
    fun http_error_is_returned_when_credentials_endpoint_returns_bad_request_response() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(400))

            val error = authService.getOauthCredentials("", "", "").getError()

            assertThat(error is Error.HttpError).isTrue()
        }
    }

    @Test
    fun http_error_with_400_code_is_returned_when_credentials_endpoint_returns_bad_request_response() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(400))

            val error = authService.getOauthCredentials("", "", "").getError()

            assertThat(error is Error.HttpError).isTrue()
            assertThat((error as Error.HttpError).code).isEqualTo(400)
        }
    }

    @Test
    fun http_error_with_proper_error_message_is_returned_when_credentials_endpoint_returns_bad_request_response() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(400)
                    .setBody("{\"message\": \"invalid client secret\"}")
            )

            val error = authService.getOauthCredentials("", "", "").getError()

            assertThat(error is Error.HttpError).isTrue()
            assertThat((error as Error.HttpError).message).isEqualTo("invalid client secret")
        }
    }

    @Test
    fun http_error_with_unknown_error_message_is_returned_when_credentials_endpoint_returns_bad_request_response() {
        runBlocking {
            val errorBody = "{\"error\": \"invalid client secret\"}"

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(400)
                    .setBody(errorBody)
            )

            val error = authService.getOauthCredentials("", "", "").getError()

            assertThat(error is Error.HttpError).isTrue()
            assertThat((error as Error.HttpError).message).isEqualTo("Unknown Error: $errorBody")
        }
    }

    @Test
    fun parsed_credentials_are_returned_when_credentials_endpoint_returns_successful_response() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """
                        {
                           "access_token": "token",
                           "expires_in": 5643175,
                           "token_type": "bearer"
                        }
                        """.trimIndent()
                    )
            )

            val parsedCredentials = authService.getOauthCredentials("", "", "").get()
            val expectedCredentials = ApiOauthCredentials(
                accessToken = "token",
                tokenType = "bearer",
                tokenTtl = 5643175L
            )

            assertThat(parsedCredentials).isEqualTo(expectedCredentials)
        }
    }

    @Test
    fun unknown_error_is_returned_when_credentials_endpoint_returns_successful_response_with_no_body() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(200))

            val error = authService.getOauthCredentials("", "", "").getError()

            assertThat(error is Error.UnknownError).isTrue()
        }
    }

    @Test
    fun unknown_error_is_returned_when_credentials_endpoint_returns_successful_response_with_bad_json() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("{}")
            )

            val error = authService.getOauthCredentials("", "", "").getError()

            assertThat(error is Error.UnknownError).isTrue()
        }
    }

    @Test
    fun network_error_is_returned_when_network_is_disconnected_while_fetching_credentials() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
            )

            val error = authService.getOauthCredentials("", "", "").getError()

            assertThat(error is Error.NetworkError).isTrue()
        }
    }

    @After
    fun cleanup() {
        mockWebServer.shutdown()
    }
}
