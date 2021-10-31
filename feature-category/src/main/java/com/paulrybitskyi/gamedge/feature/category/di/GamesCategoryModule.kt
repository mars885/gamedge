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

package com.paulrybitskyi.gamedge.feature.category.di

import com.paulrybitskyi.gamedge.domain.games.ObservableGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.RefreshableGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObserveComingSoonGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObserveMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObservePopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObserveRecentlyReleasedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.RefreshComingSoonGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.RefreshMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.RefreshPopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.RefreshRecentlyReleasedGamesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object GamesCategoryModule {


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategoryKey.Type.POPULAR)
    fun providePopularGamesObserverUseCase(
        observePopularGamesUseCase: ObservePopularGamesUseCase
    ): ObservableGamesUseCase {
        return observePopularGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategoryKey.Type.POPULAR)
    fun providePopularGamesRefresherUseCase(
        refreshPopularGamesUseCase: RefreshPopularGamesUseCase
    ): RefreshableGamesUseCase {
        return refreshPopularGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategoryKey.Type.RECENTLY_RELEASED)
    fun provideRecentlyReleasedGamesObserverUseCase(
        observeRecentlyReleasedGamesUseCase: ObserveRecentlyReleasedGamesUseCase
    ): ObservableGamesUseCase {
        return observeRecentlyReleasedGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategoryKey.Type.RECENTLY_RELEASED)
    fun provideRecentlyReleasedGamesRefresherUseCase(
        refreshRecentlyReleasedGamesUseCase: RefreshRecentlyReleasedGamesUseCase
    ): RefreshableGamesUseCase {
        return refreshRecentlyReleasedGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategoryKey.Type.COMING_SOON)
    fun provideComingSoonGamesObserverUseCase(
        observeComingSoonGamesUseCase: ObserveComingSoonGamesUseCase
    ): ObservableGamesUseCase {
        return observeComingSoonGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategoryKey.Type.COMING_SOON)
    fun provideComingSoonGamesRefresherUseCase(
        refreshComingSoonGamesUseCase: RefreshComingSoonGamesUseCase
    ): RefreshableGamesUseCase {
        return refreshComingSoonGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategoryKey.Type.MOST_ANTICIPATED)
    fun provideMostAnticipatedGamesObserverUseCase(
        observeMostAnticipatedGamesUseCase: ObserveMostAnticipatedGamesUseCase
    ): ObservableGamesUseCase {
        return observeMostAnticipatedGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesCategoryKey(GamesCategoryKey.Type.MOST_ANTICIPATED)
    fun provideMostAnticipatedGamesRefresherUseCase(
        refreshMostAnticipatedGamesUseCase: RefreshMostAnticipatedGamesUseCase
    ): RefreshableGamesUseCase {
        return refreshMostAnticipatedGamesUseCase
    }


}
