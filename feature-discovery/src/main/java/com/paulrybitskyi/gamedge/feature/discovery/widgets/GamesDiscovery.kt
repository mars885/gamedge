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

package com.paulrybitskyi.gamedge.feature.discovery.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.paulrybitskyi.gamedge.commons.ui.extensions.textSizeResource
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreviewItemModel
import com.paulrybitskyi.gamedge.feature.discovery.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Intentional delay to keep the swipe refresh visible
// because as soon as it is let go, it disappears instantaneously.
private const val SWIPE_REFRESH_INTENTIONAL_DELAY = 300L

@Composable
internal fun GamesDiscovery(
    items: List<GamesDiscoveryItemModel>,
    onCategoryMoreButtonClicked: (String) -> Unit,
    onCategoryGameClicked: (GamesDiscoveryItemGameModel) -> Unit,
    onRefreshRequested: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorContentContainer)),
    ) {
        var isRefreshing by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true

                coroutineScope.launch {
                    delay(SWIPE_REFRESH_INTENTIONAL_DELAY)
                    onRefreshRequested()
                    isRefreshing = false
                }
            },
            modifier = Modifier.matchParentSize(),
            indicator = { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                    contentColor = colorResource(R.color.games_discovery_swipe_refresh_color),
                )
            },
            content = {
                GamesDiscoveryItems(
                    items = items,
                    onCategoryMoreButtonClicked = onCategoryMoreButtonClicked,
                    onCategoryGameClicked = onCategoryGameClicked,
                )
            },
        )
    }
}

@Composable
private fun GamesDiscoveryItems(
    items: List<GamesDiscoveryItemModel>,
    onCategoryMoreButtonClicked: (String) -> Unit,
    onCategoryGameClicked: (GamesDiscoveryItemGameModel) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.games_discovery_decorator_spacing)),
    ) {
        items(items, key = GamesDiscoveryItemModel::title) { item ->
            GamesCategoryPreview(
                title = item.title,
                isProgressBarVisible = item.isProgressBarVisible,
                games = item.games.mapToCategoryItems(),
                onCategoryMoreButtonClicked = { onCategoryMoreButtonClicked(item.category) },
                onCategoryGameClicked = { onCategoryGameClicked(it.mapToDiscoveryItemGameModel()) },
            )
        }
    }
}

@Composable
private fun GamesCategoryPreview(
    title: String,
    isProgressBarVisible: Boolean,
    games: List<GamesCategoryPreviewItemModel>,
    onCategoryMoreButtonClicked: () -> Unit,
    onCategoryGameClicked: (GamesCategoryPreviewItemModel) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = colorResource(R.color.games_category_preview_card_background_color),
        elevation = dimensionResource(R.dimen.games_category_preview_card_elevation),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val titleRefMargin = dimensionResource(R.dimen.games_category_preview_title_margin)
            val progressBarMarginEnd = dimensionResource(R.dimen.games_category_preview_progress_bar_margin_end)
            val moreBtnHorizontalMargin = dimensionResource(
                R.dimen.games_category_preview_more_button_horizontal_margin
            )
            val topBarBarrierMargin = dimensionResource(R.dimen.games_category_preview_top_bar_barrier_margin)
            val refs = createRefs()
            val (titleRef, progressBarRef, moreBtnRef, itemsListRef, infoRef) = refs
            val topBarBarrier = createBottomBarrier(titleRef, progressBarRef, moreBtnRef, margin = topBarBarrierMargin)

            GamesCategoryPreviewTitle(
                title = title,
                modifier = Modifier.constrainAs(titleRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top, titleRefMargin)
                    start.linkTo(parent.start, titleRefMargin)
                    end.linkTo(progressBarRef.start, titleRefMargin)
                },
            )

            if (isProgressBarVisible) {
                GamesCategoryPreviewProgressBar(
                    modifier = Modifier.constrainAs(progressBarRef) {
                        centerVerticallyTo(moreBtnRef)
                        end.linkTo(moreBtnRef.start, progressBarMarginEnd)
                    },
                )
            }

            GamesCategoryPreviewMoreButton(
                modifier = Modifier.constrainAs(moreBtnRef) {
                    centerVerticallyTo(titleRef)
                    end.linkTo(parent.end, moreBtnHorizontalMargin)
                },
                onCategoryMoreButtonClicked = onCategoryMoreButtonClicked,
            )

            GamesCategoryPreviewContent(
                games = games,
                topBarBarrier = topBarBarrier,
                infoRef = infoRef,
                itemsListRef = itemsListRef,
                onCategoryGameClicked = onCategoryGameClicked,
            )
        }
    }
}

