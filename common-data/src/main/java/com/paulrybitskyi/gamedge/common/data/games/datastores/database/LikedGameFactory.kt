/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.common.data.games.datastores.database

import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.database.games.entities.DbLikedGame
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton

private const val ENTITY_AUTOGENERATED_ID_INDICATOR = 0

internal interface LikedGameFactory {
    fun createLikedGame(gameId: Int): DbLikedGame
}

@Singleton
@BindType
internal class LikedGameFactoryImpl @Inject constructor(
    private val timestampProvider: TimestampProvider,
) : LikedGameFactory {

    override fun createLikedGame(gameId: Int): DbLikedGame {
        return DbLikedGame(
            id = ENTITY_AUTOGENERATED_ID_INDICATOR,
            gameId = gameId,
            likeTimestamp = timestampProvider.getUnixTimestamp(),
        )
    }
}
