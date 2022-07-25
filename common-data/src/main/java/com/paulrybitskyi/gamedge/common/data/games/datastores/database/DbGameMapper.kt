/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.common.data.games.datastores.database

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

internal class DbGameMapper @Inject constructor() {

    fun mapToDomainGame(databaseGame: DatabaseGame): DomainGame {
        return DomainGame(
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
            category = databaseGame.category.toDomainCategory(),
            cover = databaseGame.cover?.toDomainImage(),
            releaseDates = databaseGame.releaseDates.toDomainReleaseDates(),
            ageRatings = databaseGame.ageRatings.toDomainAgeRatings(),
            videos = databaseGame.videos.toDomainVideos(),
            artworks = databaseGame.artworks.toDomainImages(),
            screenshots = databaseGame.screenshots.toDomainImages(),
            genres = databaseGame.genres.toDomainGenres(),
            platforms = databaseGame.platforms.toDomainPlatforms(),
            playerPerspectives = databaseGame.playerPerspectives.toDomainPlayerPerspectives(),
            themes = databaseGame.themes.toDomainThemes(),
            modes = databaseGame.modes.toDomainModes(),
            keywords = databaseGame.keywords.toDomainKeywords(),
            involvedCompanies = databaseGame.involvedCompanies.toDomainInvolvedCompanies(),
            websites = databaseGame.websites.toDomainWebsites(),
            similarGames = databaseGame.similarGames,
        )
    }

    private fun DatabaseCategory.toDomainCategory(): DomainCategory {
        return DomainCategory.valueOf(name)
    }

    private fun DatabaseImage.toDomainImage(): DomainImage {
        return DomainImage(
            id = id,
            width = width,
            height = height,
        )
    }

    private fun List<DatabaseImage>.toDomainImages(): List<DomainImage> {
        return map { it.toDomainImage() }
    }

    private fun List<DatabaseReleaseDate>.toDomainReleaseDates(): List<DomainReleaseDate> {
        return map {
            DomainReleaseDate(
                date = it.date,
                year = it.year,
                category = DomainReleaseDateCategory.valueOf(it.category.name),
            )
        }
    }

    private fun List<DatabaseAgeRating>.toDomainAgeRatings(): List<DomainAgeRating> {
        return map {
            DomainAgeRating(
                category = DomainAgeRatingCategory.valueOf(it.category.name),
                type = DomainAgeRatingType.valueOf(it.type.name),
            )
        }
    }

    private fun List<DatabaseVideo>.toDomainVideos(): List<DomainVideo> {
        return map {
            DomainVideo(
                id = it.id,
                name = it.name,
            )
        }
    }

    private fun List<DatabaseGenre>.toDomainGenres(): List<DomainGenre> {
        return map {
            DomainGenre(
                name = it.name
            )
        }
    }

    private fun List<DatabasePlatform>.toDomainPlatforms(): List<DomainPlatform> {
        return map {
            DomainPlatform(
                abbreviation = it.abbreviation,
                name = it.name,
            )
        }
    }

    private fun List<DatabasePlayerPerspective>.toDomainPlayerPerspectives(): List<DomainPlayerPerspective> {
        return map {
            DomainPlayerPerspective(
                name = it.name,
            )
        }
    }

    private fun List<DatabaseTheme>.toDomainThemes(): List<DomainTheme> {
        return map {
            DomainTheme(
                name = it.name,
            )
        }
    }

    private fun List<DatabaseMode>.toDomainModes(): List<DomainMode> {
        return map {
            DomainMode(
                name = it.name,
            )
        }
    }

    private fun List<DatabaseKeyword>.toDomainKeywords(): List<DomainKeyword> {
        return map {
            DomainKeyword(
                name = it.name,
            )
        }
    }

