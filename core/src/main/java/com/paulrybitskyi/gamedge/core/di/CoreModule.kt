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

import com.paulrybitskyi.gamedge.core.*
import com.paulrybitskyi.gamedge.core.GamedgeLogger
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilderImpl
import com.paulrybitskyi.gamedge.core.YoutubeMediaUrlBuilder
import com.paulrybitskyi.gamedge.core.YoutubeMediaUrlBuilderImpl
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
internal object CoreModule {


    @Provides
    fun provideIgdbImageUrlBuilder(): IgdbImageUrlBuilder {
        return IgdbImageUrlBuilderImpl()
    }


    @Provides
    fun provideYoutubeMediaUrlBuilder(): YoutubeMediaUrlBuilder {
        return YoutubeMediaUrlBuilderImpl()
    }


    @Provides
    fun provideGameLikeCountCalculator(): GameLikeCountCalculator {
        return GameLikeCountCalculatorImpl()
    }


    @Provides
    fun provideWebsiteNameRetriever(stringProvider: StringProvider): WebsiteNameRetriever {
        return WebsiteNameRetrieverImpl(stringProvider)
    }


    @Provides
    fun provideWebsiteIconRetriever(): WebsiteIconRetriever {
        return WebsiteIconRetrieverImpl()
    }


    @Provides
    fun provideLogger(): Logger {
        return GamedgeLogger()
    }


}