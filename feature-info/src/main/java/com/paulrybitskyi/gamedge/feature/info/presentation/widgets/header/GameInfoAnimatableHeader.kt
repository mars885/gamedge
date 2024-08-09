/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

@file:Suppress("LongMethod")

package com.paulrybitskyi.gamedge.feature.info.presentation.widgets.header

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.Transition
import androidx.constraintlayout.compose.Visibility
import com.paulrybitskyi.commons.ktx.statusBarHeight
import com.paulrybitskyi.gamedge.common.ui.clickable
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.theme.darkScrim
import com.paulrybitskyi.gamedge.common.ui.theme.lightScrim
import com.paulrybitskyi.gamedge.common.ui.theme.subtitle3
import com.paulrybitskyi.gamedge.common.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.common.ui.widgets.Info
import com.paulrybitskyi.gamedge.core.R as CoreR
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.header.artworks.Artworks
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.header.artworks.GameInfoArtworkUiModel
import org.intellij.lang.annotations.Language

private const val ConstraintIdArtworks = "artworks"
private const val ConstraintIdArtworksScrim = "artworks_scrim"
private const val ConstraintIdBackButton = "back_button"
private const val ConstraintIdPageIndicator = "page_indicator"
private const val ConstraintIdBackdrop = "backdrop"
private const val ConstraintIdCoverSpace = "cover_space"
private const val ConstraintIdCover = "cover"
private const val ConstraintIdLikeButton = "like_button"
private const val ConstraintIdFirstTitle = "first_title"
private const val ConstraintIdSecondTitle = "second_title"
private const val ConstraintIdReleaseDate = "release_date"
private const val ConstraintIdDeveloperName = "developer_name"
private const val ConstraintIdRating = "rating"
private const val ConstraintIdLikeCount = "like_count"
private const val ConstraintIdAgeRating = "age_rating"
private const val ConstraintIdGameCategory = "game_category"
private const val ConstraintIdList = "list"

private val CoverSpace = 40.dp
private val InfoIconSize = 34.dp

private const val FirstTitleScaleCollapsed = 1.1f
private const val LikeButtonScaleCollapsed = 0f

private val ArtworksHeightExpanded = 240.dp
private val ArtworksHeightCollapsed = 56.dp

private val PageIndicatorDeltaXCollapsed = 60.dp
private val CoverDeltaXCollapsed = -130.dp
private val CoverDeltaYCollapsed = -60.dp
private val SecondaryTextDeltaXCollapsed = -8.dp

private enum class State {
    Expanded,
    Collapsed,
}

