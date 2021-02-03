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

package com.paulrybitskyi.gamedge.discovery.di

import com.paulrybitskyi.gamedge.domain.games.ObservableGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObserveComingSoonGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObserveMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObservePopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObserveRecentlyReleasedGamesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object GamesDiscoveryModule {


    @Provides
    @IntoMap
    @GamesDiscoveryKey(GamesDiscoveryKey.Type.POPULAR)
    fun providePopularGamesUseCases(
        observePopularGamesUseCase: ObservePopularGamesUseCase
    ): ObservableGamesUseCase {
        return observePopularGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesDiscoveryKey(GamesDiscoveryKey.Type.RECENTLY_RELEASED)
    fun provideRecentlyReleasedGamesUseCases(
        observeRecentlyReleasedGamesUseCase: ObserveRecentlyReleasedGamesUseCase
    ): ObservableGamesUseCase {
        return observeRecentlyReleasedGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesDiscoveryKey(GamesDiscoveryKey.Type.COMING_SOON)
    fun provideComingSoonGamesUseCases(
        observeComingSoonGamesUseCase: ObserveComingSoonGamesUseCase
    ): ObservableGamesUseCase {
        return observeComingSoonGamesUseCase
    }


    @Provides
    @IntoMap
    @GamesDiscoveryKey(GamesDiscoveryKey.Type.MOST_ANTICIPATED)
    fun provideMostAnticipatedGamesUseCases(
        observeMostAnticipatedGamesUseCase: ObserveMostAnticipatedGamesUseCase
    ): ObservableGamesUseCase {
        return observeMostAnticipatedGamesUseCase
    }


}