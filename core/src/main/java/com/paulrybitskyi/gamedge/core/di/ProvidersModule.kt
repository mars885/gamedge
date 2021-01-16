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

package com.paulrybitskyi.gamedge.core.di

import com.paulrybitskyi.gamedge.core.providers.*
import com.paulrybitskyi.gamedge.core.providers.DispatcherProviderImpl
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProviderImpl
import com.paulrybitskyi.gamedge.core.providers.StringProviderImpl
import com.paulrybitskyi.gamedge.core.providers.TimestampProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ProvidersModule {

    @Binds
    fun bindCustomTabsProviderImpl(provider: CustomTabsProviderImpl): CustomTabsProvider

    @Binds
    fun bindDispatcherProviderImpl(provider: DispatcherProviderImpl): DispatcherProvider

    @Binds
    fun bindNetworkStateProviderImpl(provider: NetworkStateProviderImpl): NetworkStateProvider

    @Binds
    fun bindStringProviderImpl(provider: StringProviderImpl): StringProvider

    @Binds
    fun bindTimestampProviderImpl(provider: TimestampProviderImpl): TimestampProvider

}