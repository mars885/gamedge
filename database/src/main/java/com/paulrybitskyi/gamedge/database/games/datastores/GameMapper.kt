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
import com.paulrybitskyi.gamedge.database.commons.JsonConverter
import com.paulrybitskyi.gamedge.database.games.DatabaseGame
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface GameMapper {

    fun mapToDatabaseGame(dataGame: DataGame): DatabaseGame

    fun mapToDataGame(databaseGame: DatabaseGame): DataGame

}


@BindType
internal class GameMapperImpl @Inject constructor(
    private val jsonConverter: JsonConverter
) : GameMapper {


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
            category = jsonConverter.toJson(dataGame.category),
            cover = jsonConverter.toJson(dataGame.cover),
            releaseDates = jsonConverter.toJson(dataGame.releaseDates),
            ageRatings = jsonConverter.toJson(dataGame.ageRatings),
            videos = jsonConverter.toJson(dataGame.videos),
            artworks = jsonConverter.toJson(dataGame.artworks),
            screenshots = jsonConverter.toJson(dataGame.screenshots),
            genres = jsonConverter.toJson(dataGame.genres),
            platforms = jsonConverter.toJson(dataGame.platforms),
            playerPerspectives = jsonConverter.toJson(dataGame.playerPerspectives),
            themes = jsonConverter.toJson(dataGame.themes),
            modes = jsonConverter.toJson(dataGame.modes),
            keywords = jsonConverter.toJson(dataGame.keywords),
            involvedCompanies = jsonConverter.toJson(dataGame.involvedCompanies),
            websites = jsonConverter.toJson(dataGame.websites),
            similarGames = jsonConverter.toJson(dataGame.similarGames)
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
            category = checkNotNull(jsonConverter.fromJson(databaseGame.category)),
            cover = jsonConverter.fromJson(databaseGame.cover),
            releaseDates = (jsonConverter.fromJson(databaseGame.releaseDates) ?: emptyList()),
            ageRatings = (jsonConverter.fromJson(databaseGame.ageRatings) ?: emptyList()),
            videos = (jsonConverter.fromJson(databaseGame.videos) ?: emptyList()),
            artworks = (jsonConverter.fromJson(databaseGame.artworks) ?: emptyList()),
            screenshots = (jsonConverter.fromJson(databaseGame.screenshots) ?: emptyList()),
            genres = (jsonConverter.fromJson(databaseGame.genres) ?: emptyList()),
            platforms = (jsonConverter.fromJson(databaseGame.platforms) ?: emptyList()),
            playerPerspectives = (jsonConverter.fromJson(databaseGame.playerPerspectives) ?: emptyList()),
            themes = (jsonConverter.fromJson(databaseGame.themes) ?: emptyList()),
            modes = (jsonConverter.fromJson(databaseGame.modes) ?: emptyList()),
            keywords = (jsonConverter.fromJson(databaseGame.keywords) ?: emptyList()),
            involvedCompanies = (jsonConverter.fromJson(databaseGame.involvedCompanies) ?: emptyList()),
            websites = (jsonConverter.fromJson(databaseGame.websites) ?: emptyList()),
            similarGames = (jsonConverter.fromJson(databaseGame.similarGames) ?: emptyList())
        )
    }


}


internal fun GameMapper.mapToDatabaseGames(dataGames: List<DataGame>): List<DatabaseGame> {
    return dataGames.map(::mapToDatabaseGame)
}


internal fun GameMapper.mapToDataGames(databaseGames: List<DatabaseGame>): List<DataGame> {
    return databaseGames.map(::mapToDataGame)
}