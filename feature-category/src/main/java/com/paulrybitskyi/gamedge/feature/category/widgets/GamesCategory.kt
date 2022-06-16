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

package com.paulrybitskyi.gamedge.feature.category.widgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.paulrybitskyi.gamedge.commons.ui.CommandsHandler
import com.paulrybitskyi.gamedge.commons.ui.RoutesHandler
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.AnimatedContentContainer
import com.paulrybitskyi.gamedge.commons.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeProgressIndicator
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.RefreshableContent
import com.paulrybitskyi.gamedge.commons.ui.widgets.toolbars.Toolbar
import com.paulrybitskyi.gamedge.feature.category.GamesCategoryViewModel
import com.paulrybitskyi.gamedge.feature.category.R

@Composable
fun GamesCategory(onRoute: (Route) -> Unit) {
    GamesCategory(
        viewModel = hiltViewModel(),
        onRoute = onRoute,
    )
}

@Composable
private fun GamesCategory(
    viewModel: GamesCategoryViewModel,
    onRoute: (Route) -> Unit,
) {
    CommandsHandler(viewModel = viewModel)
    RoutesHandler(viewModel = viewModel, onRoute = onRoute)
    GamesCategory(
        uiState = viewModel.uiState.collectAsState().value,
        onBackButtonClicked = viewModel::onToolbarLeftButtonClicked,
        onGameClicked = viewModel::onGameClicked,
        onBottomReached = viewModel::onBottomReached,
    )
}

@Composable
private fun GamesCategory(
    uiState: GamesCategoryUiState,
    onBackButtonClicked: () -> Unit,
    onGameClicked: (GameCategoryUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    Scaffold(
        topBar = {
            Toolbar(
                title = uiState.title,
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars,
                ),
                leftButtonIcon = painterResource(R.drawable.arrow_left),
                onLeftButtonClick = onBackButtonClicked,
            )
        },
    ) {
        AnimatedContentContainer(uiState.finiteUiState) { finiteUiState ->
            when (finiteUiState) {
                FiniteUiState.Empty -> {
                    EmptyState(modifier = Modifier.align(Alignment.Center))
                }
                FiniteUiState.Loading -> {
                    LoadingState(modifier = Modifier.align(Alignment.Center))
                }
                FiniteUiState.Success -> {
                    SuccessState(
                        uiState = uiState,
                        modifier = Modifier
                            .matchParentSize()
                            .navigationBarsPadding(),
                        onGameClicked = onGameClicked,
                        onBottomReached = onBottomReached,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    GamedgeProgressIndicator(modifier = modifier)
}

@Composable
private fun EmptyState(modifier: Modifier) {
    Info(
        icon = painterResource(R.drawable.gamepad_variant_outline),
        title = stringResource(R.string.games_category_info_view_title),
        modifier = modifier.padding(horizontal = GamedgeTheme.spaces.spacing_7_5),
    )
}

@Composable
private fun SuccessState(
    uiState: GamesCategoryUiState,
    modifier: Modifier,
    onGameClicked: (GameCategoryUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    RefreshableContent(
        isRefreshing = uiState.isRefreshing,
        modifier = modifier,
        isSwipeEnabled = false,
    ) {
        VerticalGrid(
            games = uiState.games,
            onGameClicked = onGameClicked,
            onBottomReached = onBottomReached,
        )
    }
}

@Composable
private fun VerticalGrid(
    games: List<GameCategoryUiModel>,
    onGameClicked: (GameCategoryUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    val gridConfig = rememberGamesGridConfig()
    val gridItemSpacingInPx = gridConfig.itemSpacingInPx
    val gridSpanCount = gridConfig.spanCount
    val lastIndex = games.lastIndex

    LazyVerticalGrid(cells = GridCells.Fixed(gridConfig.spanCount)) {
        itemsIndexed(items = games) { index, game ->
            if (index == lastIndex) {
                LaunchedEffect(lastIndex) {
                    onBottomReached()
                }
            }

            val column = (index % gridConfig.spanCount)
            val paddingValues = with(LocalDensity.current) {
                PaddingValues(
                    top = (if (index < gridSpanCount) gridItemSpacingInPx else 0f).toDp(),
                    bottom = gridItemSpacingInPx.toDp(),
                    start = (gridItemSpacingInPx - (column * gridItemSpacingInPx / gridSpanCount)).toDp(),
                    end = ((column + 1) * gridItemSpacingInPx / gridSpanCount).toDp(),
                )
            }

            GameCover(
                title = game.title,
                imageUrl = game.coverUrl,
                modifier = Modifier
                    .size(
                        width = gridConfig.itemWidthInDp,
                        height = gridConfig.itemHeightInDp
                    )
                    .padding(paddingValues),
                hasRoundedShape = false,
                onCoverClicked = { onGameClicked(game) },
            )
        }
    }
}

@Preview
@Composable
private fun GamesCategorySuccessStatePreview() {
    val games = buildList {
        repeat(15) { index ->
            add(
                GameCategoryUiModel(
                    id = index + 1,
                    title = "Popular Game",
                    coverUrl = null,
                )
            )
        }
    }

    GamedgeTheme {
        GamesCategory(
            uiState = GamesCategoryUiState(
                isLoading = false,
                title = "Popular",
                games = games,
            ),
            onBackButtonClicked = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Composable
private fun GamesCategoryEmptyStatePreview() {
    GamedgeTheme {
        GamesCategory(
            uiState = GamesCategoryUiState(
                isLoading = false,
                title = "Popular",
                games = emptyList(),
            ),
            onBackButtonClicked = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Composable
private fun GamesCategoryLoadingStatePreview() {
    GamedgeTheme {
        GamesCategory(
            uiState = GamesCategoryUiState(
                isLoading = true,
                title = "Popular",
                games = emptyList(),
            ),
            onBackButtonClicked = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}
