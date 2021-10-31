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

package com.paulrybitskyi.gamedge.data.games.usecases.commons

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
import com.paulrybitskyi.gamedge.domain.games.DomainAgeRating
import com.paulrybitskyi.gamedge.domain.games.DomainAgeRatingCategory
import com.paulrybitskyi.gamedge.domain.games.DomainAgeRatingType
import com.paulrybitskyi.gamedge.domain.games.DomainCategory
import com.paulrybitskyi.gamedge.domain.games.DomainCompany
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.DomainGenre
import com.paulrybitskyi.gamedge.domain.games.DomainImage
import com.paulrybitskyi.gamedge.domain.games.DomainInvolvedCompany
import com.paulrybitskyi.gamedge.domain.games.DomainKeyword
import com.paulrybitskyi.gamedge.domain.games.DomainMode
import com.paulrybitskyi.gamedge.domain.games.DomainPlatform
import com.paulrybitskyi.gamedge.domain.games.DomainPlayerPerspective
import com.paulrybitskyi.gamedge.domain.games.DomainReleaseDate
import com.paulrybitskyi.gamedge.domain.games.DomainReleaseDateCategory
import com.paulrybitskyi.gamedge.domain.games.DomainTheme
import com.paulrybitskyi.gamedge.domain.games.DomainVideo
import com.paulrybitskyi.gamedge.domain.games.DomainWebsite
import com.paulrybitskyi.gamedge.domain.games.DomainWebsiteCategory
import javax.inject.Inject


@Suppress("TooManyFunctions")
internal class GameMapper @Inject constructor() {


    fun mapToDomainGame(game: DataGame): DomainGame {
        return DomainGame(
            id = game.id,
            followerCount = game.followerCount,
            hypeCount = game.hypeCount,
            releaseDate = game.releaseDate,
            criticsRating = game.criticsRating,
            usersRating = game.usersRating,
            totalRating = game.totalRating,
            name = game.name,
            summary = game.summary,
            storyline = game.storyline,
            category = game.category.toDomainCategory(),
            cover = game.cover?.toDomainImage(),
            releaseDates = game.releaseDates.toDomainReleaseDates(),
            ageRatings = game.ageRatings.toDomainAgeRatings(),
            videos = game.videos.toDomainVideos(),
            artworks = game.artworks.toDomainImages(),
            screenshots = game.screenshots.toDomainImages(),
            genres = game.genres.toDomainGenres(),
            platforms = game.platforms.toDomainPlatforms(),
            playerPerspectives = game.playerPerspectives.toDomainPlayerPerspectives(),
            themes = game.themes.toDomainThemes(),
            modes = game.modes.toDomainModes(),
            keywords = game.keywords.toDomainKeywords(),
            involvedCompanies = game.involvedCompanies.toDomainInvolvedCompanies(),
            websites = game.websites.toDomainWebsites(),
            similarGames = game.similarGames
        )
    }


    private fun DataCategory.toDomainCategory(): DomainCategory {
        return DomainCategory.valueOf(name)
    }


    private fun DataImage.toDomainImage(): DomainImage {
        return DomainImage(
            id = id,
            width = width,
            height = height
        )
    }


    private fun List<DataImage>.toDomainImages(): List<DomainImage> {
        return map { it.toDomainImage() }
    }


    private fun List<DataReleaseDate>.toDomainReleaseDates(): List<DomainReleaseDate> {
        return map {
            DomainReleaseDate(
                date = it.date,
                year = it.year,
                category = DomainReleaseDateCategory.valueOf(it.category.name)
            )
        }
    }


    private fun List<DataAgeRating>.toDomainAgeRatings(): List<DomainAgeRating> {
        return map {
            DomainAgeRating(
                category = DomainAgeRatingCategory.valueOf(it.category.name),
                type = DomainAgeRatingType.valueOf(it.type.name)
            )
        }
    }


    private fun List<DataVideo>.toDomainVideos(): List<DomainVideo> {
        return map {
            DomainVideo(
                id = it.id,
                name = it.name
            )
        }
    }


    private fun List<DataGenre>.toDomainGenres(): List<DomainGenre> {
        return map {
            DomainGenre(
                name = it.name
            )
        }
    }


    private fun List<DataPlatform>.toDomainPlatforms(): List<DomainPlatform> {
        return map {
            DomainPlatform(
                abbreviation = it.abbreviation,
                name = it.name
            )
        }
    }


    private fun List<DataPlayerPerspective>.toDomainPlayerPerspectives(): List<DomainPlayerPerspective> {
        return map {
            DomainPlayerPerspective(
                name = it.name
            )
        }
    }


    private fun List<DataTheme>.toDomainThemes(): List<DomainTheme> {
        return map {
            DomainTheme(
                name = it.name
            )
        }
    }


    private fun List<DataMode>.toDomainModes(): List<DomainMode> {
        return map {
            DomainMode(
                name = it.name
            )
        }
    }


