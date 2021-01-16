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

package com.paulrybitskyi.gamedge.database.games.datastores

import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.database.commons.utils.JsonConverter
import com.paulrybitskyi.gamedge.database.games.DatabaseGame
import javax.inject.Inject

internal class GameMapper @Inject constructor(private val jsonConverter: JsonConverter) {


    fun mapToDatabaseGame(dataGame: DataGame): DatabaseGame {
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
            category = dataGame.category.toJson(),
            cover = dataGame.cover.toJson(),
            releaseDates = dataGame.releaseDates.toJson(),
            ageRatings = dataGame.ageRatings.toJson(),
            videos = dataGame.videos.toJson(),
            artworks = dataGame.artworks.toJson(),
            screenshots = dataGame.screenshots.toJson(),
            genres = dataGame.genres.toJson(),
            platforms = dataGame.platforms.toJson(),
            playerPerspectives = dataGame.playerPerspectives.toJson(),
            themes = dataGame.themes.toJson(),
            modes = dataGame.modes.toJson(),
            keywords = dataGame.keywords.toJson(),
            involvedCompanies = dataGame.involvedCompanies.toJson(),
            websites = dataGame.websites.toJson(),
            similarGames = dataGame.similarGames.toJson()
        )
    }


    fun mapToDataGame(databaseGame: DatabaseGame): DataGame {
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
            category = checkNotNull(databaseGame.category.fromJson()),
            cover = databaseGame.cover.fromJson(),
            releaseDates = databaseGame.releaseDates.fromJsonList(),
            ageRatings = databaseGame.ageRatings.fromJsonList(),
            videos = databaseGame.videos.fromJsonList(),
            artworks = databaseGame.artworks.fromJsonList(),
            screenshots = databaseGame.screenshots.fromJsonList(),
            genres = databaseGame.genres.fromJsonList(),
            platforms = databaseGame.platforms.fromJsonList(),
            playerPerspectives = databaseGame.playerPerspectives.fromJsonList(),
            themes = databaseGame.themes.fromJsonList(),
            modes = databaseGame.modes.fromJsonList(),
            keywords = databaseGame.keywords.fromJsonList(),
            involvedCompanies = databaseGame.involvedCompanies.fromJsonList(),
            websites = databaseGame.websites.fromJsonList(),
            similarGames = databaseGame.similarGames.fromJsonList()
        )
    }


    private inline fun <reified T> T.toJson(): String {
        return jsonConverter.toJson(this)
    }


    private inline fun <reified T> String.fromJson(): T? {
        return jsonConverter.fromJson(this)
    }


    private inline fun <reified T> String.fromJsonList(): List<T> {
        return (jsonConverter.fromJsonList(this) ?: emptyList())
    }


}


internal fun GameMapper.mapToDatabaseGames(dataGames: List<DataGame>): List<DatabaseGame> {
    return dataGames.map(::mapToDatabaseGame)
}


internal fun GameMapper.mapToDataGames(databaseGames: List<DatabaseGame>): List<DataGame> {
    return databaseGames.map(::mapToDataGame)
}