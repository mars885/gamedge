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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeCard
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeProgressIndicator
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.R

@Composable
fun GamesCategoryPreview(
    title: String,
    isProgressBarVisible: Boolean,
    games: List<GamesCategoryPreviewItemModel>,
    onCategoryGameClicked: (GamesCategoryPreviewItemModel) -> Unit,
    topBarMargin: Dp = GamedgeTheme.spaces.spacing_2_0,
    isMoreButtonVisible: Boolean = true,
    onCategoryMoreButtonClicked: (() -> Unit)? = null,
) {
    GamedgeCard(modifier = Modifier.fillMaxWidth()) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val titleRefMargin = GamedgeTheme.spaces.spacing_3_5
            val progressBarMarginEnd = GamedgeTheme.spaces.spacing_1_5
            val moreBtnHorizontalMargin = GamedgeTheme.spaces.spacing_1_5
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
    GamedgeProgressIndicator(
        modifier = modifier.size(16.dp),
        strokeWidth = 2.dp,
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
            .padding(GamedgeTheme.spaces.spacing_2_0),
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
        val infoBottomMargin = GamedgeTheme.spaces.spacing_7_0
        val infoHorizontalMargin = GamedgeTheme.spaces.spacing_7_5

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
        iconSize = 80.dp,
    )
}

@Composable
private fun SuccessState(
    games: List<GamesCategoryPreviewItemModel>,
    modifier: Modifier,
    onCategoryGameClicked: (GamesCategoryPreviewItemModel) -> Unit,
) {
    val padding = GamedgeTheme.spaces.spacing_3_5

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = padding,
            end = padding,
            bottom = padding,
        ),
        horizontalArrangement = Arrangement.spacedBy(GamedgeTheme.spaces.spacing_1_5),
    ) {
        items(items = games, key = GamesCategoryPreviewItemModel::id) { item ->
            GameCover(
                title = item.title,
                imageUrl = item.coverUrl,
                onCoverClicked = { onCategoryGameClicked(item) },
            )
        }
    }
}

@Preview
@Composable
private fun GamesCategoryPreviewSuccessStateWithMoreButtonPreview() {
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

@Preview
@Composable
private fun GamesCategoryPreviewSuccessStateWithoutMoreButtonPreview() {
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

@Preview
@Composable
private fun GamesCategoryPreviewEmptyStatePreview() {
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
