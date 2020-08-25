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

package com.paulrybitskyi.gamedge.data.datastores

import com.paulrybitskyi.gamedge.data.utils.DataStoreResult
import com.paulrybitskyi.gameedge.domain.model.Game
import com.paulrybitskyi.gameedge.domain.repositories.utils.Pagination

interface GamesDataStore {

    suspend fun searchGames(searchQuery: String, pagination: Pagination): DataStoreResult<List<Game>>

    suspend fun getPopularGames(pagination: Pagination): DataStoreResult<List<Game>>

    suspend fun getRecentlyReleasedGames(pagination: Pagination): DataStoreResult<List<Game>>

    suspend fun getComingSoonGames(pagination: Pagination): DataStoreResult<List<Game>>

    suspend fun getMostAnticipatedGames(pagination: Pagination): DataStoreResult<List<Game>>

    suspend fun getCompanyGames(companyId: Int, pagination: Pagination): DataStoreResult<List<Game>>

    suspend fun getSimilarGames(gameId: Int, pagination: Pagination): DataStoreResult<List<Game>>

}