    private fun List<DatabaseInvolvedCompany>.toDomainInvolvedCompanies(): List<DomainInvolvedCompany> {
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

    private fun DatabaseCompany.toDomainCompany(): DomainCompany {
        return DomainCompany(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDomainImage(),
            developedGames = developedGames,
        )
    }

    private fun List<DatabaseWebsite>.toDomainWebsites(): List<DomainWebsite> {
        return map {
            DomainWebsite(
                id = it.id,
                url = it.url,
                category = DomainWebsiteCategory.valueOf(it.category.name),
            )
        }
    }

    fun mapToDatabaseGame(domainGame: DomainGame): DatabaseGame {
        return DatabaseGame(
            id = domainGame.id,
            followerCount = domainGame.followerCount,
            hypeCount = domainGame.hypeCount,
            releaseDate = domainGame.releaseDate,
            criticsRating = domainGame.criticsRating,
            usersRating = domainGame.usersRating,
            totalRating = domainGame.totalRating,
            name = domainGame.name,
            summary = domainGame.summary,
            storyline = domainGame.storyline,
            category = domainGame.category.toDatabaseCategory(),
            cover = domainGame.cover?.toDatabaseImage(),
            releaseDates = domainGame.releaseDates.toDatabaseReleaseDates(),
            ageRatings = domainGame.ageRatings.toDatabaseAgeRatings(),
            videos = domainGame.videos.toDatabaseVideos(),
            artworks = domainGame.artworks.toDatabaseImages(),
            screenshots = domainGame.screenshots.toDatabaseImages(),
            genres = domainGame.genres.toDatabaseGenres(),
            platforms = domainGame.platforms.toDatabasePlatforms(),
            playerPerspectives = domainGame.playerPerspectives.toDatabasePlayerPerspectives(),
            themes = domainGame.themes.toDatabaseThemes(),
            modes = domainGame.modes.toDatabaseModes(),
            keywords = domainGame.keywords.toDatabaseKeywords(),
            involvedCompanies = domainGame.involvedCompanies.toDatabaseInvolvedCompanies(),
            websites = domainGame.websites.toDatabaseWebsites(),
            similarGames = domainGame.similarGames,
        )
    }

    private fun DomainCategory.toDatabaseCategory(): DatabaseCategory {
        return DatabaseCategory.valueOf(name)
    }

    private fun DomainImage.toDatabaseImage(): DatabaseImage {
        return DatabaseImage(
            id = id,
            width = width,
            height = height,
        )
    }

    private fun List<DomainImage>.toDatabaseImages(): List<DatabaseImage> {
        return map { it.toDatabaseImage() }
    }

    private fun List<DomainReleaseDate>.toDatabaseReleaseDates(): List<DatabaseReleaseDate> {
        return map {
            DatabaseReleaseDate(
                date = it.date,
                year = it.year,
                category = DatabaseReleaseDateCategory.valueOf(it.category.name),
            )
        }
    }

    private fun List<DomainAgeRating>.toDatabaseAgeRatings(): List<DatabaseAgeRating> {
        return map {
            DatabaseAgeRating(
                category = DatabaseAgeRatingCategory.valueOf(it.category.name),
                type = DatabaseAgeRatingType.valueOf(it.type.name),
            )
        }
    }

    private fun List<DomainVideo>.toDatabaseVideos(): List<DatabaseVideo> {
        return map {
            DatabaseVideo(
                id = it.id,
                name = it.name,
            )
        }
    }

    private fun List<DomainGenre>.toDatabaseGenres(): List<DatabaseGenre> {
        return map {
            DatabaseGenre(
                name = it.name,
            )
        }
    }

    private fun List<DomainPlatform>.toDatabasePlatforms(): List<DatabasePlatform> {
        return map {
            DatabasePlatform(
                abbreviation = it.abbreviation,
                name = it.name,
            )
        }
    }

    private fun List<DomainPlayerPerspective>.toDatabasePlayerPerspectives(): List<DatabasePlayerPerspective> {
        return map {
            DatabasePlayerPerspective(
                name = it.name,
            )
        }
    }

    private fun List<DomainTheme>.toDatabaseThemes(): List<DatabaseTheme> {
        return map {
            DatabaseTheme(
                name = it.name,
            )
        }
    }

    private fun List<DomainMode>.toDatabaseModes(): List<DatabaseMode> {
        return map {
            DatabaseMode(
                name = it.name,
            )
        }
    }

    private fun List<DomainKeyword>.toDatabaseKeywords(): List<DatabaseKeyword> {
        return map {
            DatabaseKeyword(
                name = it.name,
            )
        }
    }

    private fun List<DomainInvolvedCompany>.toDatabaseInvolvedCompanies(): List<DatabaseInvolvedCompany> {
        return map {
            DatabaseInvolvedCompany(
                company = it.company.toDatabaseCompany(),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting,
            )
        }
    }

    private fun DomainCompany.toDatabaseCompany(): DatabaseCompany {
        return DatabaseCompany(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDatabaseImage(),
            developedGames = developedGames,
        )
    }

    private fun List<DomainWebsite>.toDatabaseWebsites(): List<DatabaseWebsite> {
        return map {
            DatabaseWebsite(
                id = it.id,
                url = it.url,
                category = DatabaseWebsiteCategory.valueOf(it.category.name),
            )
        }
    }
}

internal fun DbGameMapper.mapToDomainGames(databaseGames: List<DatabaseGame>): List<DomainGame> {
    return databaseGames.map(::mapToDomainGame)
}

internal fun DbGameMapper.mapToDatabaseGames(domainGames: List<DomainGame>): List<DatabaseGame> {
    return domainGames.map(::mapToDatabaseGame)
}
