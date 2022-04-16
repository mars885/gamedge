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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsPadding
import com.paulrybitskyi.commons.ktx.showShortToast
import com.paulrybitskyi.gamedge.commons.ui.HandleCommands
import com.paulrybitskyi.gamedge.commons.ui.HandleRoutes
import com.paulrybitskyi.gamedge.commons.ui.LocalUrlOpener
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.AnimatedContentContainer
import com.paulrybitskyi.gamedge.commons.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeProgressIndicator
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreview
import com.paulrybitskyi.gamedge.feature.info.GameInfoCommand
import com.paulrybitskyi.gamedge.feature.info.GameInfoViewModel
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.details.GameInfoDetails
import com.paulrybitskyi.gamedge.feature.info.widgets.screenshots.GameInfoScreenshots
import com.paulrybitskyi.gamedge.feature.info.widgets.GameInfoSummary
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameInfoCompanyModel
import com.paulrybitskyi.gamedge.feature.info.widgets.details.GameInfoDetailsModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeaderModel
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameInfoLinkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameInfoVideoModel
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGameModel
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGamesModel
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameInfoCompanies
import com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks.GameInfoArtworkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeader
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameInfoLinks
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.mapToCategoryModels
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.mapToInfoRelatedGameModel
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGamesType
import com.paulrybitskyi.gamedge.feature.info.widgets.screenshots.GameInfoScreenshotModel
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameInfoVideos

@Composable
fun GameInfo(onRoute: (Route) -> Unit) {
    GameInfo(
        viewModel = hiltViewModel(),
        onRoute = onRoute,
    )
}

@Composable
private fun GameInfo(
    viewModel: GameInfoViewModel,
    onRoute: (Route) -> Unit,
) {
    val urlOpener = LocalUrlOpener.current
    val context = LocalContext.current

    HandleCommands(viewModel = viewModel) { command ->
        when (command) {
            is GameInfoCommand.OpenUrl -> {
                if (!urlOpener.openUrl(command.url, context)) {
                    context.showShortToast(context.getString(R.string.url_opener_not_found))
                }
            }
        }
    }

    HandleRoutes(viewModel = viewModel, onRoute = onRoute)

    GameInfo(
        uiState = viewModel.uiState.collectAsState().value,
        onArtworkClicked = viewModel::onArtworkClicked,
        onBackButtonClicked = viewModel::onBackButtonClicked,
        onCoverClicked = viewModel::onCoverClicked,
        onLikeButtonClicked = viewModel::onLikeButtonClicked,
        onVideoClicked = viewModel::onVideoClicked,
        onScreenshotClicked = viewModel::onScreenshotClicked,
        onLinkClicked = viewModel::onLinkClicked,
        onCompanyClicked = viewModel::onCompanyClicked,
        onRelatedGameClicked = viewModel::onRelatedGameClicked,
    )
}

