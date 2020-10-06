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

import com.paulrybitskyi.gamedge.data.games.*
import com.paulrybitskyi.gamedge.domain.games.*

internal class GameMapper {


    fun mapToDomainGame(game: DataGame): DomainGame {
        return DomainGame(
            id = game.id,
            hypeCount = game.hypeCount,
            releaseDate = game.releaseDate,
            criticsRating = game.criticsRating,
            usersRating = game.usersRating,
            totalRating = game.totalRating,
            name = game.name,
            summary = game.summary,
            storyline = game.storyline,
            cover = game.cover?.toDomainImage(),
            ageRatings = game.ageRatings.toDomainAgeRatings(),
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


    private fun List<DataAgeRating>.toDomainAgeRatings(): List<DomainAgeRating> {
        return map {
            DomainAgeRating(
                category = DomainAgeRatingCategory.valueOf(it.category.name),
                type = DomainAgeRatingType.valueOf(it.type.name)
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
                abbreviation = it.abbreviation
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
                isPorter = it.isPorter
            )
        }
    }


    private fun DataCompany.toDomainCompany(): DomainCompany {
        return DomainCompany(
            name = name,
            developedGames = developedGames
        )
    }


    private fun List<DataWebsite>.toDomainWebsites(): List<DomainWebsite> {
        return map {
            DomainWebsite(
                url = it.url,
                category = DomainWebsiteCategory.valueOf(it.category.name),
                isTrusted = it.isTrusted
            )
        }
    }


}


internal fun GameMapper.mapToDomainGames(dataGames: List<DataGame>): List<DomainGame> {
    return dataGames.map(::mapToDomainGame)
}