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

package com.paulrybitskyi.gamedge.common.domain.common.entities

sealed class Error {

    sealed class ApiError : Error() {
        data class ClientError(val message: String) : ApiError()
        data class NetworkError(val message: String) : ApiError()
        object ServiceUnavailable : ApiError()
        data class Unknown(val message: String) : ApiError()
    }

    data class NotFound(val message: String) : Error()
    data class Unknown(val message: String) : Error()
}
