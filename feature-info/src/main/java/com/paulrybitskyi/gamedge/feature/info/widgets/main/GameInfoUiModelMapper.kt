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
import com.paulrybitskyi.gamedge.feature.info.widgets.details.GameInfoDetailsUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.mappers.GameInfoOtherCompanyGamesUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.mappers.GameInfoSimilarGamesUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameInfoCompanyUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.mapToUiModels
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeaderUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameInfoLinkUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGamesUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.screenshots.GameInfoScreenshotUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.widgets.screenshots.mapToUiModels
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameInfoVideoUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.mapToUiModels
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GameInfoUiModelMapper {

    fun mapToUiModel(
        game: Game,
        isLiked: Boolean,
        companyGames: List<Game>,
        similarGames: List<Game>,
    ): GameInfoUiModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
@Suppress("LongParameterList")
internal class GameInfoUiModelMapperImpl @Inject constructor(
    private val headerModelMapper: GameInfoHeaderUiModelMapper,
    private val videoModelMapper: GameInfoVideoUiModelMapper,
    private val screenshotModelMapper: GameInfoScreenshotUiModelMapper,
    private val detailsModelMapper: GameInfoDetailsUiModelMapper,
    private val linkModelMapper: GameInfoLinkUiModelMapper,
    private val companyModelMapper: GameInfoCompanyUiModelMapper,
    private val otherCompanyGamesModelMapper: GameInfoOtherCompanyGamesUiModelMapper,
    private val similarGamesModelMapper: GameInfoSimilarGamesUiModelMapper,
) : GameInfoUiModelMapper {

    override fun mapToUiModel(
        game: Game,
        isLiked: Boolean,
        companyGames: List<Game>,
        similarGames: List<Game>,
    ): GameInfoUiModel {
        return GameInfoUiModel(
            id = game.id,
            headerModel = headerModelMapper.mapToUiModel(game, isLiked),
            videoModels = videoModelMapper.mapToUiModels(game.videos),
            screenshotModels = screenshotModelMapper.mapToUiModels(game.screenshots),
            summary = game.summary,
            detailsModel = detailsModelMapper.mapToUiModel(game),
            linkModels = linkModelMapper.mapToUiModels(game.websites),
            companyModels = companyModelMapper.mapToUiModels(game.involvedCompanies),
            otherCompanyGames = game.createOtherCompanyGamesUiModel(companyGames),
            similarGames = similarGamesModelMapper.mapToUiModel(similarGames),
        )
    }

    private fun Game.createOtherCompanyGamesUiModel(otherCompanyGames: List<Game>): GameInfoRelatedGamesUiModel? {
        return otherCompanyGamesModelMapper.mapToUiModel(otherCompanyGames, this)
    }
}
