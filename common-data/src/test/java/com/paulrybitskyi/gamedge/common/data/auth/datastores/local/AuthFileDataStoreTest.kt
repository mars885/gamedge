/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.common.data.auth.datastores.local

import androidx.datastore.core.DataStore
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.common.data.DATA_OAUTH_CREDENTIALS
import com.paulrybitskyi.gamedge.common.data.auth.datastores.local.file.AuthExpiryTimeCalculator
import com.paulrybitskyi.gamedge.common.data.auth.datastores.local.file.AuthFileDataStore
import com.paulrybitskyi.gamedge.common.data.auth.datastores.local.file.ProtoAuthMapper
import com.paulrybitskyi.gamedge.common.data.auth.datastores.local.file.ProtoOauthCredentials
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
            protoAuthMapper = ProtoAuthMapper(AuthExpiryTimeCalculator(timestampProvider)),
        )
    }

    @Test
    fun `Saves credentials successfully`() {
        runTest {
            coEvery { protoDataStore.updateData(any()) } returns PROTO_OAUTH_CREDENTIALS

            SUT.saveOauthCredentials(DATA_OAUTH_CREDENTIALS)

            coVerify { protoDataStore.updateData(any()) }
        }
    }

    @Test
    fun `Retrieves credentials successfully`() {
        runTest {
            every { protoDataStore.data } returns flowOf(PROTO_OAUTH_CREDENTIALS)

            assertThat(SUT.getOauthCredentials()).isEqualTo(DATA_OAUTH_CREDENTIALS)
        }
    }

    @Test
    fun `Retrieves null credentials successfully`() {
        runTest {
            every { protoDataStore.data } returns flowOf()

            assertThat(SUT.getOauthCredentials()).isNull()
        }
    }
}
