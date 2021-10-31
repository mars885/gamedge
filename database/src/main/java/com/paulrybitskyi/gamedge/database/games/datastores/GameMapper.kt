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

import com.paulrybitskyi.gamedge.data.games.DataAgeRating
import com.paulrybitskyi.gamedge.data.games.DataAgeRatingCategory
import com.paulrybitskyi.gamedge.data.games.DataAgeRatingType
import com.paulrybitskyi.gamedge.data.games.DataCategory
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.DataGenre
import com.paulrybitskyi.gamedge.data.games.DataImage
import com.paulrybitskyi.gamedge.data.games.DataInvolvedCompany
import com.paulrybitskyi.gamedge.data.games.DataKeyword
import com.paulrybitskyi.gamedge.data.games.DataMode
import com.paulrybitskyi.gamedge.data.games.DataPlatform
import com.paulrybitskyi.gamedge.data.games.DataPlayerPerspective
import com.paulrybitskyi.gamedge.data.games.DataReleaseDate
import com.paulrybitskyi.gamedge.data.games.DataReleaseDateCategory
import com.paulrybitskyi.gamedge.data.games.DataTheme
import com.paulrybitskyi.gamedge.data.games.DataVideo
import com.paulrybitskyi.gamedge.data.games.DataWebsite
import com.paulrybitskyi.gamedge.data.games.DataWebsiteCategory
import com.paulrybitskyi.gamedge.database.games.DatabaseAgeRating
import com.paulrybitskyi.gamedge.database.games.DatabaseAgeRatingCategory
import com.paulrybitskyi.gamedge.database.games.DatabaseAgeRatingType
import com.paulrybitskyi.gamedge.database.games.DatabaseCategory
import com.paulrybitskyi.gamedge.database.games.DatabaseCompany
import com.paulrybitskyi.gamedge.database.games.DatabaseGame
import com.paulrybitskyi.gamedge.database.games.DatabaseGenre
import com.paulrybitskyi.gamedge.database.games.DatabaseImage
import com.paulrybitskyi.gamedge.database.games.DatabaseInvolvedCompany
import com.paulrybitskyi.gamedge.database.games.DatabaseKeyword
import com.paulrybitskyi.gamedge.database.games.DatabaseMode
import com.paulrybitskyi.gamedge.database.games.DatabasePlatform
import com.paulrybitskyi.gamedge.database.games.DatabasePlayerPerspective
import com.paulrybitskyi.gamedge.database.games.DatabaseReleaseDate
import com.paulrybitskyi.gamedge.database.games.DatabaseReleaseDateCategory
import com.paulrybitskyi.gamedge.database.games.DatabaseTheme
import com.paulrybitskyi.gamedge.database.games.DatabaseVideo
import com.paulrybitskyi.gamedge.database.games.DatabaseWebsite
import com.paulrybitskyi.gamedge.database.games.DatabaseWebsiteCategory
import javax.inject.Inject