@Composable
private fun GameInfo(
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
    AnimatedContentContainer(uiState.finiteUiState) { finiteUiState ->
        when (finiteUiState) {
            FiniteUiState.EMPTY -> EmptyState(Modifier.align(Alignment.Center))
            FiniteUiState.LOADING -> LoadingState(Modifier.align(Alignment.Center))
            FiniteUiState.SUCCESS -> SuccessState(
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
private fun LoadingState(modifier: Modifier) {
    GamedgeProgressIndicator(modifier)
}

@Composable
private fun EmptyState(modifier: Modifier) {
    Info(
        icon = painterResource(R.drawable.gamepad_variant_outline),
        title = stringResource(R.string.game_info_info_view_title),
        modifier = modifier.padding(horizontal = GamedgeTheme.spaces.spacing_7_5),
    )
}

@Composable
private fun SuccessState(
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
        Content(
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
private fun Content(
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
    LazyColumn(
        modifier = Modifier.navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(GamedgeTheme.spaces.spacing_3_5),
    ) {
        HeaderItem(
            model = gameInfo.headerModel,
            onArtworkClicked = onArtworkClicked,
            onBackButtonClicked = onBackButtonClicked,
            onCoverClicked = onCoverClicked,
            onLikeButtonClicked = onLikeButtonClicked,
        )

        if (gameInfo.hasVideoModels) {
            VideosItem(
                videos = gameInfo.videoModels,
                onVideoClicked = onVideoClicked,
            )
        }

        if (gameInfo.hasScreenshotModels) {
            ScreenshotsItem(
                screenshots = gameInfo.screenshotModels,
                onScreenshotClicked = onScreenshotClicked,
            )
        }

        if (gameInfo.hasSummary) {
            SummaryItem(model = checkNotNull(gameInfo.summary))
        }

        if (gameInfo.hasDetailsModel) {
            DetailsItem(model = checkNotNull(gameInfo.detailsModel))
        }

        if (gameInfo.hasLinkModels) {
            LinksItem(
                model = gameInfo.linkModels,
                onLinkClicked = onLinkClicked,
            )
        }

        if (gameInfo.hasCompanyModels) {
            CompaniesItem(
                model = gameInfo.companyModels,
                onCompanyClicked = onCompanyClicked,
            )
        }

        if (gameInfo.hasOtherCompanyGames) {
            RelatedGamesItem(
                model = checkNotNull(gameInfo.otherCompanyGames),
                onGameClicked = onRelatedGameClicked,
            )
        }

        if (gameInfo.hasSimilarGames) {
            RelatedGamesItem(
                model = checkNotNull(gameInfo.similarGames),
                onGameClicked = onRelatedGameClicked,
            )
        }
    }
}

private fun LazyListScope.HeaderItem(
    model: GameInfoHeaderModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
) {
    item(key = GameInfoItem.HEADER.id) {
        GameInfoHeader(
            headerInfo = model,
            onArtworkClicked = onArtworkClicked,
            onBackButtonClicked = onBackButtonClicked,
            onCoverClicked = onCoverClicked,
            onLikeButtonClicked = onLikeButtonClicked,
        )
    }
}

private fun LazyListScope.VideosItem(
    videos: List<GameInfoVideoModel>,
    onVideoClicked: (GameInfoVideoModel) -> Unit,
) {
    item(key = GameInfoItem.VIDEOS.id) {
        GameInfoVideos(
            videos = videos,
            onVideClicked = onVideoClicked,
        )
    }
}

private fun LazyListScope.ScreenshotsItem(
    screenshots: List<GameInfoScreenshotModel>,
    onScreenshotClicked: (screenshotIndex: Int) -> Unit,
) {
    item(key = GameInfoItem.SCREENSHOTS.id) {
        GameInfoScreenshots(
            screenshots = screenshots,
            onScreenshotClicked = onScreenshotClicked,
        )
    }
}

private fun LazyListScope.SummaryItem(model: String) {
    item(key = GameInfoItem.SUMMARY.id) {
        GameInfoSummary(summary = model)
    }
}

private fun LazyListScope.DetailsItem(model: GameInfoDetailsModel) {
    item(key = GameInfoItem.DETAILS.id) {
        GameInfoDetails(details = model)
    }
}

private fun LazyListScope.LinksItem(
    model: List<GameInfoLinkModel>,
    onLinkClicked: (GameInfoLinkModel) -> Unit,
) {
    item(key = GameInfoItem.LINKS.id) {
        GameInfoLinks(
            links = model,
            onLinkClicked = onLinkClicked,
        )
    }
}

private fun LazyListScope.CompaniesItem(
    model: List<GameInfoCompanyModel>,
    onCompanyClicked: (GameInfoCompanyModel) -> Unit,
) {
    item(key = GameInfoItem.COMPANIES.id) {
        GameInfoCompanies(
            companies = model,
            onCompanyClicked = onCompanyClicked,
        )
    }
}

private fun LazyListScope.RelatedGamesItem(
    model: GameInfoRelatedGamesModel,
    onGameClicked: (GameInfoRelatedGameModel) -> Unit,
) {
    item(
        key = when (model.type) {
            GameInfoRelatedGamesType.OTHER_COMPANY_GAMES -> GameInfoItem.OTHER_COMPANY_GAMES.id
            GameInfoRelatedGamesType.SIMILAR_GAMES -> GameInfoItem.SIMILAR_GAMES.id
        }
    ) {
        val categoryGames = remember(model.items) {
            model.items.mapToCategoryModels()
        }

        GamesCategoryPreview(
            title = model.title,
            isProgressBarVisible = false,
            games = categoryGames,
            onCategoryGameClicked = {
                onGameClicked(it.mapToInfoRelatedGameModel())
            },
            topBarMargin = GamedgeTheme.spaces.spacing_2_5,
            isMoreButtonVisible = false,
        )
    }
}

private enum class GameInfoItem(val id: Int) {
    HEADER(id = 1),
    VIDEOS(id = 2),
    SCREENSHOTS(id = 3),
    SUMMARY(id = 4),
    DETAILS(id = 5),
    LINKS(id = 6),
    COMPANIES(id = 7),
    OTHER_COMPANY_GAMES(id = 8),
    SIMILAR_GAMES(id = 9),
}

// TODO (02.01.2022): Currently, preview height is limited to 2k DP.
// Try to increase this value in the future to showcase all the UI.
@Preview(heightDp = 2000)
@Composable
internal fun GameInfoSuccessStateWithMaxUiElementsPreview() {
    GamedgeTheme {
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
        screenshotModels = emptyList(),
        summary = null,
        detailsModel = null,
        linkModels = emptyList(),
        companyModels = emptyList(),
        otherCompanyGames = null,
        similarGames = null,
    )

    GamedgeTheme {
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
}

@Preview
@Composable
internal fun GameInfoEmptyStatePreview() {
    GamedgeTheme {
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
}

@Preview
@Composable
internal fun GameInfoLoadingStatePreview() {
    GamedgeTheme {
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
}

private fun buildFakeGameModel(): GameInfoModel {
    return GameInfoModel(
        id = 1,
        headerModel = GameInfoHeaderModel(
            artworks = listOf(GameInfoArtworkModel.DefaultImage),
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
                id = "1",
                thumbnailUrl = "",
                videoUrl = "",
                title = "Announcement Trailer",
            ),
            GameInfoVideoModel(
                id = "2",
                thumbnailUrl = "",
                videoUrl = "",
                title = "Gameplay Trailer",
            ),
        ),
        screenshotModels = listOf(
            GameInfoScreenshotModel(
                id = "1",
                url = "",
            ),
            GameInfoScreenshotModel(
                id = "2",
                url = "",
            ),
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
                id = 1,
                logoUrl = null,
                logoWidth = 1400,
                logoHeight = 400,
                websiteUrl = "",
                name = "FromSoftware",
                roles = "Main Developer",
            ),
            GameInfoCompanyModel(
                id = 2,
                logoUrl = null,
                logoWidth = 500,
                logoHeight = 400,
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
