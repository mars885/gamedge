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
import com.paulrybitskyi.gamedge.database.commons.RoomTypeConverter
import com.paulrybitskyi.gamedge.database.games.entities.AgeRating
import com.paulrybitskyi.gamedge.database.games.entities.AgeRatingCategory
import com.paulrybitskyi.gamedge.database.games.entities.AgeRatingType
import com.paulrybitskyi.gamedge.database.games.entities.Category
import com.paulrybitskyi.gamedge.database.games.entities.Genre
import com.paulrybitskyi.gamedge.database.games.entities.Image
import com.paulrybitskyi.gamedge.database.games.entities.InvolvedCompany
import com.paulrybitskyi.gamedge.database.games.entities.Keyword
import com.paulrybitskyi.gamedge.database.games.entities.Mode
import com.paulrybitskyi.gamedge.database.games.entities.Platform
import com.paulrybitskyi.gamedge.database.games.entities.PlayerPerspective
import com.paulrybitskyi.gamedge.database.games.entities.ReleaseDate
import com.paulrybitskyi.gamedge.database.games.entities.ReleaseDateCategory
import com.paulrybitskyi.gamedge.database.games.entities.Theme
import com.paulrybitskyi.gamedge.database.games.entities.Video
import com.paulrybitskyi.gamedge.database.games.entities.Website
import com.paulrybitskyi.gamedge.database.games.entities.WebsiteCategory
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@ProvidedTypeConverter
@BindType(contributesTo = BindType.Collection.SET)
@Suppress("TooManyFunctions")
internal class GamesTypeConverter @Inject constructor(
    private val jsonConverter: JsonConverter
) : RoomTypeConverter {

    @TypeConverter
    fun fromCategory(category: Category): String {
        return jsonConverter.toJson(category)
    }

    @TypeConverter
    fun toCategory(json: String): Category {
        return (jsonConverter.fromJson(json) ?: Category.UNKNOWN)
    }

    @TypeConverter
    fun fromImage(image: Image?): String {
        return jsonConverter.toJson(image)
    }

    @TypeConverter
    fun toImage(json: String): Image? {
        return jsonConverter.fromJson(json)
    }

    @TypeConverter
    fun fromImages(images: List<Image>): String {
        return jsonConverter.toJson(images)
    }

    @TypeConverter
    fun toImages(json: String): List<Image> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromReleaseDates(releaseDates: List<ReleaseDate>): String {
        return jsonConverter.toJson(releaseDates)
    }

    @TypeConverter
    fun toReleaseDates(json: String): List<ReleaseDate> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromReleaseDateCategory(category: ReleaseDateCategory): String {
        return jsonConverter.toJson(category)
    }

    @TypeConverter
    fun toReleaseDateCategory(json: String): ReleaseDateCategory {
        return (jsonConverter.fromJson(json) ?: ReleaseDateCategory.UNKNOWN)
    }

    @TypeConverter
    fun fromAgeRatings(ageRatings: List<AgeRating>): String {
        return jsonConverter.toJson(ageRatings)
    }

    @TypeConverter
    fun toAgeRatings(json: String): List<AgeRating> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromAgeRatingCategory(category: AgeRatingCategory): String {
        return jsonConverter.toJson(category)
    }

    @TypeConverter
    fun toAgeRatingCategory(json: String): AgeRatingCategory {
        return (jsonConverter.fromJson(json) ?: AgeRatingCategory.UNKNOWN)
    }

    @TypeConverter
    fun fromAgeRatingType(type: AgeRatingType): String {
        return jsonConverter.toJson(type)
    }

    @TypeConverter
    fun toAgeRatingType(json: String): AgeRatingType {
        return (jsonConverter.fromJson(json) ?: AgeRatingType.UNKNOWN)
    }

    @TypeConverter
    fun fromVideos(videos: List<Video>): String {
        return jsonConverter.toJson(videos)
    }

    @TypeConverter
    fun toVideos(json: String): List<Video> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromGenres(genres: List<Genre>): String {
        return jsonConverter.toJson(genres)
    }

    @TypeConverter
    fun toGenres(json: String): List<Genre> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromPlatforms(platforms: List<Platform>): String {
        return jsonConverter.toJson(platforms)
    }

    @TypeConverter
    fun toPlatforms(json: String): List<Platform> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromPlayerPerspectives(playerPerspectives: List<PlayerPerspective>): String {
        return jsonConverter.toJson(playerPerspectives)
    }

    @TypeConverter
    fun toPlayerPerspectives(json: String): List<PlayerPerspective> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromThemes(themes: List<Theme>): String {
        return jsonConverter.toJson(themes)
    }

    @TypeConverter
    fun toThemes(json: String): List<Theme> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromModes(modes: List<Mode>): String {
        return jsonConverter.toJson(modes)
    }

    @TypeConverter
    fun toModes(json: String): List<Mode> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromKeywords(keywords: List<Keyword>): String {
        return jsonConverter.toJson(keywords)
    }

    @TypeConverter
    fun toKeywords(json: String): List<Keyword> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromInvolvedCompanies(involvedCompanies: List<InvolvedCompany>): String {
        return jsonConverter.toJson(involvedCompanies)
    }

    @TypeConverter
    fun toInvolvedCompanies(json: String): List<InvolvedCompany> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromWebsites(websites: List<Website>): String {
        return jsonConverter.toJson(websites)
    }

    @TypeConverter
    fun toWebsites(json: String): List<Website> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromWebsiteCategory(category: WebsiteCategory): String {
        return jsonConverter.toJson(category)
    }

    @TypeConverter
    fun toWebsiteCategory(json: String): WebsiteCategory {
        return (jsonConverter.fromJson(json) ?: WebsiteCategory.UNKNOWN)
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
