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
import com.paulrybitskyi.gamedge.igdb.api.commons.di.qualifiers.ErrorMessageExtractorKey
import com.paulrybitskyi.hiltbinder.BindType
import org.json.JSONObject
import javax.inject.Inject


private const val ERROR_MESSAGE_NAME = "message"


@BindType(withQualifier = true)
@ErrorMessageExtractorKey(ErrorMessageExtractorKey.Type.TWITCH)
internal class TwitchErrorMessageExtractor @Inject constructor() : ErrorMessageExtractor {


    override fun extract(responseBody: String): String = try {
        val jsonObject = JSONObject(responseBody)
        val message = jsonObject.getString(ERROR_MESSAGE_NAME)

        message
    } catch(error: Throwable) {
        throw IllegalStateException(
            "Cannot extract a message from the response body: $responseBody",
            error
        )
    }


}