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

package com.paulrybitskyi.gamedge.common.data.auth.igdb

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.data.auth.datastores.igdb.AuthIgdbDataStore
import com.paulrybitskyi.gamedge.common.data.auth.datastores.igdb.IgdbAuthMapper
import com.paulrybitskyi.gamedge.common.data.common.ApiErrorMapper
import com.paulrybitskyi.gamedge.common.testing.API_ERROR_HTTP
import com.paulrybitskyi.gamedge.common.testing.API_ERROR_NETWORK
import com.paulrybitskyi.gamedge.common.testing.API_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.igdb.api.auth.AuthEndpoint
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.ApiOauthCredentials
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val API_OAUTH_CREDENTIALS = ApiOauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 500L,
)

internal class AuthIgdbDataStoreTest {

    @MockK private lateinit var authEndpoint: AuthEndpoint

    private lateinit var igdbAuthMapper: IgdbAuthMapper
    private lateinit var apiErrorMapper: ApiErrorMapper
    private lateinit var SUT: AuthIgdbDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        igdbAuthMapper = IgdbAuthMapper()
        apiErrorMapper = ApiErrorMapper()
        SUT = AuthIgdbDataStore(
            authEndpoint = authEndpoint,
            igdbAuthMapper = igdbAuthMapper,
            apiErrorMapper = apiErrorMapper,
        )
    }

    @Test
    fun `Returns oauth credentials successfully`() {
        runTest {
            coEvery { authEndpoint.getOauthCredentials() } returns Ok(API_OAUTH_CREDENTIALS)

            val result = SUT.getOauthCredentials()

            assertThat(result.get())
                .isEqualTo(igdbAuthMapper.mapToDomainOauthCredentials(API_OAUTH_CREDENTIALS))
        }
    }

    @Test
    fun `Returns http error successfully`() {
        runTest {
            coEvery { authEndpoint.getOauthCredentials() } returns Err(API_ERROR_HTTP)

            val result = SUT.getOauthCredentials()

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error successfully`() {
        runTest {
            coEvery { authEndpoint.getOauthCredentials() } returns Err(API_ERROR_NETWORK)

            val result = SUT.getOauthCredentials()

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error successfully`() {
        runTest {
            coEvery { authEndpoint.getOauthCredentials() } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getOauthCredentials()

            assertThat(result.getError())
                .isEqualTo(apiErrorMapper.mapToDomainError(API_ERROR_UNKNOWN))
        }
    }
}
