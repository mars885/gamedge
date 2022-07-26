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

package com.paulrybitskyi.gamedge.database.games.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = DbGame.Schema.TABLE_NAME,
    primaryKeys = [DbGame.Schema.ID],
    indices = [
        Index(DbGame.Schema.HYPE_COUNT),
        Index(DbGame.Schema.RELEASE_DATE),
        Index(DbGame.Schema.USERS_RATING),
        Index(DbGame.Schema.TOTAL_RATING),
        Index(DbGame.Schema.NAME)
    ]
)
data class DbGame(
    @ColumnInfo(name = Schema.ID) val id: Int,
    @ColumnInfo(name = Schema.FOLLOWER_COUNT) val followerCount: Int?,
    @ColumnInfo(name = Schema.HYPE_COUNT) val hypeCount: Int?,
    @ColumnInfo(name = Schema.RELEASE_DATE) val releaseDate: Long?,
    @ColumnInfo(name = Schema.CRITICS_RATING) val criticsRating: Double?,
    @ColumnInfo(name = Schema.USERS_RATING) val usersRating: Double?,
    @ColumnInfo(name = Schema.TOTAL_RATING) val totalRating: Double?,
    @ColumnInfo(name = Schema.NAME) val name: String,
    @ColumnInfo(name = Schema.SUMMARY) val summary: String?,
    @ColumnInfo(name = Schema.STORYLINE) val storyline: String?,
    @ColumnInfo(name = Schema.CATEGORY) val category: DbCategory,
    @ColumnInfo(name = Schema.COVER) val cover: DbImage?,
    @ColumnInfo(name = Schema.RELEASE_DATES) val releaseDates: List<DbReleaseDate>,
    @ColumnInfo(name = Schema.AGE_RATINGS) val ageRatings: List<DbAgeRating>,
    @ColumnInfo(name = Schema.VIDEOS) val videos: List<DbVideo>,
    @ColumnInfo(name = Schema.ARTWORKS) val artworks: List<DbImage>,
    @ColumnInfo(name = Schema.SCREENSHOTS) val screenshots: List<DbImage>,
    @ColumnInfo(name = Schema.GENRES) val genres: List<DbGenre>,
    @ColumnInfo(name = Schema.PLATFORMS) val platforms: List<DbPlatform>,
    @ColumnInfo(name = Schema.PLAYER_PERSPECTIVES) val playerPerspectives: List<DbPlayerPerspective>,
    @ColumnInfo(name = Schema.THEMES) val themes: List<DbTheme>,
    @ColumnInfo(name = Schema.MODES) val modes: List<DbMode>,
    @ColumnInfo(name = Schema.KEYWORDS) val keywords: List<DbKeyword>,
    @ColumnInfo(name = Schema.INVOLVED_COMPANIES) val involvedCompanies: List<DbInvolvedCompany>,
    @ColumnInfo(name = Schema.WEBSITES) val websites: List<DbWebsite>,
    @ColumnInfo(name = Schema.SIMILAR_GAMES) val similarGames: List<Int>,
) {

    object Schema {
        const val TABLE_NAME = "games"
        const val ID = "id"
        const val FOLLOWER_COUNT = "follower_count"
        const val HYPE_COUNT = "hype_count"
        const val RELEASE_DATE = "release_date"
        const val CRITICS_RATING = "critics_rating"
        const val USERS_RATING = "users_rating"
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
        const val MODES = "modes"
        const val KEYWORDS = "keywords"
        const val INVOLVED_COMPANIES = "involved_companies"
        const val WEBSITES = "websites"
        const val SIMILAR_GAMES = "similar_games"
    }
}
