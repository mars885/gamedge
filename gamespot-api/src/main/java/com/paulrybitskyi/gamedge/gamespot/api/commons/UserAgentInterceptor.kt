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

package com.paulrybitskyi.gamedge.gamespot.api.commons

import com.paulrybitskyi.gamedge.commons.api.HttpHeaders
import com.paulrybitskyi.gamedge.commons.api.UserAgentProvider
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

internal class UserAgentInterceptor @Inject constructor(
    private val userAgentProvider: UserAgentProvider
) : Interceptor {

    private val userAgent by lazy {
        userAgentProvider.getUserAgent()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val modifiedRequest = chain.request()
            .newBuilder()
            .header(HttpHeaders.USER_AGENT, userAgent)
            .build()

        return chain.proceed(modifiedRequest)
    }
}
