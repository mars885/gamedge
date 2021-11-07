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

package com.paulrybitskyi.gamedge.database.games.tables

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paulrybitskyi.gamedge.database.games.entities.Game
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GamesTable {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGames(games: List<Game>)

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE ${Game.Schema.ID} = :id
        """
    )
    suspend fun getGame(id: Int): Game?

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE ${Game.Schema.ID} IN (:ids) 
        LIMIT :offset, :limit
        """
    )
    suspend fun getGames(ids: List<Int>, offset: Int, limit: Int): List<Game>

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE LOWER(${Game.Schema.NAME}) LIKE (:searchQuery || '%') 
        ORDER BY 
            ${Game.Schema.TOTAL_RATING} IS NULL,
            ${Game.Schema.TOTAL_RATING} DESC,
            ${Game.Schema.ID} ASC 
        LIMIT :offset, :limit
        """
    )
    suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): List<Game>

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE ${Game.Schema.USERS_RATING} IS NOT NULL AND 
        ${Game.Schema.RELEASE_DATE} IS NOT NULL AND 
        ${Game.Schema.RELEASE_DATE} > :minReleaseDateTimestamp 
        ORDER BY ${Game.Schema.TOTAL_RATING} DESC 
        LIMIT :offset, :limit
        """
    )
    fun observePopularGames(minReleaseDateTimestamp: Long, offset: Int, limit: Int): Flow<List<Game>>

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE ${Game.Schema.RELEASE_DATE} IS NOT NULL AND 
        ${Game.Schema.RELEASE_DATE} > :minReleaseDateTimestamp AND 
        ${Game.Schema.RELEASE_DATE} < :maxReleaseDateTimestamp 
        ORDER BY ${Game.Schema.RELEASE_DATE} DESC 
        LIMIT :offset, :limit
        """
    )
    fun observeRecentlyReleasedGames(
        minReleaseDateTimestamp: Long,
        maxReleaseDateTimestamp: Long,
        offset: Int,
        limit: Int
    ): Flow<List<Game>>

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE ${Game.Schema.RELEASE_DATE} IS NOT NULL AND 
        ${Game.Schema.RELEASE_DATE} > :minReleaseDateTimestamp 
        ORDER BY ${Game.Schema.RELEASE_DATE} ASC 
        LIMIT :offset, :limit
        """
    )
    fun observeComingSoonGames(minReleaseDateTimestamp: Long, offset: Int, limit: Int): Flow<List<Game>>

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE ${Game.Schema.RELEASE_DATE} IS NOT NULL AND 
        ${Game.Schema.RELEASE_DATE} > :minReleaseDateTimestamp AND 
        ${Game.Schema.HYPE_COUNT} IS NOT NULL 
        ORDER BY ${Game.Schema.HYPE_COUNT} DESC 
        LIMIT :offset, :limit
        """
    )
    fun observeMostAnticipatedGames(minReleaseDateTimestamp: Long, offset: Int, limit: Int): Flow<List<Game>>
}
