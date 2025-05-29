/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paulrybitskyi.gamedge.database.articles.ArticlesTypeConverter
import com.paulrybitskyi.gamedge.database.articles.entities.DbArticle
import com.paulrybitskyi.gamedge.database.articles.tables.ArticlesTable
import com.paulrybitskyi.gamedge.database.common.MIGRATION_FROM_2_TO_3_SPEC
import com.paulrybitskyi.gamedge.database.games.GamesTypeConverter
import com.paulrybitskyi.gamedge.database.games.entities.DbGame
import com.paulrybitskyi.gamedge.database.games.entities.DbLikedGame
import com.paulrybitskyi.gamedge.database.games.tables.GamesTable
import com.paulrybitskyi.gamedge.database.games.tables.LikedGamesTable

@Database(
    entities = [
        DbGame::class,
        DbLikedGame::class,
        DbArticle::class,
    ],
    version = Constants.VERSION,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3, MIGRATION_FROM_2_TO_3_SPEC::class),
    ],
)
// Seems really strange that I have to specify this annotation here
// with custom provided type converters
@TypeConverters(
    GamesTypeConverter::class,
    ArticlesTypeConverter::class,
)
internal abstract class GamedgeDatabase : RoomDatabase() {
    abstract val gamesTable: GamesTable
    abstract val likedGamesTable: LikedGamesTable
    abstract val articlesTable: ArticlesTable
}
