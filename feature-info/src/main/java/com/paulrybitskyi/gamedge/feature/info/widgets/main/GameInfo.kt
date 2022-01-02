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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreview
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.details.GameDetails
import com.paulrybitskyi.gamedge.feature.info.widgets.GameInfoUiState
import com.paulrybitskyi.gamedge.feature.info.widgets.GameScreenshots
import com.paulrybitskyi.gamedge.feature.info.widgets.GameSummary
import com.paulrybitskyi.gamedge.feature.info.widgets.model.GameInfoCompanyModel
import com.paulrybitskyi.gamedge.feature.info.widgets.model.GameInfoDetailsModel
import com.paulrybitskyi.gamedge.feature.info.widgets.model.GameInfoHeaderModel
import com.paulrybitskyi.gamedge.feature.info.widgets.model.GameInfoLinkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.model.GameInfoModel
import com.paulrybitskyi.gamedge.feature.info.widgets.model.GameInfoVideoModel
import com.paulrybitskyi.gamedge.feature.info.widgets.model.games.GameInfoRelatedGameModel
import com.paulrybitskyi.gamedge.feature.info.widgets.model.games.GameInfoRelatedGamesModel
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameCompanies
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameHeader
import com.paulrybitskyi.gamedge.feature.info.widgets.isInEmptyState
import com.paulrybitskyi.gamedge.feature.info.widgets.isInLoadingState
import com.paulrybitskyi.gamedge.feature.info.widgets.isInSuccessState
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameLinks
import com.paulrybitskyi.gamedge.feature.info.widgets.mapToCategoryModels
import com.paulrybitskyi.gamedge.feature.info.widgets.mapToCompanyModels
import com.paulrybitskyi.gamedge.feature.info.widgets.mapToInfoCompanyModel
import com.paulrybitskyi.gamedge.feature.info.widgets.mapToInfoRelatedGameModel
import com.paulrybitskyi.gamedge.feature.info.widgets.mapToInfoVideoModel
import com.paulrybitskyi.gamedge.feature.info.widgets.mapToVideoModels
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameVideos

@Composable
internal fun GameInfo(
    uiState: GameInfoUiState,
    onArtworkClicked: (Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    onVideoClicked: (GameInfoVideoModel) -> Unit,
    onScreenshotClicked: (Int) -> Unit,
    onLinkClicked: (GameInfoLinkModel) -> Unit,
    onCompanyClicked: (GameInfoCompanyModel) -> Unit,
    onRelatedGameClicked: (GameInfoRelatedGameModel) -> Unit,
) {
    ProvideWindowInsets {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.colorContentContainer))
        ) {
            when {
                uiState.isInLoadingState -> GameInfoLoadingState(Modifier.align(Alignment.Center))
                uiState.isInEmptyState -> GameInfoEmptyState(Modifier.align(Alignment.Center))
                uiState.isInSuccessState -> GameInfoSuccessState(
                    gameInfo = checkNotNull(uiState.game),
                    onArtworkClicked = onArtworkClicked,
                    onBackButtonClicked = onBackButtonClicked,
                    onCoverClicked = onCoverClicked,
                    onLikeButtonClicked = onLikeButtonClicked,
                    onVideoClicked = onVideoClicked,
                    onScreenshotClicked = onScreenshotClicked,
                    onLinkClicked = onLinkClicked,
                    onCompanyClicked = onCompanyClicked,
                    onRelatedGameClicked = onRelatedGameClicked,
                )
            }
        }
    }
}

@Composable
private fun GameInfoLoadingState(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = colorResource(R.color.colorProgressBar)
    )
}

@Composable
private fun GameInfoEmptyState(modifier: Modifier) {
    Info(
        icon = painterResource(R.drawable.gamepad_variant_outline),
        title = stringResource(R.string.game_info_info_view_title),
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.game_info_info_view_horizontal_margin)
        ),
        iconColor = colorResource(R.color.colorInfoView),
        titleTextColor = colorResource(R.color.colorInfoView),
    )
}

@Composable
private fun GameInfoSuccessState(
    gameInfo: GameInfoModel,
    onArtworkClicked: (Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    onVideoClicked: (GameInfoVideoModel) -> Unit,
    onScreenshotClicked: (Int) -> Unit,
    onLinkClicked: (GameInfoLinkModel) -> Unit,
    onCompanyClicked: (GameInfoCompanyModel) -> Unit,
    onRelatedGameClicked: (GameInfoRelatedGameModel) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GameInfoContent(
            gameInfo = gameInfo,
            onArtworkClicked = onArtworkClicked,
            onBackButtonClicked = onBackButtonClicked,
            onCoverClicked = onCoverClicked,
            onLikeButtonClicked = onLikeButtonClicked,
            onVideoClicked = onVideoClicked,
            onScreenshotClicked = onScreenshotClicked,
            onLinkClicked = onLinkClicked,
            onCompanyClicked = onCompanyClicked,
            onRelatedGameClicked = onRelatedGameClicked,
        )
    }
}

