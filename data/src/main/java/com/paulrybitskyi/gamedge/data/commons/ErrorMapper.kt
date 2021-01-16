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

package com.paulrybitskyi.gamedge.data.commons

import javax.inject.Inject
import com.paulrybitskyi.gamedge.data.commons.entities.Error as DataError
import com.paulrybitskyi.gamedge.domain.commons.entities.Error as DomainError

internal class ErrorMapper @Inject constructor() {


    fun mapToDomainError(dataError: DataError): DomainError {
        return when(dataError) {
            is DataError.ApiError -> dataError.toDomainApiError()
            is DataError.NotFound -> DomainError.NotFound(dataError.message)
            is DataError.Unknown -> DomainError.Unknown(dataError.message)
        }
    }


    private fun DataError.ApiError.toDomainApiError(): DomainError.ApiError {
        return when(this) {
            is DataError.ApiError.ClientError -> DomainError.ApiError.ClientError(message)
            is DataError.ApiError.NetworkError -> DomainError.ApiError.NetworkError(message)
            is DataError.ApiError.ServiceUnavailable -> DomainError.ApiError.ServiceUnavailable
            is DataError.ApiError.Unknown -> DomainError.ApiError.Unknown(message)
        }
    }


}