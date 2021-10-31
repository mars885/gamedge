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

package com.paulrybitskyi.gamedge.commons.api.calladapter

import com.paulrybitskyi.gamedge.commons.api.ApiResult
import com.paulrybitskyi.gamedge.commons.api.ErrorMessageExtractor
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiResultCallAdapter<R>(
    private val successType: Type,
    private val errorMessageExtractor: ErrorMessageExtractor
) : CallAdapter<R, Call<ApiResult<R>>> {


    override fun adapt(call: Call<R>): Call<ApiResult<R>> {
        return ApiResultCall(
            delegate = call,
            successType = successType,
            errorMessageExtractor = errorMessageExtractor
        )
    }


    override fun responseType(): Type = successType


}
