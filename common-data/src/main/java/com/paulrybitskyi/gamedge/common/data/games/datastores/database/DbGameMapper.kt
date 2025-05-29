/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

import com.paulrybitskyi.gamedge.common.domain.games.entities.AgeRating
import com.paulrybitskyi.gamedge.common.domain.games.entities.AgeRatingCategory
import com.paulrybitskyi.gamedge.common.domain.games.entities.AgeRatingType
import com.paulrybitskyi.gamedge.common.domain.games.entities.Category
import com.paulrybitskyi.gamedge.common.domain.games.entities.Company
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.common.domain.games.entities.Genre
import com.paulrybitskyi.gamedge.common.domain.games.entities.Image
import com.paulrybitskyi.gamedge.common.domain.games.entities.InvolvedCompany
import com.paulrybitskyi.gamedge.common.domain.games.entities.Keyword
import com.paulrybitskyi.gamedge.common.domain.games.entities.Mode
import com.paulrybitskyi.gamedge.common.domain.games.entities.Platform
import com.paulrybitskyi.gamedge.common.domain.games.entities.PlayerPerspective
import com.paulrybitskyi.gamedge.common.domain.games.entities.ReleaseDate
import com.paulrybitskyi.gamedge.common.domain.games.entities.ReleaseDateCategory
import com.paulrybitskyi.gamedge.common.domain.games.entities.Theme
import com.paulrybitskyi.gamedge.common.domain.games.entities.Video
import com.paulrybitskyi.gamedge.common.domain.games.entities.Website
import com.paulrybitskyi.gamedge.common.domain.games.entities.WebsiteCategory
import com.paulrybitskyi.gamedge.database.games.entities.DbAgeRating
import com.paulrybitskyi.gamedge.database.games.entities.DbAgeRatingCategory
import com.paulrybitskyi.gamedge.database.games.entities.DbAgeRatingType
import com.paulrybitskyi.gamedge.database.games.entities.DbCategory
import com.paulrybitskyi.gamedge.database.games.entities.DbCompany
import com.paulrybitskyi.gamedge.database.games.entities.DbGame
import com.paulrybitskyi.gamedge.database.games.entities.DbGenre
import com.paulrybitskyi.gamedge.database.games.entities.DbImage
import com.paulrybitskyi.gamedge.database.games.entities.DbInvolvedCompany
import com.paulrybitskyi.gamedge.database.games.entities.DbKeyword
import com.paulrybitskyi.gamedge.database.games.entities.DbMode
import com.paulrybitskyi.gamedge.database.games.entities.DbPlatform
import com.paulrybitskyi.gamedge.database.games.entities.DbPlayerPerspective
import com.paulrybitskyi.gamedge.database.games.entities.DbReleaseDate
import com.paulrybitskyi.gamedge.database.games.entities.DbReleaseDateCategory
import com.paulrybitskyi.gamedge.database.games.entities.DbTheme
import com.paulrybitskyi.gamedge.database.games.entities.DbVideo
import com.paulrybitskyi.gamedge.database.games.entities.DbWebsite
import com.paulrybitskyi.gamedge.database.games.entities.DbWebsiteCategory
import javax.inject.Inject

internal class DbGameMapper @Inject constructor() {

