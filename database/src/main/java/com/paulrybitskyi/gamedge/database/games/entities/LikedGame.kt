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

package com.paulrybitskyi.gamedge.database.games.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = LikedGame.Schema.TABLE_NAME,
    indices = [
        Index(LikedGame.Schema.GAME_ID)
    ]
)
internal data class LikedGame(
    @ColumnInfo(name = Schema.ID) @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = Schema.GAME_ID) val gameId: Int,
    @ColumnInfo(name = Schema.LIKE_TIMESTAMP) val likeTimestamp: Long
) {


    object Schema {

        const val TABLE_NAME = "liked_games"
        const val ID = "id"
        const val GAME_ID = "game_id"
        const val LIKE_TIMESTAMP = "like_timestamp"

    }


}