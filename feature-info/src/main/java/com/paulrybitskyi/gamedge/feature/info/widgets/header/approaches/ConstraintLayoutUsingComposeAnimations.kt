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

package com.paulrybitskyi.gamedge.feature.info.widgets.header.approaches

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.insets.statusBarsPadding
import com.paulrybitskyi.commons.ktx.statusBarHeight
import com.paulrybitskyi.gamedge.commons.ui.clickable
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.theme.darkScrim
import com.paulrybitskyi.gamedge.commons.ui.theme.lightScrim
import com.paulrybitskyi.gamedge.commons.ui.theme.subtitle3
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.header.COVER_SPACE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeaderModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.INFO_ICON_SIZE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.State
import com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks.Artworks
import kotlin.math.roundToInt

private const val ANIM_DURATION = 3000

@SuppressLint("Range")
@Composable
internal fun ConstraintLayoutUsingComposeAnimations(
    headerInfo: GameInfoHeaderModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
) {
    val artworkModels = headerInfo.artworks
    val isPageIndicatorVisible by remember(artworkModels) { mutableStateOf(artworkModels.size > 1) }
    var selectedArtworkPage by remember { mutableStateOf(0) }
    var secondTitleText by rememberSaveable { mutableStateOf("") }
    val isSecondTitleVisible by remember {
        derivedStateOf {
            secondTitleText.isNotEmpty()
        }
    }

    val coverHeight = 153.dp
    var state by remember { mutableStateOf(State.EXPANDED) }
    val transition = updateTransition(state, label = "game_header")
    val artworksHeight by transition.animateDp(
        label = "artworksHeight",
        transitionSpec = { getAnimSpec() },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 240.dp
            State.COLLAPSED -> calculateArtworksCollapsedHeight()
        }
    }
    val scrimColor by transition.animateColor(
        label = "scrimColor",
        transitionSpec = { getAnimSpec() },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> Color.Transparent
            State.COLLAPSED -> GamedgeTheme.colors.darkScrim
        }
    }
    val pageIndicatorTranslationX by transition.animateDp(
        label = "pageIndicatorTranslationX",
        transitionSpec = {
            keyframes {
                durationMillis = ANIM_DURATION
                60.dp at ((ANIM_DURATION * 0.8f).roundToInt())
            }
        },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 0.dp
            State.COLLAPSED -> 60.dp
        }
    }
    val backdropElevation by transition.animateDp(
        label = "backdropElevation",
        transitionSpec = { getAnimSpec() },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 2.dp
            State.COLLAPSED -> 4.dp
        }
    }
    val coverTranslationY by transition.animateDp(
        label = "coverTranslationY",
        transitionSpec = { getAnimSpec() },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 0.dp
            State.COLLAPSED -> (-60).dp
        }
    }
    val coverAlpha by transition.animateFloat(
        label = "coverAlpha",
        transitionSpec = { getAnimSpec() },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 1f
            State.COLLAPSED -> 0f
        }
    }
    val likeButtonPropertiesValue by transition.animateFloat(
        label = "likeButtonPropertiesValue",
        transitionSpec = {
            keyframes {
                durationMillis = ANIM_DURATION
                0f at ((ANIM_DURATION * 0.6f).roundToInt())
            }
        },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 1f
            State.COLLAPSED -> 0f
        }
    }
    val mainTextAlpha by transition.animateFloat(
        label = "mainTextAlpha",
        transitionSpec = { getAnimSpec() },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 1f
            State.COLLAPSED -> 0.5f
        }
    }
    val mainTextTranslationX by transition.animateDp(
        label = "mainTextTranslationX",
        transitionSpec = { getAnimSpec() },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 0.dp
            State.COLLAPSED -> (-8).dp
        }
    }
    val bottomBarrierMarginAnim by transition.animateDp(
        label = "bottomBarrierMarginAnim",
        transitionSpec = { getAnimSpec() },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 20.dp
            State.COLLAPSED -> -(coverHeight - COVER_SPACE)
        }
    }
    val coverTranslationX by transition.animateDp(
        label = "coverTranslationX",
        transitionSpec = {
            keyframes {
                durationMillis = ANIM_DURATION
                (-130).dp at ((ANIM_DURATION * 0.2f).roundToInt())
            }
        },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> 0.dp
            State.COLLAPSED -> (-130).dp
        }
    }
    val coverSpaceMarginAnim by transition.animateDp(
        label = "coverSpaceMargin",
        transitionSpec = {
            keyframes {
                durationMillis = ANIM_DURATION
                coverHeight at ((ANIM_DURATION * 0.6f).roundToInt())
            }
        },
    ) { currentState ->
        when (currentState) {
            State.EXPANDED -> COVER_SPACE
            State.COLLAPSED -> coverHeight
        }
    }

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


    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val artworks = createRef()
        val artworksScrim = createRef()
        val backButton = createRef()
        val pageIndicator = createRef()
        val backdrop = createRef()
        val coverSpace = createRef()
        val cover = createRef()
        val likeButton = createRef()
        val firstTitle = createRef()
        val secondTitle = createRef()
        val releaseDate = createRef()
        val developerName = createRef()
        val bottomBarrier = createBottomBarrier(cover, developerName, margin = bottomBarrierMarginAnim)
        val rating = createRef()
        val animContent = createRef()
        val likeCount = createRef()
        val ageRating = createRef()
        val gameCategory = createRef()

        Artworks(
            artworks = artworkModels,
            isScrollingEnabled = true,
            modifier = Modifier.constrainAs(artworks) {
                width = Dimension.fillToConstraints
                height = Dimension.value(artworksHeight)
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
            },
            onArtworkChanged = { page ->
                selectedArtworkPage = page
            },
            onArtworkClicked = onArtworkClicked,
        )

        Box(
            Modifier
                .constrainAs(artworksScrim) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    centerVerticallyTo(artworks)
                    centerHorizontallyTo(artworks)
                }
                .background(scrimColor),
        )

        Icon(
            painter = painterResource(R.drawable.arrow_left),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(backButton) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
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
                    .constrainAs(pageIndicator) {
                        top.linkTo(parent.top, pageIndicatorMargin)
                        end.linkTo(parent.end, pageIndicatorMargin)
                        translationX = pageIndicatorTranslationX
                    }
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

/*        transition.AnimatedVisibility(
            visible = { currentState -> currentState == State.EXPANDED },
            modifier = Modifier.constrainAs(backdrop) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                top.linkTo(artworks.bottom)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
            },
            enter = EnterTransition.None,
            exit = ExitTransition.None,
        ) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = backdropElevation,
                        shape = RectangleShape,
                        clip = false,
                    )
                    .background(
                        color = GamedgeTheme.colors.surface,
                        shape = RectangleShape,
                    )
                    .clip(RectangleShape),
            )
        }*/

        Box(
            modifier = Modifier
                .constrainAs(backdrop) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    top.linkTo(artworks.bottom)
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(parent)
                }
                .shadow(
                    elevation = backdropElevation,
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
                .constrainAs(coverSpace) {
                    start.linkTo(parent.start)
                    bottom.linkTo(artworks.bottom, coverSpaceMarginAnim)
                }
        )

        GameCover(
            title = null,
            imageUrl = headerInfo.coverImageUrl,
            modifier = Modifier
                .constrainAs(cover) {
                    top.linkTo(coverSpace.bottom)
                    start.linkTo(parent.start, coverMarginStart)
                }
                .offset { IntOffset(coverTranslationX.roundToPx(), 0) }
                .alpha(coverAlpha),
            onCoverClicked = onCoverClicked,
        )

        FloatingActionButton(
            onClick = onLikeButtonClicked,
            modifier = Modifier.constrainAs(likeButton) {
                top.linkTo(artworks.bottom)
                bottom.linkTo(artworks.bottom)
                end.linkTo(parent.end, likeBtnMarginEnd)
                alpha = likeButtonPropertiesValue
                scaleX = likeButtonPropertiesValue
                scaleY = likeButtonPropertiesValue
            },
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
            modifier = Modifier.constrainAs(firstTitle) {
                width = Dimension.fillToConstraints
                top.linkTo(artworks.bottom, firstTitleMarginTop)
                start.linkTo(cover.end, titleMarginStart)
                end.linkTo(likeButton.start, firstTitleMarginEnd)
                alpha = mainTextAlpha
                translationX = mainTextTranslationX
            },
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

        Box(
            Modifier.constrainAs(secondTitle) {
                width = Dimension.fillToConstraints
                top.linkTo(firstTitle.bottom)
                start.linkTo(cover.end, titleMarginStart)
                end.linkTo(parent.end, secondTitleMarginEnd)
                alpha = mainTextAlpha
                translationX = mainTextTranslationX
            }
        ) {
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
            modifier = Modifier.constrainAs(releaseDate) {
                width = Dimension.fillToConstraints
                top.linkTo(secondTitle.bottom, releaseDateMarginTop)
                start.linkTo(cover.end, releaseDateMarginHorizontal)
                end.linkTo(parent.end, releaseDateMarginHorizontal)
                alpha = mainTextAlpha
                translationX = mainTextTranslationX
            },
            color = GamedgeTheme.colors.onSurface,
            style = GamedgeTheme.typography.subtitle3,
        )

        Box(
            Modifier.constrainAs(developerName) {
                width = Dimension.fillToConstraints
                top.linkTo(releaseDate.bottom)
                start.linkTo(cover.end, developerNameMarginHorizontal)
                end.linkTo(parent.end, developerNameMarginHorizontal)
                alpha = mainTextAlpha
                translationX = mainTextTranslationX
            }
        ) {
            if (headerInfo.hasDeveloperName) {
                Text(
                    text = checkNotNull(headerInfo.developerName),
                    color = GamedgeTheme.colors.onSurface,
                    style = GamedgeTheme.typography.subtitle3,
                )
            }
        }

/*        transition.AnimatedContent(
            modifier = Modifier.constrainAs(animContent) {
                if (state == State.EXPANDED) {
                    top.linkTo(bottomBarrier)
                } else {
                    top.linkTo(artworks.bottom)
                }
                bottom.linkTo(parent.bottom, infoItemMarginBottom)
                centerHorizontallyTo(parent)
            },
            transitionSpec = {
                fadeIn(getAnimSpec()) with fadeOut(getAnimSpec())
            },
        ) { targetState ->
            if (targetState == State.EXPANDED) {
                Text(
                    text = "Text1",
                    color = GamedgeTheme.colors.onSurface,
                    style = GamedgeTheme.typography.subtitle3,
                )
            } else {
                Text(
                    text = "Text2",
                    color = GamedgeTheme.colors.onSurface,
                    style = GamedgeTheme.typography.subtitle3,
                )
            }
        }*/

        Info(
            icon = painterResource(R.drawable.star_circle_outline),
            title = headerInfo.rating,
            modifier = Modifier
                .constrainAs(rating) {
                    width = Dimension.fillToConstraints
                    top.linkTo(bottomBarrier)
                    bottom.linkTo(parent.bottom, infoItemMarginBottom)
                    linkTo(start = parent.start, end = likeCount.start, bias = 0.25f)
                }
                .clickable {
                    state = if (state == State.EXPANDED) State.COLLAPSED else State.EXPANDED
                },
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.account_heart_outline),
            title = headerInfo.likeCount,
            modifier = Modifier.constrainAs(likeCount) {
                width = Dimension.fillToConstraints
                top.linkTo(bottomBarrier)
                bottom.linkTo(parent.bottom, infoItemMarginBottom)
                linkTo(start = rating.end, end = ageRating.start, bias = 0.25f)
            },
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.age_rating_outline),
            title = headerInfo.ageRating,
            modifier = Modifier.constrainAs(ageRating) {
                width = Dimension.fillToConstraints
                top.linkTo(bottomBarrier)
                bottom.linkTo(parent.bottom, infoItemMarginBottom)
                linkTo(start = likeCount.end, end = gameCategory.start, bias = 0.25f)
            },
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.shape_outline),
            title = headerInfo.gameCategory,
            modifier = Modifier.constrainAs(gameCategory) {
                width = Dimension.fillToConstraints
                top.linkTo(bottomBarrier)
                bottom.linkTo(parent.bottom, infoItemMarginBottom)
                linkTo(start = ageRating.end, end = parent.end, bias = 0.25f)
            },
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
    }
}

@Stable
private fun <T> getAnimSpec(): TweenSpec<T> {
    return tween(ANIM_DURATION)
}

@Composable
private fun calculateArtworksCollapsedHeight(): Dp {
    val density = LocalDensity.current
    val context = LocalContext.current
    val statusBarHeight = context.statusBarHeight
    val statusBarHeightInDp = with(density) { statusBarHeight.toDp() }
    val artworksHeight = 56.dp + statusBarHeightInDp

    return artworksHeight
}
