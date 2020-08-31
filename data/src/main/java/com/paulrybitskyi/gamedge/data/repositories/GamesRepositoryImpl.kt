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

package com.paulrybitskyi.gamedge.data.repositories

import com.paulrybitskyi.gamedge.data.datastores.GamesDataStore
import com.paulrybitskyi.gamedge.domain.entities.Game
import com.paulrybitskyi.gamedge.domain.repositories.utils.Pagination
import com.paulrybitskyi.gamedge.domain.repositories.GamesRepository
import com.paulrybitskyi.gamedge.domain.utils.RepositoryResult

class GamesRepositoryImpl(
    private val cacheDataStore: GamesDataStore,
    private val databaseDataStore: GamesDataStore,
    private val serverDataStore: GamesDataStore
) : GamesRepository {


    override suspend fun searchGames(searchQuery: String, pagination: Pagination): RepositoryResult<List<Game>> {
        TODO("Not yet implemented")
    }


    override suspend fun getPopularGames(pagination: Pagination): RepositoryResult<List<Game>> {
        TODO("Not yet implemented")
    }


    override suspend fun getRecentlyReleasedGames(pagination: Pagination): RepositoryResult<List<Game>> {
        TODO("Not yet implemented")
    }


    override suspend fun getComingSoonGames(pagination: Pagination): RepositoryResult<List<Game>> {
        TODO("Not yet implemented")
    }


    override suspend fun getMostAnticipatedGames(pagination: Pagination): RepositoryResult<List<Game>> {
        TODO("Not yet implemented")
    }


    override suspend fun getCompanyGames(companyId: Int, pagination: Pagination): RepositoryResult<List<Game>> {
        TODO("Not yet implemented")
    }


    override suspend fun getSimilarGames(gameId: Int, pagination: Pagination): RepositoryResult<List<Game>> {
        TODO("Not yet implemented")
    }


}