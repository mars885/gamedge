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

package com.paulrybitskyi.gamedge.igdb.api.commons.entities

import com.paulrybitskyi.gamedge.igdb.api.commons.utils.HttpCodes


internal sealed class Error {

    data class HttpError(val code: Int, val message: String) : Error()

    data class NetworkError(val throwable: Throwable) : Error()

    data class UnknownError(val throwable: Throwable) : Error()

}


internal val Error.isHttpError: Boolean
    get() = (this is Error.HttpError)

internal val Error.isServerError: Boolean
    get() = ((this is Error.HttpError) && (code >= HttpCodes.SERVER_ERROR))

internal val Error.isNetworkError: Boolean
    get() = (this is Error.NetworkError)

internal val Error.isUnknownError: Boolean
    get() = (this is Error.UnknownError)

internal val Error.httpErrorMessage: String
    get() = (if(this is Error.HttpError) message else "")

internal val Error.networkErrorMessage: String
    get() = (if(this is Error.NetworkError) (throwable.message ?: "") else "")

internal val Error.unknownErrorMessage: String
    get() = (if(this is Error.UnknownError) (throwable.message ?: "") else "")