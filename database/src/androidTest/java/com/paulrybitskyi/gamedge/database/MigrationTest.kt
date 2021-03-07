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

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.paulrybitskyi.gamedge.database.commons.MIGRATIONS
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


private const val TEST_DB_NAME = "migration-test"


// https://developer.android.com/training/data-storage/room/migrating-db-versions
@RunWith(AndroidJUnit4::class)
internal class MigrationTest {


    @get:Rule
    val migrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        GamedgeDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )


    @Test
    fun run_all_migrations() {
        migrationTestHelper.createDatabase(TEST_DB_NAME, 1).close()

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            GamedgeDatabase::class.java,
            TEST_DB_NAME
        ).addMigrations(*MIGRATIONS)
            .build()
            .apply {
                openHelper.writableDatabase
                close()
            }
    }


}