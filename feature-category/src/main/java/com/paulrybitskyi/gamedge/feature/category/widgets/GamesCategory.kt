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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.paulrybitskyi.commons.device.info.screenMetrics
import com.paulrybitskyi.commons.ktx.getFloat
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.RefreshableContent
import com.paulrybitskyi.gamedge.commons.ui.widgets.Toolbar
import com.paulrybitskyi.gamedge.feature.category.R

@Composable
internal fun GamesCategory(
    uiState: GamesCategoryUiState,
    onBackButtonClicked: () -> Unit,
    onGameClicked: (GameCategoryModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Toolbar(
            title = uiState.title,
            modifier = Modifier.statusBarsPadding(),
            leftButtonIcon = painterResource(R.drawable.arrow_left),
            onLeftButtonClick = onBackButtonClicked,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.colorContentContainer))
        ) {
            when {
                uiState.isInLoadingState -> GamesLoadingState(Modifier.align(Alignment.Center))
                uiState.isInEmptyState -> GamesEmptyState(Modifier.align(Alignment.Center))
                uiState.isInSuccessState -> GamesSuccessState(
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

@Composable
private fun GamesLoadingState(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = colorResource(R.color.colorProgressBar)
    )
}

@Composable
private fun GamesEmptyState(modifier: Modifier) {
    Info(
        icon = painterResource(R.drawable.gamepad_variant_outline),
        title = stringResource(R.string.games_category_info_view_title),
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.games_category_info_view_horizontal_padding),
        ),
        iconColor = colorResource(R.color.colorInfoView),
        titleTextColor = colorResource(R.color.colorInfoView)
    )
}

@Composable
private fun GamesSuccessState(
    uiState: GamesCategoryUiState,
    modifier: Modifier,
    onGameClicked: (GameCategoryModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    RefreshableContent(
        isRefreshing = uiState.isInRefreshingState,
        modifier = modifier,
        isSwipeEnabled = false,
    ) {
        GamesVerticalGrid(
            games = uiState.games,
            onGameClicked = onGameClicked,
            onBottomReached = onBottomReached,
        )
    }
}

@Composable
private fun GamesVerticalGrid(
    games: List<GameCategoryModel>,
    onGameClicked: (GameCategoryModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val gridSpanCount = integerResource(R.integer.games_category_grid_span_count)
    val gridItemSpacingInDp = dimensionResource(R.dimen.games_category_grid_item_spacing)
    val gridItemSpacingInPx = remember {
        with(density) { gridItemSpacingInDp.toPx() }
    }
    val itemWidth = remember {
        val horizontalTotalSpacing = (gridItemSpacingInPx * (gridSpanCount + 1))
        val screenWidth = (context.screenMetrics.width.sizeInPixels - horizontalTotalSpacing)

        (screenWidth / gridSpanCount.toFloat())
    }
    val itemWidthInDp = remember {
        with(density) { itemWidth.toDp() }
    }
    val itemHeightInDp = remember {
        val itemHeightToWidthRatio = context.getFloat(R.integer.games_category_item_height_to_width_ratio)
        val itemHeight = (itemWidth * itemHeightToWidthRatio)

        with(density) { itemHeight.toDp() }
    }
    val lastIndex = games.lastIndex

    LazyVerticalGrid(cells = GridCells.Fixed(gridSpanCount)) {
        itemsIndexed(games) { index, game ->
            if (index == lastIndex) {
                LaunchedEffect(lastIndex) {
                    onBottomReached()
                }
            }

            val column = (index % gridSpanCount)
            val paddingValues = with(density) {
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
                    .size(width = itemWidthInDp, height = itemHeightInDp)
                    .padding(paddingValues),
                hasRoundedShape = false,
                onCoverClicked = { onGameClicked(game) },
            )
        }
    }
}

@Preview
@Composable
internal fun GamesCategorySuccessStatePreview() {
    val games = buildList {
        repeat(15) { index ->
            add(
                GameCategoryModel(
                    id = index + 1,
                    title = "Popular Game",
                    coverUrl = null,
                )
            )
        }
    }

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

@Preview
@Composable
internal fun GamesCategoryEmptyStatePreview() {
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

@Preview
@Composable
internal fun GamesCategoryLoadingStatePreview() {
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
