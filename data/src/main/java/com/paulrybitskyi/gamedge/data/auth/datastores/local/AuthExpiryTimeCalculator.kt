/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.data.auth.datastores.local

import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.data.auth.entities.OauthCredentials
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal val AUTH_TOKEN_TTL_DEDUCTION = TimeUnit.DAYS.toMillis(@Suppress("MagicNumber") 7)

internal class AuthExpiryTimeCalculator @Inject constructor(
    private val timestampProvider: TimestampProvider
) {

    fun calculateExpiryTime(oauthCredentials: OauthCredentials): Long {
        val currentTimestamp = timestampProvider.getUnixTimestamp()
        val tokenTtlInMillis = TimeUnit.SECONDS.toMillis(oauthCredentials.tokenTtl)
        val expiryTime = (currentTimestamp + tokenTtlInMillis - AUTH_TOKEN_TTL_DEDUCTION)

        return expiryTime
    }
}
