/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.common.domain.common.extensions

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapEither
import com.github.michaelbull.result.mapError
import com.paulrybitskyi.gamedge.common.domain.common.DomainException
import com.paulrybitskyi.gamedge.common.domain.common.DomainResult
import com.paulrybitskyi.gamedge.common.domain.common.entities.Error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun <T> Flow<DomainResult<T>>.onEachSuccess(action: suspend (T) -> Unit): Flow<DomainResult<T>> {
    return onEach { it.onSuccess(action) }
}

fun <T> Flow<DomainResult<T>>.onEachFailure(action: suspend (Error) -> Unit): Flow<DomainResult<T>> {
    return onEach { it.onFailure(action) }
}

fun <T> Flow<DomainResult<T>>.resultOrError(): Flow<T> {
    return map {
        if (it is Ok) return@map it.value
        if (it is Err) throw DomainException(it.error)

        error("The result is neither Ok nor Err.")
    }
}

fun <S1, E1, S2, E2> Flow<Result<S1, E1>>.mapResult(
    success: (S1) -> S2,
    failure: (E1) -> E2
): Flow<Result<S2, E2>> {
    return map { it.mapEither(success, failure) }
}

fun <S1, S2, E1> Flow<Result<S1, E1>>.mapSuccess(
    success: (S1) -> S2
): Flow<Result<S2, E1>> {
    return map { it.map(success) }
}

fun <S1, E1, E2> Flow<Result<S1, E1>>.mapError(
    failure: (E1) -> E2
): Flow<Result<S1, E2>> {
    return map { it.mapError(failure) }
}
