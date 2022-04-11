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

package com.paulrybitskyi.gamedge.feature.info.widgets.header

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.clickable
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.theme.lightScrim
import com.paulrybitskyi.gamedge.commons.ui.theme.subtitle3
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks.GameInfoArtworkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks.id

private const val CONSTRAINT_ID_ARTWORKS = "artworks"
private const val CONSTRAINT_ID_ARTWORKS_SCRIM = "artworks_scrim"
private const val CONSTRAINT_ID_BACK_BUTTON = "back_button"
private const val CONSTRAINT_ID_PAGE_INDICATOR = "page_indicator"
private const val CONSTRAINT_ID_BACKDROP = "backdrop"
private const val CONSTRAINT_ID_COVER_SPACE = "cover_space"
private const val CONSTRAINT_ID_COVER = "cover"
private const val CONSTRAINT_ID_LIKE_BUTTON = "like_button"
private const val CONSTRAINT_ID_FIRST_TITLE = "first_title"
private const val CONSTRAINT_ID_SECOND_TITLE = "second_title"
private const val CONSTRAINT_ID_RELEASE_DATE = "release_date"
private const val CONSTRAINT_ID_DEVELOPER_NAME = "developer_name"
private const val CONSTRAINT_ID_BOTTOM_BARRIER = "bottom_barrier"
private const val CONSTRAINT_ID_RATING = "rating"
private const val CONSTRAINT_ID_LIKE_COUNT = "like_count"
private const val CONSTRAINT_ID_AGE_RATING = "age_rating"
private const val CONSTRAINT_ID_GAME_CATEGORY = "game_category"

private val COVER_SPACE = 40.dp
private val INFO_ICON_SIZE = 34.dp

@Composable
internal fun GameInfoHeader(
    headerInfo: GameInfoHeaderModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
) {
    val artworks = headerInfo.artworks
    val isPageIndicatorVisible by remember(artworks) { mutableStateOf(artworks.size > 1) }
    var selectedArtworkPage by remember { mutableStateOf(0) }
    var secondTitleText by remember { mutableStateOf("") }
    val isSecondTitleVisible by remember {
        derivedStateOf {
            secondTitleText.isNotEmpty()
        }
    }

    ConstraintLayout(
        constraintSet = constructExpandedConstraintSet(),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Artworks(
            artworks = artworks,
            isScrollingEnabled = true,
            modifier = Modifier.layoutId(CONSTRAINT_ID_ARTWORKS),
            onArtworkChanged = { page ->
                selectedArtworkPage = page
            },
            onArtworkClicked = onArtworkClicked,
        )

        Box(
            Modifier
                .layoutId(CONSTRAINT_ID_ARTWORKS_SCRIM)
                .background(Color.Transparent),
        )

        Icon(
            painter = painterResource(R.drawable.arrow_left),
            contentDescription = null,
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_BACK_BUTTON)
                .statusBarsPadding()
                .size(56.dp)
                .clickable(
                    indication = rememberRipple(
                        bounded = false,
                        radius = 18.dp,
                    ),
                    onClick = onBackButtonClicked,
                )
                .padding(GamedgeTheme.spaces.spacing_2_5)
                .background(
                    color = GamedgeTheme.colors.lightScrim,
                    shape = CircleShape,
                )
                .padding(GamedgeTheme.spaces.spacing_1_5),
            tint = GamedgeTheme.colors.onPrimary,
        )

        if (isPageIndicatorVisible) {
            Text(
                text = stringResource(
                    R.string.game_info_header_page_indicator_template,
                    selectedArtworkPage + 1,
                    headerInfo.artworks.size,
                ),
                modifier = Modifier
                    .layoutId(CONSTRAINT_ID_PAGE_INDICATOR)
                    .statusBarsPadding()
                    .background(
                        color = GamedgeTheme.colors.lightScrim,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .padding(
                        vertical = GamedgeTheme.spaces.spacing_1_5,
                        horizontal = GamedgeTheme.spaces.spacing_2_0,
                    ),
                color = GamedgeTheme.colors.onPrimary,
                style = GamedgeTheme.typography.subtitle3,
            )
        }

        Box(
            Modifier
                .layoutId(CONSTRAINT_ID_BACKDROP)
                .shadow(
                    elevation = GamedgeTheme.spaces.spacing_0_5,
                    shape = RectangleShape,
                    clip = false,
                )
                .background(
                    color = GamedgeTheme.colors.surface,
                    shape = RectangleShape,
                )
                .clip(RectangleShape),
        )

        Spacer(
            Modifier
                .layoutId(CONSTRAINT_ID_COVER_SPACE)
                .height(COVER_SPACE),
        )

        GameCover(
            title = null,
            imageUrl = headerInfo.coverImageUrl,
            modifier = Modifier.layoutId(CONSTRAINT_ID_COVER),
            onCoverClicked = onCoverClicked,
        )

        FloatingActionButton(
            onClick = onLikeButtonClicked,
            modifier = Modifier.layoutId(CONSTRAINT_ID_LIKE_BUTTON),
            backgroundColor = GamedgeTheme.colors.secondary,
        ) {
            // Animated selector drawables are not currently supported by the Jetpack Compose.
            // https://issuetracker.google.com/issues/212418566
            // Consider to use the R.drawable.heart_animated_selector when the support arrives.

            Icon(
                painter = rememberAnimatedVectorPainter(
                    animatedImageVector = AnimatedImageVector.animatedVectorResource(
                        R.drawable.heart_animated_fill
                    ),
                    atEnd = headerInfo.isLiked,
                ),
                contentDescription = null,
                modifier = Modifier.size(52.dp),
                tint = GamedgeTheme.colors.onSecondary,
            )
        }

        Text(
            text = headerInfo.title,
            modifier = Modifier.layoutId(CONSTRAINT_ID_FIRST_TITLE),
            color = GamedgeTheme.colors.onPrimary,
            maxLines = 1,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    val firstTitleWidth = textLayoutResult.size.width.toFloat()
                    val firstTitleOffset = Offset(firstTitleWidth, 0f)
                    val firstTitleVisibleTextEndIndex = textLayoutResult.getOffsetForPosition(firstTitleOffset) + 1

                    secondTitleText = headerInfo.title.substring(firstTitleVisibleTextEndIndex)
                }
            },
            style = GamedgeTheme.typography.h6,
        )

        Box(Modifier.layoutId(CONSTRAINT_ID_SECOND_TITLE)) {
            if (isSecondTitleVisible) {
                // Remove font padding once https://issuetracker.google.com/issues/171394808
                // is implemented (includeFontPadding="false" in XML)
                Text(
                    text = secondTitleText,
                    color = GamedgeTheme.colors.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = GamedgeTheme.typography.h6,
                )
            }
        }

        Text(
            text = headerInfo.releaseDate,
            modifier = Modifier.layoutId(CONSTRAINT_ID_RELEASE_DATE),
            color = GamedgeTheme.colors.onSurface,
            style = GamedgeTheme.typography.subtitle3,
        )

        Box(Modifier.layoutId(CONSTRAINT_ID_DEVELOPER_NAME)) {
            if (headerInfo.hasDeveloperName) {
                Text(
                    text = checkNotNull(headerInfo.developerName),
                    color = GamedgeTheme.colors.onSurface,
                    style = GamedgeTheme.typography.subtitle3,
                )
            }
        }

        Info(
            icon = painterResource(R.drawable.star_circle_outline),
            title = headerInfo.rating,
            modifier = Modifier.layoutId(CONSTRAINT_ID_RATING),
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.account_heart_outline),
            title = headerInfo.likeCount,
            modifier = Modifier.layoutId(CONSTRAINT_ID_LIKE_COUNT),
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.age_rating_outline),
            title = headerInfo.ageRating,
            modifier = Modifier.layoutId(CONSTRAINT_ID_AGE_RATING),
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.shape_outline),
            title = headerInfo.gameCategory,
            modifier = Modifier.layoutId(CONSTRAINT_ID_GAME_CATEGORY),
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
    }
}