// Try out this again when a new version of MotionLayout for compose
// comes out (as of 12.06.2022, the latest is 1.1.0-alpha02).
@OptIn(ExperimentalMotionApi::class)
@Composable
internal fun GameInfoAnimatableHeader(
    headerInfo: GameInfoHeaderUiModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    content: @Composable (Modifier) -> Unit,
) {
    var state by remember { mutableStateOf(State.Expanded) }
    val progress by animateFloatAsState(
        targetValue = if (state == State.Expanded) 0f else 1f,
        animationSpec = tween(3000),
    )

    val artworks = headerInfo.artworks
    val isPageIndicatorVisible = remember(artworks) { artworks.size > 1 }
    val hasDefaultPlaceholderArtwork = remember(artworks) {
        artworks.size == 1 &&
        artworks.single() is GameInfoArtworkUiModel.DefaultImage
    }
    var selectedArtworkPage by remember { mutableStateOf(0) }
    var secondTitleText by rememberSaveable { mutableStateOf("") }
    val isSecondTitleVisible by remember {
        derivedStateOf {
            secondTitleText.isNotEmpty()
        }
    }
    val isArtworkInteractionEnabled by remember {
        derivedStateOf {
            progress < 0.01f
        }
    }
    val firstTitleOverflowMode by remember {
        derivedStateOf {
            if (progress < 0.95f) {
                TextOverflow.Clip
            } else {
                TextOverflow.Ellipsis
            }
        }
    }

    MotionLayout(
        // motionScene = MotionScene(constructJson()),
        start = constructExpandedConstraintSet(
            hasDefaultPlaceholderArtwork = hasDefaultPlaceholderArtwork,
            isSecondTitleVisible = isSecondTitleVisible,
        ),
        end = constructCollapsedConstraintSet(
            hasDefaultPlaceholderArtwork = hasDefaultPlaceholderArtwork,
        ),
        transition = Transition(constructTransition()),
        modifier = Modifier.fillMaxSize(),
        progress = progress,
    ) {
        Artworks(
            artworks = artworks,
            isScrollingEnabled = isArtworkInteractionEnabled,
            modifier = Modifier.layoutId(ConstraintIdArtworks),
            onArtworkChanged = { page ->
                selectedArtworkPage = page
            },
            onArtworkClicked = { artworkIndex ->
                if (isArtworkInteractionEnabled) {
                    onArtworkClicked(artworkIndex)
                }
            },
        )

        Box(
            modifier = Modifier
                .layoutId(ConstraintIdArtworksScrim)
                .background(GamedgeTheme.colors.darkScrim),
        )

        Icon(
            painter = painterResource(CoreR.drawable.arrow_left),
            contentDescription = null,
            modifier = Modifier
                .layoutId(ConstraintIdBackButton)
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
                    .layoutId(ConstraintIdPageIndicator)
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
            modifier = Modifier
                .layoutId(ConstraintIdBackdrop)
                .background(
                    color = GamedgeTheme.colors.surface,
                    shape = RectangleShape,
                )
                .clip(RectangleShape),
        )

        Spacer(
            Modifier
                .layoutId(ConstraintIdCoverSpace)
                .height(CoverSpace),
        )

        GameCover(
            title = null,
            imageUrl = headerInfo.coverImageUrl,
            modifier = Modifier
                .layoutId(ConstraintIdCover)
                .drawOnTop(),
            onCoverClicked = if (headerInfo.hasCoverImageUrl) onCoverClicked else null,
        )

        FloatingActionButton(
            onClick = onLikeButtonClicked,
            modifier = Modifier
                .layoutId(ConstraintIdLikeButton)
                .drawOnTop(),
            backgroundColor = GamedgeTheme.colors.secondary,
        ) {
            // Animated selector drawables are not currently supported by the Jetpack Compose.
            // https://issuetracker.google.com/issues/212418566
            // Consider to use the R.drawable.heart_animated_selector when the support arrives.

            Icon(
                painter = rememberAnimatedVectorPainter(
                    animatedImageVector = AnimatedImageVector.animatedVectorResource(
                        CoreR.drawable.heart_animated_fill
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
            modifier = Modifier
                .layoutId(ConstraintIdFirstTitle)
                .drawOnTop(),
            color = GamedgeTheme.colors.onPrimary,
            overflow = firstTitleOverflowMode,
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

        Box(
            modifier = Modifier
                .layoutId(ConstraintIdSecondTitle)
                .drawOnTop(),
        ) {
            if (isSecondTitleVisible) {
                // Remove font padding once https://issuetracker.google.com/issues/171394808
                // is implemented (includeFontPadding="false" in XML)
                Text(
                    text = secondTitleText,
                    color = GamedgeTheme.colors.onPrimary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = GamedgeTheme.typography.h6,
                )
            }
        }

        Text(
            text = headerInfo.releaseDate,
            modifier = Modifier
                .layoutId(ConstraintIdReleaseDate)
                .drawOnTop(),
            color = GamedgeTheme.colors.onSurface,
            style = GamedgeTheme.typography.subtitle3,
        )

        Box(
            modifier = Modifier
                .layoutId(ConstraintIdDeveloperName)
                .drawOnTop(),
        ) {
            if (headerInfo.hasDeveloperName) {
                Text(
                    text = checkNotNull(headerInfo.developerName),
                    color = GamedgeTheme.colors.onSurface,
                    style = GamedgeTheme.typography.subtitle3,
                )
            }
        }

        Info(
            icon = painterResource(CoreR.drawable.star_circle_outline),
            title = headerInfo.rating,
            modifier = Modifier
                .layoutId(ConstraintIdRating)
                .drawOnTop()
                .clickable {
                    // TODO: To be removed, only for debugging purposes
                    state = if (state == State.Expanded) {
                        State.Collapsed
                    } else {
                        State.Expanded
                    }
                },
            iconSize = InfoIconSize,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(CoreR.drawable.account_heart_outline),
            title = headerInfo.likeCount,
            modifier = Modifier
                .layoutId(ConstraintIdLikeCount)
                .drawOnTop(),
            iconSize = InfoIconSize,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(CoreR.drawable.age_rating_outline),
            title = headerInfo.ageRating,
            modifier = Modifier
                .layoutId(ConstraintIdAgeRating)
                .drawOnTop(),
            iconSize = InfoIconSize,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(CoreR.drawable.shape_outline),
            title = headerInfo.gameCategory,
            modifier = Modifier
                .layoutId(ConstraintIdGameCategory)
                .drawOnTop(),
            iconSize = InfoIconSize,
            titleTextStyle = GamedgeTheme.typography.caption,
        )

        content(Modifier.layoutId(ConstraintIdList))
    }
}

@Composable
private fun constructExpandedConstraintSet(
    hasDefaultPlaceholderArtwork: Boolean = false,
    isSecondTitleVisible: Boolean = false,
): ConstraintSet {
    val pageIndicatorMargin = GamedgeTheme.spaces.spacing_2_5
    val backdropElevation = GamedgeTheme.spaces.spacing_0_5
    val coverSpaceMargin = CoverSpace
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
        val artworks = createRefFor(ConstraintIdArtworks)
        val artworksScrim = createRefFor(ConstraintIdArtworksScrim)
        val backButton = createRefFor(ConstraintIdBackButton)
        val pageIndicator = createRefFor(ConstraintIdPageIndicator)
        val backdrop = createRefFor(ConstraintIdBackdrop)
        val coverSpace = createRefFor(ConstraintIdCoverSpace)
        val cover = createRefFor(ConstraintIdCover)
        val likeButton = createRefFor(ConstraintIdLikeButton)
        val firstTitle = createRefFor(ConstraintIdFirstTitle)
        val secondTitle = createRefFor(ConstraintIdSecondTitle)
        val releaseDate = createRefFor(ConstraintIdReleaseDate)
        val developerName = createRefFor(ConstraintIdDeveloperName)
        val bottomBarrier = createBottomBarrier(cover, developerName, margin = bottomBarrierMargin)
        val rating = createRefFor(ConstraintIdRating)
        val likeCount = createRefFor(ConstraintIdLikeCount)
        val ageRating = createRefFor(ConstraintIdAgeRating)
        val gameCategory = createRefFor(ConstraintIdGameCategory)
        val list = createRefFor(ConstraintIdList)

        constrain(artworks) {
            width = Dimension.fillToConstraints
            height = Dimension.value(ArtworksHeightExpanded)
            top.linkTo(parent.top)
            centerHorizontallyTo(parent)
        }
        constrain(artworksScrim) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            centerVerticallyTo(artworks)
            centerHorizontallyTo(artworks)
            visibility = if (hasDefaultPlaceholderArtwork) {
                Visibility.Gone
            } else {
                Visibility.Invisible
            }
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
            bottom.linkTo(list.top)
            centerHorizontallyTo(parent)
            translationZ = backdropElevation
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
            isVisible = isSecondTitleVisible
        }
        constrain(releaseDate) {
            width = Dimension.fillToConstraints
            top.linkTo(secondTitle.bottom, releaseDateMarginTop, releaseDateMarginTop)
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
            bottom.linkTo(list.top, infoItemMarginBottom)
            linkTo(start = parent.start, end = likeCount.start, bias = 0.25f)
        }
        constrain(likeCount) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(list.top, infoItemMarginBottom)
            linkTo(start = rating.end, end = ageRating.start, bias = 0.25f)
        }
        constrain(ageRating) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(list.top, infoItemMarginBottom)
            linkTo(start = likeCount.end, end = gameCategory.start, bias = 0.25f)
        }
        constrain(gameCategory) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(list.top, infoItemMarginBottom)
            linkTo(start = ageRating.end, end = parent.end, bias = 0.25f)
        }
        constrain(list) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(rating.bottom)
            bottom.linkTo(parent.bottom)
            centerHorizontallyTo(parent)
        }
    }
}

