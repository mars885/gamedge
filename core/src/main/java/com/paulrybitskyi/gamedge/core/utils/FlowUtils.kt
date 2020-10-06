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

package com.paulrybitskyi.gamedge.core.utils

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.gamedge.domain.commons.DomainException
import com.paulrybitskyi.gamedge.domain.commons.entities.Error
import kotlinx.coroutines.flow.*
import java.io.Serializable


data class Tuple4<T1, T2, T3, T4>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4
) : Serializable


data class Tuple5<T1, T2, T3, T4, T5>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4,
    val fifth: T5
) : Serializable


fun <T1, T2> combine(
    f1: Flow<T1>,
    f2: Flow<T2>
) : Flow<Pair<T1, T2>> {
    return combine(f1, f2) { t1, t2 ->
        Pair(t1, t2)
    }
}


fun <T1, T2, T3> combine(
    f1: Flow<T1>,
    f2: Flow<T2>,
    f3: Flow<T3>
) : Flow<Triple<T1, T2, T3>> {
    return combine(f1, f2, f3) { t1, t2, t3 ->
        Triple(t1, t2, t3)
    }
}


fun <T1, T2, T3, T4> combine(
    f1: Flow<T1>,
    f2: Flow<T2>,
    f3: Flow<T3>,
    f4: Flow<T4>
) : Flow<Tuple4<T1, T2, T3, T4>> {
    return combine(f1, f2, f3, f4) { t1, t2, t3, t4 ->
        Tuple4(t1, t2, t3, t4)
    }
}


fun <T1, T2, T3, T4, T5> combine(
    f1: Flow<T1>,
    f2: Flow<T2>,
    f3: Flow<T3>,
    f4: Flow<T4>,
    f5: Flow<T5>
) : Flow<Tuple5<T1, T2, T3, T4, T5>> {
    return combine(f1, f2, f3, f4, f5) { t1, t2, t3, t4, t5 ->
        Tuple5(t1, t2, t3, t4, t5)
    }
}


fun <S1, E1, S2, E2> Flow<Result<S1, E1>>.mapResult(
    success: (S1) -> S2,
    failure: (E1) -> E2
): Flow<Result<S2, E2>> {
    return map { it.mapEither(success, failure) }
}


fun <T> Flow<Result<T, Error>>.resultOrError(): Flow<T> {
    return map {
        if(it is Ok) return@map it.value
        if(it is Err) throw DomainException(it.error)

        throw IllegalStateException("The result is neither Ok or Err.")
    }
}


fun <T> Flow<T>.onError(action: suspend FlowCollector<T>.(cause: Throwable) -> Unit): Flow<T> = catch(action)


fun <T> Flow<T>.onEachError(action: (cause: Throwable) -> Unit): Flow<T> {
    return onError {
        action(it)
        throw it
    }
}