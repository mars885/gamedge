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

package com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.R

@Composable
fun GamesCategoryPreview(
    title: String,
    isProgressBarVisible: Boolean,
    games: List<GamesCategoryPreviewItemModel>,
    onCategoryGameClicked: (GamesCategoryPreviewItemModel) -> Unit,
    topBarMargin: Dp = dimensionResource(R.dimen.games_category_preview_top_bar_barrier_margin),
    isMoreButtonVisible: Boolean = true,
    onCategoryMoreButtonClicked: (() -> Unit)? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        elevation = dimensionResource(R.dimen.games_category_preview_card_elevation),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val titleRefMargin = dimensionResource(R.dimen.games_category_preview_title_margin)
            val progressBarMarginEnd = dimensionResource(R.dimen.games_category_preview_progress_bar_margin_end)
            val moreBtnHorizontalMargin = dimensionResource(
                R.dimen.games_category_preview_more_button_horizontal_margin
            )
            val refs = createRefs()
            val (titleRef, progressBarRef, moreBtnRef, itemsListRef, infoRef) = refs
            val topBarBarrier = createBottomBarrier(titleRef, progressBarRef, moreBtnRef, margin = topBarMargin)

            Title(
                title = title,
                modifier = Modifier.constrainAs(titleRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top, titleRefMargin)
                    start.linkTo(parent.start, titleRefMargin)
                    end.linkTo(progressBarRef.start, titleRefMargin)
                },
            )

            if (isProgressBarVisible) {
                ProgressBar(
                    modifier = Modifier.constrainAs(progressBarRef) {
                        centerVerticallyTo(moreBtnRef)
                        end.linkTo(moreBtnRef.start, progressBarMarginEnd)
                    },
                )
            }

            if (isMoreButtonVisible) {
                MoreButton(
                    modifier = Modifier.constrainAs(moreBtnRef) {
                        centerVerticallyTo(titleRef)
                        end.linkTo(parent.end, moreBtnHorizontalMargin)
                    },
                    onCategoryMoreButtonClicked = onCategoryMoreButtonClicked,
                )
            }

            Content(
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
private fun Title(
    title: String,
    modifier: Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        color = GamedgeTheme.colors.onPrimary,
        textAlign = TextAlign.Start,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = GamedgeTheme.typography.h6,
    )
}

@Composable
private fun ProgressBar(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(dimensionResource(R.dimen.games_category_preview_progress_bar_size)),
        color = GamedgeTheme.colors.secondary,
        strokeWidth = dimensionResource(R.dimen.games_category_preview_progress_bar_stroke_width),
    )
}

@Composable
private fun MoreButton(
    modifier: Modifier,
    onCategoryMoreButtonClicked: (() -> Unit)?,
) {
    val clickableModifier = if (onCategoryMoreButtonClicked != null) {
        Modifier.clickable(onClick = onCategoryMoreButtonClicked)
    } else {
        Modifier
    }

    Text(
        text = stringResource(R.string.games_category_preview_more_button_text).uppercase(),
        modifier = modifier
            .then(clickableModifier)
            .padding(dimensionResource(R.dimen.games_category_preview_more_button_padding)),
        color = GamedgeTheme.colors.secondary,
        style = GamedgeTheme.typography.button,
    )
}

@Composable
private fun ConstraintLayoutScope.Content(
    games: List<GamesCategoryPreviewItemModel>,
    topBarBarrier: ConstraintLayoutBaseScope.HorizontalAnchor,
    infoRef: ConstrainedLayoutReference,
    itemsListRef: ConstrainedLayoutReference,
    onCategoryGameClicked: (GamesCategoryPreviewItemModel) -> Unit,
) {
    if (games.isEmpty()) {
        val infoBottomMargin = dimensionResource(R.dimen.games_category_preview_info_view_margin_bottom)
        val infoHorizontalMargin = dimensionResource(R.dimen.games_category_preview_info_view_horizontal_margin)

        EmptyState(
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
        SuccessState(
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
private fun EmptyState(modifier: Modifier) {
    Info(
        icon = painterResource(R.drawable.google_controller),
        title = stringResource(R.string.games_category_preview_info_view_title),
        modifier = modifier,
        iconSize = dimensionResource(R.dimen.games_category_preview_info_view_icon_size),
    )
}

@Composable
private fun SuccessState(
    games: List<GamesCategoryPreviewItemModel>,
    modifier: Modifier,
    onCategoryGameClicked: (GamesCategoryPreviewItemModel) -> Unit,
) {
    val horizontalPadding = dimensionResource(R.dimen.games_category_preview_padding_horizontal)
    val bottomPadding = dimensionResource(R.dimen.games_category_preview_padding_bottom)

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

@Composable
@Preview
internal fun GamesCategoryPreviewSuccessStateWithMoreButtonPreview() {
    GamedgeTheme {
        GamesCategoryPreview(
            title = "Popular",
            isProgressBarVisible = false,
            games = listOf(
                GamesCategoryPreviewItemModel(
                    id = 1,
                    title = "Ghost of Tsushima: Director's Cut",
                    coverUrl = null,
                ),
                GamesCategoryPreviewItemModel(
                    id = 2,
                    title = "Outer Wilds: Echoes of the Eye",
                    coverUrl = null,
                ),
                GamesCategoryPreviewItemModel(
                    id = 3,
                    title = "Kena: Bridge of Spirits",
                    coverUrl = null,
                ),
                GamesCategoryPreviewItemModel(
                    id = 4,
                    title = "Forza Horizon 5",
                    coverUrl = null,
                ),
            ),
            onCategoryMoreButtonClicked = {},
            onCategoryGameClicked = {},
        )
    }
}

@Composable
@Preview
internal fun GamesCategoryPreviewSuccessStateWithoutMoreButtonPreview() {
    GamedgeTheme {
        GamesCategoryPreview(
            title = "Popular",
            isProgressBarVisible = false,
            isMoreButtonVisible = false,
            games = listOf(
                GamesCategoryPreviewItemModel(
                    id = 1,
                    title = "Ghost of Tsushima: Director's Cut",
                    coverUrl = null,
                ),
                GamesCategoryPreviewItemModel(
                    id = 2,
                    title = "Outer Wilds: Echoes of the Eye",
                    coverUrl = null,
                ),
                GamesCategoryPreviewItemModel(
                    id = 3,
                    title = "Kena: Bridge of Spirits",
                    coverUrl = null,
                ),
                GamesCategoryPreviewItemModel(
                    id = 4,
                    title = "Forza Horizon 5",
                    coverUrl = null,
                ),
            ),
            onCategoryMoreButtonClicked = {},
            onCategoryGameClicked = {},
        )
    }
}

@Composable
@Preview
internal fun GamesCategoryPreviewEmptyStatePreview() {
    GamedgeTheme {
        GamesCategoryPreview(
            title = "Popular",
            isProgressBarVisible = false,
            games = emptyList(),
            onCategoryMoreButtonClicked = {},
            onCategoryGameClicked = {},
        )
    }
}
