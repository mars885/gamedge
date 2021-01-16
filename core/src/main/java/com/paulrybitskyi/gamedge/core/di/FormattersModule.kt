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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FormattersModule {

    @Binds
    fun bindGameReleaseDateFormatterImpl(formatter: GameReleaseDateFormatterImpl): GameReleaseDateFormatter

    @Binds
    fun bindGameRatingFormatterImpl(formatter: GameRatingFormatterImpl): GameRatingFormatter

    @Binds
    fun bindGameAgeRatingFormatterImpl(formatter: GameAgeRatingFormatterImpl): GameAgeRatingFormatter

    @Binds
    fun bindGameCategoryFormatterImpl(formatter: GameCategoryFormatterImpl): GameCategoryFormatter

}