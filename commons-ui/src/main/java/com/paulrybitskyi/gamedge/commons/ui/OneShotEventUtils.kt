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

package com.paulrybitskyi.gamedge.commons.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn

// https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
class FlowObserver<T>(
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>
) {


    private var job: Job? = null


    init {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
            @Suppress("NON_EXHAUSTIVE_WHEN")
            when(event) {

                Lifecycle.Event.ON_START -> {
                    job = flow.launchIn(source.lifecycleScope)
                }

                Lifecycle.Event.ON_STOP -> {
                    job?.cancel()
                    job = null
                }

            }
        })
    }


}


inline fun <reified T> Flow<T>.observeIn(lifecycleOwner: LifecycleOwner): FlowObserver<T> {
    return FlowObserver(lifecycleOwner, this)
}


class FragmentObserver<T>(
    fragment: Fragment,
    private val flow: Flow<T>
) {


    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
            FlowObserver(viewLifecycleOwner, flow)
        }
    }


}


inline fun <reified T> Flow<T>.observeIn(fragment: Fragment): FragmentObserver<T> {
    return FragmentObserver(fragment, this)
}