    fun mapToDomainGame(dbGame: DbGame): Game {
        return Game(
            id = dbGame.id,
            hypeCount = dbGame.hypeCount,
            releaseDate = dbGame.releaseDate,
            criticsRating = dbGame.criticsRating,
            usersRating = dbGame.usersRating,
            totalRating = dbGame.totalRating,
            name = dbGame.name,
            summary = dbGame.summary,
            storyline = dbGame.storyline,
            category = dbGame.category.toDomainCategory(),
            cover = dbGame.cover?.toDomainImage(),
            releaseDates = dbGame.releaseDates.toDomainReleaseDates(),
            ageRatings = dbGame.ageRatings.toDomainAgeRatings(),
            videos = dbGame.videos.toDomainVideos(),
            artworks = dbGame.artworks.toDomainImages(),
            screenshots = dbGame.screenshots.toDomainImages(),
            genres = dbGame.genres.toDomainGenres(),
            platforms = dbGame.platforms.toDomainPlatforms(),
            playerPerspectives = dbGame.playerPerspectives.toDomainPlayerPerspectives(),
            themes = dbGame.themes.toDomainThemes(),
            modes = dbGame.modes.toDomainModes(),
            keywords = dbGame.keywords.toDomainKeywords(),
            involvedCompanies = dbGame.involvedCompanies.toDomainInvolvedCompanies(),
            websites = dbGame.websites.toDomainWebsites(),
            similarGames = dbGame.similarGames,
        )
    }

    private fun DbCategory.toDomainCategory(): Category {
        return Category.valueOf(name)
    }

    private fun DbImage.toDomainImage(): Image {
        return Image(
            id = id,
            width = width,
            height = height,
        )
    }

    private fun List<DbImage>.toDomainImages(): List<Image> {
        return map { it.toDomainImage() }
    }

    private fun List<DbReleaseDate>.toDomainReleaseDates(): List<ReleaseDate> {
        return map {
            ReleaseDate(
                date = it.date,
                year = it.year,
                category = ReleaseDateCategory.valueOf(it.category.name),
            )
        }
    }

    private fun List<DbAgeRating>.toDomainAgeRatings(): List<AgeRating> {
        return map {
            AgeRating(
                category = AgeRatingCategory.valueOf(it.category.name),
                type = AgeRatingType.valueOf(it.type.name),
            )
        }
    }

    private fun List<DbVideo>.toDomainVideos(): List<Video> {
        return map {
            Video(
                id = it.id,
                name = it.name,
            )
        }
    }

    private fun List<DbGenre>.toDomainGenres(): List<Genre> {
        return map {
            Genre(
                name = it.name,
            )
        }
    }

    private fun List<DbPlatform>.toDomainPlatforms(): List<Platform> {
        return map {
            Platform(
                abbreviation = it.abbreviation,
                name = it.name,
            )
        }
    }

    private fun List<DbPlayerPerspective>.toDomainPlayerPerspectives(): List<PlayerPerspective> {
        return map {
            PlayerPerspective(
                name = it.name,
            )
        }
    }

    private fun List<DbTheme>.toDomainThemes(): List<Theme> {
        return map {
            Theme(
                name = it.name,
            )
        }
    }

    private fun List<DbMode>.toDomainModes(): List<Mode> {
        return map {
            Mode(
                name = it.name,
            )
        }
    }

    private fun List<DbKeyword>.toDomainKeywords(): List<Keyword> {
        return map {
            Keyword(
                name = it.name,
            )
        }
    }

    private fun List<DbInvolvedCompany>.toDomainInvolvedCompanies(): List<InvolvedCompany> {
        return map {
            InvolvedCompany(
                company = it.company.toDomainCompany(),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting,
            )
        }
    }

    private fun DbCompany.toDomainCompany(): Company {
        return Company(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDomainImage(),
            developedGames = developedGames,
        )
    }

    private fun List<DbWebsite>.toDomainWebsites(): List<Website> {
        return map {
            Website(
                id = it.id,
                url = it.url,
                category = WebsiteCategory.valueOf(it.category.name),
            )
        }
    }

