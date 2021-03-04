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

import com.paulrybitskyi.gamedge.data.auth.DataOauthCredentials
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthLocalDataStore
import com.paulrybitskyi.gamedge.igdb.api.auth.Authorizer
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.AuthorizationType
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val DATA_OAUTH_CREDENTIALS = DataOauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 500L
)


internal class AuthorizerTest {


    private lateinit var authLocalDataStore: FakeAuthLocalDataStore
    private lateinit var SUT: Authorizer


    @Before
    fun setup() {
        authLocalDataStore = FakeAuthLocalDataStore()
        SUT = Authorizer(authLocalDataStore)
    }


    @Test
    fun `Builds basic authorization header successfully`() {
        runBlockingTest {
            authLocalDataStore.saveOauthCredentials(DATA_OAUTH_CREDENTIALS)

            assertEquals(
                "Basic access_token",
                SUT.buildAuthorizationHeader(AuthorizationType.BASIC)
            )
        }
    }


    @Test
    fun `Builds bearer authorization header successfully`() {
        runBlockingTest {
            authLocalDataStore.saveOauthCredentials(DATA_OAUTH_CREDENTIALS)

            assertEquals(
                "Bearer access_token",
                SUT.buildAuthorizationHeader(AuthorizationType.BEARER)
            )
        }
    }


    @Test(expected = IllegalStateException::class)
    fun `Throws exception if no oauth credentials are present`() {
        runBlockingTest {
            SUT.buildAuthorizationHeader(AuthorizationType.BEARER)
        }
    }


    private class FakeAuthLocalDataStore : AuthLocalDataStore {

        private var oauthCredentials: DataOauthCredentials? = null

        override suspend fun saveOauthCredentials(oauthCredentials: DataOauthCredentials) {
            this.oauthCredentials = oauthCredentials
        }

        override suspend fun getOauthCredentials(): DataOauthCredentials? {
            return this.oauthCredentials
        }

        override suspend fun isExpired(): Boolean {
            return false
        }

    }


}