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

package com.paulrybitskyi.gamedge.data.games.utils

import com.paulrybitskyi.gamedge.data.games.usecases.commons.throttling.GamesRefreshingThrottler

internal class FakeGamesRefreshingThrottler : GamesRefreshingThrottler {


    var canRefreshGames = false
    var canRefreshCompanyDevelopedGames = false
    var canRefreshSimilarGames = false

    var areGamesLastRefreshTimeUpdated = false


    override suspend fun canRefreshGames(key: String): Boolean {
        return canRefreshGames
    }


    override suspend fun updateGamesLastRefreshTime(key: String) {
        areGamesLastRefreshTimeUpdated = true
    }


    override suspend fun canRefreshCompanyDevelopedGames(key: String): Boolean {
        return canRefreshCompanyDevelopedGames
    }


    override suspend fun canRefreshSimilarGames(key: String): Boolean {
        return canRefreshSimilarGames
    }


}