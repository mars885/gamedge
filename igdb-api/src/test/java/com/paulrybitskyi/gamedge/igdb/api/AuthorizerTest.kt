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

import com.paulrybitskyi.gamedge.commons.testing.DATA_OAUTH_CREDENTIALS
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthLocalDataStore
import com.paulrybitskyi.gamedge.igdb.api.auth.Authorizer
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.AuthorizationType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Before
import org.junit.Test

internal class AuthorizerTest {

    @MockK private lateinit var authLocalDataStore: AuthLocalDataStore

    private lateinit var SUT: Authorizer

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = Authorizer(authLocalDataStore)
    }

    @Test
    fun `Builds basic authorization header successfully`() {
        runTest {
            coEvery { authLocalDataStore.getOauthCredentials() } returns DATA_OAUTH_CREDENTIALS

            assertThat(SUT.buildAuthorizationHeader(AuthorizationType.BASIC))
                .isEqualTo("Basic access_token")
        }
    }

    @Test
    fun `Builds bearer authorization header successfully`() {
        runTest {
            coEvery { authLocalDataStore.getOauthCredentials() } returns DATA_OAUTH_CREDENTIALS

            assertThat(SUT.buildAuthorizationHeader(AuthorizationType.BEARER))
                .isEqualTo("Bearer access_token")
        }
    }

    @Test
    fun `Throws exception if no oauth credentials are present`() {
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy {
                runTest {
                    coEvery { authLocalDataStore.getOauthCredentials() } returns null

                    SUT.buildAuthorizationHeader(AuthorizationType.BEARER)
                }
            }
    }
}
