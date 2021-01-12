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

package com.paulrybitskyi.gamedge.igdb.api.games.di

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.games.datastores.GamesRemoteDataStore
import com.paulrybitskyi.gamedge.igdb.api.commons.ErrorMapper
import com.paulrybitskyi.gamedge.igdb.api.games.GamesEndpoint
import com.paulrybitskyi.gamedge.igdb.api.games.datastores.GameMapper
import com.paulrybitskyi.gamedge.igdb.api.games.datastores.GamesIgdbDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal object DataStoreModule {


    @Provides
    @Singleton
    fun provideGamesIgdbDataStore(
        gamesEndpoint: GamesEndpoint,
        dispatcherProvider: DispatcherProvider,
        gameMapper: GameMapper,
        errorMapper: ErrorMapper
    ): GamesRemoteDataStore {
        return GamesIgdbDataStoreImpl(
            gamesEndpoint = gamesEndpoint,
            dispatcherProvider = dispatcherProvider,
            gameMapper = gameMapper,
            errorMapper = errorMapper
        )
    }


    @Provides
    fun provideGameMapper(): GameMapper {
        return GameMapper()
    }


}