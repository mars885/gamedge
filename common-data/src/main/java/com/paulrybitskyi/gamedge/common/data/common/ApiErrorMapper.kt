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

package com.paulrybitskyi.gamedge.common.data.common

import com.paulrybitskyi.gamedge.common.api.httpErrorMessage
import com.paulrybitskyi.gamedge.common.api.isHttpError
import com.paulrybitskyi.gamedge.common.api.isNetworkError
import com.paulrybitskyi.gamedge.common.api.isServerError
import com.paulrybitskyi.gamedge.common.api.isUnknownError
import com.paulrybitskyi.gamedge.common.api.networkErrorMessage
import com.paulrybitskyi.gamedge.common.api.unknownErrorMessage
import com.paulrybitskyi.gamedge.common.domain.common.entities.Error
import javax.inject.Inject
import com.paulrybitskyi.gamedge.common.api.Error as ApiError

class ApiErrorMapper @Inject constructor() {

    fun mapToDomainError(apiError: ApiError): Error = with(apiError) {
        return when {
            isServerError -> Error.ApiError.ServiceUnavailable
            isHttpError -> Error.ApiError.ClientError(httpErrorMessage)
            isNetworkError -> Error.ApiError.NetworkError(networkErrorMessage)
            isUnknownError -> Error.ApiError.Unknown(unknownErrorMessage)

            else -> error("Could not map the api error $this to a data error.")
        }
    }
}
