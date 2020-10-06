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

package com.paulrybitskyi.gamedge.ui.discovery

import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategory
import com.paulrybitskyi.gamedge.core.ImageUrlBuilder
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.games.usecases.observers.ObserveComingSoonGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.observers.ObserveMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.observers.ObservePopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.observers.ObserveRecentlyReleasedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshComingSoonGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshPopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshRecentlyReleasedGamesUseCase
import com.paulrybitskyi.gamedge.ui.discovery.mapping.GamesDiscoveryItemGameModelMapper
import com.paulrybitskyi.gamedge.ui.discovery.mapping.GamesDiscoveryItemGameModelMapperImpl
import com.paulrybitskyi.gamedge.ui.discovery.mapping.GamesDiscoveryItemModelFactory
import com.paulrybitskyi.gamedge.ui.discovery.mapping.GamesDiscoveryItemModelFactoryImpl
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object GamesDiscoveryModule {


    @MapKey
    internal annotation class GamesCategoryKey(val category: GamesCategory)


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategory.POPULAR)
    fun providePopularGamesUseCases(
        observePopularGamesUseCase: ObservePopularGamesUseCase,
        refreshPopularGamesUseCase: RefreshPopularGamesUseCase,
    ): GamesDiscoveryUseCases {
        return GamesDiscoveryUseCases(
            observePopularGamesUseCase,
            refreshPopularGamesUseCase
        )
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategory.RECENTLY_RELEASED)
    fun provideRecentlyReleasedGamesUseCases(
        observeRecentlyReleasedGamesUseCase: ObserveRecentlyReleasedGamesUseCase,
        refreshRecentlyReleasedGamesUseCase: RefreshRecentlyReleasedGamesUseCase,
    ): GamesDiscoveryUseCases {
        return GamesDiscoveryUseCases(
            observeRecentlyReleasedGamesUseCase,
            refreshRecentlyReleasedGamesUseCase
        )
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategory.COMING_SOON)
    fun provideComingSoonGamesUseCases(
        observeComingSoonGamesUseCase: ObserveComingSoonGamesUseCase,
        refreshComingSoonGamesUseCase: RefreshComingSoonGamesUseCase,
    ): GamesDiscoveryUseCases {
        return GamesDiscoveryUseCases(
            observeComingSoonGamesUseCase,
            refreshComingSoonGamesUseCase
        )
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategory.MOST_ANTICIPATED)
    fun provideMostAnticipatedGamesUseCases(
        observeMostAnticipatedGamesUseCase: ObserveMostAnticipatedGamesUseCase,
        refreshMostAnticipatedGamesUseCase: RefreshMostAnticipatedGamesUseCase,
    ): GamesDiscoveryUseCases {
        return GamesDiscoveryUseCases(
            observeMostAnticipatedGamesUseCase,
            refreshMostAnticipatedGamesUseCase
        )
    }


    @Provides
    fun provideDiscoveryItemModelFactory(stringProvider: StringProvider): GamesDiscoveryItemModelFactory {
        return GamesDiscoveryItemModelFactoryImpl(stringProvider)
    }


    @Provides
    fun provideDiscoveryItemGameModelMapper(
        imageUrlBuilder: ImageUrlBuilder
    ): GamesDiscoveryItemGameModelMapper {
        return GamesDiscoveryItemGameModelMapperImpl(imageUrlBuilder)
    }


}