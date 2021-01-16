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

package com.paulrybitskyi.gamedge.ui.info.mapping

import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.gamedge.commons.ui.widgets.header.GameHeaderImageModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.GameInfoHeaderModel
import com.paulrybitskyi.gamedge.core.GameLikeCountCalculator
import com.paulrybitskyi.gamedge.core.IgdbImageSize
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.core.formatters.GameAgeRatingFormatter
import com.paulrybitskyi.gamedge.core.formatters.GameCategoryFormatter
import com.paulrybitskyi.gamedge.core.formatters.GameRatingFormatter
import com.paulrybitskyi.gamedge.core.formatters.GameReleaseDateFormatter
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.entities.extensions.developerCompany
import javax.inject.Inject


internal interface GameInfoHeaderModelFactory {

    fun createHeaderModel(game: Game, isLiked: Boolean): GameInfoHeaderModel

}


@BindType(installIn = BindType.Component.ACTIVITY_RETAINED)
internal class GameInfoHeaderModelFactoryImpl @Inject constructor(
    private val igdbImageUrlBuilder: IgdbImageUrlBuilder,
    private val releaseDateFormatter: GameReleaseDateFormatter,
    private val ratingFormatter: GameRatingFormatter,
    private val likeCountCalculator: GameLikeCountCalculator,
    private val ageRatingFormatter: GameAgeRatingFormatter,
    private val categoryFormatter: GameCategoryFormatter
) : GameInfoHeaderModelFactory {


    override fun createHeaderModel(game: Game, isLiked: Boolean): GameInfoHeaderModel {
        return GameInfoHeaderModel(
            backgroundImageModels = game.buildBackgroundImageModels(),
            isLiked = isLiked,
            coverImageUrl = game.buildCoverImageUrl(),
            title = game.name,
            releaseDate = game.formatReleaseDate(),
            developerName = game.developerCompany?.name,
            rating = game.formatRating(),
            likeCount = game.calculateLikeCount(),
            ageRating = game.formatAgeRating(),
            gameCategory = game.formatCategory()
        )
    }


    private fun Game.buildBackgroundImageModels(): List<GameHeaderImageModel> {
        if(artworks.isEmpty()) return listOf(GameHeaderImageModel.DefaultImage)

        return igdbImageUrlBuilder
            .buildUrls(artworks, IgdbImageUrlBuilder.Config(IgdbImageSize.BIG_SCREENSHOT))
            .map(GameHeaderImageModel::UrlImage)
    }


    private fun Game.buildCoverImageUrl(): String? {
        return cover?.let { cover ->
            igdbImageUrlBuilder.buildUrl(cover, IgdbImageUrlBuilder.Config(IgdbImageSize.BIG_COVER))
        }
    }


    private fun Game.formatReleaseDate(): String {
        return releaseDateFormatter.formatReleaseDate(this)
    }


    private fun Game.formatRating(): String {
        return ratingFormatter.formatRating(totalRating)
    }


    private fun Game.calculateLikeCount(): String {
        return likeCountCalculator.calculateLikeCount(this).toString()
    }


    private fun Game.formatCategory(): String {
        return categoryFormatter.formatCategory(category)
    }


    private fun Game.formatAgeRating(): String {
        return ageRatingFormatter.formatAgeRating(this)
    }


}