@SuppressLint("Range")
@Composable
private fun constructCollapsedConstraintSet(
    hasDefaultPlaceholderArtwork: Boolean = false,
): ConstraintSet {
    val artworksHeight = calculateArtworksHeightInCollapsedState()
    val statusBarHeight = calculateStatusBarHeightInDp()
    val pageIndicatorMargin = GamedgeTheme.spaces.spacing_2_5
    val backdropElevation = GamedgeTheme.spaces.spacing_1_0
    val coverSpaceMargin = CoverSpace
    val coverMarginStart = GamedgeTheme.spaces.spacing_3_5
    val likeBtnMarginEnd = GamedgeTheme.spaces.spacing_2_5
    val titleMarginStart = GamedgeTheme.spaces.spacing_3_5
    val firstTitleMarginStart = GamedgeTheme.spaces.spacing_7_5
    val firstTitleMarginEnd = GamedgeTheme.spaces.spacing_6_0
    val secondTitleMarginEnd = GamedgeTheme.spaces.spacing_3_5
    val releaseDateMarginTop = GamedgeTheme.spaces.spacing_2_5
    val releaseDateMarginHorizontal = GamedgeTheme.spaces.spacing_3_5
    val developerNameMarginHorizontal = GamedgeTheme.spaces.spacing_3_5
    val infoItemVerticalMargin = GamedgeTheme.spaces.spacing_3_5

    return ConstraintSet {
        val artworks = createRefFor(ConstraintIdArtworks)
        val artworksScrim = createRefFor(ConstraintIdArtworksScrim)
        val backButton = createRefFor(ConstraintIdBackButton)
        val pageIndicator = createRefFor(ConstraintIdPageIndicator)
        val backdrop = createRefFor(ConstraintIdBackdrop)
        val coverSpace = createRefFor(ConstraintIdCoverSpace)
        val cover = createRefFor(ConstraintIdCover)
        val likeButton = createRefFor(ConstraintIdLikeButton)
        val firstTitle = createRefFor(ConstraintIdFirstTitle)
        val secondTitle = createRefFor(ConstraintIdSecondTitle)
        val releaseDate = createRefFor(ConstraintIdReleaseDate)
        val developerName = createRefFor(ConstraintIdDeveloperName)
        val rating = createRefFor(ConstraintIdRating)
        val likeCount = createRefFor(ConstraintIdLikeCount)
        val ageRating = createRefFor(ConstraintIdAgeRating)
        val gameCategory = createRefFor(ConstraintIdGameCategory)
        val list = createRefFor(ConstraintIdList)

        constrain(artworks) {
            width = Dimension.fillToConstraints
            height = Dimension.value(artworksHeight)
            top.linkTo(parent.top)
            bottom.linkTo(backdrop.top)
            centerHorizontallyTo(parent)
        }
        constrain(artworksScrim) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            centerVerticallyTo(artworks)
            centerHorizontallyTo(artworks)
            visibility = if (hasDefaultPlaceholderArtwork) {
                Visibility.Gone
            } else {
                Visibility.Visible
            }
        }
        constrain(backButton) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }
        constrain(pageIndicator) {
            top.linkTo(parent.top, pageIndicatorMargin)
            end.linkTo(parent.end, pageIndicatorMargin)
            translationX = PageIndicatorDeltaXCollapsed
        }
        constrain(backdrop) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(artworks.bottom)
            bottom.linkTo(list.top)
            centerHorizontallyTo(parent)
            translationZ = backdropElevation
        }
        constrain(coverSpace) {
            start.linkTo(parent.start)
            bottom.linkTo(artworks.bottom, coverSpaceMargin)
        }
        constrain(cover) {
            top.linkTo(coverSpace.bottom)
            start.linkTo(parent.start, coverMarginStart)
            translationX = CoverDeltaXCollapsed
            translationY = CoverDeltaYCollapsed
        }
        constrain(likeButton) {
            top.linkTo(artworks.bottom)
            bottom.linkTo(artworks.bottom)
            end.linkTo(parent.end, likeBtnMarginEnd)
            alpha = 0f
            setScale(LikeButtonScaleCollapsed)
        }
        constrain(firstTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.top, statusBarHeight)
            bottom.linkTo(artworks.bottom)
            start.linkTo(backButton.end, firstTitleMarginStart)
            end.linkTo(parent.end, firstTitleMarginEnd)
            setScale(FirstTitleScaleCollapsed)
        }
        constrain(secondTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(firstTitle.bottom)
            start.linkTo(cover.end, titleMarginStart)
            end.linkTo(parent.end, secondTitleMarginEnd)
            alpha = 0f
            translationX = SecondaryTextDeltaXCollapsed
        }
        constrain(releaseDate) {
            width = Dimension.fillToConstraints
            top.linkTo(secondTitle.bottom, releaseDateMarginTop, releaseDateMarginTop)
            start.linkTo(cover.end, releaseDateMarginHorizontal)
            end.linkTo(parent.end, releaseDateMarginHorizontal)
            alpha = 0f
            translationX = SecondaryTextDeltaXCollapsed
        }
        constrain(developerName) {
            width = Dimension.fillToConstraints
            top.linkTo(releaseDate.bottom)
            start.linkTo(cover.end, developerNameMarginHorizontal)
            end.linkTo(parent.end, developerNameMarginHorizontal)
            alpha = 0f
            translationX = SecondaryTextDeltaXCollapsed
        }
        constrain(rating) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, infoItemVerticalMargin)
            bottom.linkTo(list.top, infoItemVerticalMargin)
            linkTo(start = parent.start, end = likeCount.start, bias = 0.25f)
        }
        constrain(likeCount) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, infoItemVerticalMargin)
            bottom.linkTo(list.top, infoItemVerticalMargin)
            linkTo(start = rating.end, end = ageRating.start, bias = 0.25f)
        }
        constrain(ageRating) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, infoItemVerticalMargin)
            bottom.linkTo(list.top, infoItemVerticalMargin)
            linkTo(start = likeCount.end, end = gameCategory.start, bias = 0.25f)
        }
        constrain(gameCategory) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, infoItemVerticalMargin)
            bottom.linkTo(list.top, infoItemVerticalMargin)
            linkTo(start = ageRating.end, end = parent.end, bias = 0.25f)
        }
        constrain(list) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(rating.bottom)
            bottom.linkTo(parent.bottom)
            centerHorizontallyTo(parent)
        }
    }
}

