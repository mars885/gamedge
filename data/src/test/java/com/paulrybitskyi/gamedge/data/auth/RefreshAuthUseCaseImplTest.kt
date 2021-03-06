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
import com.paulrybitskyi.gamedge.commons.testing.DATA_ERROR_NOT_FOUND
import com.paulrybitskyi.gamedge.data.auth.datastores.AuthRemoteDataStore
import com.paulrybitskyi.gamedge.data.auth.datastores.commons.AuthDataStores
import com.paulrybitskyi.gamedge.data.auth.datastores.local.AuthLocalDataStore
import com.paulrybitskyi.gamedge.data.auth.usecases.RefreshAuthUseCaseImpl
import com.paulrybitskyi.gamedge.data.auth.usecases.mappers.AuthMapper
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.domain.commons.extensions.execute
import com.paulrybitskyi.gamedge.commons.testing.DATA_OAUTH_CREDENTIALS
import com.paulrybitskyi.gamedge.commons.testing.coVerifyNotCalled
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test

internal class RefreshAuthUseCaseImplTest {


    @MockK private lateinit var authLocalDataStore: AuthLocalDataStore
    @MockK private lateinit var authRemoteDataStore: AuthRemoteDataStore
    private lateinit var authMapper: AuthMapper
    private lateinit var SUT: RefreshAuthUseCaseImpl


    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        authMapper = AuthMapper()
        SUT = RefreshAuthUseCaseImpl(
            authDataStores = AuthDataStores(
                local = authLocalDataStore,
                remote = authRemoteDataStore
            ),
            authMapper = authMapper,
            errorMapper = ErrorMapper()
        )
    }


    @Test
    fun `Emits remote credentials when current credentials are expired`() {
        runBlockingTest {
            coEvery { authLocalDataStore.isExpired() } returns true
            coEvery { authRemoteDataStore.getOauthCredentials() } returns Ok(DATA_OAUTH_CREDENTIALS)

            assertThat(SUT.execute().first().get())
                .isEqualTo(authMapper.mapToDomainOauthCredentials(DATA_OAUTH_CREDENTIALS))
        }
    }


    @Test
    fun `Does not emit remote credentials when current credentials are not expired`() {
        runBlockingTest {
            coEvery { authLocalDataStore.isExpired() } returns false

            var isEmptyFlow = false

            SUT.execute()
                .onEmpty { isEmptyFlow = true }
                .firstOrNull()

            assertThat(isEmptyFlow).isTrue
        }
    }


    @Test
    fun `Saves remote credentials into local data store when refresh is successful`() {
        runBlockingTest {
            coEvery { authLocalDataStore.isExpired() } returns true
            coEvery { authRemoteDataStore.getOauthCredentials() } returns Ok(DATA_OAUTH_CREDENTIALS)

            SUT.execute().firstOrNull()

            coVerify { authLocalDataStore.saveOauthCredentials(DATA_OAUTH_CREDENTIALS) }
        }
    }


    @Test
    fun `Does not save remote credentials into local data store when current credentials are not expired`() {
        runBlockingTest {
            coEvery { authLocalDataStore.isExpired() } returns false

            SUT.execute().firstOrNull()

            coVerifyNotCalled { authLocalDataStore.saveOauthCredentials(any()) }
        }
    }


    @Test
    fun `Does not save remote credentials into local data store when refresh is unsuccessful`() {
        runBlockingTest {
            coEvery { authLocalDataStore.isExpired() } returns false
            coEvery { authRemoteDataStore.getOauthCredentials() } returns Err(DATA_ERROR_NOT_FOUND)

            SUT.execute().firstOrNull()

            coVerifyNotCalled { authLocalDataStore.saveOauthCredentials(any()) }
        }
    }


}