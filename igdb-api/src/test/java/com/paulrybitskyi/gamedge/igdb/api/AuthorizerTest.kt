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

import com.paulrybitskyi.gamedge.igdb.api.auth.Authorizer
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.AuthorizationType
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class AuthorizerTest {

    private companion object {
        const val ACCESS_TOKEN = "access_token"
    }

    private lateinit var SUT: Authorizer

    @Before
    fun setup() {
        SUT = Authorizer()
    }

    @Test
    fun `Builds basic authorization header successfully`() {
        runTest {
            val expectedHeader = "Basic $ACCESS_TOKEN"
            val actualHeader = SUT.buildAuthorizationHeader(
                type = AuthorizationType.BASIC,
                token = ACCESS_TOKEN,
            )

            assertThat(actualHeader).isEqualTo(expectedHeader)
        }
    }

    @Test
    fun `Builds bearer authorization header successfully`() {
        runTest {
            val expectedHeader = "Bearer $ACCESS_TOKEN"
            val actualHeader = SUT.buildAuthorizationHeader(
                type = AuthorizationType.BEARER,
                token = ACCESS_TOKEN,
            )

            assertThat(actualHeader).isEqualTo(expectedHeader)
        }
    }
}
