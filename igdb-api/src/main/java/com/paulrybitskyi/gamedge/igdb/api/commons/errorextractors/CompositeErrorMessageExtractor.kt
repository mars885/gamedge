/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors

import com.paulrybitskyi.gamedge.commons.api.ErrorMessageExtractor
import com.paulrybitskyi.gamedge.igdb.api.commons.di.qualifiers.IgdbApi
import com.paulrybitskyi.hiltbinder.BindType
import okhttp3.ResponseBody
import javax.inject.Inject


private const val UNKNOWN_ERROR = "Unknown Error"


@BindType(withQualifier = true)
@IgdbApi
internal class CompositeErrorMessageExtractor @Inject constructor(
    private val errorMessageExtractors: Set<@JvmSuppressWildcards ErrorMessageExtractor>
) : ErrorMessageExtractor {


    override fun extract(responseBody: ResponseBody): String {
        return try {
            for(errorMessageExtractor in errorMessageExtractors) {
                val message = errorMessageExtractor.extract(responseBody)

                if(message.isNotBlank()) return message
            }

            UNKNOWN_ERROR
        } catch(error: Throwable) {
            (error.message ?: error.javaClass.simpleName)
        }
    }


}