private var ConstrainScope.isVisible: Boolean
    set(isVisible) {
        visibility = if (isVisible) Visibility.Visible else Visibility.Gone
    }
    get() = visibility == Visibility.Visible

private fun ConstrainScope.setScale(scale: Float) {
    scaleX = scale
    scaleY = scale
}

@Language("json5")
@Composable
private fun constructTransition(): String {
    /*
        onSwipe: {
          direction: "up",
          touchUp: "decelerateComplete",
          anchor: "$ConstraintIdList",
          side: "top",
          mode: "velocity",
        },
    */

    return """
        {
          from: "start",
          to: "end",
          easing: "easeInOut",
          duration: 400,
          pathMotionArc: "none",
          KeyFrames: {
              KeyAttributes: [
                  {
                    target: ["$ConstraintIdSecondTitle"],
                    frames: [15, 100],
                    alpha: [0, 0],
                    translationX: ${SecondaryTextDeltaXCollapsed.value.toInt()},
                  },
                  {
                    target: ["$ConstraintIdReleaseDate"],
                    frames: [15, 100],
                    alpha: [0, 0],
                    translationX: ${SecondaryTextDeltaXCollapsed.value.toInt()},
                  },
                  {
                    target: ["$ConstraintIdDeveloperName"],
                    frames: [15, 100],
                    alpha: [0, 0],
                    translationX: ${SecondaryTextDeltaXCollapsed.value.toInt()},
                  },
                  {
                    target: ["$ConstraintIdCover"],
                    frames: [50],
                    alpha: 0,
                    translationX: ${CoverDeltaXCollapsed.value.toInt()},
                    translationY: ${CoverDeltaYCollapsed.value.toInt()},
                  },
                  {
                    target: ["$ConstraintIdLikeButton"],
                    frames: [60],
                    alpha: 0,
                    scaleX: 0,
                    scaleY: 0,
                  },
                  {
                    target: ["$ConstraintIdPageIndicator"],
                    frames: [80],
                    translationX: ${PageIndicatorDeltaXCollapsed.value.toInt()},
                  }
             ]
          }
        }
    """.trimIndent()
}

