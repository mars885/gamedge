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

import androidx.datastore.core.Serializer
import com.paulrybitskyi.gamedge.OauthCredentials
import java.io.InputStream
import java.io.OutputStream


internal typealias ProtoOauthCredentials = OauthCredentials


internal fun ProtoOauthCredentials.isNotEmpty(): Boolean {
    return (
        accessToken.isNotEmpty() &&
        tokenType.isNotEmpty() &&
        (tokenTtl != 0L) &&
        (expirationTime != 0L)
    )
}


@Suppress("BlockingMethodInNonBlockingContext")
internal object ProtoOauthCredentialsSerializer : Serializer<ProtoOauthCredentials> {

    override val defaultValue: ProtoOauthCredentials
        get() = ProtoOauthCredentials.newBuilder()
            .setAccessToken("")
            .setTokenType("")
            .setTokenTtl(0L)
            .setExpirationTime(0L)
            .build()

    override suspend fun writeTo(t: ProtoOauthCredentials, output: OutputStream) = t.writeTo(output)
    override suspend fun readFrom(input: InputStream): ProtoOauthCredentials = ProtoOauthCredentials.parseFrom(input)

}