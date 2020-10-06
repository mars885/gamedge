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

package com.paulrybitskyi.gamedge.data.games.datastores

import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


internal interface LikedGamesLocalDataStore {

    suspend fun likeGame(gameId: Int)

    suspend fun unlikeGame(gameId: Int)

    suspend fun isGamedLiked(gameId: Int): Boolean

    suspend fun observeGameLikeState(gameId: Int): Flow<Boolean>

}


internal class LikedGamesLocalDataStoreImpl(
    private val gamesPreferences: DataStore<Preferences>
) : LikedGamesLocalDataStore {


    private companion object {

        val LIKED_GAMES_KEY = preferencesSetKey<String>("liked_games_key")

    }


    override suspend fun likeGame(gameId: Int) {
        updateLikeGames {
            it.add(gameId.toString())
        }
    }


    override suspend fun unlikeGame(gameId: Int) {
        updateLikeGames {
            it.remove(gameId.toString())
        }
    }


    private suspend fun updateLikeGames(block: (MutableSet<String>) -> Unit) {
        gamesPreferences.edit { preferences ->
            (preferences[LIKED_GAMES_KEY] ?: emptySet())
                .toMutableSet()
                .also(block)
                .also { preferences[LIKED_GAMES_KEY] = it }
        }
    }


    override suspend fun isGamedLiked(gameId: Int): Boolean {
        return observeGameLikeState(gameId).first()
    }


    override suspend fun observeGameLikeState(gameId: Int): Flow<Boolean> {
        return gamesPreferences.data
            .map { it[LIKED_GAMES_KEY] ?: setOf() }
            .map { it.contains(gameId.toString()) }
    }


}