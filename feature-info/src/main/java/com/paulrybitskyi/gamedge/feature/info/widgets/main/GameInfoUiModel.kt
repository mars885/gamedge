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

import androidx.compose.runtime.Immutable
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameInfoCompanyUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.details.GameInfoDetailsUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeaderUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameInfoLinkUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGamesUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.screenshots.GameInfoScreenshotUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameInfoVideoUiModel

@Immutable
internal data class GameInfoUiModel(
    val id: Int,
    val headerModel: GameInfoHeaderUiModel,
    val videoModels: List<GameInfoVideoUiModel>,
    val screenshotModels: List<GameInfoScreenshotUiModel>,
    val summary: String?,
    val detailsModel: GameInfoDetailsUiModel?,
    val linkModels: List<GameInfoLinkUiModel>,
    val companyModels: List<GameInfoCompanyUiModel>,
    val otherCompanyGames: GameInfoRelatedGamesUiModel?,
    val similarGames: GameInfoRelatedGamesUiModel?,
) {

    val hasVideos: Boolean
        get() = videoModels.isNotEmpty()

    val hasScreenshots: Boolean
        get() = screenshotModels.isNotEmpty()

    val hasSummary: Boolean
        get() = ((summary != null) && summary.isNotBlank())

    val hasDetails: Boolean
        get() = (detailsModel != null)

    val hasLinks: Boolean
        get() = linkModels.isNotEmpty()

    val hasCompanies: Boolean
        get() = companyModels.isNotEmpty()

    val hasOtherCompanyGames: Boolean
        get() = ((otherCompanyGames != null) && otherCompanyGames.hasItems)

    val hasSimilarGames: Boolean
        get() = ((similarGames != null) && similarGames.hasItems)
}