@Suppress("UnusedPrivateMember")
@Language("json5")
@Composable
private fun constructJson(): String {
    /*
        For it to work properly, the following things must be completed:
        1. Creating a bottom barrier of cover & developerName components.
        2. Custom properties (like scrim color & backdrop elevation) must
        referenced from the MotionLayout's content parameter.
        3. Scrim color should not be hardcoded in raw JSON.
        4. Scrim color should not be applied when default artwork image is used.
     */

    val statusBarHeight = calculateStatusBarHeightInDp().value.toInt()
    val density = LocalDensity.current

    val scrimColorExpanded = Integer.toHexString(Color.Transparent.toArgb())
    val pageIndicatorMargin = GamedgeTheme.spaces.spacing_2_5.value.toInt()
    val backdropElevationExpanded = GamedgeTheme.spaces.spacing_0_5.value.toInt()
    val coverSpaceMargin = CoverSpace.value.toInt()
    val coverMarginStart = GamedgeTheme.spaces.spacing_3_5.value.toInt()
    val likeBtnMarginEnd = GamedgeTheme.spaces.spacing_2_5.value.toInt()
    val titleMarginStart = GamedgeTheme.spaces.spacing_3_5.value.toInt()
    val firstTitleMarginTop = titleMarginStart
    val firstTitleMarginEnd = GamedgeTheme.spaces.spacing_1_0.value.toInt()
    val secondTitleMarginEnd = GamedgeTheme.spaces.spacing_3_5.value.toInt()
    val releaseDateMarginTop = GamedgeTheme.spaces.spacing_2_5.value.toInt()
    val releaseDateMarginHorizontal = GamedgeTheme.spaces.spacing_3_5.value.toInt()
    val developerNameMarginHorizontal = GamedgeTheme.spaces.spacing_3_5.value.toInt()
    val bottomBarrierMargin = GamedgeTheme.spaces.spacing_5_0.value.toInt()
    val infoItemMarginBottom = GamedgeTheme.spaces.spacing_3_5.value.toInt()

    val scrimColorCollapsed = Integer.toHexString(GamedgeTheme.colors.darkScrim.toArgb())
    val artworksHeightCollapsed = calculateArtworksHeightInCollapsedState().value.toInt()
    val backdropElevationCollapsed = GamedgeTheme.spaces.spacing_1_0.value.toInt()
    val firstTitleMarginStartCollapsed = GamedgeTheme.spaces.spacing_7_5.value.toInt()
    val firstTitleMarginEndCollapsed = GamedgeTheme.spaces.spacing_6_0.value.toInt()
    val infoItemVerticalMarginCollapsed = GamedgeTheme.spaces.spacing_3_5.value.toInt()
    val pageIndicatorDeltaXInPx = with(density) { PageIndicatorDeltaXCollapsed.roundToPx() }
    val coverDeltaXInPx = with(density) { CoverDeltaXCollapsed.roundToPx() }
    val coverDeltaYInPx = with(density) { CoverDeltaYCollapsed.roundToPx() }
    val secondaryTextDeltaXInPx = with(density) { SecondaryTextDeltaXCollapsed.roundToPx() }

    return """
        {
          ConstraintSets: {
            start: {
              $ConstraintIdArtworks: {
                width: "spread",
                height: ${ArtworksHeightExpanded.value.toInt()},
                top: ["parent", "top"],
                start: ["parent", "start"],
                end: ["parent", "end"],
              },
              $ConstraintIdArtworksScrim: {
                width: "spread",
                height: "spread",
                top: ["artworks", "top"],
                bottom: ["artworks", "bottom"],
                start: ["artworks", "start"],
                end: ["artworks", "end"],
                visibility: "invisible",
                custom: {
                  scrim_color: "#$scrimColorExpanded",
                },
              },
              $ConstraintIdBackButton: {
                top: ["parent", "top"],
                start: ["parent", "start"],
              },
              $ConstraintIdPageIndicator: {
                top: ["parent", "top", $pageIndicatorMargin],
                end: ["parent", "end", $pageIndicatorMargin],
              },
              $ConstraintIdBackdrop: {
                width: "spread",
                height: "spread",
                top: ["artworks", "bottom"],
                bottom: ["list", "top"],
                start: ["parent", "start"],
                end: ["parent", "end"],
                custom: {
                  elevation: $backdropElevationExpanded,
                },
              },
              $ConstraintIdCoverSpace: {
                start: ["parent", "start"],
                bottom: ["artworks", "bottom", $coverSpaceMargin],
              },
              $ConstraintIdCover: {
                top: ["cover_space", "bottom"],
                start: ["parent", "start", $coverMarginStart],
              },
              $ConstraintIdLikeButton: {
                top: ["artworks", "bottom"],
                bottom: ["artworks", "bottom"],
                end: ["parent", "end", $likeBtnMarginEnd],
              },
              $ConstraintIdFirstTitle: {
                width: "spread",
                top: ["artworks", "bottom", $firstTitleMarginTop],
                start: ["cover", "end", $titleMarginStart],
                end: ["like_button", "start", $firstTitleMarginEnd],
              },
              $ConstraintIdSecondTitle: {
                width: "spread",
                top: ["first_title", "bottom"],
                start: ["cover", "end", $titleMarginStart],
                end: ["parent", "end", $secondTitleMarginEnd],
              },
              $ConstraintIdReleaseDate: {
                width: "spread",
                top: ["second_title", "bottom", $releaseDateMarginTop],
                start: ["cover", "end", $releaseDateMarginHorizontal],
                end: ["parent", "end", $releaseDateMarginHorizontal],
              },
              $ConstraintIdDeveloperName: {
                width: "spread",
                top: ["release_date", "bottom"],
                start: ["cover", "end", $developerNameMarginHorizontal],
                end: ["parent", "end", $developerNameMarginHorizontal],
              },
              $ConstraintIdRating: {
                width: "spread",
                top: ["cover", "bottom", $bottomBarrierMargin],
                bottom: ["list", "top", $infoItemMarginBottom],
                start: ["parent", "start"],
                end: ["like_count", "start"],
                hBias: 0.25
              },
              $ConstraintIdLikeCount: {
                width: "spread",
                top: ["cover", "bottom", $bottomBarrierMargin],
                bottom: ["list", "top", $infoItemMarginBottom],
                start: ["rating", "end"],
                end: ["age_rating", "start"],
                hBias: 0.25
              },
              $ConstraintIdAgeRating: {
                width: "spread",
                top: ["cover", "bottom", $bottomBarrierMargin],
                bottom: ["list", "top", $infoItemMarginBottom],
                start: ["like_count", "end"],
                end: ["game_category", "start"],
                hBias: 0.25
              },
              $ConstraintIdGameCategory: {
                width: "spread",
                top: ["cover", "bottom", $bottomBarrierMargin],
                bottom: ["list", "top", $infoItemMarginBottom],
                start: ["age_rating", "end"],
                end: ["parent", "end"],
                hBias: 0.25
              },
              $ConstraintIdList: {
                width: "spread",
                height: "spread",
                top: ["rating", "bottom"],
                bottom: ["parent", "bottom"],
                start: ["parent", "start"],
                end: ["parent", "end"],
              },
            },
            end: {
              $ConstraintIdArtworks: {
                width: "spread",
                height: $artworksHeightCollapsed,
                top: ["parent", "top"],
                bottom: ["backdrop", "top"],
                start: ["parent", "start"],
                end: ["parent", "end"],
              },
              $ConstraintIdArtworksScrim: {
                width: "spread",
                height: "spread",
                top: ["artworks", "top"],
                bottom: ["artworks", "bottom"],
                start: ["artworks", "start"],
                end: ["artworks", "end"],
                visibility: "visible",
                custom: {
                  scrim_color: "#$scrimColorCollapsed",
                },
              },
              $ConstraintIdBackButton: {
                top: ["parent", "top"],
                start: ["parent", "start"],
              },
              $ConstraintIdPageIndicator: {
                top: ["parent", "top", $pageIndicatorMargin],
                end: ["parent", "end", $pageIndicatorMargin],
                translationX: $pageIndicatorDeltaXInPx,
              },
              $ConstraintIdBackdrop: {
                width: "spread",
                height: "spread",
                top: ["artworks", "bottom"],
                bottom: ["list", "top"],
                start: ["parent", "start"],
                end: ["parent", "end"],
                custom: {
                  elevation: $backdropElevationCollapsed,
                },
              },
              $ConstraintIdCoverSpace: {
                start: ["parent", "start"],
                bottom: ["artworks", "bottom", $coverSpaceMargin],
              },
              $ConstraintIdCover: {
                top: ["cover_space", "bottom"],
                start: ["parent", "start", $coverMarginStart],
                translationX: $coverDeltaXInPx,
                translationY: $coverDeltaYInPx,
                visibility: "invisible",
              },
              $ConstraintIdLikeButton: {
                top: ["artworks", "bottom"],
                bottom: ["artworks", "bottom"],
                end: ["parent", "end", $likeBtnMarginEnd],
                alpha: 0,
                scaleX: 0,
                scaleY: 0,
              },
              $ConstraintIdFirstTitle: {
                width: "spread",
                top: ["artworks", "top", $statusBarHeight],
                bottom: ["artworks", "bottom"],
                start: ["back_button", "end", $firstTitleMarginStartCollapsed],
                end: ["parent", "end", $firstTitleMarginEndCollapsed],
                scaleX: 1.1,
                scaleY: 1.1,
              },
              $ConstraintIdSecondTitle: {
                width: "spread",
                top: ["first_title", "bottom"],
                start: ["cover", "end", $titleMarginStart],
                end: ["parent", "end", $secondTitleMarginEnd],
                alpha: 0,
                translationX: $secondaryTextDeltaXInPx,
              },
              $ConstraintIdReleaseDate: {
                width: "spread",
                top: ["second_title", "bottom", $releaseDateMarginTop],
                start: ["cover", "end", $releaseDateMarginHorizontal],
                end: ["parent", "end", $releaseDateMarginHorizontal],
                alpha: 0,
                translationX: $secondaryTextDeltaXInPx,
              },
              $ConstraintIdDeveloperName: {
                width: "spread",
                top: ["release_date", "bottom"],
                start: ["cover", "end", $developerNameMarginHorizontal],
                end: ["parent", "end", $developerNameMarginHorizontal],
                alpha: 0,
                translationX: $secondaryTextDeltaXInPx,
              },
              $ConstraintIdRating: {
                width: "spread",
                top: ["artworks", "bottom", $infoItemVerticalMarginCollapsed],
                bottom: ["list", "top", $infoItemVerticalMarginCollapsed],
                start: ["parent", "start"],
                end: ["like_count", "start"],
                hBias: 0.25
              },
              $ConstraintIdLikeCount: {
                width: "spread",
                top: ["artworks", "bottom", $infoItemVerticalMarginCollapsed],
                bottom: ["list", "top", $infoItemVerticalMarginCollapsed],
                start: ["rating", "end"],
                end: ["age_rating", "start"],
                hBias: 0.25
              },
              $ConstraintIdAgeRating: {
                width: "spread",
                top: ["artworks", "bottom", $infoItemVerticalMarginCollapsed],
                bottom: ["list", "top", $infoItemVerticalMarginCollapsed],
                start: ["like_count", "end"],
                end: ["game_category", "start"],
                hBias: 0.25
              },
              $ConstraintIdGameCategory: {
                width: "spread",
                top: ["artworks", "bottom", $infoItemVerticalMarginCollapsed],
                bottom: ["list", "top", $infoItemVerticalMarginCollapsed],
                start: ["age_rating", "end"],
                end: ["parent", "end"],
                hBias: 0.25
              },
              $ConstraintIdList: {
                width: "spread",
                height: "spread",
                top: ["rating", "bottom"],
                bottom: ["parent", "bottom"],
                start: ["parent", "start"],
                end: ["parent", "end"],
              },
            }
          },
          Transitions: {
            default: {
              from: "start",
              to: "end",
              easing: "easeInOut",
              duration: 400,
              pathMotionArc: "none",
              KeyFrames: {
                KeyAttributes: [
                  {
                    target: ["$ConstraintIdSecondTitle"],
                    frames: [15, 100],
                    alpha: [0, 0],
                    translationX: ${SecondaryTextDeltaXCollapsed.value.toInt()},
                  },
                  {
                    target: ["$ConstraintIdReleaseDate"],
                    frames: [15, 100],
                    alpha: [0, 0],
                    translationX: ${SecondaryTextDeltaXCollapsed.value.toInt()},
                  },
                  {
                    target: ["$ConstraintIdDeveloperName"],
                    frames: [15, 100],
                    alpha: [0, 0],
                    translationX: ${SecondaryTextDeltaXCollapsed.value.toInt()},
                  },
                  {
                    target: ["$ConstraintIdCover"],
                    frames: [50],
                    alpha: 0,
                    translationX: ${CoverDeltaXCollapsed.value.toInt()},
                    translationY: ${CoverDeltaYCollapsed.value.toInt()},
                  },
                  {
                    target: ["$ConstraintIdLikeButton"],
                    frames: [60],
                    alpha: 0,
                    scaleX: 0,
                    scaleY: 0,
                  },
                  {
                    target: ["$ConstraintIdPageIndicator"],
                    frames: [80],
                    translationX: ${PageIndicatorDeltaXCollapsed.value.toInt()},
                  }
                ]
              }
            }
          }
        }
    """.trimIndent()
}

@Composable
private fun calculateArtworksHeightInCollapsedState(): Dp {
    return ArtworksHeightCollapsed + calculateStatusBarHeightInDp()
}

@Composable
private fun calculateStatusBarHeightInDp(): Dp {
    val statusBarHeight = LocalContext.current.statusBarHeight

    return with(LocalDensity.current) { statusBarHeight.toDp() }
}

private fun Modifier.drawOnTop(): Modifier {
    return zIndex(Float.MAX_VALUE)
}
