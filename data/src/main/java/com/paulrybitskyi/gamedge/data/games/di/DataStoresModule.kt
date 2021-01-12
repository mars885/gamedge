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

package com.paulrybitskyi.gamedge.data.games.di

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.GamesRemoteDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStoreImpl
import com.paulrybitskyi.gamedge.data.games.datastores.commons.GamesDataStores
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal object DataStoresModule {


    @Provides
    @Singleton
    fun provideLikedGamesLocalDataStore(
        gamesPreferences: DataStore<Preferences>
    ): LikedGamesLocalDataStore {
        return LikedGamesLocalDataStoreImpl(gamesPreferences)
    }


    @Provides
    fun provideGamesDataStores(
        gamesLocalDataStore: GamesLocalDataStore,
        gamesRemoteDataStore: GamesRemoteDataStore
    ): GamesDataStores {
        return GamesDataStores(
            local = gamesLocalDataStore,
            remote = gamesRemoteDataStore
        )
    }


}