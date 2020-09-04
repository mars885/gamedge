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

package com.paulrybitskyi.gamedge.igdb.api.entities

import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.Apicalypse
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.ApicalypseClass
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@ApicalypseClass
@JsonClass(generateAdapter = true)
internal data class Game(
    @Apicalypse(name = Schema.ID)
    @Json(name = Schema.ID)
    val id: Int = -1,
    @Apicalypse(name = Schema.HYPE_COUNT)
    @Json(name = Schema.HYPE_COUNT)
    val hypeCount: Int? = null,
    @Apicalypse(name = Schema.RELEASE_DATE)
    @Json(name = Schema.RELEASE_DATE)
    val releaseDate: Long? = null,
    @Apicalypse(name = Schema.CRITICS_RATING)
    @Json(name = Schema.CRITICS_RATING)
    val criticsRating: Double? = null,
    @Apicalypse(name = Schema.USERS_RATING)
    @Json(name = Schema.USERS_RATING)
    val usersRating: Double? = null,
    @Apicalypse(name = Schema.TOTAL_RATING)
    @Json(name = Schema.TOTAL_RATING)
    val totalRating: Double? = null,
    @Apicalypse(name = Schema.POPULARITY)
    @Json(name = Schema.POPULARITY)
    val popularity: Double? = null,
    @Apicalypse(name = Schema.NAME)
    @Json(name = Schema.NAME)
    val name: String = "",
    @Apicalypse(name = Schema.SUMMARY)
    @Json(name = Schema.SUMMARY)
    val summary: String? = null,
    @Apicalypse(name = Schema.STORYLINE)
    @Json(name = Schema.STORYLINE)
    val storyline: String? = null,
    @Apicalypse(name = Schema.COVER)
    @Json(name = Schema.COVER)
    val cover: Image? = null,
    @Apicalypse(name = Schema.TIME_TO_BEAT)
    @Json(name = Schema.TIME_TO_BEAT)
    val timeToBeat: TimeToBeat? = null,
    @Apicalypse(name = Schema.AGE_RATINGS)
    @Json(name = Schema.AGE_RATINGS)
    val ageRatings: List<AgeRating> = listOf(),
    @Apicalypse(name = Schema.ARTWORKS)
    @Json(name = Schema.ARTWORKS)
    val artworks: List<Image> = listOf(),
    @Apicalypse(name = Schema.SCREENSHOTS)
    @Json(name = Schema.SCREENSHOTS)
    val screenshots: List<Image> = listOf(),
    @Apicalypse(name = Schema.GENRES)
    @Json(name = Schema.GENRES)
    val genres: List<Genre> = listOf(),
    @Apicalypse(name = Schema.PLATFORMS)
    @Json(name = Schema.PLATFORMS)
    val platforms: List<Platform> = listOf(),
    @Apicalypse(name = Schema.PLAYER_PERSPECTIVES)
    @Json(name = Schema.PLAYER_PERSPECTIVES)
    val playerPerspectives: List<PlayerPerspective> = listOf(),
    @Apicalypse(name = Schema.THEMES)
    @Json(name = Schema.THEMES)
    val themes: List<Theme> = listOf(),
    @Apicalypse(name = Schema.MODES)
    @Json(name = Schema.MODES)
    val modes: List<Mode> = listOf(),
    @Apicalypse(name = Schema.KEYWORDS)
    @Json(name = Schema.KEYWORDS)
    val keywords: List<Keyword> = listOf(),
    @Apicalypse(name = Schema.INVOLVED_COMPANIES)
    @Json(name = Schema.INVOLVED_COMPANIES)
    val involvedCompanies: List<InvolvedCompany> = listOf(),
    @Apicalypse(name = Schema.WEBSITES)
    @Json(name = Schema.WEBSITES)
    val websites: List<Website> = listOf(),
    @Apicalypse(name = Schema.SIMILAR_GAMES)
    @Json(name = Schema.SIMILAR_GAMES)
    val similarGames: List<Int> = listOf(),
) {


    object Schema {

        const val ID = "id"
        const val HYPE_COUNT = "hypes"
        const val RELEASE_DATE = "first_release_date"
        const val CRITICS_RATING = "aggregated_rating"
        const val USERS_RATING = "rating"
        const val TOTAL_RATING = "total_rating"
        const val POPULARITY = "popularity"
        const val NAME = "name"
        const val SUMMARY = "summary"
        const val STORYLINE = "storyline"
        const val COVER = "cover"
        const val TIME_TO_BEAT = "time_to_beat"
        const val AGE_RATINGS = "age_ratings"
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