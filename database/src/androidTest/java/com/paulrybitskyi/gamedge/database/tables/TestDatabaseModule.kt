/*
 * Copyright 2021 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.database.tables

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.paulrybitskyi.gamedge.database.GamedgeDatabase
import com.paulrybitskyi.gamedge.database.common.MIGRATIONS
import com.paulrybitskyi.gamedge.database.common.RoomTypeConverter
import com.paulrybitskyi.gamedge.database.common.addTypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module
@DisableInstallInCheck
internal object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideGamedgeDatabase(
        typeConverters: Set<@JvmSuppressWildcards RoomTypeConverter>,
    ): GamedgeDatabase {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GamedgeDatabase::class.java,
        )
        .addTypeConverters(typeConverters)
        .addMigrations(*MIGRATIONS)
        .build()
    }
}
