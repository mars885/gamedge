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
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.navigationBarsPadding
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreview
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.details.GameDetails
import com.paulrybitskyi.gamedge.feature.info.widgets.GameScreenshots
import com.paulrybitskyi.gamedge.feature.info.widgets.GameSummary
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameInfoCompanyModel
import com.paulrybitskyi.gamedge.feature.info.widgets.details.GameInfoDetailsModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeaderModel
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameInfoLinkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameInfoVideoModel
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGameModel
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGamesModel
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameCompanies
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameArtworkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameHeader
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameLinks
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.mapToCategoryModels
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.mapToInfoRelatedGameModel
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGamesType
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameVideos

@Composable
internal fun GameInfo(
    uiState: GameInfoUiState,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    onVideoClicked: (GameInfoVideoModel) -> Unit,
    onScreenshotClicked: (screenshotIndex: Int) -> Unit,
    onLinkClicked: (GameInfoLinkModel) -> Unit,
    onCompanyClicked: (GameInfoCompanyModel) -> Unit,
    onRelatedGameClicked: (GameInfoRelatedGameModel) -> Unit,
) {
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
            horizontal = dimensionResource(R.dimen.game_info_info_view_horizontal_padding)
        ),
        iconColor = colorResource(R.color.colorInfoView),
        titleTextColor = colorResource(R.color.colorInfoView),
    )
}

@Composable
private fun GameInfoSuccessState(
    gameInfo: GameInfoModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    onVideoClicked: (GameInfoVideoModel) -> Unit,
    onScreenshotClicked: (screenshotIndex: Int) -> Unit,
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
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    onVideoClicked: (GameInfoVideoModel) -> Unit,
    onScreenshotClicked: (screenshotIndex: Int) -> Unit,
    onLinkClicked: (GameInfoLinkModel) -> Unit,
    onCompanyClicked: (GameInfoCompanyModel) -> Unit,
    onRelatedGameClicked: (GameInfoRelatedGameModel) -> Unit,
) {
    val arrangementSpacing = dimensionResource(R.dimen.game_info_vertical_arrangement_spacing)

    LazyColumn(
        modifier = Modifier.navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(arrangementSpacing),
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
    onArtworkClicked: (artworkIndex: Int) -> Unit,
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
            videos = videos,
            onVideClicked = onVideoClicked,
        )
    }
}

private fun LazyListScope.GameScreenshotsItem(
    model: List<String>,
    onScreenshotClicked: (screenshotIndex: Int) -> Unit,
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
        GameDetails(details = model)
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
            companies = model,
            onCompanyClicked = onCompanyClicked,
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
            games = model.items.mapToCategoryModels(),
            onCategoryGameClicked = {
                onGameClicked(it.mapToInfoRelatedGameModel())
            },
            topBarMargin = dimensionResource(R.dimen.game_info_related_games_top_bar_margin),
            isMoreButtonVisible = false,
        )
    }
}

// TODO (02.01.2022): Currently, preview height is limited to 2k DP.
// Try to increase this value in the future to showcase all the UI.
@Preview(heightDp = 2000)
@Composable
internal fun GameInfoSuccessStateWithMaxUiElementsPreview() {
    GameInfo(
        uiState = GameInfoUiState(
            isLoading = false,
            game = buildFakeGameModel(),
        ),
        onArtworkClicked = {},
        onBackButtonClicked = {},
        onCoverClicked = {},
        onLikeButtonClicked = {},
        onVideoClicked = {},
        onScreenshotClicked = {},
        onLinkClicked = {},
        onCompanyClicked = {},
        onRelatedGameClicked = {},
    )
}

@Preview
@Composable
internal fun GameInfoSuccessStateWithMinUiElementsPreview() {
    val game = buildFakeGameModel()
    val strippedGame = game.copy(
        headerModel = game.headerModel.copy(
            developerName = null,
            likeCount = "0",
            gameCategory = "N/A",
        ),
        videoModels = emptyList(),
        screenshotUrls = emptyList(),
        summary = null,
        detailsModel = null,
        linkModels = emptyList(),
        companyModels = emptyList(),
        otherCompanyGames = null,
        similarGames = null,
    )

    GameInfo(
        uiState = GameInfoUiState(
            isLoading = false,
            game = strippedGame,
        ),
        onArtworkClicked = {},
        onBackButtonClicked = {},
        onCoverClicked = {},
        onLikeButtonClicked = {},
        onVideoClicked = {},
        onScreenshotClicked = {},
        onLinkClicked = {},
        onCompanyClicked = {},
        onRelatedGameClicked = {},
    )
}

@Preview
@Composable
internal fun GameInfoEmptyStatePreview() {
    GameInfo(
        uiState = GameInfoUiState(
            isLoading = false,
            game = null,
        ),
        onArtworkClicked = {},
        onBackButtonClicked = {},
        onCoverClicked = {},
        onLikeButtonClicked = {},
        onVideoClicked = {},
        onScreenshotClicked = {},
        onLinkClicked = {},
        onCompanyClicked = {},
        onRelatedGameClicked = {},
    )
}

