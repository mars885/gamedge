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

package com.paulrybitskyi.gamedge.feature.info.widgets.main

import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.feature.info.widgets.details.GameInfoDetailsUiModelFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.factories.GameInfoOtherCompanyGamesUiModelFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.factories.GameInfoSimilarGamesUiModelFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameInfoCompanyUiModelFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.createCompanyUiModels
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeaderUiModelFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameInfoLinkUiModelFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGamesUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.screenshots.GameInfoScreenshotUiModelFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.screenshots.createScreenshotUiModels
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameInfoVideoUiModelFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.createVideoUiModels
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GameInfoUiModelFactory {

    fun createInfoUiModel(
        game: Game,
        isLiked: Boolean,
        companyGames: List<Game>,
        similarGames: List<Game>
    ): GameInfoUiModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
@Suppress("LongParameterList")
internal class GameInfoUiModelFactoryImpl @Inject constructor(
    private val headerModelFactory: GameInfoHeaderUiModelFactory,
    private val videoModelFactory: GameInfoVideoUiModelFactory,
    private val screenshotModelFactory: GameInfoScreenshotUiModelFactory,
    private val detailsModelFactory: GameInfoDetailsUiModelFactory,
    private val linkModelFactory: GameInfoLinkUiModelFactory,
    private val companyModelFactory: GameInfoCompanyUiModelFactory,
    private val otherCompanyGamesModelFactory: GameInfoOtherCompanyGamesUiModelFactory,
    private val similarGamesModelFactory: GameInfoSimilarGamesUiModelFactory,
) : GameInfoUiModelFactory {

    override fun createInfoUiModel(
        game: Game,
        isLiked: Boolean,
        companyGames: List<Game>,
        similarGames: List<Game>,
    ): GameInfoUiModel {
        return GameInfoUiModel(
            id = game.id,
            headerModel = headerModelFactory.createHeaderUiModel(game, isLiked),
            videoModels = videoModelFactory.createVideoUiModels(game.videos),
            screenshotModels = screenshotModelFactory.createScreenshotUiModels(game.screenshots),
            summary = game.summary,
            detailsModel = detailsModelFactory.createDetailsUiModel(game),
            linkModels = linkModelFactory.createLinkUiModels(game.websites),
            companyModels = companyModelFactory.createCompanyUiModels(game.involvedCompanies),
            otherCompanyGames = game.createOtherCompanyGamesUiModel(companyGames),
            similarGames = similarGamesModelFactory.createSimilarGamesUiModel(similarGames),
        )
    }

    private fun Game.createOtherCompanyGamesUiModel(otherCompanyGames: List<Game>): GameInfoRelatedGamesUiModel? {
        return otherCompanyGamesModelFactory.createOtherCompanyGamesUiModel(otherCompanyGames, this)
    }
}
