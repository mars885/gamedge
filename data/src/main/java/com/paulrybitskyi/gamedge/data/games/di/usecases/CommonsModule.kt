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

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.ObserveGamesUseCaseMappers
import com.paulrybitskyi.gamedge.data.games.usecases.commons.PaginationMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.RefreshGamesUseCaseMappers
import com.paulrybitskyi.gamedge.data.games.usecases.commons.throttling.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal object CommonsModule {


    @Provides
    fun provideGameMapper(): GameMapper {
        return GameMapper()
    }


    @Provides
    fun providePaginationMapper(): PaginationMapper {
        return PaginationMapper()
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


}