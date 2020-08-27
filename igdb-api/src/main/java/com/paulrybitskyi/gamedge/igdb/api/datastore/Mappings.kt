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

package com.paulrybitskyi.gamedge.igdb.api.datastore

import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.gamedge.data.utils.*
import com.paulrybitskyi.gamedge.igdb.api.entities.*
import com.paulrybitskyi.gamedge.igdb.api.utils.*
import com.paulrybitskyi.gamedge.data.entities.Error as DataError
import com.paulrybitskyi.gamedge.igdb.api.entities.Error as ApiError


internal fun DataGame.toApiGame(): ApiGame {
    return ApiGame(
        id = id,
        hypeCount = hypeCount,
        releaseDate = releaseDate,
        criticsRating = criticsRating,
        usersRating = usersRating,
        totalRating = totalRating,
        popularity = popularity,
        name = name,
        summary = summary,
        storyline = storyline,
        cover = cover.toApiImage(),
        timeToBeat = timeToBeat.toApiTimeToBeat(),
        ageRatings = ageRatings.toApiAgeRatings(),
        artworks = artworks.toApiImages(),
        screenshots = screenshots.toApiImages(),
        genres = genres.toApiGenres(),
        platforms = platforms.toApiPlatforms(),
        playerPerspectives = playerPerspectives.toApiPlayerPerspectives(),
        themes = themes.toApiThemes(),
        modes = modes.toApiModes(),
        keywords = keywords.toApiKeywords(),
        involvedCompanies = involvedCompanies.toApiInvolvedCompanies(),
        websites = websites.toApiWebsites(),
        similarGames = similarGames
    )
}


internal fun List<DataImage>.toApiImages(): List<ApiImage> {
    return map { it.toApiImage() }
}


internal fun DataImage.toApiImage(): ApiImage {
    return ApiImage(
        width = width,
        height = height,
        url = url
    )
}


internal fun DataTimeToBeat.toApiTimeToBeat(): ApiTimeToBeat {
    return ApiTimeToBeat(
        completely = completely,
        hastily = hastily,
        normally = normally
    )
}


internal fun List<DataAgeRating>.toApiAgeRatings(): List<ApiAgeRating> {
    return map { it.toApiAgeRating() }
}


internal fun DataAgeRating.toApiAgeRating(): ApiAgeRating {
    return ApiAgeRating(
        category = ApiAgeRatingCategory.valueOf(category.name),
        type = ApiAgeRatingType.valueOf(type.name)
    )
}


internal fun List<DataGenre>.toApiGenres(): List<ApiGenre> {
    return map { it.toApiGenre() }
}


internal fun DataGenre.toApiGenre(): ApiGenre {
    return ApiGenre(
        name = name
    )
}


internal fun List<DataPlatform>.toApiPlatforms(): List<ApiPlatform> {
    return map { it.toApiPlatform() }
}


internal fun DataPlatform.toApiPlatform(): ApiPlatform {
    return ApiPlatform(
        abbreviation = abbreviation
    )
}


internal fun List<DataPlayerPerspective>.toApiPlayerPerspectives(): List<ApiPlayerPerspective> {
    return map { it.toApiPlayerPerspective() }
}


internal fun DataPlayerPerspective.toApiPlayerPerspective(): ApiPlayerPerspective {
    return ApiPlayerPerspective(
        name = name
    )
}


internal fun List<DataTheme>.toApiThemes(): List<ApiTheme> {
    return map { it.toApiTheme() }
}


internal fun DataTheme.toApiTheme(): ApiTheme {
    return ApiTheme(
        name = name
    )
}


internal fun List<DataMode>.toApiModes(): List<ApiMode> {
    return map { it.toApiMode() }
}


internal fun DataMode.toApiMode(): ApiMode {
    return ApiMode(
        name = name
    )
}


internal fun List<DataKeyword>.toApiKeywords(): List<ApiKeyword> {
    return map { it.toApiKeyword() }
}


internal fun DataKeyword.toApiKeyword(): ApiKeyword {
    return ApiKeyword(
        name = name
    )
}


internal fun List<DataInvolvedCompany>.toApiInvolvedCompanies(): List<ApiInvolvedCompany> {
    return map { it.toApiInvolvedCompany() }
}


internal fun DataInvolvedCompany.toApiInvolvedCompany(): ApiInvolvedCompany {
    return ApiInvolvedCompany(
        company = company.toApiCompany(),
        isDeveloper = isDeveloper,
        isPublisher = isPublisher,
        isPorter = isPorter
    )
}


internal fun DataCompany.toApiCompany(): ApiCompany {
    return ApiCompany(
        name = name,
        developedGames = developedGames
    )
}


internal fun List<DataWebsite>.toApiWebsites(): List<ApiWebsite> {
    return map { it.toApiWebsite() }
}


internal fun DataWebsite.toApiWebsite(): ApiWebsite {
    return ApiWebsite(
        url = url,
        category = ApiWebsiteCategory.valueOf(category.name),
        isTrusted = isTrusted
    )
}


