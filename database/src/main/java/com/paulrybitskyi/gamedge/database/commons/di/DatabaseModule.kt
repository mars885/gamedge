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

package com.paulrybitskyi.gamedge.database.commons.di

import android.content.Context
import androidx.room.Room
import com.paulrybitskyi.gamedge.database.Constants
import com.paulrybitskyi.gamedge.database.GamedgeDatabase
import com.paulrybitskyi.gamedge.database.commons.MIGRATIONS
import com.paulrybitskyi.gamedge.database.commons.RoomTypeConverter
import com.paulrybitskyi.gamedge.database.commons.addTypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    @Suppress("SpreadOperator")
    fun provideGamedgeDatabase(
        @ApplicationContext context: Context,
        typeConverters: Set<@JvmSuppressWildcards RoomTypeConverter>
    ): GamedgeDatabase {
        return Room.databaseBuilder(
            context,
            GamedgeDatabase::class.java,
            Constants.DATABASE_NAME
        )
        .addTypeConverters(typeConverters)
        .addMigrations(*MIGRATIONS)
        .build()
    }
}
