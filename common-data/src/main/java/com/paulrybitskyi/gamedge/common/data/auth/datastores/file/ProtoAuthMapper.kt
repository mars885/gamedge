/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.common.data.auth.datastores.file

import com.paulrybitskyi.gamedge.common.domain.auth.entities.OauthCredentials
import javax.inject.Inject

internal class ProtoAuthMapper @Inject constructor(
    private val authExpiryTimeCalculator: AuthExpiryTimeCalculator,
) {

    fun mapToProtoOauthCredentials(oauthCredentials: OauthCredentials): ProtoOauthCredentials {
        return ProtoOauthCredentials.newBuilder()
            .setAccessToken(oauthCredentials.accessToken)
            .setTokenType(oauthCredentials.tokenType)
            .setTokenTtl(oauthCredentials.tokenTtl)
            .setExpirationTime(authExpiryTimeCalculator.calculateExpiryTime(oauthCredentials))
            .build()
    }

    fun mapToDomainOauthCredentials(oauthCredentials: ProtoOauthCredentials): OauthCredentials {
        return OauthCredentials(
            accessToken = oauthCredentials.accessToken,
            tokenType = oauthCredentials.tokenType,
            tokenTtl = oauthCredentials.tokenTtl,
        )
    }
}
