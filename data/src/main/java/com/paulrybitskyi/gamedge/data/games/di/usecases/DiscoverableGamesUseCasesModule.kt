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

package com.paulrybitskyi.gamedge.data.games.di.usecases

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.commons.GamesDataStores
import com.paulrybitskyi.gamedge.data.games.usecases.commons.ObserveGamesUseCaseMappers
import com.paulrybitskyi.gamedge.data.games.usecases.commons.RefreshGamesUseCaseMappers
import com.paulrybitskyi.gamedge.data.games.usecases.commons.throttling.GamesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.data.games.usecases.discovery.*
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal object DiscoverableGamesUseCasesModule {


    @Provides
    @Singleton
    fun provideComingSoonGamesObservableUseCase(
        gamesLocalDataStore: GamesLocalDataStore,
        dispatcherProvider: DispatcherProvider,
        mappers: ObserveGamesUseCaseMappers
    ): ObserveComingSoonGamesUseCase {
        return ObserveComingSoonGamesUseCaseImpl(
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideComingSoonGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        dispatcherProvider: DispatcherProvider,
        throttlerTools: GamesRefreshingThrottlerTools,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshComingSoonGamesUseCase {
        return RefreshComingSoonGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            dispatcherProvider = dispatcherProvider,
            throttlerTools = throttlerTools,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideMostAnticipatedGamesObservableUseCase(
        gamesLocalDataStore: GamesLocalDataStore,
        dispatcherProvider: DispatcherProvider,
        mappers: ObserveGamesUseCaseMappers
    ): ObserveMostAnticipatedGamesUseCase {
        return ObserveMostAnticipatedGamesUseCaseImpl(
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideMostAnticipatedGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        dispatcherProvider: DispatcherProvider,
        throttlerTools: GamesRefreshingThrottlerTools,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshMostAnticipatedGamesUseCase {
        return RefreshMostAnticipatedGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            dispatcherProvider = dispatcherProvider,
            throttlerTools = throttlerTools,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun providePopularGamesObservableUseCase(
        gamesLocalDataStore: GamesLocalDataStore,
        dispatcherProvider: DispatcherProvider,
        mappers: ObserveGamesUseCaseMappers
    ): ObservePopularGamesUseCase {
        return ObservePopularGamesUseCaseImpl(
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun providePopularGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        dispatcherProvider: DispatcherProvider,
        throttlerTools: GamesRefreshingThrottlerTools,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshPopularGamesUseCase {
        return RefreshPopularGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            dispatcherProvider = dispatcherProvider,
            throttlerTools = throttlerTools,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideRecentlyReleasedGamesObservableUseCase(
        gamesLocalDataStore: GamesLocalDataStore,
        dispatcherProvider: DispatcherProvider,
        mappers: ObserveGamesUseCaseMappers
    ): ObserveRecentlyReleasedGamesUseCase {
        return ObserveRecentlyReleasedGamesUseCaseImpl(
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideRecentlyReleasedGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        dispatcherProvider: DispatcherProvider,
        throttlerTools: GamesRefreshingThrottlerTools,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshRecentlyReleasedGamesUseCase {
        return RefreshRecentlyReleasedGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            dispatcherProvider = dispatcherProvider,
            throttlerTools = throttlerTools,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideAllDiscoverableGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        dispatcherProvider: DispatcherProvider,
        throttlerTools: GamesRefreshingThrottlerTools,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshAllDiscoverableGamesUseCase {
        return RefreshAllDiscoverableGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            dispatcherProvider = dispatcherProvider,
            throttlerTools = throttlerTools,
            mappers = mappers
        )
    }


}