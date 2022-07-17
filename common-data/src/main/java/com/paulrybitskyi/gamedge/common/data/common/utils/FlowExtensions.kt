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

package com.paulrybitskyi.gamedge.common.data.common.utils

import com.paulrybitskyi.gamedge.core.utils.onFailure
import com.paulrybitskyi.gamedge.core.utils.onSuccess
import com.paulrybitskyi.gamedge.common.data.common.DataResult
import com.paulrybitskyi.gamedge.common.data.common.entities.Error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

fun <T> Flow<DataResult<T>>.onEachSuccess(action: suspend (T) -> Unit): Flow<DataResult<T>> {
    return onEach { it.onSuccess(action) }
}

fun <T> Flow<DataResult<T>>.onEachFailure(action: suspend (Error) -> Unit): Flow<DataResult<T>> {
    return onEach { it.onFailure(action) }
}
