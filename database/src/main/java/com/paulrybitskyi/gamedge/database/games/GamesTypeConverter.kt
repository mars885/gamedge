/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.database.games

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.paulrybitskyi.gamedge.core.JsonConverter
import com.paulrybitskyi.gamedge.database.common.RoomTypeConverter
import com.paulrybitskyi.gamedge.database.games.entities.DbAgeRating
import com.paulrybitskyi.gamedge.database.games.entities.DbAgeRatingCategory
import com.paulrybitskyi.gamedge.database.games.entities.DbAgeRatingType
import com.paulrybitskyi.gamedge.database.games.entities.DbCategory
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
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@ProvidedTypeConverter
@BindType(contributesTo = BindType.Collection.SET)
@Suppress("TooManyFunctions")
internal class GamesTypeConverter @Inject constructor(
    private val jsonConverter: JsonConverter
) : RoomTypeConverter {

    @TypeConverter
    fun fromCategory(category: DbCategory): String {
        return jsonConverter.toJson(category)
    }

    @TypeConverter
    fun toCategory(json: String): DbCategory {
        return (jsonConverter.fromJson(json) ?: DbCategory.UNKNOWN)
    }

    @TypeConverter
    fun fromImage(image: DbImage?): String {
        return jsonConverter.toJson(image)
    }

    @TypeConverter
    fun toImage(json: String): DbImage? {
        return jsonConverter.fromJson(json)
    }

    @TypeConverter
    fun fromImages(images: List<DbImage>): String {
        return jsonConverter.toJson(images)
    }

    @TypeConverter
    fun toImages(json: String): List<DbImage> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromReleaseDates(releaseDates: List<DbReleaseDate>): String {
        return jsonConverter.toJson(releaseDates)
    }

    @TypeConverter
    fun toReleaseDates(json: String): List<DbReleaseDate> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromReleaseDateCategory(category: DbReleaseDateCategory): String {
        return jsonConverter.toJson(category)
    }

    @TypeConverter
    fun toReleaseDateCategory(json: String): DbReleaseDateCategory {
        return (jsonConverter.fromJson(json) ?: DbReleaseDateCategory.UNKNOWN)
    }

    @TypeConverter
    fun fromAgeRatings(ageRatings: List<DbAgeRating>): String {
        return jsonConverter.toJson(ageRatings)
    }

    @TypeConverter
    fun toAgeRatings(json: String): List<DbAgeRating> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromAgeRatingCategory(category: DbAgeRatingCategory): String {
        return jsonConverter.toJson(category)
    }

    @TypeConverter
    fun toAgeRatingCategory(json: String): DbAgeRatingCategory {
        return (jsonConverter.fromJson(json) ?: DbAgeRatingCategory.UNKNOWN)
    }

    @TypeConverter
    fun fromAgeRatingType(type: DbAgeRatingType): String {
        return jsonConverter.toJson(type)
    }

    @TypeConverter
    fun toAgeRatingType(json: String): DbAgeRatingType {
        return (jsonConverter.fromJson(json) ?: DbAgeRatingType.UNKNOWN)
    }

    @TypeConverter
    fun fromVideos(videos: List<DbVideo>): String {
        return jsonConverter.toJson(videos)
    }

    @TypeConverter
    fun toVideos(json: String): List<DbVideo> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromGenres(genres: List<DbGenre>): String {
        return jsonConverter.toJson(genres)
    }

    @TypeConverter
    fun toGenres(json: String): List<DbGenre> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromPlatforms(platforms: List<DbPlatform>): String {
        return jsonConverter.toJson(platforms)
    }

    @TypeConverter
    fun toPlatforms(json: String): List<DbPlatform> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromPlayerPerspectives(playerPerspectives: List<DbPlayerPerspective>): String {
        return jsonConverter.toJson(playerPerspectives)
    }

    @TypeConverter
    fun toPlayerPerspectives(json: String): List<DbPlayerPerspective> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromThemes(themes: List<DbTheme>): String {
        return jsonConverter.toJson(themes)
    }

    @TypeConverter
    fun toThemes(json: String): List<DbTheme> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromModes(modes: List<DbMode>): String {
        return jsonConverter.toJson(modes)
    }

    @TypeConverter
    fun toModes(json: String): List<DbMode> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromKeywords(keywords: List<DbKeyword>): String {
        return jsonConverter.toJson(keywords)
    }

    @TypeConverter
    fun toKeywords(json: String): List<DbKeyword> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromInvolvedCompanies(involvedCompanies: List<DbInvolvedCompany>): String {
        return jsonConverter.toJson(involvedCompanies)
    }

    @TypeConverter
    fun toInvolvedCompanies(json: String): List<DbInvolvedCompany> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromWebsites(websites: List<DbWebsite>): String {
        return jsonConverter.toJson(websites)
    }

    @TypeConverter
    fun toWebsites(json: String): List<DbWebsite> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromWebsiteCategory(category: DbWebsiteCategory): String {
        return jsonConverter.toJson(category)
    }

    @TypeConverter
    fun toWebsiteCategory(json: String): DbWebsiteCategory {
        return (jsonConverter.fromJson(json) ?: DbWebsiteCategory.UNKNOWN)
    }

    @TypeConverter
    fun fromSimilarGames(similarGames: List<Int>): String {
        return jsonConverter.toJson(similarGames)
    }

    @TypeConverter
    fun toSimilarGames(json: String): List<Int> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }
}
