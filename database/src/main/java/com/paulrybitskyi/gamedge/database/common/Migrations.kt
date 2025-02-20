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

package com.paulrybitskyi.gamedge.database.common

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private val MIGRATION_FROM_1_TO_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) = with(db) {
        execSQL(
            """
            CREATE TABLE IF NOT EXISTS new_games (
                id INTEGER NOT NULL, follower_count INTEGER, hype_count INTEGER, release_date INTEGER,
                critics_rating REAL, users_rating REAL, total_rating REAL, name TEXT NOT NULL,
                summary TEXT, storyline TEXT, category TEXT NOT NULL, cover TEXT,
                release_dates TEXT NOT NULL, age_ratings TEXT NOT NULL, videos TEXT NOT NULL,
                artworks TEXT NOT NULL, screenshots TEXT NOT NULL,
                genres TEXT NOT NULL, platforms TEXT NOT NULL, player_perspectives TEXT NOT NULL,
                themes TEXT NOT NULL, modes TEXT NOT NULL,
                keywords TEXT NOT NULL, involved_companies TEXT NOT NULL,
                websites TEXT NOT NULL, similar_games TEXT NOT NULL, PRIMARY KEY(id)
            )
            """.trimIndent(),
        )

        execSQL("INSERT INTO new_games SELECT * FROM games")
        execSQL("DROP TABLE games")
        execSQL("ALTER TABLE new_games RENAME TO games")

        execSQL("CREATE INDEX index_games_hype_count ON games(hype_count)")
        execSQL("CREATE INDEX index_games_release_date ON games(release_date)")
        execSQL("CREATE INDEX index_games_users_rating ON games(users_rating)")
        execSQL("CREATE INDEX index_games_total_rating ON games(total_rating)")
        execSQL("CREATE INDEX index_games_name ON games(name)")
    }
}

internal val MIGRATIONS = arrayOf<Migration>(
    MIGRATION_FROM_1_TO_2,
)
