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

package com.paulrybitskyi.gamedge.igdb.api.auth

import com.paulrybitskyi.gamedge.igdb.api.auth.entities.AuthorizationType
import javax.inject.Inject

internal class AuthHeaderParser @Inject constructor() {

    private companion object {
        const val SEPARATOR = ' '
    }

    fun parse(header: String): AuthHeaderParsingResult? {
        val stringParts = header.split(SEPARATOR)

        if (stringParts.size != 2) return null

        val rawType = stringParts[0]
        val token = stringParts[1]

        return AuthHeaderParsingResult(
            type = AuthorizationType.forRawType(rawType),
            token = token,
        )
    }
}

internal data class AuthHeaderParsingResult(
    val type: AuthorizationType,
    val token: String?,
)
