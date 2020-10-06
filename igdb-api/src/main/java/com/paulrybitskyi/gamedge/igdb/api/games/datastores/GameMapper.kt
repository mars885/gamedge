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

package com.paulrybitskyi.gamedge.igdb.api.games.datastores

import com.paulrybitskyi.gamedge.data.games.*
import com.paulrybitskyi.gamedge.igdb.api.games.*

internal class GameMapper {


    fun mapToDataGame(apiGame: ApiGame): DataGame {
        return DataGame(
            id = apiGame.id,
            followerCount = apiGame.followerCount,
            hypeCount = apiGame.hypeCount,
            releaseDate = apiGame.releaseDate,
            criticsRating = apiGame.criticsRating,
            usersRating = apiGame.usersRating,
            totalRating = apiGame.totalRating,
            name = apiGame.name,
            summary = apiGame.summary,
            storyline = apiGame.storyline,
            category = apiGame.category.toDataCategory(),
            cover = apiGame.cover?.toDataImage(),
            releaseDates = apiGame.releaseDates.toDataReleaseDates(),
            ageRatings = apiGame.ageRatings.toDataAgeRatings(),
            videos = apiGame.videos.toDataVideos(),
            artworks = apiGame.artworks.toDataImages(),
            screenshots = apiGame.screenshots.toDataImages(),
            genres = apiGame.genres.toDataGenres(),
            platforms = apiGame.platforms.toDataPlatforms(),
            playerPerspectives = apiGame.playerPerspectives.toDataPlayerPerspectives(),
            themes = apiGame.themes.toDataThemes(),
            modes = apiGame.modes.toDataModes(),
            keywords = apiGame.keywords.toDataKeywords(),
            involvedCompanies = apiGame.involvedCompanies.toDataInvolvedCompanies(),
            websites = apiGame.websites.toDataWebsites(),
            similarGames = apiGame.similarGames
        )
    }


    private fun ApiCategory.toDataCategory(): DataCategory {
        return DataCategory.valueOf(name)
    }


    private fun ApiImage.toDataImage(): DataImage {
        return DataImage(
            id = id,
            width = width,
            height = height
        )
    }


    private fun List<ApiImage>.toDataImages(): List<DataImage> {
        return map { it.toDataImage() }
    }


    private fun List<ApiVideo>.toDataVideos(): List<DataVideo> {
        return map {
            DataVideo(
                id = it.id,
                name = it.name
            )
        }
    }


    private fun List<ApiReleaseDate>.toDataReleaseDates(): List<DataReleaseDate> {
        return map {
            DataReleaseDate(
                date = it.date,
                year = it.year,
                category = DataReleaseDateCategory.valueOf(it.category.name)
            )
        }
    }


    private fun List<ApiAgeRating>.toDataAgeRatings(): List<DataAgeRating> {
        return map {
            DataAgeRating(
                category = DataAgeRatingCategory.valueOf(it.category.name),
                type = DataAgeRatingType.valueOf(it.type.name)
            )
        }
    }


    private fun List<ApiGenre>.toDataGenres(): List<DataGenre> {
        return map {
            DataGenre(
                name = it.name
            )
        }
    }


    private fun List<ApiPlatform>.toDataPlatforms(): List<DataPlatform> {
        return map {
            DataPlatform(
                abbreviation = it.abbreviation,
                name = it.name
            )
        }
    }


    private fun List<ApiPlayerPerspective>.toDataPlayerPerspectives(): List<DataPlayerPerspective> {
        return map {
            DataPlayerPerspective(
                name = it.name
            )
        }
    }


    private fun List<ApiTheme>.toDataThemes(): List<DataTheme> {
        return map {
            DataTheme(
                name = it.name
            )
        }
    }


    private fun List<ApiMode>.toDataModes(): List<DataMode> {
        return map {
            DataMode(
                name = it.name
            )
        }
    }


    private fun List<ApiKeyword>.toDataKeywords(): List<DataKeyword> {
        return map {
            DataKeyword(
                name = it.name
            )
        }
    }


    private fun List<ApiInvolvedCompany>.toDataInvolvedCompanies(): List<DataInvolvedCompany> {
        return map {
            DataInvolvedCompany(
                company = it.company.toDataCompany(),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting
            )
        }
    }


    private fun ApiCompany.toDataCompany(): DataCompany {
        return DataCompany(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDataImage(),
            developedGames = developedGames
        )
    }


    private fun List<ApiWebsite>.toDataWebsites(): List<DataWebsite> {
        return map {
            DataWebsite(
                id = it.id,
                url = it.url,
                category = DataWebsiteCategory.valueOf(it.category.name)
            )
        }
    }


}


internal fun GameMapper.mapToDataGames(apiGames: List<ApiGame>): List<DataGame> {
    return apiGames.map(::mapToDataGame)
}