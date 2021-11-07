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

package com.paulrybitskyi.gamedge.feature.info.mapping

import com.paulrybitskyi.gamedge.core.factories.IgdbImageSize
import com.paulrybitskyi.gamedge.core.factories.IgdbImageUrlFactory
import com.paulrybitskyi.gamedge.core.factories.createUrls
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.games.GameInfoRelatedGamesModel
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GameInfoModelFactory {

    fun createInfoModel(
        game: Game,
        isLiked: Boolean,
        companyGames: List<Game>,
        similarGames: List<Game>
    ): GameInfoModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
@Suppress("LongParameterList")
internal class GameInfoModelFactoryImpl @Inject constructor(
    private val headerModelFactory: GameInfoHeaderModelFactory,
    private val videoModelFactory: GameInfoVideoModelFactory,
    private val detailsModelFactory: GameInfoDetailsModelFactory,
    private val linkModelFactory: GameInfoLinkModelFactory,
    private val companyModelFactory: GameInfoCompanyModelFactory,
    private val otherCompanyGamesModelFactory: GameInfoOtherCompanyGamesModelFactory,
    private val similarGamesModelFactory: GameInfoSimilarGamesModelFactory,
    private val igdbImageUrlFactory: IgdbImageUrlFactory
) : GameInfoModelFactory {

    override fun createInfoModel(
        game: Game,
        isLiked: Boolean,
        companyGames: List<Game>,
        similarGames: List<Game>
    ): GameInfoModel {
        return GameInfoModel(
            id = game.id,
            headerModel = headerModelFactory.createHeaderModel(game, isLiked),
            videoModels = videoModelFactory.createVideoModels(game.videos),
            screenshotUrls = game.createScreenshotUrls(),
            summary = game.summary,
            detailsModel = detailsModelFactory.createDetailsModel(game),
            linkModels = linkModelFactory.createLinkModels(game.websites),
            companyModels = companyModelFactory.createCompanyModels(game.involvedCompanies),
            otherCompanyGames = game.createOtherCompanyGamesModel(companyGames),
            similarGames = similarGamesModelFactory.createSimilarGamesModel(similarGames)
        )
    }

    private fun Game.createScreenshotUrls(): List<String> {
        return igdbImageUrlFactory.createUrls(
            screenshots,
            IgdbImageUrlFactory.Config(IgdbImageSize.MEDIUM_SCREENSHOT)
        )
    }

    private fun Game.createOtherCompanyGamesModel(otherCompanyGames: List<Game>): GameInfoRelatedGamesModel? {
        return otherCompanyGamesModelFactory.createOtherCompanyGamesModel(otherCompanyGames, this)
    }
}
