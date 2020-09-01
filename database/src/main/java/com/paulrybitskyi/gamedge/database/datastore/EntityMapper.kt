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

package com.paulrybitskyi.gamedge.database.datastore

import com.paulrybitskyi.gamedge.data.utils.DataGame
import com.paulrybitskyi.gamedge.database.utils.DatabaseGame
import com.paulrybitskyi.gamedge.database.utils.JsonConverter

internal class EntityMapper(
    private val jsonConverter: JsonConverter
) {


    fun mapToDatabaseGame(dataGame: DataGame): DatabaseGame {
        return DatabaseGame(
            id = dataGame.id,
            hypeCount = dataGame.hypeCount,
            releaseDate = dataGame.releaseDate,
            criticsRating = dataGame.criticsRating,
            usersRating = dataGame.usersRating,
            totalRating = dataGame.totalRating,
            popularity = dataGame.popularity,
            name = dataGame.name,
            summary = dataGame.summary,
            storyline = dataGame.storyline,
            cover = dataGame.cover.toJson(),
            timeToBeat = dataGame.timeToBeat.toJson(),
            ageRatings = dataGame.ageRatings.toJson(),
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
            hypeCount = databaseGame.hypeCount,
            releaseDate = databaseGame.releaseDate,
            criticsRating = databaseGame.criticsRating,
            usersRating = databaseGame.usersRating,
            totalRating = databaseGame.totalRating,
            popularity = databaseGame.popularity,
            name = databaseGame.name,
            summary = databaseGame.summary,
            storyline = databaseGame.storyline,
            cover = databaseGame.cover.fromJson(),
            timeToBeat = databaseGame.timeToBeat.fromJson(),
            ageRatings = databaseGame.ageRatings.fromJson(),
            artworks = databaseGame.artworks.fromJson(),
            screenshots = databaseGame.screenshots.fromJson(),
            genres = databaseGame.genres.fromJson(),
            platforms = databaseGame.platforms.fromJson(),
            playerPerspectives = databaseGame.playerPerspectives.fromJson(),
            themes = databaseGame.themes.fromJson(),
            modes = databaseGame.modes.fromJson(),
            keywords = databaseGame.keywords.fromJson(),
            involvedCompanies = databaseGame.involvedCompanies.fromJson(),
            websites = databaseGame.websites.fromJson(),
            similarGames = databaseGame.similarGames.fromJson()
        )
    }


    private inline fun <reified T : Any> T.toJson(): String {
        return jsonConverter.toJson(this::class.java)
    }


    private inline fun <reified T> String.fromJson(): T {
        return checkNotNull(jsonConverter.fromJson(this))
    }


}


internal fun EntityMapper.mapToDatabaseGames(dataGames: List<DataGame>): List<DatabaseGame> {
    return dataGames.map(::mapToDatabaseGame)
}


internal fun EntityMapper.mapToDataGames(databaseGames: List<DatabaseGame>): List<DataGame> {
    return databaseGames.map(::mapToDataGame)
}