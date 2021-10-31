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

import com.github.michaelbull.result.Result as ApiResult
import com.paulrybitskyi.gamedge.commons.api.Error as ApiError
import com.paulrybitskyi.gamedge.commons.api.ErrorMessageExtractor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit

private const val PARAM_UPPER_BOUND_INDEX_API_RESULT = 0
private const val PARAM_UPPER_BOUND_INDEX_SUCCESS = 0
private const val PARAM_UPPER_BOUND_INDEX_ERROR = 1

class ApiResultCallAdapterFactory(
    private val errorMessageExtractor: ErrorMessageExtractor
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

        val apiResultType = getParameterUpperBound(PARAM_UPPER_BOUND_INDEX_API_RESULT, returnType)
        if (getRawType(apiResultType) != ApiResult::class.java) return null
        check(apiResultType is ParameterizedType) { "Response type must be a parameterized type." }

        val errorType = getParameterUpperBound(PARAM_UPPER_BOUND_INDEX_ERROR, apiResultType)
        if (getRawType(errorType) != ApiError::class.java) return null

        val successType = getParameterUpperBound(PARAM_UPPER_BOUND_INDEX_SUCCESS, apiResultType)
        return ApiResultCallAdapter<Any>(successType, errorMessageExtractor)
    }
}
