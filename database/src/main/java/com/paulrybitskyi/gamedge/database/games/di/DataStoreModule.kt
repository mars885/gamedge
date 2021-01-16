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

package com.paulrybitskyi.gamedge.database.games.di

import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.database.games.datastores.GamesDatabaseDataStoreImpl
import com.paulrybitskyi.gamedge.database.games.datastores.LikedGamesLocalDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataStoreModule {

    @Binds
    fun bindGamesDatabaseDataStoreImpl(dataStore: GamesDatabaseDataStoreImpl): GamesLocalDataStore

    @Binds
    fun bindLikedGamesLocalDataStoreImpl(dataStore: LikedGamesLocalDataStoreImpl): LikedGamesLocalDataStore

}