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

package com.paulrybitskyi.gamedge.utils

import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.commons.DomainException
import com.paulrybitskyi.gamedge.domain.commons.entities.Error


interface ErrorMapper {

    fun mapToMessage(error: Throwable): String

}


internal class ErrorMapperImpl(
    private val stringProvider: StringProvider
) : ErrorMapper {


    override fun mapToMessage(error: Throwable): String {
        if(error is DomainException) return error.toMessage()

        return stringProvider.getString(R.string.error_unknown_message)
    }


    private fun DomainException.toMessage(): String {
        return stringProvider.getString(
            when(error) {
                is Error.NetworkError -> R.string.error_network_message
                is Error.ServiceUnavailable -> R.string.error_server_message

                is Error.ClientError,
                is Error.Unknown -> R.string.error_unknown_message
            }
        )
    }


}