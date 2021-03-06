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

package com.paulrybitskyi.gamedge.database.utils

import com.paulrybitskyi.gamedge.data.games.DataCategory
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.database.games.DatabaseGame
import com.paulrybitskyi.gamedge.database.games.datastores.GameMapper

internal class FakeGameMapper : GameMapper {


    override fun mapToDatabaseGame(dataGame: DataGame): DatabaseGame {
        return DatabaseGame(
            id = dataGame.id,
            followerCount = dataGame.followerCount,
            hypeCount = dataGame.hypeCount,
            releaseDate = dataGame.releaseDate,
            criticsRating = dataGame.criticsRating,
            usersRating = dataGame.usersRating,
            totalRating = dataGame.totalRating,
            name = dataGame.name,
            summary = dataGame.summary,
            storyline = dataGame.storyline,
            category = "category",
            cover = "cover",
            releaseDates = "release_dates",
            ageRatings = "age_ratings",
            videos = "videos",
            artworks = "artworks",
            screenshots = "screenshots",
            genres = "genres",
            platforms = "platforms",
            playerPerspectives = "player_perspectives",
            themes = "themes",
            modes = "modes",
            keywords = "keywords",
            involvedCompanies = "involved_companies",
            websites = "websites",
            similarGames = "similar_games"
        )
    }


    override fun mapToDataGame(databaseGame: DatabaseGame): DataGame {
        return DataGame(
            id = databaseGame.id,
            followerCount = databaseGame.followerCount,
            hypeCount = databaseGame.hypeCount,
            releaseDate = databaseGame.releaseDate,
            criticsRating = databaseGame.criticsRating,
            usersRating = databaseGame.usersRating,
            totalRating = databaseGame.totalRating,
            name = databaseGame.name,
            summary = databaseGame.summary,
            storyline = databaseGame.storyline,
            category = DataCategory.UNKNOWN,
            cover = null,
            releaseDates = emptyList(),
            ageRatings = emptyList(),
            videos = emptyList(),
            artworks = emptyList(),
            screenshots = emptyList(),
            genres = emptyList(),
            platforms = emptyList(),
            playerPerspectives = emptyList(),
            themes = emptyList(),
            modes = emptyList(),
            keywords = emptyList(),
            involvedCompanies = emptyList(),
            websites = emptyList(),
            similarGames = emptyList()
        )
    }


}