@Composable
private fun constructExpandedConstraintSet(): ConstraintSet {
    val artworksHeight = 240.dp
    val pageIndicatorMargin = GamedgeTheme.spaces.spacing_2_5
    val coverSpaceMargin = COVER_SPACE
    val coverMarginStart = GamedgeTheme.spaces.spacing_3_5
    val likeBtnMarginEnd = GamedgeTheme.spaces.spacing_2_5
    val titleMarginStart = GamedgeTheme.spaces.spacing_3_5
    val firstTitleMarginTop = titleMarginStart
    val firstTitleMarginEnd = GamedgeTheme.spaces.spacing_1_0
    val secondTitleMarginEnd = GamedgeTheme.spaces.spacing_3_5
    val releaseDateMarginTop = GamedgeTheme.spaces.spacing_2_5
    val releaseDateMarginHorizontal = GamedgeTheme.spaces.spacing_3_5
    val developerNameMarginHorizontal = GamedgeTheme.spaces.spacing_3_5
    val bottomBarrierMargin = GamedgeTheme.spaces.spacing_5_0
    val infoItemMarginBottom = GamedgeTheme.spaces.spacing_3_5

    return ConstraintSet {
        val artworks = createRefFor(CONSTRAINT_ID_ARTWORKS)
        val artworksScrim = createRefFor(CONSTRAINT_ID_ARTWORKS_SCRIM)
        val backButton = createRefFor(CONSTRAINT_ID_BACK_BUTTON)
        val pageIndicator = createRefFor(CONSTRAINT_ID_PAGE_INDICATOR)
        val backdrop = createRefFor(CONSTRAINT_ID_BACKDROP)
        val coverSpace = createRefFor(CONSTRAINT_ID_COVER_SPACE)
        val cover = createRefFor(CONSTRAINT_ID_COVER)
        val likeButton = createRefFor(CONSTRAINT_ID_LIKE_BUTTON)
        val firstTitle = createRefFor(CONSTRAINT_ID_FIRST_TITLE)
        val secondTitle = createRefFor(CONSTRAINT_ID_SECOND_TITLE)
        val releaseDate = createRefFor(CONSTRAINT_ID_RELEASE_DATE)
        val developerName = createRefFor(CONSTRAINT_ID_DEVELOPER_NAME)
        val bottomBarrier = createBottomBarrier(cover, developerName, margin = bottomBarrierMargin)
        val rating = createRefFor(CONSTRAINT_ID_RATING)
        val likeCount = createRefFor(CONSTRAINT_ID_LIKE_COUNT)
        val ageRating = createRefFor(CONSTRAINT_ID_AGE_RATING)
        val gameCategory = createRefFor(CONSTRAINT_ID_GAME_CATEGORY)

        constrain(artworks) {
            width = Dimension.fillToConstraints
            height = Dimension.value(artworksHeight)
            top.linkTo(parent.top)
            centerHorizontallyTo(parent)
        }
        constrain(artworksScrim) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            centerVerticallyTo(artworks)
            centerHorizontallyTo(artworks)
        }
        constrain(backButton) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }
        constrain(pageIndicator) {
            top.linkTo(parent.top, pageIndicatorMargin)
            end.linkTo(parent.end, pageIndicatorMargin)
        }
        constrain(backdrop) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(artworks.bottom)
            bottom.linkTo(parent.bottom)
            centerHorizontallyTo(parent)
        }
        constrain(coverSpace) {
            start.linkTo(parent.start)
            bottom.linkTo(artworks.bottom, coverSpaceMargin)
        }
        constrain(cover) {
            top.linkTo(coverSpace.bottom)
            start.linkTo(parent.start, coverMarginStart)
        }
        constrain(likeButton) {
            top.linkTo(artworks.bottom)
            bottom.linkTo(artworks.bottom)
            end.linkTo(parent.end, likeBtnMarginEnd)
        }
        constrain(firstTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, firstTitleMarginTop)
            start.linkTo(cover.end, titleMarginStart)
            end.linkTo(likeButton.start, firstTitleMarginEnd)
        }
        constrain(secondTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(firstTitle.bottom)
            start.linkTo(cover.end, titleMarginStart)
            end.linkTo(parent.end, secondTitleMarginEnd)
        }
        constrain(releaseDate) {
            width = Dimension.fillToConstraints
            top.linkTo(secondTitle.bottom, releaseDateMarginTop)
            start.linkTo(cover.end, releaseDateMarginHorizontal)
            end.linkTo(parent.end, releaseDateMarginHorizontal)
        }
        constrain(developerName) {
            width = Dimension.fillToConstraints
            top.linkTo(releaseDate.bottom)
            start.linkTo(cover.end, developerNameMarginHorizontal)
            end.linkTo(parent.end, developerNameMarginHorizontal)
        }
        constrain(rating) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(parent.bottom, infoItemMarginBottom)
            linkTo(start = parent.start, end = likeCount.start, bias = 0.25f)
        }
        constrain(likeCount) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(parent.bottom, infoItemMarginBottom)
            linkTo(start = rating.end, end = ageRating.start, bias = 0.25f)
        }
        constrain(ageRating) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(parent.bottom, infoItemMarginBottom)
            linkTo(start = likeCount.end, end = gameCategory.start, bias = 0.25f)
        }
        constrain(gameCategory) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(parent.bottom, infoItemMarginBottom)
            linkTo(start = ageRating.end, end = parent.end, bias = 0.25f)
        }
    }
}

