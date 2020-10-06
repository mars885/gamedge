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

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner


internal inline fun Lifecycle.addObserver(
    crossinline onCreate: (LifecycleOwner) -> Unit = {},
    crossinline onStart: (LifecycleOwner) -> Unit = {},
    crossinline onResume: (LifecycleOwner) -> Unit = {},
    crossinline onPause: (LifecycleOwner) -> Unit = {},
    crossinline onStop: (LifecycleOwner) -> Unit = {},
    crossinline onDestroy: (LifecycleOwner) -> Unit = {}
): DefaultLifecycleObserver {
    return object : DefaultLifecycleObserver {

        override fun onCreate(owner: LifecycleOwner) = onCreate(owner)
        override fun onStart(owner: LifecycleOwner) = onStart(owner)
        override fun onResume(owner: LifecycleOwner) = onResume(owner)
        override fun onPause(owner: LifecycleOwner) = onPause(owner)
        override fun onStop(owner: LifecycleOwner) = onStop(owner)
        override fun onDestroy(owner: LifecycleOwner) = onDestroy(owner)

    }
    .also(::addObserver)
}