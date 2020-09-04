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

package com.paulrybitskyi.gamedge.database.tables

import androidx.room.Dao
import androidx.room.Query
import com.paulrybitskyi.gamedge.commons.data.Constants
import com.paulrybitskyi.gamedge.database.entities.Game

@Dao
internal interface GamesTable {

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE LOWER(${Game.Schema.NAME}) LIKE (:searchQuery || '%') 
        LIMIT :offset, :limit
        """
    )
    suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): List<Game>

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE ${Game.Schema.POPULARITY} IS NOT NULL AND 
        ${Game.Schema.POPULARITY} > ${Constants.POPULAR_GAMES_MIN_POPULARITY} AND 
        ${Game.Schema.RELEASE_DATE} IS NOT NULL AND 
        ${Game.Schema.RELEASE_DATE} > :minReleaseDateTimestamp 
        ORDER BY ${Game.Schema.POPULARITY} DESC 
        LIMIT :offset, :limit
        """
    )
    suspend fun getPopularGames(minReleaseDateTimestamp: Long, offset: Int, limit: Int): List<Game>

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
    suspend fun getRecentlyReleasedGames(
        minReleaseDateTimestamp: Long,
        maxReleaseDateTimestamp: Long,
        offset: Int,
        limit: Int
    ): List<Game>

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE ${Game.Schema.RELEASE_DATE} IS NOT NULL AND 
        ${Game.Schema.RELEASE_DATE} > :minReleaseDateTimestamp 
        ORDER BY ${Game.Schema.RELEASE_DATE} ASC 
        LIMIT :offset, :limit
        """
    )
    suspend fun getComingSoonGames(minReleaseDateTimestamp: Long, offset: Int, limit: Int): List<Game>

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
    suspend fun getMostAnticipatedGames(minReleaseDateTimestamp: Long, offset: Int, limit: Int): List<Game>

    @Query(
        """
        SELECT * FROM ${Game.Schema.TABLE_NAME} 
        WHERE ${Game.Schema.ID} IN (:ids) 
        LIMIT :offset, :limit
        """
    )
    suspend fun getGames(ids: List<Int>, offset: Int, limit: Int): List<Game>

}