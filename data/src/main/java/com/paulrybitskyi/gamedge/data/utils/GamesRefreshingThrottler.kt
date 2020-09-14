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

package com.paulrybitskyi.gamedge.data.utils

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit


interface GamesRefreshingThrottler {

    suspend fun canRefreshPopularGames(): Boolean

    suspend fun updatePopularGamesLastRefreshingTime()

    suspend fun canRefreshRecentlyReleasedGames(): Boolean

    suspend fun updateRecentlyReleasedGamesLastRefreshingTime()

    suspend fun canRefreshComingSoonGames(): Boolean

    suspend fun updateComingSoonGamesLastRefreshingTime()

    suspend fun canRefreshMostAnticipatedGames(): Boolean

    suspend fun updateMostAnticipatedGamesLastRefreshingTime()

}


internal class GamesRefreshingThrottlerImpl(
    private val dataStore: DataStore<Preferences>,
    private val timestampProvider: TimestampProvider
) : GamesRefreshingThrottler {


    private companion object {

        val POPULAR_GAMES_LAST_REFRESHING_TIME = preferencesKey<Long>("popular_games_last_refreshing_time")
        val RECENTLY_RELEASED_GAMES_LAST_REFRESHING_TIME = preferencesKey<Long>("recently_released_games_last_refreshing_time")
        val COMING_SOON_GAMES_LAST_REFRESHING_TIME = preferencesKey<Long>("coming_soon_games_last_refreshing_time")
        val MOST_ANTICIPATED_GAMES_LAST_REFRESHING_TIME = preferencesKey<Long>("most_anticipated_games_last_refreshing_time")

        val GAMES_REFRESHING_TIMEOUT_IN_MILLIS = TimeUnit.SECONDS.toMillis(30L)

    }


    override suspend fun canRefreshPopularGames(): Boolean {
        return canRefreshGames(POPULAR_GAMES_LAST_REFRESHING_TIME)
    }


    override suspend fun updatePopularGamesLastRefreshingTime() {
        updateGamesLastRefreshingTime(POPULAR_GAMES_LAST_REFRESHING_TIME)
    }


    override suspend fun canRefreshRecentlyReleasedGames(): Boolean {
        return canRefreshGames(RECENTLY_RELEASED_GAMES_LAST_REFRESHING_TIME)
    }


    override suspend fun updateRecentlyReleasedGamesLastRefreshingTime() {
        updateGamesLastRefreshingTime(RECENTLY_RELEASED_GAMES_LAST_REFRESHING_TIME)
    }


    override suspend fun canRefreshComingSoonGames(): Boolean {
        return canRefreshGames(COMING_SOON_GAMES_LAST_REFRESHING_TIME)
    }


    override suspend fun updateComingSoonGamesLastRefreshingTime() {
        updateGamesLastRefreshingTime(COMING_SOON_GAMES_LAST_REFRESHING_TIME)
    }


    override suspend fun canRefreshMostAnticipatedGames(): Boolean {
        return canRefreshGames(MOST_ANTICIPATED_GAMES_LAST_REFRESHING_TIME)
    }


    override suspend fun updateMostAnticipatedGamesLastRefreshingTime() {
        updateGamesLastRefreshingTime(MOST_ANTICIPATED_GAMES_LAST_REFRESHING_TIME)
    }


    private suspend fun canRefreshGames(key: Preferences.Key<Long>): Boolean {
        return dataStore.data
            .map { it[key] ?: 0L }
            .map { timestampProvider.getUnixTimestamp() > (it + GAMES_REFRESHING_TIMEOUT_IN_MILLIS) }
            .first()
    }


    private suspend fun updateGamesLastRefreshingTime(key: Preferences.Key<Long>) {
        dataStore.edit {
            it[key] = timestampProvider.getUnixTimestamp()
        }
    }


}