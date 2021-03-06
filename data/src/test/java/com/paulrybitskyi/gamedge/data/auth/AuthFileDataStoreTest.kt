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

import androidx.datastore.core.DataStore
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthExpiryTimeCalculator
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthFileDataStore
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthMapper
import com.paulrybitskyi.gamedge.data.auth.datastores.local.ProtoOauthCredentials
import com.paulrybitskyi.gamedge.commons.testing.DATA_OAUTH_CREDENTIALS
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test


private val PROTO_OAUTH_CREDENTIALS = ProtoOauthCredentials.newBuilder()
    .setAccessToken("access_token")
    .setTokenType("token_type")
    .setTokenTtl(5000L)
    .setExpirationTime(10_000L)
    .build()


internal class AuthFileDataStoreTest {


    @MockK private lateinit var protoDataStore: DataStore<ProtoOauthCredentials>
    @MockK private lateinit var timestampProvider: TimestampProvider
    private lateinit var SUT: AuthFileDataStore


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = AuthFileDataStore(
            protoDataStore = protoDataStore,
            timestampProvider = timestampProvider,
            mapper = AuthMapper(AuthExpiryTimeCalculator(timestampProvider))
        )
    }


    @Test
    fun `Saves credentials successfully`() {
        runBlockingTest {
            coEvery { protoDataStore.updateData(any()) } returns PROTO_OAUTH_CREDENTIALS

            SUT.saveOauthCredentials(DATA_OAUTH_CREDENTIALS)

            coVerify { protoDataStore.updateData(any()) }
        }
    }


    @Test
    fun `Retrieves credentials successfully`() {
        runBlockingTest {
            coEvery { protoDataStore.data } returns flowOf(PROTO_OAUTH_CREDENTIALS)

            assertThat(SUT.getOauthCredentials())
                .isEqualTo(DATA_OAUTH_CREDENTIALS)
        }
    }


    @Test
    fun `Retrieves null credentials successfully`() {
        runBlockingTest {
            coEvery { protoDataStore.data } returns flowOf()

            assertThat(SUT.getOauthCredentials()).isNull()
        }
    }


    @Test
    fun `Credentials should not be expired`() {
        runBlockingTest {
            coEvery { protoDataStore.data } returns flowOf(PROTO_OAUTH_CREDENTIALS)
            coEvery { timestampProvider.getUnixTimestamp(any()) } returns 0L

            assertThat(SUT.isExpired()).isFalse
        }
    }


    @Test
    fun `Credentials should be expired`() {
        runBlockingTest {
            coEvery { protoDataStore.data } returns flowOf(PROTO_OAUTH_CREDENTIALS)
            coEvery {
                timestampProvider.getUnixTimestamp(any())
            } returns (PROTO_OAUTH_CREDENTIALS.expirationTime + 10_000L)

            assertThat(SUT.isExpired()).isTrue
        }
    }


    @Test
    fun `Credentials are expired if data store is empty`() {
        runBlockingTest {
            coEvery { protoDataStore.data } returns flowOf()

            assertThat(SUT.isExpired()).isTrue
        }
    }


}