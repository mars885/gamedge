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

package com.paulrybitskyi.gamedge.common.api

sealed class Error {
    data class HttpError(val code: Int, val message: String) : Error()
    data class NetworkError(val throwable: Throwable) : Error()
    data class UnknownError(val throwable: Throwable) : Error()
}

val Error.isHttpError: Boolean
    get() = (this is Error.HttpError)

val Error.isServerError: Boolean
    get() = ((this is Error.HttpError) && (code >= HttpCodes.SERVER_ERROR))

val Error.isNetworkError: Boolean
    get() = (this is Error.NetworkError)

val Error.isUnknownError: Boolean
    get() = (this is Error.UnknownError)

val Error.httpErrorMessage: String
    get() = (if (this is Error.HttpError) message else "")

val Error.networkErrorMessage: String
    get() = (if (this is Error.NetworkError) (throwable.message ?: "") else "")

val Error.unknownErrorMessage: String
    get() = (if (this is Error.UnknownError) (throwable.message ?: "") else "")