@Composable
private fun GamesCategoryPreviewTitle(
    title: String,
    modifier: Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        color = colorResource(R.color.games_category_preview_title_text_color),
        fontSize = textSizeResource(R.dimen.games_category_preview_title_text_size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Start,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
private fun GamesCategoryPreviewProgressBar(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(dimensionResource(R.dimen.games_category_preview_progress_bar_size)),
        color = colorResource(R.color.colorProgressBar),
        strokeWidth = dimensionResource(R.dimen.games_category_preview_progress_bar_stroke_width),
    )
}

@Composable
private fun GamesCategoryPreviewMoreButton(
    modifier: Modifier,
    onCategoryMoreButtonClicked: () -> Unit,
) {
    Text(
        text = stringResource(R.string.games_category_preview_more_button_text).uppercase(),
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onCategoryMoreButtonClicked,
            )
            .padding(dimensionResource(R.dimen.games_category_preview_more_button_padding)),
        color = colorResource(R.color.games_category_preview_more_button_text_color),
        fontSize = textSizeResource(R.dimen.games_category_preview_more_button_text_size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
    )
}

@Composable
private fun ConstraintLayoutScope.GamesCategoryPreviewContent(
    games: List<GamesCategoryPreviewItemModel>,
    topBarBarrier: ConstraintLayoutBaseScope.HorizontalAnchor,
    infoRef: ConstrainedLayoutReference,
    itemsListRef: ConstrainedLayoutReference,
    onCategoryGameClicked: (GamesCategoryPreviewItemModel) -> Unit,
) {
    if (games.isEmpty()) {
        val infoBottomMargin = dimensionResource(R.dimen.games_category_preview_info_view_margin_bottom)
        val infoHorizontalMargin = dimensionResource(R.dimen.games_category_preview_info_view_horizontal_margin)

        GamesCategoryPreviewEmptyState(
            modifier = Modifier.constrainAs(infoRef) {
                top.linkTo(topBarBarrier)
                bottom.linkTo(parent.bottom, infoBottomMargin)
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = infoHorizontalMargin,
                    endMargin = infoHorizontalMargin
                )
            },
        )
    } else {
        GamesCategoryPreviewSuccessState(
            games = games,
            modifier = Modifier.constrainAs(itemsListRef) {
                width = Dimension.fillToConstraints
                top.linkTo(topBarBarrier)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
            },
            onCategoryGameClicked = onCategoryGameClicked,
        )
    }
}

@Composable
private fun GamesCategoryPreviewEmptyState(modifier: Modifier) {
    Info(
        icon = painterResource(R.drawable.google_controller),
        title = stringResource(R.string.games_category_preview_info_view_title),
        modifier = modifier,
        iconSize = dimensionResource(R.dimen.games_category_preview_info_view_icon_size),
        iconColor = colorResource(R.color.colorInfoView),
        titleTextSize = textSizeResource(R.dimen.games_category_preview_info_view_title_text_size),
        titleTextColor = colorResource(R.color.colorInfoView),
    )
}

@Composable
private fun GamesCategoryPreviewSuccessState(
    games: List<GamesCategoryPreviewItemModel>,
    modifier: Modifier,
    onCategoryGameClicked: (GamesCategoryPreviewItemModel) -> Unit,
) {
    val horizontalPadding = dimensionResource(R.dimen.games_category_preview_recycler_view_horizontal_padding)
    val bottomPadding = dimensionResource(R.dimen.games_category_preview_recycler_view_bottom_padding)

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            bottom = bottomPadding,
        ),
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.games_category_preview_cover_horizontal_arrangement)
        ),
    ) {
        items(games, key = GamesCategoryPreviewItemModel::id) { item ->
            GameCover(
                title = item.title,
                imageUrl = item.coverUrl,
                modifier = Modifier.size(
                    width = dimensionResource(R.dimen.games_category_preview_cover_item_width),
                    height = dimensionResource(R.dimen.games_category_preview_cover_item_height)
                ),
                onCoverClicked = { onCategoryGameClicked(item) },
            )
        }
    }
}

@Preview
@Composable
internal fun GamesDiscoveryEmptyStatePreview() {
    GamesDiscovery(
        items = listOf(
            GamesDiscoveryItemModel(
                category = "Popular",
                title = "Popular",
                isProgressBarVisible = true,
                games = emptyList(),
            ),
            GamesDiscoveryItemModel(
                category = "Recently Released",
                title = "Recently Released",
                isProgressBarVisible = true,
                games = emptyList(),
            ),
            GamesDiscoveryItemModel(
                category = "Coming Soon",
                title = "Coming Soon",
                isProgressBarVisible = true,
                games = emptyList(),
            ),
            GamesDiscoveryItemModel(
                category = "Most Anticipated",
                title = "Most Anticipated",
                isProgressBarVisible = true,
                games = emptyList(),
            ),
        ),
        onCategoryMoreButtonClicked = {},
        onCategoryGameClicked = {},
        onRefreshRequested = {},
    )
}

@Preview
@Composable
internal fun GamesDiscoverySuccessStatePreview() {
    val games = listOf(
        GamesDiscoveryItemGameModel(
            id = 1,
            title = "Ghost of Tsushima: Director's Cut",
            coverUrl = null,
        ),
        GamesDiscoveryItemGameModel(
            id = 2,
            title = "Outer Wilds: Echoes of the Eye",
            coverUrl = null,
        ),
        GamesDiscoveryItemGameModel(
            id = 3,
            title = "Kena: Bridge of Spirits",
            coverUrl = null,
        ),
        GamesDiscoveryItemGameModel(
            id = 4,
            title = "Forza Horizon 5",
            coverUrl = null,
        ),
    )

    GamesDiscovery(
        items = listOf(
            GamesDiscoveryItemModel(
                category = "Popular",
                title = "Popular",
                isProgressBarVisible = true,
                games = games,
            ),
            GamesDiscoveryItemModel(
                category = "Recently Released",
                title = "Recently Released",
                isProgressBarVisible = true,
                games = games,
            ),
            GamesDiscoveryItemModel(
                category = "Coming Soon",
                title = "Coming Soon",
                isProgressBarVisible = true,
                games = games,
            ),
            GamesDiscoveryItemModel(
                category = "Most Anticipated",
                title = "Most Anticipated",
                isProgressBarVisible = true,
                games = games,
            ),
        ),
        onCategoryMoreButtonClicked = {},
        onCategoryGameClicked = {},
        onRefreshRequested = {},
    )
}
