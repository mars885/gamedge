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

package com.paulrybitskyi.hiltbinder.processor.model

internal enum class HiltComponent(
    val title: String,
    val typeName: String,
    val scopeName: String
) {

    SINGLETON(
        title = "SingletonComponent",
        typeName = "dagger.hilt.components.SingletonComponent",
        scopeName = "javax.inject.Singleton"
    ),
    ACTIVITY_RETAINED(
        title = "ActivityRetainedComponent",
        typeName = "dagger.hilt.android.components.ActivityRetainedComponent",
        scopeName = "dagger.hilt.android.scopes.ActivityRetainedScoped"
    ),
    SERVICE(
        title = "ServiceComponent",
        typeName = "dagger.hilt.android.components.ServiceComponent",
        scopeName = "dagger.hilt.android.scopes.ServiceScoped"
    ),
    ACTIVITY(
        title = "ActivityComponent",
        typeName = "dagger.hilt.android.components.ActivityComponent",
        scopeName = "dagger.hilt.android.scopes.ActivityScoped"
    ),
    VIEW_MODEL(
        title = "ViewModelComponent",
        typeName = "dagger.hilt.android.components.ViewModelComponent",
        scopeName = "dagger.hilt.android.scopes.ViewModelScoped"
    ),
    FRAGMENT(
        title = "FragmentComponent",
        typeName = "dagger.hilt.android.components.FragmentComponent",
        scopeName = "dagger.hilt.android.scopes.FragmentScoped"
    ),
    VIEW(
        title = "ViewComponent",
        typeName = "dagger.hilt.android.components.ViewComponent",
        scopeName = "dagger.hilt.android.scopes.ViewScoped"
    ),
    VIEW_WITH_FRAGMENT(
        title = "ViewWithFragmentComponent",
        typeName = "dagger.hilt.android.components.ViewWithFragmentComponent",
        scopeName = "dagger.hilt.android.scopes.ViewScoped"
    )

}