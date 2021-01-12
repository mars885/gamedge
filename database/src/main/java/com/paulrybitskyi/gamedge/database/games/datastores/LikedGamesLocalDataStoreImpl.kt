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

package com.paulrybitskyi.gamedge.database.games.datastores

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.commons.Pagination
import com.paulrybitskyi.gamedge.data.games.entities.Game
import com.paulrybitskyi.gamedge.database.games.DatabaseGame
import com.paulrybitskyi.gamedge.database.games.tables.LikedGamesTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class LikedGamesLocalDataStoreImpl(
    private val likedGamesTable: LikedGamesTable,
    private val likedGameFactory: LikedGameFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val gameMapper: GameMapper
) : LikedGamesLocalDataStore {


    override suspend fun likeGame(gameId: Int) {
        likedGamesTable.saveLikedGame(likedGameFactory.createLikedGame(gameId))
    }


    override suspend fun unlikeGame(gameId: Int) {
        likedGamesTable.deleteLikedGame(gameId)
    }


    override suspend fun isGamedLiked(gameId: Int): Boolean {
        return likedGamesTable.isGameLiked(gameId)
    }


    override suspend fun observeGameLikeState(gameId: Int): Flow<Boolean> {
        return likedGamesTable.observeGameLikeState(gameId)
    }


    override suspend fun observeLikedGames(pagination: Pagination): Flow<List<Game>> {
         return likedGamesTable.observeLikedGames(
            offset = pagination.offset,
            limit = pagination.limit
        )
         .toDataGamesFlow()
    }


    private fun Flow<List<DatabaseGame>>.toDataGamesFlow(): Flow<List<DataGame>> {
        return distinctUntilChanged()
            .map(gameMapper::mapToDataGames)
            .flowOn(dispatcherProvider.computation)
    }


}
