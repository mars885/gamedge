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

package com.paulrybitskyi.gamedge.data.di

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.data.datastores.GamesServerDataStore
import com.paulrybitskyi.gamedge.data.usecases.mappers.EntityMapper
import com.paulrybitskyi.gamedge.data.usecases.mappers.PaginationMapper
import com.paulrybitskyi.gamedge.data.usecases.refreshers.RefreshComingSoonGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.usecases.refreshers.RefreshMostAnticipatedGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.usecases.refreshers.RefreshPopularGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.usecases.refreshers.RefreshRecentlyReleasedGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.utils.GamesRefreshingThrottler
import com.paulrybitskyi.gamedge.domain.usecases.games.refreshers.RefreshComingSoonGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.games.refreshers.RefreshMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.games.refreshers.RefreshPopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.games.refreshers.RefreshRecentlyReleasedGamesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal object RefresherUseCasesModule {


    @Singleton
    @Provides
    fun provideComingSoonGamesRefresherUseCase(
        gamesServerDataStore: GamesServerDataStore,
        gamesDatabaseDataStore: GamesDatabaseDataStore,
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        dispatcherProvider: DispatcherProvider,
        paginationMapper: PaginationMapper,
        entityMapper: EntityMapper
    ): RefreshComingSoonGamesUseCase {
        return RefreshComingSoonGamesUseCaseImpl(
            gamesServerDataStore = gamesServerDataStore,
            gamesDatabaseDataStore = gamesDatabaseDataStore,
            gamesRefreshingThrottler = gamesRefreshingThrottler,
            dispatcherProvider = dispatcherProvider,
            paginationMapper = paginationMapper,
            entityMapper = entityMapper
        )
    }


    @Singleton
    @Provides
    fun provideMostAnticipatedGamesRefresherUseCase(
        gamesServerDataStore: GamesServerDataStore,
        gamesDatabaseDataStore: GamesDatabaseDataStore,
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        dispatcherProvider: DispatcherProvider,
        paginationMapper: PaginationMapper,
        entityMapper: EntityMapper
    ): RefreshMostAnticipatedGamesUseCase {
        return RefreshMostAnticipatedGamesUseCaseImpl(
            gamesServerDataStore = gamesServerDataStore,
            gamesDatabaseDataStore = gamesDatabaseDataStore,
            gamesRefreshingThrottler = gamesRefreshingThrottler,
            dispatcherProvider = dispatcherProvider,
            paginationMapper = paginationMapper,
            entityMapper = entityMapper
        )
    }


    @Singleton
    @Provides
    fun providePopularGamesRefresherUseCase(
        gamesServerDataStore: GamesServerDataStore,
        gamesDatabaseDataStore: GamesDatabaseDataStore,
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        dispatcherProvider: DispatcherProvider,
        paginationMapper: PaginationMapper,
        entityMapper: EntityMapper
    ): RefreshPopularGamesUseCase {
        return RefreshPopularGamesUseCaseImpl(
            gamesServerDataStore = gamesServerDataStore,
            gamesDatabaseDataStore = gamesDatabaseDataStore,
            gamesRefreshingThrottler = gamesRefreshingThrottler,
            dispatcherProvider = dispatcherProvider,
            paginationMapper = paginationMapper,
            entityMapper = entityMapper
        )
    }


    @Singleton
    @Provides
    fun provideRecentlyReleasedGamesRefresherUseCase(
        gamesServerDataStore: GamesServerDataStore,
        gamesDatabaseDataStore: GamesDatabaseDataStore,
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        dispatcherProvider: DispatcherProvider,
        paginationMapper: PaginationMapper,
        entityMapper: EntityMapper
    ): RefreshRecentlyReleasedGamesUseCase {
        return RefreshRecentlyReleasedGamesUseCaseImpl(
            gamesServerDataStore = gamesServerDataStore,
            gamesDatabaseDataStore = gamesDatabaseDataStore,
            gamesRefreshingThrottler = gamesRefreshingThrottler,
            dispatcherProvider = dispatcherProvider,
            paginationMapper = paginationMapper,
            entityMapper = entityMapper
        )
    }


}
