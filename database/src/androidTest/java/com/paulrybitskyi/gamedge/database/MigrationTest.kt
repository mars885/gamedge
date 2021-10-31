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

package com.paulrybitskyi.gamedge.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import javax.inject.Provider
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// https://developer.android.com/training/data-storage/room/migrating-db-versions
@HiltAndroidTest
internal class MigrationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val migrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        GamedgeDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Inject lateinit var databaseProvider: Provider<GamedgeDatabase>

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun run_all_migrations() {
        migrationTestHelper.createDatabase(Constants.DATABASE_NAME, 1).close()

        databaseProvider.get().apply {
            openHelper.writableDatabase
            close()
        }
    }
}
