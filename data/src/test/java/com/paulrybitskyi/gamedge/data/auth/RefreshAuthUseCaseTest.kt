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

package com.paulrybitskyi.gamedge.data.auth

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.paulrybitskyi.gamedge.data.auth.datastores.AuthRemoteDataStore
import com.paulrybitskyi.gamedge.data.auth.datastores.commons.AuthDataStores
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthLocalDataStore
import com.paulrybitskyi.gamedge.data.auth.entities.OauthCredentials
import com.paulrybitskyi.gamedge.data.auth.usecases.RefreshAuthUseCaseImpl
import com.paulrybitskyi.gamedge.data.auth.usecases.mappers.AuthMapper
import com.paulrybitskyi.gamedge.data.commons.DataResult
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.data.commons.entities.Error
import com.paulrybitskyi.gamedge.domain.auth.usecases.RefreshAuthUseCase
import com.paulrybitskyi.gamedge.domain.commons.extensions.execute
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val DATA_OAUTH_CREDENTIALS = DataOauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 5000L
)


internal class RefreshAuthUseCaseTest {


    private lateinit var authLocalDataStore: FakeAuthLocalDataStore
    private lateinit var authRemoteDataStore: FakeAuthRemoteDataStore
    private lateinit var authMapper: AuthMapper
    private lateinit var refreshAuthUseCase: RefreshAuthUseCase


    @Before
    fun setup() {
        authLocalDataStore = FakeAuthLocalDataStore()
        authRemoteDataStore = FakeAuthRemoteDataStore()
        authMapper = AuthMapper()
        refreshAuthUseCase = RefreshAuthUseCaseImpl(
            authDataStores = AuthDataStores(
                local = authLocalDataStore,
                remote = authRemoteDataStore
            ),
            authMapper = authMapper,
            errorMapper = ErrorMapper()
        )
    }


    @Test
    fun `Emits remote credentials when current credentials are expired`() = runBlockingTest {
        authLocalDataStore.isExpired = true
        authRemoteDataStore.shouldReturnCredentials = true

        assertEquals(
            authMapper.mapToDomainOauthCredentials(DATA_OAUTH_CREDENTIALS),
            refreshAuthUseCase.execute().first().get()
        )
    }


    @Test
    fun `Does not emit remote credentials when current credentials are not expired`() = runBlockingTest {
        authLocalDataStore.isExpired = false

        var isEmptyFlow = false

        refreshAuthUseCase.execute()
            .onEmpty { isEmptyFlow = true }
            .firstOrNull()

        assertTrue(isEmptyFlow)
    }


    @Test
    fun `Saves remote credentials into local data store when refresh is successful`() = runBlockingTest {
        authLocalDataStore.isExpired = true
        authRemoteDataStore.shouldReturnCredentials = true

        refreshAuthUseCase.execute().firstOrNull()

        assertEquals(DATA_OAUTH_CREDENTIALS, authLocalDataStore.credentials)
    }


    @Test
    fun `Does not save remote credentials into local data store when current credentials are not expired`() {
        runBlockingTest {
            authLocalDataStore.isExpired = false

            refreshAuthUseCase.execute().firstOrNull()

            assertNull(authLocalDataStore.credentials)
        }
    }


    @Test
    fun `Does not save remote credentials into local data store when refresh is unsuccessful`() {
        runBlockingTest {
            authLocalDataStore.isExpired = false
            authRemoteDataStore.shouldReturnError = true

            refreshAuthUseCase.execute().firstOrNull()

            assertNull(authLocalDataStore.credentials)
        }
    }


    private class FakeAuthLocalDataStore : AuthLocalDataStore {

        var credentials: OauthCredentials? = null

        var isExpired = false

        override suspend fun saveOauthCredentials(oauthCredentials: OauthCredentials) {
            credentials = oauthCredentials
        }

        override suspend fun getOauthCredentials(): OauthCredentials? {
            // no-op
            return null
        }

        override suspend fun isExpired(): Boolean {
            return isExpired
        }

    }


    private class FakeAuthRemoteDataStore : AuthRemoteDataStore {

        var shouldReturnCredentials = false
        var shouldReturnError = false

        override suspend fun getOauthCredentials(): DataResult<OauthCredentials> {
            return when {
                shouldReturnCredentials -> Ok(DATA_OAUTH_CREDENTIALS)
                shouldReturnError -> Err(Error.NotFound("error"))

                else -> throw IllegalStateException()
            }
        }

    }


}