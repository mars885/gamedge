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

import androidx.datastore.DataStore
import com.paulrybitskyi.gamedge.OauthCredentials
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthExpiryTimeCalculator
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthFileDataStore
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthMapper
import com.paulrybitskyi.gamedge.data.auth.datastores.local.ProtoOauthCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit


private val PROTO_OAUTH_CREDENTIALS = OauthCredentials.newBuilder()
    .setAccessToken("access_token")
    .setTokenType("token_type")
    .setTokenTtl(5000L)
    .setExpirationTime(10_000L)
    .build()

private val DATA_OAUTH_CREDENTIALS = DataOauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 5000L
)


internal class AuthFileDataStoreTest {


    private lateinit var protoDataStore: FakeProtoDataStore
    private lateinit var timestampProvider: FakeTimestampProvider
    private lateinit var dataStore: AuthFileDataStore


    @Before
    fun setup() {
        protoDataStore = FakeProtoDataStore()
        timestampProvider = FakeTimestampProvider()
        dataStore = AuthFileDataStore(
            protoDataStore = protoDataStore,
            timestampProvider = timestampProvider,
            mapper = AuthMapper(AuthExpiryTimeCalculator(timestampProvider))
        )
    }


    @Test
    fun `Saves credentials successfully`() = runBlockingTest {
        dataStore.saveOauthCredentials(DATA_OAUTH_CREDENTIALS)

        assertTrue(protoDataStore.isDataUpdated)
    }


    @Test
    fun `Retrieves credentials successfully`() = runBlockingTest {
        protoDataStore.shouldReturnCredentials = true

        assertEquals(
            DATA_OAUTH_CREDENTIALS,
            dataStore.getOauthCredentials()
        )
    }


    @Test
    fun `Retrieves null credentials successfully`() = runBlockingTest {
        protoDataStore.shouldReturnEmptyFlow = true

        assertNull(dataStore.getOauthCredentials())
    }


    @Test
    fun `Credentials should not be expired`() = runBlockingTest {
        protoDataStore.shouldReturnCredentials = true
        timestampProvider.timestamp = 0L

        assertFalse(dataStore.isExpired())
    }


    @Test
    fun `Credentials should be expired`() = runBlockingTest {
        protoDataStore.shouldReturnCredentials = true
        timestampProvider.timestamp = (PROTO_OAUTH_CREDENTIALS.expirationTime + 10_000L)

        assertTrue(dataStore.isExpired())
    }


    @Test
    fun `Credentials are expired if data store is empty`() = runBlockingTest {
        protoDataStore.shouldReturnEmptyFlow = true

        assertTrue(dataStore.isExpired())
    }


    private class FakeProtoDataStore : DataStore<ProtoOauthCredentials> {

        var shouldReturnCredentials = false
        var shouldReturnEmptyFlow = false

        var isDataUpdated = false

        override val data: Flow<ProtoOauthCredentials>
            get() = (if(shouldReturnCredentials) flowOf(PROTO_OAUTH_CREDENTIALS) else flowOf())

        override suspend fun updateData(
            transform: suspend (t: ProtoOauthCredentials) -> ProtoOauthCredentials
        ): ProtoOauthCredentials {
            isDataUpdated = true

            return PROTO_OAUTH_CREDENTIALS
        }

    }


    private class FakeTimestampProvider : TimestampProvider {

        var timestamp = 0L

        override fun getUnixTimestamp(timeUnit: TimeUnit): Long {
            return timestamp
        }

    }


}