@Composable
private fun GameInfoContent(
    gameInfo: GameInfoModel,
    onArtworkClicked: (Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    onVideoClicked: (GameInfoVideoModel) -> Unit,
    onScreenshotClicked: (Int) -> Unit,
    onLinkClicked: (GameInfoLinkModel) -> Unit,
    onCompanyClicked: (GameInfoCompanyModel) -> Unit,
    onRelatedGameClicked: (GameInfoRelatedGameModel) -> Unit,
) {
    val decoratorSpacing = dimensionResource(R.dimen.game_info_decorator_spacing)

    LazyColumn(
        modifier = Modifier.navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(decoratorSpacing),
    ) {
        GameHeaderItem(
            model = gameInfo.headerModel,
            onArtworkClicked = onArtworkClicked,
            onBackButtonClicked = onBackButtonClicked,
            onCoverClicked = onCoverClicked,
            onLikeButtonClicked = onLikeButtonClicked,
        )

        if (gameInfo.hasVideoModels) {
            GameVideosItem(
                videos = gameInfo.videoModels,
                onVideoClicked = onVideoClicked,
            )
        }

        if (gameInfo.hasScreenshotUrls) {
            GameScreenshotsItem(
                model = gameInfo.screenshotUrls,
                onScreenshotClicked = onScreenshotClicked,
            )
        }

        if (gameInfo.hasSummary) {
            GameSummaryItem(model = checkNotNull(gameInfo.summary))
        }

        if (gameInfo.hasDetailsModel) {
            GameDetailsItem(model = checkNotNull(gameInfo.detailsModel))
        }

        if (gameInfo.hasLinkModels) {
            GameLinksItem(
                model = gameInfo.linkModels,
                onLinkClicked = onLinkClicked,
            )
        }

        if (gameInfo.hasCompanyModels) {
            GameCompaniesItem(
                model = gameInfo.companyModels,
                onCompanyClicked = onCompanyClicked,
            )
        }

        if (gameInfo.hasOtherCompanyGames) {
            GameRelatedGamesItem(
                model = checkNotNull(gameInfo.otherCompanyGames),
                onGameClicked = onRelatedGameClicked,
            )
        }

        if (gameInfo.hasSimilarGames) {
            GameRelatedGamesItem(
                model = checkNotNull(gameInfo.similarGames),
                onGameClicked = onRelatedGameClicked,
            )
        }
    }
}

private fun LazyListScope.GameHeaderItem(
    model: GameInfoHeaderModel,
    onArtworkClicked: (Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
) {
    item {
        GameHeader(
            headerInfo = model,
            onArtworkClicked = onArtworkClicked,
            onBackButtonClicked = onBackButtonClicked,
            onCoverClicked = onCoverClicked,
            onLikeButtonClicked = onLikeButtonClicked,
        )
    }
}

private fun LazyListScope.GameVideosItem(
    videos: List<GameInfoVideoModel>,
    onVideoClicked: (GameInfoVideoModel) -> Unit,
) {
    item {
        GameVideos(
            videos = videos.mapToVideoModels(),
            onVideClicked = { onVideoClicked(it.mapToInfoVideoModel()) },
        )
    }
}

private fun LazyListScope.GameScreenshotsItem(
    model: List<String>,
    onScreenshotClicked: (Int) -> Unit,
) {
    item {
        GameScreenshots(
            screenshotUrls = model,
            onScreenshotClicked = onScreenshotClicked,
        )
    }
}

private fun LazyListScope.GameSummaryItem(model: String) {
    item {
        GameSummary(summary = model)
    }
}

private fun LazyListScope.GameDetailsItem(model: GameInfoDetailsModel) {
    item {
        GameDetails(
            genres = model.genresText,
            platforms = model.platformsText,
            modes = model.modesText,
            playerPerspectives = model.playerPerspectivesText,
            themes = model.themesText,
        )
    }
}

private fun LazyListScope.GameLinksItem(
    model: List<GameInfoLinkModel>,
    onLinkClicked: (GameInfoLinkModel) -> Unit,
) {
    item {
        GameLinks(
            links = model,
            onLinkClicked = onLinkClicked,
        )
    }
}

private fun LazyListScope.GameCompaniesItem(
    model: List<GameInfoCompanyModel>,
    onCompanyClicked: (GameInfoCompanyModel) -> Unit,
) {
    item {
        GameCompanies(
            companies = model.mapToCompanyModels(),
            onCompanyClicked = { onCompanyClicked(it.mapToInfoCompanyModel()) },
        )
    }
}

private fun LazyListScope.GameRelatedGamesItem(
    model: GameInfoRelatedGamesModel,
    onGameClicked: (GameInfoRelatedGameModel) -> Unit,
) {
    item {
        GamesCategoryPreview(
            title = model.title,
            isProgressBarVisible = false,
            isMoreButtonVisible = false,
            topBarMargin = dimensionResource(R.dimen.game_info_related_games_top_bar_margin),
            games = model.items.mapToCategoryModels(),
            onCategoryGameClicked = {
                onGameClicked(it.mapToInfoRelatedGameModel())
            },
        )
    }
}
