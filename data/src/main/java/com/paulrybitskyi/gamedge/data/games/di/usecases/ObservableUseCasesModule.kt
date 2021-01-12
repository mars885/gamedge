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

package com.paulrybitskyi.gamedge.data.games.di.usecases

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.PaginationMapper
import com.paulrybitskyi.gamedge.data.games.usecases.observers.ObserveComingSoonGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.observers.ObserveGameLikeStateUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.observers.ObserveMostAnticipatedGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.observers.ObservePopularGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.observers.ObserveRecentlyReleasedGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.observers.commons.ObserveGamesUseCaseMappers
import com.paulrybitskyi.gamedge.domain.games.usecases.observers.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal object ObservableUseCasesModule {


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
    fun provideGameLikeStateObservableUseCase(
        likedGamesLocalDataStore: LikedGamesLocalDataStore
    ): ObserveGameLikeStateUseCase {
        return ObserveGameLikeStateUseCaseImpl(likedGamesLocalDataStore)
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
    fun provideGamesObserverUseCaseMappers(
        gameMapper: GameMapper,
        paginationMapper: PaginationMapper
    ): ObserveGamesUseCaseMappers {
        return ObserveGamesUseCaseMappers(
            game = gameMapper,
            pagination = paginationMapper
        )
    }


}