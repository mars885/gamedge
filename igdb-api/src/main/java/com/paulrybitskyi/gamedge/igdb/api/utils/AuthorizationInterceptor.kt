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

package com.paulrybitskyi.gamedge.igdb.api.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class AuthorizationInterceptor(
    private val apiKey: String
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().withApiKeyHeader())
    }


    private fun Request.withApiKeyHeader(): Request {
        return newBuilder()
            .header(Headers.USER_KEY, apiKey)
            .build()
    }


}