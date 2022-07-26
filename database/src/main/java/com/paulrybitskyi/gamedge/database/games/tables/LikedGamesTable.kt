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
import com.paulrybitskyi.gamedge.database.games.entities.DbGame
import com.paulrybitskyi.gamedge.database.games.entities.DbLikedGame
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedGamesTable {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLikedGame(likedGame: DbLikedGame)

    @Query(
        """
        DELETE FROM ${DbLikedGame.Schema.TABLE_NAME}
        WHERE ${DbLikedGame.Schema.GAME_ID} = :gameId
        """
    )
    suspend fun deleteLikedGame(gameId: Int)

    @Query(
        """
        SELECT COUNT(*) FROM ${DbLikedGame.Schema.TABLE_NAME}
        WHERE ${DbLikedGame.Schema.GAME_ID} = :gameId
        """
    )
    suspend fun isGameLiked(gameId: Int): Boolean

    @Query(
        """
        SELECT COUNT(*) FROM ${DbLikedGame.Schema.TABLE_NAME}
        WHERE ${DbLikedGame.Schema.GAME_ID} = :gameId
        """
    )
    fun observeGameLikeState(gameId: Int): Flow<Boolean>

    @Query(
        """
        SELECT g.* FROM ${DbGame.Schema.TABLE_NAME} AS g
        INNER JOIN ${DbLikedGame.Schema.TABLE_NAME} AS lg
        ON lg.${DbLikedGame.Schema.GAME_ID} = g.${DbGame.Schema.ID}
        ORDER BY lg.${DbLikedGame.Schema.LIKE_TIMESTAMP} DESC
        LIMIT :offset, :limit
        """
    )
    fun observeLikedGames(offset: Int, limit: Int): Flow<List<DbGame>>
}