    private fun List<DataKeyword>.toDomainKeywords(): List<DomainKeyword> {
        return map {
            DomainKeyword(
                name = it.name
            )
        }
    }


    private fun List<DataInvolvedCompany>.toDomainInvolvedCompanies(): List<DomainInvolvedCompany> {
        return map {
            DomainInvolvedCompany(
                company = it.company.toDomainCompany(),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting
            )
        }
    }


    private fun DataCompany.toDomainCompany(): DomainCompany {
        return DomainCompany(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDomainImage(),
            developedGames = developedGames
        )
    }


    private fun List<DataWebsite>.toDomainWebsites(): List<DomainWebsite> {
        return map {
            DomainWebsite(
                id = it.id,
                url = it.url,
                category = DomainWebsiteCategory.valueOf(it.category.name)
            )
        }
    }


    fun mapToDataGame(game: DomainGame): DataGame {
        return DataGame(
            id = game.id,
            followerCount = game.followerCount,
            hypeCount = game.hypeCount,
            releaseDate = game.releaseDate,
            criticsRating = game.criticsRating,
            usersRating = game.usersRating,
            totalRating = game.totalRating,
            name = game.name,
            summary = game.summary,
            storyline = game.storyline,
            category = game.category.toDataCategory(),
            cover = game.cover?.toDataImage(),
            releaseDates = game.releaseDates.toDataReleaseDates(),
            ageRatings = game.ageRatings.toDataAgeRatings(),
            artworks = game.artworks.toDataImages(),
            videos = game.videos.toDataVideos(),
            screenshots = game.screenshots.toDataImages(),
            genres = game.genres.toDataGenres(),
            platforms = game.platforms.toDataPlatforms(),
            playerPerspectives = game.playerPerspectives.toDataPlayerPerspectives(),
            themes = game.themes.toDataThemes(),
            modes = game.modes.toDataModes(),
            keywords = game.keywords.toDataKeywords(),
            involvedCompanies = game.involvedCompanies.toDataInvolvedCompanies(),
            websites = game.websites.toDataWebsites(),
            similarGames = game.similarGames
        )
    }


    private fun DomainCategory.toDataCategory(): DataCategory {
        return DataCategory.valueOf(name)
    }


    private fun DomainImage.toDataImage(): DataImage {
        return DataImage(
            id = id,
            width = width,
            height = height
        )
    }


    private fun List<DomainImage>.toDataImages(): List<DataImage> {
        return map { it.toDataImage() }
    }


    private fun List<DomainReleaseDate>.toDataReleaseDates(): List<DataReleaseDate> {
        return map {
            DataReleaseDate(
                date = it.date,
                year = it.year,
                category = DataReleaseDateCategory.valueOf(it.category.name)
            )
        }
    }


    private fun List<DomainAgeRating>.toDataAgeRatings(): List<DataAgeRating> {
        return map {
            DataAgeRating(
                category = DataAgeRatingCategory.valueOf(it.category.name),
                type = DataAgeRatingType.valueOf(it.type.name)
            )
        }
    }


    private fun List<DomainVideo>.toDataVideos(): List<DataVideo> {
        return map {
            DataVideo(
                id = it.id,
                name = it.name
            )
        }
    }


    private fun List<DomainGenre>.toDataGenres(): List<DataGenre> {
        return map {
            DataGenre(
                name = it.name
            )
        }
    }


    private fun List<DomainPlatform>.toDataPlatforms(): List<DataPlatform> {
        return map {
            DataPlatform(
                abbreviation = it.abbreviation,
                name = it.name
            )
        }
    }


    private fun List<DomainPlayerPerspective>.toDataPlayerPerspectives(): List<DataPlayerPerspective> {
        return map {
            DataPlayerPerspective(
                name = it.name
            )
        }
    }


    private fun List<DomainTheme>.toDataThemes(): List<DataTheme> {
        return map {
            DataTheme(
                name = it.name
            )
        }
    }


    private fun List<DomainMode>.toDataModes(): List<DataMode> {
        return map {
            DataMode(
                name = it.name
            )
        }
    }


    private fun List<DomainKeyword>.toDataKeywords(): List<DataKeyword> {
        return map {
            DataKeyword(
                name = it.name
            )
        }
    }


    private fun List<DomainInvolvedCompany>.toDataInvolvedCompanies(): List<DataInvolvedCompany> {
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


    fun mapToDataCompany(company: DomainCompany): DataCompany {
        return DataCompany(
            id = company.id,
            name = company.name,
            websiteUrl = company.websiteUrl,
            logo = company.logo?.toDataImage(),
            developedGames = company.developedGames
        )
    }


    private fun List<DomainWebsite>.toDataWebsites(): List<DataWebsite> {
        return map {
            DataWebsite(
                id = it.id,
                url = it.url,
                category = DataWebsiteCategory.valueOf(it.category.name)
            )
        }
    }


}


internal fun GameMapper.mapToDomainGames(dataGames: List<DataGame>): List<DomainGame> {
    return dataGames.map(::mapToDomainGame)
}
