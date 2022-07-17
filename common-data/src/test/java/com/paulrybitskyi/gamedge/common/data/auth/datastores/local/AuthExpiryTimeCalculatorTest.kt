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

import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.common.data.DATA_OAUTH_CREDENTIALS
import com.paulrybitskyi.gamedge.common.data.auth.datastores.local.file.AUTH_TOKEN_TTL_DEDUCTION
import com.paulrybitskyi.gamedge.common.data.auth.datastores.local.file.AuthExpiryTimeCalculator
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

private const val CURRENT_TIMESTAMP = 10_000L

internal class AuthExpiryTimeCalculatorTest {

    @MockK private lateinit var timestampProvider: TimestampProvider

    private lateinit var SUT: AuthExpiryTimeCalculator

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = AuthExpiryTimeCalculator(timestampProvider)

        every { timestampProvider.getUnixTimestamp(any()) } returns CURRENT_TIMESTAMP
    }

    @Test
    fun `Calculates expiry time successfully`() {
        val credentials = DATA_OAUTH_CREDENTIALS
        val expiryTime = SUT.calculateExpiryTime(credentials)
        val expected = (CURRENT_TIMESTAMP + TimeUnit.SECONDS.toMillis(credentials.tokenTtl) - AUTH_TOKEN_TTL_DEDUCTION)

        assertThat(expiryTime).isEqualTo(expected)
    }
}