internal fun ApiResult<List<ApiGame>>.toDataStoreResult(): DataStoreResult<List<DataGame>> {
    return mapEither(
        success = { it.toDataGames() },
        failure = { it.toDataError() }
    )
}


internal fun List<ApiGame>.toDataGames(): List<DataGame> {
    return map { it.toDataGame() }
}


internal fun ApiGame.toDataGame(): DataGame {
    return DataGame(
        id = id,
        hypeCount = hypeCount,
        releaseDate = releaseDate,
        criticsRating = criticsRating,
        usersRating = usersRating,
        totalRating = totalRating,
        popularity = popularity,
        name = name,
        summary = summary,
        storyline = storyline,
        cover = cover.toDataImage(),
        timeToBeat = timeToBeat.toDataTimeToBeat(),
        ageRatings = ageRatings.toDataAgeRatings(),
        artworks = artworks.toDataImages(),
        screenshots = screenshots.toDataImages(),
        genres = genres.toDataGenres(),
        platforms = platforms.toDataPlatforms(),
        playerPerspectives = playerPerspectives.toDataPlayerPerspectives(),
        themes = themes.toDataThemes(),
        modes = modes.toDataModes(),
        keywords = keywords.toDataKeywords(),
        involvedCompanies = involvedCompanies.toDataInvolvedCompanies(),
        websites = websites.toDataWebsites(),
        similarGames = similarGames
    )
}


internal fun List<ApiImage>.toDataImages(): List<DataImage> {
    return map { it.toDataImage() }
}


internal fun ApiImage.toDataImage(): DataImage {
    return DataImage(
        width = width,
        height = height,
        url = url
    )
}


internal fun ApiTimeToBeat.toDataTimeToBeat(): DataTimeToBeat {
    return DataTimeToBeat(
        completely = completely,
        hastily = hastily,
        normally = normally
    )
}


internal fun List<ApiAgeRating>.toDataAgeRatings(): List<DataAgeRating> {
    return map { it.toDataAgeRating() }
}


internal fun ApiAgeRating.toDataAgeRating(): DataAgeRating {
    return DataAgeRating(
        category = DataAgeRatingCategory.valueOf(category.name),
        type = DataAgeRatingType.valueOf(type.name)
    )
}


internal fun List<ApiGenre>.toDataGenres(): List<DataGenre> {
    return map { it.toDataGenre() }
}


internal fun ApiGenre.toDataGenre(): DataGenre {
    return DataGenre(
        name = name
    )
}


internal fun List<ApiPlatform>.toDataPlatforms(): List<DataPlatform> {
    return map { it.toDataPlatform() }
}


internal fun ApiPlatform.toDataPlatform(): DataPlatform {
    return DataPlatform(
        abbreviation = abbreviation
    )
}


internal fun List<ApiPlayerPerspective>.toDataPlayerPerspectives(): List<DataPlayerPerspective> {
    return map { it.toDataPlayerPerspective() }
}


internal fun ApiPlayerPerspective.toDataPlayerPerspective(): DataPlayerPerspective {
    return DataPlayerPerspective(
        name = name
    )
}


internal fun List<ApiTheme>.toDataThemes(): List<DataTheme> {
    return map { it.toDataTheme() }
}


internal fun ApiTheme.toDataTheme(): DataTheme {
    return DataTheme(
        name = name
    )
}


internal fun List<ApiMode>.toDataModes(): List<DataMode> {
    return map { it.toDataMode() }
}


internal fun ApiMode.toDataMode(): DataMode {
    return DataMode(
        name = name
    )
}


internal fun List<ApiKeyword>.toDataKeywords(): List<DataKeyword> {
    return map { it.toDataKeyword() }
}


internal fun ApiKeyword.toDataKeyword(): DataKeyword {
    return DataKeyword(
        name = name
    )
}


internal fun List<ApiInvolvedCompany>.toDataInvolvedCompanies(): List<DataInvolvedCompany> {
    return map { it.toDataInvolvedCompany() }
}


internal fun ApiInvolvedCompany.toDataInvolvedCompany(): DataInvolvedCompany {
    return DataInvolvedCompany(
        company = company.toDataCompany(),
        isDeveloper = isDeveloper,
        isPublisher = isPublisher,
        isPorter = isPorter
    )
}


internal fun ApiCompany.toDataCompany(): DataCompany {
    return DataCompany(
        name = name,
        developedGames = developedGames
    )
}


internal fun List<ApiWebsite>.toDataWebsites(): List<DataWebsite> {
    return map { it.toDataWebsite() }
}


internal fun ApiWebsite.toDataWebsite(): DataWebsite {
    return DataWebsite(
        url = url,
        category = DataWebsiteCategory.valueOf(category.name),
        isTrusted = isTrusted
    )
}


internal fun ApiError.toDataError(): DataError {
    return when {
        isServerError -> DataError.ServiceUnavailable
        isHttpError -> DataError.ClientError(httpErrorMessage)
        isNetworkError -> DataError.NetworkError(networkErrorMessage)
        isUnknownError -> DataError.Unknown(unknownErrorMessage)

        else -> throw IllegalStateException("Could not map the api error $this to a data error. ")
    }
}