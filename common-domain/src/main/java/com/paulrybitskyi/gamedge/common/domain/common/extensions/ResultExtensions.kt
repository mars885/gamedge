/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

fun <T> T.asSuccess(): Result<T, Nothing> = Ok(this)

fun <T> T.asFailure(): Result<Nothing, T> = Err(this)

suspend fun <V, E> Result<V, E>.onSuccess(action: suspend (V) -> Unit): Result<V, E> {
    if (isOk) {
        action(value)
    }

    return this
}

suspend fun <V, E> Result<V, E>.onFailure(action: suspend (E) -> Unit): Result<V, E> {
    if (isErr) {
        action(error)
    }

    return this
}
