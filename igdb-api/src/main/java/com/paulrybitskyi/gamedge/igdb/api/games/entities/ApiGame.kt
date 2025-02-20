/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.igdb.api.games.entities

import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.Apicalypse
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.ApicalypseClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@ApicalypseClass
@Serializable
data class ApiGame(
    @Apicalypse(Schema.ID)
    @SerialName(Schema.ID)
    val id: Int,
    @Apicalypse(Schema.FOLLOWER_COUNT)
    @SerialName(Schema.FOLLOWER_COUNT)
    val followerCount: Int? = null,
    @Apicalypse(Schema.HYPE_COUNT)
    @SerialName(Schema.HYPE_COUNT)
    val hypeCount: Int? = null,
    @Apicalypse(Schema.RELEASE_DATE)
    @SerialName(Schema.RELEASE_DATE)
    val releaseDate: Long? = null,
    @Apicalypse(Schema.CRITICS_RATING)
    @SerialName(Schema.CRITICS_RATING)
    val criticsRating: Double? = null,
    @Apicalypse(Schema.USERS_RATING)
    @SerialName(Schema.USERS_RATING)
    val usersRating: Double? = null,
    @Apicalypse(Schema.TOTAL_RATING)
    @SerialName(Schema.TOTAL_RATING)
    val totalRating: Double? = null,
    @Apicalypse(Schema.NAME)
    @SerialName(Schema.NAME)
    val name: String,
    @Apicalypse(Schema.SUMMARY)
    @SerialName(Schema.SUMMARY)
    val summary: String? = null,
    @Apicalypse(Schema.STORYLINE)
    @SerialName(Schema.STORYLINE)
    val storyline: String? = null,
    @Apicalypse(Schema.CATEGORY)
    @SerialName(Schema.CATEGORY)
    val category: ApiCategory = ApiCategory.UNKNOWN,
    @Apicalypse(Schema.COVER)
    @SerialName(Schema.COVER)
    val cover: ApiImage? = null,
    @Apicalypse(Schema.RELEASE_DATES)
    @SerialName(Schema.RELEASE_DATES)
    val releaseDates: List<ApiReleaseDate> = listOf(),
    @Apicalypse(Schema.AGE_RATINGS)
    @SerialName(Schema.AGE_RATINGS)
    val ageRatings: List<ApiAgeRating> = listOf(),
    @Apicalypse(Schema.VIDEOS)
    @SerialName(Schema.VIDEOS)
    val videos: List<ApiVideo> = listOf(),
    @Apicalypse(Schema.ARTWORKS)
    @SerialName(Schema.ARTWORKS)
    val artworks: List<ApiImage> = listOf(),
    @Apicalypse(Schema.SCREENSHOTS)
    @SerialName(Schema.SCREENSHOTS)
    val screenshots: List<ApiImage> = listOf(),
    @Apicalypse(Schema.GENRES)
    @SerialName(Schema.GENRES)
    val genres: List<ApiGenre> = listOf(),
    @Apicalypse(Schema.PLATFORMS)
    @SerialName(Schema.PLATFORMS)
    val platforms: List<ApiPlatform> = listOf(),
    @Apicalypse(Schema.PLAYER_PERSPECTIVES)
    @SerialName(Schema.PLAYER_PERSPECTIVES)
    val playerPerspectives: List<ApiPlayerPerspective> = listOf(),
    @Apicalypse(Schema.THEMES)
    @SerialName(Schema.THEMES)
    val themes: List<ApiTheme> = listOf(),
    @Apicalypse(Schema.MODES)
    @SerialName(Schema.MODES)
    val modes: List<ApiMode> = listOf(),
    @Apicalypse(Schema.KEYWORDS)
    @SerialName(Schema.KEYWORDS)
    val keywords: List<ApiKeyword> = listOf(),
    @Apicalypse(Schema.INVOLVED_COMPANIES)
    @SerialName(Schema.INVOLVED_COMPANIES)
    val involvedCompanies: List<ApiInvolvedCompany> = listOf(),
    @Apicalypse(Schema.WEBSITES)
    @SerialName(Schema.WEBSITES)
    val websites: List<ApiWebsite> = listOf(),
    @Apicalypse(Schema.SIMILAR_GAMES)
    @SerialName(Schema.SIMILAR_GAMES)
    val similarGames: List<Int> = listOf(),
) {

    object Schema {
        const val ID = "id"
        const val FOLLOWER_COUNT = "follows"
        const val HYPE_COUNT = "hypes"
        const val RELEASE_DATE = "first_release_date"
        const val CRITICS_RATING = "aggregated_rating"
        const val USERS_RATING = "rating"
        const val TOTAL_RATING = "total_rating"
        const val NAME = "name"
        const val SUMMARY = "summary"
        const val STORYLINE = "storyline"
        const val CATEGORY = "category"
        const val COVER = "cover"
        const val RELEASE_DATES = "release_dates"
        const val AGE_RATINGS = "age_ratings"
        const val VIDEOS = "videos"
        const val ARTWORKS = "artworks"
        const val SCREENSHOTS = "screenshots"
        const val GENRES = "genres"
        const val PLATFORMS = "platforms"
        const val PLAYER_PERSPECTIVES = "player_perspectives"
        const val THEMES = "themes"
        const val MODES = "game_modes"
        const val KEYWORDS = "keywords"
        const val INVOLVED_COMPANIES = "involved_companies"
        const val WEBSITES = "websites"
        const val SIMILAR_GAMES = "similar_games"
    }
}
