/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.common.domain.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.database.games.entities.DbGame
import com.paulrybitskyi.gamedge.database.games.tables.LikedGamesTable
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class LikedGamesDatabaseDataStore @Inject constructor(
    private val likedGamesTable: LikedGamesTable,
    private val likedGameFactory: LikedGameFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val dbGameMapper: DbGameMapper,
) : LikedGamesLocalDataStore {

    override suspend fun likeGame(gameId: Int) {
        likedGamesTable.saveLikedGame(likedGameFactory.createLikedGame(gameId))
    }

    override suspend fun unlikeGame(gameId: Int) {
        likedGamesTable.deleteLikedGame(gameId)
    }

    override suspend fun isGameLiked(gameId: Int): Boolean {
        return likedGamesTable.isGameLiked(gameId)
    }

    override fun observeGameLikeState(gameId: Int): Flow<Boolean> {
        return likedGamesTable.observeGameLikeState(gameId)
    }

    override fun observeLikedGames(pagination: Pagination): Flow<List<Game>> {
         return likedGamesTable.observeLikedGames(
            offset = pagination.offset,
            limit = pagination.limit,
        )
         .toDataGamesFlow()
    }

    private fun Flow<List<DbGame>>.toDataGamesFlow(): Flow<List<Game>> {
        return distinctUntilChanged()
            .map(dbGameMapper::mapToDomainGames)
            .flowOn(dispatcherProvider.computation)
    }
}
