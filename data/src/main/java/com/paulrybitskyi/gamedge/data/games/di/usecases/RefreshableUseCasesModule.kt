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
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.data.games.GamesRefreshingThrottler
import com.paulrybitskyi.gamedge.data.games.datastores.commons.GamesDataStores
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.PaginationMapper
import com.paulrybitskyi.gamedge.data.games.usecases.refreshers.*
import com.paulrybitskyi.gamedge.data.games.usecases.refreshers.commons.RefreshGamesUseCaseMappers
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal object RefreshableUseCasesModule {


    @Singleton
    @Provides
    fun provideComingSoonGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        dispatcherProvider: DispatcherProvider,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshComingSoonGamesUseCase {
        return RefreshComingSoonGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            gamesRefreshingThrottler = gamesRefreshingThrottler,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Singleton
    @Provides
    fun provideMostAnticipatedGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        dispatcherProvider: DispatcherProvider,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshMostAnticipatedGamesUseCase {
        return RefreshMostAnticipatedGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            gamesRefreshingThrottler = gamesRefreshingThrottler,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Singleton
    @Provides
    fun providePopularGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        dispatcherProvider: DispatcherProvider,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshPopularGamesUseCase {
        return RefreshPopularGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            gamesRefreshingThrottler = gamesRefreshingThrottler,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Singleton
    @Provides
    fun provideRecentlyReleasedGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        dispatcherProvider: DispatcherProvider,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshRecentlyReleasedGamesUseCase {
        return RefreshRecentlyReleasedGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            gamesRefreshingThrottler = gamesRefreshingThrottler,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Singleton
    @Provides
    fun provideAllGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        dispatcherProvider: DispatcherProvider,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshAllGamesUseCase {
        return RefreshAllGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            gamesRefreshingThrottler = gamesRefreshingThrottler,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Provides
    fun provideGamesRefresherUseCaseMappers(
        gameMapper: GameMapper,
        paginationMapper: PaginationMapper,
        errorMapper: ErrorMapper
    ): RefreshGamesUseCaseMappers {
        return RefreshGamesUseCaseMappers(
            game = gameMapper,
            pagination = paginationMapper,
            error = errorMapper
        )
    }


}
