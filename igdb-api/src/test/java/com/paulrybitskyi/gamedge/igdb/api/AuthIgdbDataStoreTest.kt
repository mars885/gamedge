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
import com.paulrybitskyi.gamedge.data.auth.datastores.AuthRemoteDataStore
import com.paulrybitskyi.gamedge.igdb.api.auth.AuthEndpoint
import com.paulrybitskyi.gamedge.igdb.api.auth.datastores.AuthIgdbDataStoreImpl
import com.paulrybitskyi.gamedge.igdb.api.auth.datastores.AuthMapper
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.OauthCredentials
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val API_OAUTH_CREDENTIALS = OauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 500L
)

private val HTTP_ERROR = Error.HttpError(code = 2, message = "http_error")
private val NETWORK_ERROR = Error.NetworkError(Exception("network_error"))
private val UNKNOWN_ERROR = Error.NetworkError(Exception("unknown_error"))


internal class AuthIgdbDataStoreTest {


    private lateinit var authEndpoint: FakeAuthEndpoint
    private lateinit var authMapper: AuthMapper
    private lateinit var errorMapper: ErrorMapper
    private lateinit var authRemoteDataStore: AuthRemoteDataStore


    @Before
    fun setup() {
        authEndpoint = FakeAuthEndpoint()
        authMapper = AuthMapper()
        errorMapper = ErrorMapper()
        authRemoteDataStore = AuthIgdbDataStoreImpl(
            authEndpoint = authEndpoint,
            authMapper = authMapper,
            errorMapper = errorMapper
        )
    }


    @Test
    fun `Returns oauth credentials successfully`() = runBlockingTest {
        authEndpoint.shouldReturnCredentials = true

        val result = authRemoteDataStore.getOauthCredentials()

        assertEquals(
            result.get(),
            authMapper.mapToDataOauthCredentials(API_OAUTH_CREDENTIALS)
        )
    }


    @Test
    fun `Returns http error successfully`() = runBlockingTest {
        authEndpoint.shouldReturnHttpError = true

        val result = authRemoteDataStore.getOauthCredentials()

        assertEquals(
            result.getError(),
            errorMapper.mapToDataError(HTTP_ERROR)
        )
    }


    @Test
    fun `Returns network error successfully`() = runBlockingTest {
        authEndpoint.shouldReturnNetworkError = true

        val result = authRemoteDataStore.getOauthCredentials()

        assertEquals(
            result.getError(),
            errorMapper.mapToDataError(NETWORK_ERROR)
        )
    }


    @Test
    fun `Returns unknown error successfully`() = runBlockingTest {
        authEndpoint.shouldReturnUnknownError = true

        val result = authRemoteDataStore.getOauthCredentials()

        assertEquals(
            result.getError(),
            errorMapper.mapToDataError(UNKNOWN_ERROR)
        )
    }


    private class FakeAuthEndpoint : AuthEndpoint {

        var shouldReturnCredentials = false
        var shouldReturnHttpError = false
        var shouldReturnNetworkError = false
        var shouldReturnUnknownError = false

        override suspend fun getOauthCredentials(): ApiResult<OauthCredentials> {
            return when {
                shouldReturnCredentials -> Ok(API_OAUTH_CREDENTIALS)
                shouldReturnHttpError -> Err(HTTP_ERROR)
                shouldReturnNetworkError -> Err(NETWORK_ERROR)
                shouldReturnUnknownError -> Err(UNKNOWN_ERROR)

                else -> throw IllegalStateException()
            }
        }

    }


}