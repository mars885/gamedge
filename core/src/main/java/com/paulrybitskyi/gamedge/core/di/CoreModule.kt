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

import android.content.Context
import com.paulrybitskyi.gamedge.core.*
import com.paulrybitskyi.gamedge.core.GamedgeLogger
import com.paulrybitskyi.gamedge.core.ImageUrlBuilderImpl
import com.paulrybitskyi.gamedge.core.providers.*
import com.paulrybitskyi.gamedge.core.providers.DispatcherProviderImpl
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ApplicationComponent::class)
@Module
internal object CoreModule {


    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return DispatcherProviderImpl()
    }


    @Provides
    fun provideNetworkStateProvider(@ApplicationContext context: Context): NetworkStateProvider {
        return NetworkStateProviderImpl(context)
    }


    @Provides
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider {
        return StringProviderImpl(context)
    }


    @Provides
    fun provideTimestampProvider(): TimestampProvider {
        return TimestampProviderImpl()
    }


    @Provides
    fun provideImageUrlBuilder(): ImageUrlBuilder {
        return ImageUrlBuilderImpl()
    }


    @Provides
    fun provideLogger(): Logger {
        return GamedgeLogger()
    }


}