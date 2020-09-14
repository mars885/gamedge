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

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.data.Constants
import com.paulrybitskyi.gamedge.data.utils.GamesRefreshingThrottler
import com.paulrybitskyi.gamedge.data.utils.GamesRefreshingThrottlerImpl
import com.paulrybitskyi.gamedge.data.usecases.mappers.EntityMapper
import com.paulrybitskyi.gamedge.data.usecases.mappers.PaginationMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal object CommonsModule {


    @Singleton
    @Provides
    fun provideGamesRefreshingThrottler(
        dataStore: DataStore<Preferences>,
        timestampProvider: TimestampProvider
    ): GamesRefreshingThrottler {
        return GamesRefreshingThrottlerImpl(
            dataStore = dataStore,
            timestampProvider = timestampProvider
        )
    }


    @Provides
    fun provideGamesPreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.createDataStore(Constants.GAMES_PREFERENCES_DATA_STORE_NAME)
    }


    @Provides
    fun provideEntityMapper(): EntityMapper {
        return EntityMapper()
    }


    @Provides
    fun providePaginationMapper(): PaginationMapper {
        return PaginationMapper()
    }


}