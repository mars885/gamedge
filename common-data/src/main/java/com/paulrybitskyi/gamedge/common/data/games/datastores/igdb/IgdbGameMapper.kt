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

package com.paulrybitskyi.gamedge.common.data.games.datastores.igdb

import com.paulrybitskyi.gamedge.common.domain.games.DomainAgeRating
import com.paulrybitskyi.gamedge.common.domain.games.DomainAgeRatingCategory
import com.paulrybitskyi.gamedge.common.domain.games.DomainAgeRatingType
import com.paulrybitskyi.gamedge.common.domain.games.DomainCategory
import com.paulrybitskyi.gamedge.common.domain.games.DomainCompany
import com.paulrybitskyi.gamedge.common.domain.games.DomainGame
import com.paulrybitskyi.gamedge.common.domain.games.DomainGenre
import com.paulrybitskyi.gamedge.common.domain.games.DomainImage
import com.paulrybitskyi.gamedge.common.domain.games.DomainInvolvedCompany
import com.paulrybitskyi.gamedge.common.domain.games.DomainKeyword
import com.paulrybitskyi.gamedge.common.domain.games.DomainMode
import com.paulrybitskyi.gamedge.common.domain.games.DomainPlatform
import com.paulrybitskyi.gamedge.common.domain.games.DomainPlayerPerspective
import com.paulrybitskyi.gamedge.common.domain.games.DomainReleaseDate
import com.paulrybitskyi.gamedge.common.domain.games.DomainReleaseDateCategory
import com.paulrybitskyi.gamedge.common.domain.games.DomainTheme
import com.paulrybitskyi.gamedge.common.domain.games.DomainVideo
import com.paulrybitskyi.gamedge.common.domain.games.DomainWebsite
import com.paulrybitskyi.gamedge.common.domain.games.DomainWebsiteCategory
import com.paulrybitskyi.gamedge.igdb.api.games.ApiAgeRating
import com.paulrybitskyi.gamedge.igdb.api.games.ApiCategory
import com.paulrybitskyi.gamedge.igdb.api.games.ApiCompany
import com.paulrybitskyi.gamedge.igdb.api.games.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.games.ApiGenre
import com.paulrybitskyi.gamedge.igdb.api.games.ApiImage
import com.paulrybitskyi.gamedge.igdb.api.games.ApiInvolvedCompany
import com.paulrybitskyi.gamedge.igdb.api.games.ApiKeyword
import com.paulrybitskyi.gamedge.igdb.api.games.ApiMode
import com.paulrybitskyi.gamedge.igdb.api.games.ApiPlatform
import com.paulrybitskyi.gamedge.igdb.api.games.ApiPlayerPerspective
import com.paulrybitskyi.gamedge.igdb.api.games.ApiReleaseDate
import com.paulrybitskyi.gamedge.igdb.api.games.ApiTheme
import com.paulrybitskyi.gamedge.igdb.api.games.ApiVideo
import com.paulrybitskyi.gamedge.igdb.api.games.ApiWebsite
import javax.inject.Inject

internal class IgdbGameMapper @Inject constructor() {

    fun mapToDomainGame(apiGame: ApiGame): DomainGame {
        return DomainGame(
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
            category = apiGame.category.toDomainCategory(),
            cover = apiGame.cover?.toDomainImage(),
            releaseDates = apiGame.releaseDates.toDomainReleaseDates(),
            ageRatings = apiGame.ageRatings.toDomainAgeRatings(),
            videos = apiGame.videos.toDomainVideos(),
            artworks = apiGame.artworks.toDomainImages(),
            screenshots = apiGame.screenshots.toDomainImages(),
            genres = apiGame.genres.toDomainGenres(),
            platforms = apiGame.platforms.toDomainPlatforms(),
            playerPerspectives = apiGame.playerPerspectives.toDomainPlayerPerspectives(),
            themes = apiGame.themes.toDomainThemes(),
            modes = apiGame.modes.toDomainModes(),
            keywords = apiGame.keywords.toDomainKeywords(),
            involvedCompanies = apiGame.involvedCompanies.toDomainInvolvedCompanies(),
            websites = apiGame.websites.toDomainWebsites(),
            similarGames = apiGame.similarGames,
        )
    }

    private fun ApiCategory.toDomainCategory(): DomainCategory {
        return DomainCategory.valueOf(name)
    }

    private fun ApiImage.toDomainImage(): DomainImage {
        return DomainImage(
            id = id,
            width = width,
            height = height,
        )
    }

    private fun List<ApiImage>.toDomainImages(): List<DomainImage> {
        return map { it.toDomainImage() }
    }

    private fun List<ApiVideo>.toDomainVideos(): List<DomainVideo> {
        return map {
            DomainVideo(
                id = it.id,
                name = it.name,
            )
        }
    }

    private fun List<ApiReleaseDate>.toDomainReleaseDates(): List<DomainReleaseDate> {
        return map {
            DomainReleaseDate(
                date = it.date,
                year = it.year,
                category = DomainReleaseDateCategory.valueOf(it.category.name),
            )
        }
    }

    private fun List<ApiAgeRating>.toDomainAgeRatings(): List<DomainAgeRating> {
        return map {
            DomainAgeRating(
                category = DomainAgeRatingCategory.valueOf(it.category.name),
                type = DomainAgeRatingType.valueOf(it.type.name),
            )
        }
    }

    private fun List<ApiGenre>.toDomainGenres(): List<DomainGenre> {
        return map {
            DomainGenre(
                name = it.name,
            )
        }
    }

    private fun List<ApiPlatform>.toDomainPlatforms(): List<DomainPlatform> {
        return map {
            DomainPlatform(
                abbreviation = it.abbreviation,
                name = it.name,
            )
        }
    }

    private fun List<ApiPlayerPerspective>.toDomainPlayerPerspectives(): List<DomainPlayerPerspective> {
        return map {
            DomainPlayerPerspective(
                name = it.name,
            )
        }
    }

    private fun List<ApiTheme>.toDomainThemes(): List<DomainTheme> {
        return map {
            DomainTheme(
                name = it.name,
            )
        }
    }

    private fun List<ApiMode>.toDomainModes(): List<DomainMode> {
        return map {
            DomainMode(
                name = it.name,
            )
        }
    }

    private fun List<ApiKeyword>.toDomainKeywords(): List<DomainKeyword> {
        return map {
            DomainKeyword(
                name = it.name,
            )
        }
    }

    private fun List<ApiInvolvedCompany>.toDomainInvolvedCompanies(): List<DomainInvolvedCompany> {
        return map {
            DomainInvolvedCompany(
                company = it.company.toDomainCompany(),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting,
            )
        }
    }

    private fun ApiCompany.toDomainCompany(): DomainCompany {
        return DomainCompany(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDomainImage(),
            developedGames = developedGames,
        )
    }

    private fun List<ApiWebsite>.toDomainWebsites(): List<DomainWebsite> {
        return map {
            DomainWebsite(
                id = it.id,
                url = it.url,
                category = DomainWebsiteCategory.valueOf(it.category.name),
            )
        }
    }
}

internal fun IgdbGameMapper.mapToDomainGames(apiGames: List<ApiGame>): List<DomainGame> {
    return apiGames.map(::mapToDomainGame)
}