@Composable
private fun Artworks(
    artworks: List<GameInfoArtworkModel>,
    isScrollingEnabled: Boolean, // todo
    modifier: Modifier,
    onArtworkChanged: (artworkIndex: Int) -> Unit,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
) {
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page -> onArtworkChanged(page) }
    }

    HorizontalPager(
        count = artworks.size,
        modifier = modifier,
        state = pagerState,
        key = { page -> artworks[page].id },
    ) { page ->
        Artwork(
            artwork = artworks[page],
            onArtworkClicked = { onArtworkClicked(page) },
        )
    }
}

@Composable
private fun Artwork(
    artwork: GameInfoArtworkModel,
    onArtworkClicked: () -> Unit,
) {
    val painter = when (artwork) {
        is GameInfoArtworkModel.DefaultImage -> painterResource(R.drawable.game_background_placeholder)
        is GameInfoArtworkModel.UrlImage -> rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(artwork.url)
                .fallback(R.drawable.game_background_placeholder)
                .placeholder(R.drawable.game_background_placeholder)
                .error(R.drawable.game_background_placeholder)
                .crossfade(CROSSFADE_ANIMATION_DURATION)
                .build(),
            contentScale = ContentScale.Crop,
        )
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onArtworkClicked,
            ),
        contentScale = when (artwork) {
            is GameInfoArtworkModel.DefaultImage -> ContentScale.Crop
            is GameInfoArtworkModel.UrlImage -> ContentScale.FillBounds
        },
    )
}

@Preview
@Composable
internal fun GameInfoHeaderPreview() {
    GamedgeTheme {
        GameInfoHeader(
            headerInfo = GameInfoHeaderModel(
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
            onArtworkClicked = {},
            onBackButtonClicked = {},
            onCoverClicked = {},
            onLikeButtonClicked = {},
        )
    }
}
