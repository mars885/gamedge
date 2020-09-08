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

import com.paulrybitskyi.gamedge.data.utils.DataCompany
import com.paulrybitskyi.gamedge.data.utils.DataGame
import kotlinx.coroutines.flow.Flow

interface GamesDatabaseDataStore {

    suspend fun saveGames(games: List<DataGame>)

    suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): List<DataGame>

    suspend fun observePopularGames(offset: Int, limit: Int): Flow<List<DataGame>>

    suspend fun observeRecentlyReleasedGames(offset: Int, limit: Int): Flow<List<DataGame>>

    suspend fun observeComingSoonGames(offset: Int, limit: Int): Flow<List<DataGame>>

    suspend fun observeMostAnticipatedGames(offset: Int, limit: Int): Flow<List<DataGame>>

    suspend fun observeCompanyGames(company: DataCompany, offset: Int, limit: Int): Flow<List<DataGame>>

    suspend fun observeSimilarGames(game: DataGame, offset: Int, limit: Int): Flow<List<DataGame>>

}