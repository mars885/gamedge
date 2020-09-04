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

package com.paulrybitskyi.gamedge.cache

import com.paulrybitskyi.gamedge.cache.utils.take
import com.paulrybitskyi.gamedge.commons.data.Constants
import com.paulrybitskyi.gamedge.commons.data.extensions.asSuccess
import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesDataStore
import com.paulrybitskyi.gamedge.data.entities.Game
import com.paulrybitskyi.gamedge.data.utils.DataCompany
import com.paulrybitskyi.gamedge.data.utils.DataGame
import com.paulrybitskyi.gamedge.data.utils.DataStoreResult
import java.util.concurrent.ConcurrentHashMap

internal class GamesCacheDataStore(
    private val queryTimestampProvider: QueryTimestampProvider
) : GamesDataStore {


    private val gamesCache = ConcurrentHashMap<Int, Game>()


    override suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesCache.values
            .filter { it.name.toLowerCase().contains(searchQuery) }
            .take(offset, limit)
            .asSuccess()
    }


    override suspend fun getPopularGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesCache.values
            .filter {
                it.popularity != null &&
                it.popularity!! > Constants.POPULAR_GAMES_MIN_POPULARITY &&
                it.releaseDate != null &&
                it.releaseDate!! > queryTimestampProvider.getPopularGamesMinReleaseDate()
            }
            .sortedByDescending { it.popularity }
            .take(offset, limit)
            .asSuccess()
    }


    override suspend fun getRecentlyReleasedGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesCache.values
            .filter {
                it.releaseDate != null &&
                it.releaseDate!! > queryTimestampProvider.getRecentlyReleasedGamesMinReleaseDate() &&
                it.releaseDate!! < queryTimestampProvider.getRecentlyReleasedGamesMaxReleaseDate()
            }
            .sortedByDescending { it.releaseDate }
            .take(offset, limit)
            .asSuccess()
    }


    override suspend fun getComingSoonGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesCache.values
            .filter {
                it.releaseDate != null &&
                it.releaseDate!! > queryTimestampProvider.getComingSoonGamesMinReleaseDate()
            }
            .sortedBy { it.releaseDate }
            .take(offset, limit)
            .asSuccess()
    }


    override suspend fun getMostAnticipatedGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesCache.values
            .filter {
                it.releaseDate != null &&
                it.releaseDate!! > queryTimestampProvider.getMostAnticipatedGamesMinReleaseDate() &&
                it.hypeCount != null
            }
            .sortedByDescending { it.hypeCount }
            .take(offset, limit)
            .asSuccess()
    }


    override suspend fun getCompanyGames(company: DataCompany, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return getGames(gameIds = company.developedGames, offset = offset, limit = limit)
    }


    override suspend fun getSimilarGames(game: DataGame, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return getGames(gameIds = game.similarGames, offset = offset, limit = limit)
    }


    private fun getGames(gameIds: List<Int>, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesCache.values
            .filter { it.id in gameIds }
            .take(offset, limit)
            .asSuccess()
    }


}