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

import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryCategory
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.games.ObservableGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObserveComingSoonGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObserveMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObservePopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObserveRecentlyReleasedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.RefreshAllDiscoverableGamesUseCase
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
    internal annotation class GamesDiscoveryCategoryKey(val category: GamesDiscoveryCategory)


    @Provides
    fun provideGamesDiscoveryUseCases(
        observeGamesUseCasesMap: Map<GamesDiscoveryCategory, @JvmSuppressWildcards ObservableGamesUseCase>,
        refreshAllDiscoverableGamesUseCase: RefreshAllDiscoverableGamesUseCase
    ): GamesDiscoveryUseCases {
        return GamesDiscoveryUseCases(
            observeGamesUseCasesMap = observeGamesUseCasesMap,
            refreshAllDiscoverableGamesUseCase = refreshAllDiscoverableGamesUseCase
        )
    }


    @Provides
    @IntoMap
    @GamesDiscoveryCategoryKey(GamesDiscoveryCategory.POPULAR)
    fun providePopularGamesUseCases(
        observePopularGamesUseCase: ObservePopularGamesUseCase
    ): ObservableGamesUseCase {
        return observePopularGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesDiscoveryCategoryKey(GamesDiscoveryCategory.RECENTLY_RELEASED)
    fun provideRecentlyReleasedGamesUseCases(
        observeRecentlyReleasedGamesUseCase: ObserveRecentlyReleasedGamesUseCase
    ): ObservableGamesUseCase {
        return observeRecentlyReleasedGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesDiscoveryCategoryKey(GamesDiscoveryCategory.COMING_SOON)
    fun provideComingSoonGamesUseCases(
        observeComingSoonGamesUseCase: ObserveComingSoonGamesUseCase
    ): ObservableGamesUseCase {
        return observeComingSoonGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesDiscoveryCategoryKey(GamesDiscoveryCategory.MOST_ANTICIPATED)
    fun provideMostAnticipatedGamesUseCases(
        observeMostAnticipatedGamesUseCase: ObserveMostAnticipatedGamesUseCase
    ): ObservableGamesUseCase {
        return observeMostAnticipatedGamesUseCase
    }


    @Provides
    fun provideDiscoveryItemModelFactory(stringProvider: StringProvider): GamesDiscoveryItemModelFactory {
        return GamesDiscoveryItemModelFactoryImpl(stringProvider)
    }


    @Provides
    fun provideDiscoveryItemGameModelMapper(
        igdbImageUrlBuilder: IgdbImageUrlBuilder
    ): GamesDiscoveryItemGameModelMapper {
        return GamesDiscoveryItemGameModelMapperImpl(igdbImageUrlBuilder)
    }


}