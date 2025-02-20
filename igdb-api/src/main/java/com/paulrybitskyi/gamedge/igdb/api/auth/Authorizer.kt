/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

import com.paulrybitskyi.gamedge.igdb.api.auth.entities.ApiAuthorizationType
import javax.inject.Inject

internal class Authorizer @Inject constructor() {

    fun buildAuthorizationHeader(type: ApiAuthorizationType, token: String): String {
        return buildString {
            append(type.rawType)
            append(" ")
            append(token)
        }
    }
}