@Suppress("TooManyFunctions")
internal class GameMapper @Inject constructor() {


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
            category = dataGame.category.toDatabaseCategory(),
            cover = dataGame.cover?.toDatabaseImage(),
            releaseDates = dataGame.releaseDates.toDatabaseReleaseDates(),
            ageRatings = dataGame.ageRatings.toDatabaseAgeRatings(),
            videos = dataGame.videos.toDatabaseVideos(),
            artworks = dataGame.artworks.toDatabaseImages(),
            screenshots = dataGame.screenshots.toDatabaseImages(),
            genres = dataGame.genres.toDatabaseGenres(),
            platforms = dataGame.platforms.toDatabasePlatforms(),
            playerPerspectives = dataGame.playerPerspectives.toDatabasePlayerPerspectives(),
            themes = dataGame.themes.toDatabaseThemes(),
            modes = dataGame.modes.toDatabaseModes(),
            keywords = dataGame.keywords.toDatabaseKeywords(),
            involvedCompanies = dataGame.involvedCompanies.toDatabaseInvolvedCompanies(),
            websites = dataGame.websites.toDatabaseWebsites(),
            similarGames = dataGame.similarGames
        )
    }


    private fun DataCategory.toDatabaseCategory(): DatabaseCategory {
        return DatabaseCategory.valueOf(name)
    }


    private fun DataImage.toDatabaseImage(): DatabaseImage {
        return DatabaseImage(
            id = id,
            width = width,
            height = height
        )
    }


    private fun List<DataImage>.toDatabaseImages(): List<DatabaseImage> {
        return map { it.toDatabaseImage() }
    }


    private fun List<DataReleaseDate>.toDatabaseReleaseDates(): List<DatabaseReleaseDate> {
        return map {
            DatabaseReleaseDate(
                date = it.date,
                year = it.year,
                category = DatabaseReleaseDateCategory.valueOf(it.category.name)
            )
        }
    }


    private fun List<DataAgeRating>.toDatabaseAgeRatings(): List<DatabaseAgeRating> {
        return map {
            DatabaseAgeRating(
                category = DatabaseAgeRatingCategory.valueOf(it.category.name),
                type = DatabaseAgeRatingType.valueOf(it.type.name)
            )
        }
    }


    private fun List<DataVideo>.toDatabaseVideos(): List<DatabaseVideo> {
        return map {
            DatabaseVideo(
                id = it.id,
                name = it.name
            )
        }
    }


    private fun List<DataGenre>.toDatabaseGenres(): List<DatabaseGenre> {
        return map {
            DatabaseGenre(
                name = it.name
            )
        }
    }


    private fun List<DataPlatform>.toDatabasePlatforms(): List<DatabasePlatform> {
        return map {
            DatabasePlatform(
                abbreviation = it.abbreviation,
                name = it.name
            )
        }
    }


    private fun List<DataPlayerPerspective>.toDatabasePlayerPerspectives(): List<DatabasePlayerPerspective> {
        return map {
            DatabasePlayerPerspective(
                name = it.name
            )
        }
    }


    private fun List<DataTheme>.toDatabaseThemes(): List<DatabaseTheme> {
        return map {
            DatabaseTheme(
                name = it.name
            )
        }
    }


    private fun List<DataMode>.toDatabaseModes(): List<DatabaseMode> {
        return map {
            DatabaseMode(
                name = it.name
            )
        }
    }


    private fun List<DataKeyword>.toDatabaseKeywords(): List<DatabaseKeyword> {
        return map {
            DatabaseKeyword(
                name = it.name
            )
        }
    }


    private fun List<DataInvolvedCompany>.toDatabaseInvolvedCompanies(): List<DatabaseInvolvedCompany> {
        return map {
            DatabaseInvolvedCompany(
                company = it.company.toDatabaseCompany(),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting
            )
        }
    }


    private fun DataCompany.toDatabaseCompany(): DatabaseCompany {
        return DatabaseCompany(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDatabaseImage(),
            developedGames = developedGames
        )
    }


    private fun List<DataWebsite>.toDatabaseWebsites(): List<DatabaseWebsite> {
        return map {
            DatabaseWebsite(
                id = it.id,
                url = it.url,
                category = DatabaseWebsiteCategory.valueOf(it.category.name)
            )
        }
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
            category = databaseGame.category.toDataCategory(),
            cover = databaseGame.cover?.toDataImage(),
            releaseDates = databaseGame.releaseDates.toDataReleaseDates(),
            ageRatings = databaseGame.ageRatings.toDataAgeRatings(),
            videos = databaseGame.videos.toDataVideos(),
            artworks = databaseGame.artworks.toDataImages(),
            screenshots = databaseGame.screenshots.toDataImages(),
            genres = databaseGame.genres.toDataGenres(),
            platforms = databaseGame.platforms.toDataPlatforms(),
            playerPerspectives = databaseGame.playerPerspectives.toDataPlayerPerspectives(),
            themes = databaseGame.themes.toDataThemes(),
            modes = databaseGame.modes.toDataModes(),
            keywords = databaseGame.keywords.toDataKeywords(),
            involvedCompanies = databaseGame.involvedCompanies.toDataInvolvedCompanies(),
            websites = databaseGame.websites.toDataWebsites(),
            similarGames = databaseGame.similarGames
        )
    }


    private fun DatabaseCategory.toDataCategory(): DataCategory {
        return DataCategory.valueOf(name)
    }


    private fun DatabaseImage.toDataImage(): DataImage {
        return DataImage(
            id = id,
            width = width,
            height = height
        )
    }


    private fun List<DatabaseImage>.toDataImages(): List<DataImage> {
        return map { it.toDataImage() }
    }


    private fun List<DatabaseReleaseDate>.toDataReleaseDates(): List<DataReleaseDate> {
        return map {
            DataReleaseDate(
                date = it.date,
                year = it.year,
                category = DataReleaseDateCategory.valueOf(it.category.name)
            )
        }
    }


    private fun List<DatabaseAgeRating>.toDataAgeRatings(): List<DataAgeRating> {
        return map {
            DataAgeRating(
                category = DataAgeRatingCategory.valueOf(it.category.name),
                type = DataAgeRatingType.valueOf(it.type.name)
            )
        }
    }


    private fun List<DatabaseVideo>.toDataVideos(): List<DataVideo> {
        return map {
            DataVideo(
                id = it.id,
                name = it.name
            )
        }
    }


    private fun List<DatabaseGenre>.toDataGenres(): List<DataGenre> {
        return map {
            DataGenre(
                name = it.name
            )
        }
    }


    private fun List<DatabasePlatform>.toDataPlatforms(): List<DataPlatform> {
        return map {
            DataPlatform(
                abbreviation = it.abbreviation,
                name = it.name
            )
        }
    }


    private fun List<DatabasePlayerPerspective>.toDataPlayerPerspectives(): List<DataPlayerPerspective> {
        return map {
            DataPlayerPerspective(
                name = it.name
            )
        }
    }


    private fun List<DatabaseTheme>.toDataThemes(): List<DataTheme> {
        return map {
            DataTheme(
                name = it.name
            )
        }
    }


    private fun List<DatabaseMode>.toDataModes(): List<DataMode> {
        return map {
            DataMode(
                name = it.name
            )
        }
    }


    private fun List<DatabaseKeyword>.toDataKeywords(): List<DataKeyword> {
        return map {
            DataKeyword(
                name = it.name
            )
        }
    }


    private fun List<DatabaseInvolvedCompany>.toDataInvolvedCompanies(): List<DataInvolvedCompany> {
        return map {
            DataInvolvedCompany(
                company = mapToDataCompany(it.company),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting
            )
        }
    }


    fun mapToDataCompany(company: DatabaseCompany): DataCompany {
        return DataCompany(
            id = company.id,
            name = company.name,
            websiteUrl = company.websiteUrl,
            logo = company.logo?.toDataImage(),
            developedGames = company.developedGames
        )
    }


    private fun List<DatabaseWebsite>.toDataWebsites(): List<DataWebsite> {
        return map {
            DataWebsite(
                id = it.id,
                url = it.url,
                category = DataWebsiteCategory.valueOf(it.category.name)
            )
        }
    }


}


internal fun GameMapper.mapToDatabaseGames(dataGames: List<DataGame>): List<DatabaseGame> {
    return dataGames.map(::mapToDatabaseGame)
}


internal fun GameMapper.mapToDataGames(databaseGames: List<DatabaseGame>): List<DataGame> {
    return databaseGames.map(::mapToDataGame)
}
