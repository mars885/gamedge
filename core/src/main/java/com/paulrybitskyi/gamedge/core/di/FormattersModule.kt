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

import com.paulrybitskyi.gamedge.core.formatters.*
import com.paulrybitskyi.gamedge.core.formatters.GameAgeRatingFormatterImpl
import com.paulrybitskyi.gamedge.core.formatters.GameCategoryFormatterImpl
import com.paulrybitskyi.gamedge.core.formatters.GameRatingFormatterImpl
import com.paulrybitskyi.gamedge.core.formatters.GameReleaseDateFormatterImpl
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object FormattersModule {


    @Provides
    fun provideGameReleaseDateFormatter(stringProvider: StringProvider): GameReleaseDateFormatter {
        return GameReleaseDateFormatterImpl(stringProvider)
    }


    @Provides
    fun provideGameRatingFormatter(stringProvider: StringProvider): GameRatingFormatter {
        return GameRatingFormatterImpl(stringProvider)
    }


    @Provides
    fun provideGameAgeRatingFormatter(stringProvider: StringProvider): GameAgeRatingFormatter {
        return GameAgeRatingFormatterImpl(stringProvider)
    }


    @Provides
    fun provideGameCategoryFormatter(stringProvider: StringProvider): GameCategoryFormatter {
        return GameCategoryFormatterImpl(stringProvider)
    }


}