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

package com.paulrybitskyi.gamedge.ui.commons

import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.core.formatters.GameReleaseDateFormatter
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object CommonsModule {


    @Provides
    fun provideErrorMapper(stringProvider: StringProvider): ErrorMapper {
        return ErrorMapperImpl(stringProvider)
    }


    @Provides
    fun provideGameModelFactory(
        igdbImageUrlBuilder: IgdbImageUrlBuilder,
        gameReleaseDateFormatter: GameReleaseDateFormatter
    ): GameModelFactory {
        return GameModelFactoryImpl(
            igdbImageUrlBuilder = igdbImageUrlBuilder,
            gameReleaseDateFormatter = gameReleaseDateFormatter
        )
    }


    @Provides
    fun provideGamesUiStateFactory(gameModelFactory: GameModelFactory): GamesUiStateFactory {
        return GamesUiStateFactoryImpl(gameModelFactory)
    }


}