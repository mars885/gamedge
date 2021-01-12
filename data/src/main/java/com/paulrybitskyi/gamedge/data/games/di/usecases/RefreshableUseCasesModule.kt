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

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.data.games.datastores.commons.GamesDataStores
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.PaginationMapper
import com.paulrybitskyi.gamedge.data.games.usecases.refreshers.*
import com.paulrybitskyi.gamedge.data.games.usecases.refreshers.commons.*
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal object RefreshableUseCasesModule {


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
    fun provideAllGamesRefresherUseCase(
        gamesDataStores: GamesDataStores,
        dispatcherProvider: DispatcherProvider,
        throttlerTools: GamesRefreshingThrottlerTools,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshAllGamesUseCase {
        return RefreshAllGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            dispatcherProvider = dispatcherProvider,
            throttlerTools = throttlerTools,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideRefreshCompanyDevelopedGamesUseCase(
        gamesDataStores: GamesDataStores,
        dispatcherProvider: DispatcherProvider,
        throttlerTools: GamesRefreshingThrottlerTools,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshCompanyDevelopedGamesUseCase {
        return RefreshCompanyDevelopedGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            dispatcherProvider = dispatcherProvider,
            throttlerTools = throttlerTools,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideRefreshSimilarGamesUseCase(
        gamesDataStores: GamesDataStores,
        dispatcherProvider: DispatcherProvider,
        throttlerTools: GamesRefreshingThrottlerTools,
        mappers: RefreshGamesUseCaseMappers
    ): RefreshSimilarGamesUseCase {
        return RefreshSimilarGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            dispatcherProvider = dispatcherProvider,
            throttlerTools = throttlerTools,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideGamesRefreshingThrottler(
        gamesPreferences: DataStore<Preferences>,
        timestampProvider: TimestampProvider
    ): GamesRefreshingThrottler {
        return GamesRefreshingThrottlerImpl(
            gamesPreferences = gamesPreferences,
            timestampProvider = timestampProvider
        )
    }


    @Provides
    fun provideGamesRefreshingThrottlerKeyBuilder(): GamesRefreshingThrottlerKeyBuilder {
        return GamesRefreshingThrottlerKeyBuilderImpl()
    }


    @Provides
    fun provideGamesRefreshingThrottlerTools(
        gamesRefreshingThrottler: GamesRefreshingThrottler,
        gamesRefreshingThrottlerKeyBuilder: GamesRefreshingThrottlerKeyBuilder
    ): GamesRefreshingThrottlerTools {
        return GamesRefreshingThrottlerTools(
            throttler = gamesRefreshingThrottler,
            keyBuilder = gamesRefreshingThrottlerKeyBuilder
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