    fun mapToDatabaseGame(domainGame: Game): DbGame {
        return DbGame(
            id = domainGame.id,
            hypeCount = domainGame.hypeCount,
            releaseDate = domainGame.releaseDate,
            criticsRating = domainGame.criticsRating,
            usersRating = domainGame.usersRating,
            totalRating = domainGame.totalRating,
            name = domainGame.name,
            summary = domainGame.summary,
            storyline = domainGame.storyline,
            category = domainGame.category.toDbCategory(),
            cover = domainGame.cover?.toDbImage(),
            releaseDates = domainGame.releaseDates.toDbReleaseDates(),
            ageRatings = domainGame.ageRatings.toDbAgeRatings(),
            videos = domainGame.videos.toDbVideos(),
            artworks = domainGame.artworks.toDbImages(),
            screenshots = domainGame.screenshots.toDbImages(),
            genres = domainGame.genres.toDbGenres(),
            platforms = domainGame.platforms.toDbPlatforms(),
            playerPerspectives = domainGame.playerPerspectives.toDbPlayerPerspectives(),
            themes = domainGame.themes.toDbThemes(),
            modes = domainGame.modes.toDbModes(),
            keywords = domainGame.keywords.toDbKeywords(),
            involvedCompanies = domainGame.involvedCompanies.toDatabaseInvolvedCompanies(),
            websites = domainGame.websites.toDbWebsites(),
            similarGames = domainGame.similarGames,
        )
    }

    private fun Category.toDbCategory(): DbCategory {
        return DbCategory.valueOf(name)
    }

    private fun Image.toDbImage(): DbImage {
        return DbImage(
            id = id,
            width = width,
            height = height,
        )
    }

    private fun List<Image>.toDbImages(): List<DbImage> {
        return map { it.toDbImage() }
    }

    private fun List<ReleaseDate>.toDbReleaseDates(): List<DbReleaseDate> {
        return map {
            DbReleaseDate(
                date = it.date,
                year = it.year,
                category = DbReleaseDateCategory.valueOf(it.category.name),
            )
        }
    }

    private fun List<AgeRating>.toDbAgeRatings(): List<DbAgeRating> {
        return map {
            DbAgeRating(
                category = DbAgeRatingCategory.valueOf(it.category.name),
                type = DbAgeRatingType.valueOf(it.type.name),
            )
        }
    }

    private fun List<Video>.toDbVideos(): List<DbVideo> {
        return map {
            DbVideo(
                id = it.id,
                name = it.name,
            )
        }
    }

    private fun List<Genre>.toDbGenres(): List<DbGenre> {
        return map {
            DbGenre(
                name = it.name,
            )
        }
    }

    private fun List<Platform>.toDbPlatforms(): List<DbPlatform> {
        return map {
            DbPlatform(
                abbreviation = it.abbreviation,
                name = it.name,
            )
        }
    }

    private fun List<PlayerPerspective>.toDbPlayerPerspectives(): List<DbPlayerPerspective> {
        return map {
            DbPlayerPerspective(
                name = it.name,
            )
        }
    }

    private fun List<Theme>.toDbThemes(): List<DbTheme> {
        return map {
            DbTheme(
                name = it.name,
            )
        }
    }

    private fun List<Mode>.toDbModes(): List<DbMode> {
        return map {
            DbMode(
                name = it.name,
            )
        }
    }

    private fun List<Keyword>.toDbKeywords(): List<DbKeyword> {
        return map {
            DbKeyword(
                name = it.name,
            )
        }
    }

    private fun List<InvolvedCompany>.toDatabaseInvolvedCompanies(): List<DbInvolvedCompany> {
        return map {
            DbInvolvedCompany(
                company = it.company.toDbCompany(),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting,
            )
        }
    }

    private fun Company.toDbCompany(): DbCompany {
        return DbCompany(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDbImage(),
            developedGames = developedGames,
        )
    }

    private fun List<Website>.toDbWebsites(): List<DbWebsite> {
        return map {
            DbWebsite(
                id = it.id,
                url = it.url,
                category = DbWebsiteCategory.valueOf(it.category.name),
            )
        }
    }
}

internal fun DbGameMapper.mapToDomainGames(dbGames: List<DbGame>): List<Game> {
    return dbGames.map(::mapToDomainGame)
}

internal fun DbGameMapper.mapToDatabaseGames(domainGames: List<Game>): List<DbGame> {
    return domainGames.map(::mapToDatabaseGame)
}