@Preview
@Composable
internal fun GameInfoLoadingStatePreview() {
    GameInfo(
        uiState = GameInfoUiState(
            isLoading = true,
            game = null,
        ),
        onArtworkClicked = {},
        onBackButtonClicked = {},
        onCoverClicked = {},
        onLikeButtonClicked = {},
        onVideoClicked = {},
        onScreenshotClicked = {},
        onLinkClicked = {},
        onCompanyClicked = {},
        onRelatedGameClicked = {},
    )
}

private fun buildFakeGameModel(): GameInfoModel {
    return GameInfoModel(
        id = 1,
        headerModel = GameInfoHeaderModel(
            artworks = listOf(GameArtworkModel.DefaultImage),
            isLiked = true,
            coverImageUrl = null,
            title = "Elden Ring",
            releaseDate = "Feb 25, 2022 (in a month)",
            developerName = "FromSoftware",
            rating = "N/A",
            likeCount = "92",
            ageRating = "N/A",
            gameCategory = "Main",
        ),
        videoModels = listOf(
            GameInfoVideoModel(
                thumbnailUrl = "",
                videoUrl = "",
                title = "Announcement Trailer",
            ),
            GameInfoVideoModel(
                thumbnailUrl = "",
                videoUrl = "",
                title = "Gameplay Trailer",
            ),
        ),
        screenshotUrls = listOf(
            "screenshot_url_1",
            "screenshot_url_2",
        ),
        summary = "Elden Ring is an action-RPG open world game with RPG " +
            "elements such as stats, weapons and spells.",
        detailsModel = GameInfoDetailsModel(
            genresText = "Role-playing (RPG)",
            platformsText = "PC (Microsoft Windows) • PlayStation 4 • " +
                "Xbox One • PlayStation 5 • Xbox Series X|S",
            modesText = "Single player • Multiplayer • Co-operative",
            playerPerspectivesText = "Third person",
            themesText = "Action",
        ),
        linkModels = listOf(
            GameInfoLinkModel(
                id = 1,
                text = "Steam",
                iconId = R.drawable.steam,
            ),
            GameInfoLinkModel(
                id = 2,
                text = "Official",
                iconId = R.drawable.web,
            ),
            GameInfoLinkModel(
                id = 3,
                text = "Twitter",
                iconId = R.drawable.twitter,
            ),
            GameInfoLinkModel(
                id = 4,
                text = "Subreddit",
                iconId = R.drawable.reddit,
            ),
            GameInfoLinkModel(
                id = 5,
                text = "YouTube",
                iconId = R.drawable.youtube,
            ),
            GameInfoLinkModel(
                id = 6,
                text = "Twitch",
                iconId = R.drawable.twitch,
            ),
        ),
        companyModels = listOf(
            GameInfoCompanyModel(
                logoContainerSize = 750 to 400,
                logoImageSize = 0 to 0,
                logoUrl = null,
                websiteUrl = "",
                name = "FromSoftware",
                roles = "Main Developer",
            ),
            GameInfoCompanyModel(
                logoContainerSize = 500 to 400,
                logoImageSize = 0 to 0,
                logoUrl = null,
                websiteUrl = "",
                name = "Bandai Namco Entertainment",
                roles = "Publisher",
            ),
        ),
        otherCompanyGames = GameInfoRelatedGamesModel(
            type = GameInfoRelatedGamesType.OTHER_COMPANY_GAMES,
            title = "More games by FromSoftware",
            items = listOf(
                GameInfoRelatedGameModel(
                    id = 1,
                    title = "Dark Souls",
                    coverUrl = null,
                ),
                GameInfoRelatedGameModel(
                    id = 2,
                    title = "Dark Souls II",
                    coverUrl = null,
                ),
                GameInfoRelatedGameModel(
                    id = 3,
                    title = "Lost Kingdoms",
                    coverUrl = null,
                ),
                GameInfoRelatedGameModel(
                    id = 4,
                    title = "Lost Kingdoms II",
                    coverUrl = null,
                ),
            ),
        ),
        similarGames = GameInfoRelatedGamesModel(
            type = GameInfoRelatedGamesType.SIMILAR_GAMES,
            title = "Similar Games",
            items = listOf(
                GameInfoRelatedGameModel(
                    id = 1,
                    title = "Nights of Azure 2: Bride of the New Moon",
                    coverUrl = null,
                ),
                GameInfoRelatedGameModel(
                    id = 2,
                    title = "God Eater 3",
                    coverUrl = null,
                ),
                GameInfoRelatedGameModel(
                    id = 3,
                    title = "Shadows: Awakening",
                    coverUrl = null,
                ),
                GameInfoRelatedGameModel(
                    id = 3,
                    title = "SoulWorker",
                    coverUrl = null,
                ),
            ),
        ),
    )
}
