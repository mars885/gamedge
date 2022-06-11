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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.Transition
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
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_AGE_RATING
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_ARTWORKS
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_ARTWORKS_SCRIM
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_BACKDROP
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_BACK_BUTTON
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_COVER
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_COVER_SPACE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_DEVELOPER_NAME
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_FIRST_TITLE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_GAME_CATEGORY
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_LIKE_BUTTON
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_LIKE_COUNT
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_LIST
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_PAGE_INDICATOR
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_RATING
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_RELEASE_DATE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_SECOND_TITLE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeaderModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.INFO_ICON_SIZE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.State
import com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks.Artworks
import com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks.GameInfoArtworkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.constructCollapsedConstraintSet
import com.paulrybitskyi.gamedge.feature.info.widgets.header.constructExpandedConstraintSet
import org.intellij.lang.annotations.Language

@OptIn(ExperimentalMotionApi::class)
@Composable
internal fun MotionLayoutBasedOnJsonTransitionOnly(
    headerInfo: GameInfoHeaderModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    content: @Composable (Modifier) -> Unit = {},
) {
    val artworks = headerInfo.artworks
    val isPageIndicatorVisible = remember(artworks) { artworks.size > 1 }
    val hasDefaultPlaceholderArtwork = remember(artworks) {
        artworks.size == 1 &&
                artworks.single() is GameInfoArtworkModel.DefaultImage
    }
    var selectedArtworkPage by remember { mutableStateOf(0) }
    var secondTitleText by rememberSaveable { mutableStateOf("") }
    val isSecondTitleVisible by remember {
        derivedStateOf {
            secondTitleText.isNotEmpty()
        }
    }
    var state by remember { mutableStateOf(State.EXPANDED) }
    val progress by animateFloatAsState(
        targetValue = if (state == State.EXPANDED) 0f else 1f,
        animationSpec = tween(3000),
    )
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
        //motionScene = MotionScene(constructJson()),
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
            modifier = Modifier.layoutId(CONSTRAINT_ID_ARTWORKS),
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
            Modifier
                .layoutId(CONSTRAINT_ID_ARTWORKS_SCRIM)
                .background(GamedgeTheme.colors.darkScrim),
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
                .background(
                    color = GamedgeTheme.colors.surface,
                    shape = RectangleShape,
                )
                .clip(RectangleShape),
        )

        Spacer(Modifier.layoutId(CONSTRAINT_ID_COVER_SPACE))

        GameCover(
            title = null,
            imageUrl = headerInfo.coverImageUrl,
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_COVER)
                .zIndex(Float.MAX_VALUE),
            onCoverClicked = onCoverClicked,
        )

        FloatingActionButton(
            onClick = onLikeButtonClicked,
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_LIKE_BUTTON)
                .zIndex(Float.MAX_VALUE),
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
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_FIRST_TITLE)
                .zIndex(Float.MAX_VALUE), // Modifier.drawOnTop(),
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
            Modifier
                .layoutId(CONSTRAINT_ID_SECOND_TITLE)
                .zIndex(Float.MAX_VALUE),
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
                .layoutId(CONSTRAINT_ID_RELEASE_DATE)
                .zIndex(Float.MAX_VALUE),
            color = GamedgeTheme.colors.onSurface,
            style = GamedgeTheme.typography.subtitle3,
        )

        Box(
            Modifier
                .layoutId(CONSTRAINT_ID_DEVELOPER_NAME)
                .zIndex(Float.MAX_VALUE),
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
            icon = painterResource(R.drawable.star_circle_outline),
            title = headerInfo.rating,
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_RATING)
                .zIndex(Float.MAX_VALUE)
                .clickable {
                    state = if (state == State.EXPANDED) State.COLLAPSED else State.EXPANDED
                },
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.account_heart_outline),
            title = headerInfo.likeCount,
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_LIKE_COUNT)
                .zIndex(Float.MAX_VALUE),
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.age_rating_outline),
            title = headerInfo.ageRating,
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_AGE_RATING)
                .zIndex(Float.MAX_VALUE),
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )
        Info(
            icon = painterResource(R.drawable.shape_outline),
            title = headerInfo.gameCategory,
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_GAME_CATEGORY)
                .zIndex(Float.MAX_VALUE),
            iconSize = INFO_ICON_SIZE,
            titleTextStyle = GamedgeTheme.typography.caption,
        )

        content(Modifier.layoutId(CONSTRAINT_ID_LIST))
    }
}

@Language("json5")
@Composable
private fun constructJson(): String {
    val density = LocalDensity.current
    val artworksCollapsedHeightInDp = calculateArtworksCollapsedHeight().value.toInt()
    val statusBarHeight = calculateStatusBarHeightInDp().value.toInt()
    val pageIndicatorTranslationXInPx = with(density) { 60.dp.roundToPx() }
    val coverTranslationXInPx = with(density) { (-130).dp.roundToPx() }
    val coverTranslationYInPx = with(density) { (-60).dp.roundToPx() }
    val secondaryTextTranslationXInPx = with(density) { (-8).dp.roundToPx() }
    val scrimColorCollapsed = Integer.toHexString(GamedgeTheme.colors.darkScrim.toArgb())
    val backdropExpandedElevation = GamedgeTheme.spaces.spacing_0_5.value.toInt()
    val backdropCollapsedElevation = GamedgeTheme.spaces.spacing_1_0.value.toInt()

    return """
        {
          ConstraintSets: {
            start: {
              artworks: {
                width: "spread",
                height: 240,
                top: ["parent", "top"],
                start: ["parent", "start"],
                end: ["parent", "end"],
              },
              artworks_scrim: {
                width: "spread",
                height: "spread",
                top: ["artworks", "top"],
                start: ["artworks", "start"],
                end: ["artworks", "end"],
                bottom: ["artworks", "bottom"],
                visibility: "invisible",
                custom: {
                  scrim_color: "#00000000",
                },
              },
              back_button: {
                top: ["parent", "top"],
                start: ["parent", "start"],
              },
              page_indicator: {
                top: ["parent", "top", 10],
                end: ["parent", "end", 10],
              },
              backdrop: {
                width: "spread",
                height: "spread",
                top: ["artworks", "bottom"],
                bottom: ["list", "top"],
                start: ["parent", "start"],
                end: ["parent", "end"],
                custom: {
                  elevation: $backdropExpandedElevation,
                },
              },
              cover_space: {
                start: ["parent", "start"],
                bottom: ["artworks", "bottom", 40],
              },
              cover: {
                top: ["cover_space", "bottom"],
                start: ["parent", "start", 14],
              },
              like_button: {
                top: ["artworks", "bottom"],
                bottom: ["artworks", "bottom"],
                end: ["parent", "end", 10],
              },
              first_title: {
                width: "spread",
                top: ["artworks", "bottom", 14],
                start: ["cover", "end", 14],
                end: ["like_button", "start", 4],
              },
              second_title: {
                width: "spread",
                top: ["first_title", "bottom"],
                start: ["cover", "end", 14],
                end: ["parent", "end", 14],
              },
              release_date: {
                width: "spread",
                top: ["second_title", "bottom", 10],
                start: ["cover", "end", 14],
                end: ["parent", "end", 14],
              },
              developer_name: {
                width: "spread",
                top: ["release_date", "bottom"],
                start: ["cover", "end", 14],
                end: ["parent", "end", 14],
              },
              rating: {
                width: "spread",
                top: ["cover", "bottom", 20],
                start: ["parent", "start"],
                end: ["like_count", "start"],
                bottom: ["list", "top", 14],
                hBias: 0.25
              },
              like_count: {
                width: "spread",
                top: ["cover", "bottom", 20],
                start: ["rating", "end"],
                end: ["age_rating", "start"],
                bottom: ["list", "top", 14],
                hBias: 0.25
              },
              age_rating: {
                width: "spread",
                top: ["cover", "bottom", 20],
                start: ["like_count", "end"],
                end: ["game_category", "start"],
                bottom: ["list", "top", 14],
                hBias: 0.25
              },
              game_category: {
                width: "spread",
                top: ["cover", "bottom", 20],
                start: ["age_rating", "end"],
                end: ["parent", "end"],
                bottom: ["list", "top", 14],
                hBias: 0.25
              },
              list: {
                width: "spread",
                height: "spread",
                top: ["rating", "bottom"],
                start: ["parent", "start"],
                end: ["parent", "end"],
                bottom: ["parent", "bottom"],
              },
            },
            end: {
              artworks: {
                width: "spread",
                height: $artworksCollapsedHeightInDp,
                top: ["parent", "top"],
                bottom: ["backdrop", "top"],
                start: ["parent", "start"],
                end: ["parent", "end"],
              },
              artworks_scrim: {
                width: "spread",
                height: "spread",
                top: ["artworks", "top"],
                start: ["artworks", "start"],
                end: ["artworks", "end"],
                bottom: ["artworks", "bottom"],
                visibility: "visible",
                custom: {
                  scrim_color: "#$scrimColorCollapsed",
                },
              },
              back_button: {
                top: ["parent", "top"],
                start: ["parent", "start"],
              },
              page_indicator: {
                top: ["parent", "top", 10],
                end: ["parent", "end", 10],
                translationX: $pageIndicatorTranslationXInPx,
              },
              backdrop: {
                width: "spread",
                height: "spread",
                top: ["artworks", "bottom"],
                bottom: ["list", "top"],
                start: ["parent", "start"],
                end: ["parent", "end"],
                custom: {
                  elevation: $backdropCollapsedElevation,
                },
              },
              cover_space: {
                start: ["parent", "start"],
                bottom: ["artworks", "bottom", 80],
              },
              cover: {
                top: ["cover_space", "bottom"],
                start: ["parent", "start", 14],
                translationX: $coverTranslationXInPx,
                translationY: $coverTranslationYInPx,
                visibility: "invisible",
              },
              like_button: {
                top: ["artworks", "bottom"],
                bottom: ["artworks", "bottom"],
                end: ["parent", "end", 10],
                alpha: 0,
                scaleX: 0,
                scaleY: 0,
              },
              first_title: {
                width: "spread",
                top: ["artworks", "top", $statusBarHeight],
                bottom: ["artworks", "bottom"],
                start: ["back_button", "end", 30],
                end: ["parent", "end", 25],
                scaleX: 1.1,
                scaleY: 1.1,
              },
              second_title: {
                width: "spread",
                top: ["first_title", "bottom"],
                start: ["cover", "end", 14],
                end: ["parent", "end", 14],
                alpha: 0,
                translationX: $secondaryTextTranslationXInPx,
              },
              release_date: {
                width: "spread",
                top: ["second_title", "bottom", 10],
                start: ["cover", "end", 14],
                end: ["parent", "end", 14],
                visibility: "invisible",
                translationX: $secondaryTextTranslationXInPx,
              },
              developer_name: {
                width: "spread",
                top: ["release_date", "bottom"],
                start: ["cover", "end", 14],
                end: ["parent", "end", 14],
                visibility: "invisible",
                translationX: $secondaryTextTranslationXInPx,
              },
              rating: {
                width: "spread",
                top: ["artworks", "bottom", 15],
                bottom: ["list", "top", 15],
                start: ["parent", "start"],
                end: ["like_count", "start"],
                hBias: 0.25
              },
              like_count: {
                width: "spread",
                top: ["artworks", "bottom", 15],
                bottom: ["list", "top", 15],
                start: ["rating", "end"],
                end: ["age_rating", "start"],
                hBias: 0.25
              },
              age_rating: {
                width: "spread",
                top: ["artworks", "bottom", 15],
                bottom: ["list", "top", 15],
                start: ["like_count", "end"],
                end: ["game_category", "start"],
                hBias: 0.25
              },
              game_category: {
                width: "spread",
                top: ["artworks", "bottom", 15],
                bottom: ["list", "top", 15],
                start: ["age_rating", "end"],
                end: ["parent", "end"],
                hBias: 0.25
              },
                list: {
                  width: "spread",
                  height: "spread",
                  top: ["rating", "bottom"],
                  start: ["parent", "start"],
                  end: ["parent", "end"],
                  bottom: ["parent", "bottom"],
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
                    target: ["second_title"],
                    frames: [15, 100],
                    alpha: [0, 0],
                    translationX: $secondaryTextTranslationXInPx,
                  },
                    {
                      target: ["release_date"],
                      frames: [15, 100],
                      alpha: [0, 0],
                      translationX: $secondaryTextTranslationXInPx,
                    },
                    {
                      target: ["developer_name"],
                      frames: [15, 100],
                      alpha: [0, 0],
                      translationX: $secondaryTextTranslationXInPx,
                    },
                  {
                    target: ["cover"],
                    frames: [60],
                    alpha: 0,
                    translationX: $coverTranslationXInPx,
                    translationY: $coverTranslationYInPx,
                  },
                  {
                    target: ["like_button"],
                    frames: [60],
                    alpha: 0,
                    scaleX: 0,
                    scaleY: 0,
                  },
                  {
                    target: ["page_indicator"],
                    frames: [80],
                    translationX: $pageIndicatorTranslationXInPx,
                  }
                ]
              }
            }
          }
        }
    """.trimIndent()
}

@Language("json5")
@Composable
private fun constructTransition(): String {
    val density = LocalDensity.current
    val pageIndicatorTranslationXInPx = with(density) { 60.dp.roundToPx() }
    val coverTranslationXInPx = with(density) { (-130).dp.roundToPx() }
    val coverTranslationYInPx = with(density) { (-60).dp.roundToPx() }
    val secondaryTextTranslationXInPx = with(density) { (-8).dp.roundToPx() }

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
                    target: ["second_title"],
                    frames: [15, 100],
                    alpha: [0, 0],
                    translationX: $secondaryTextTranslationXInPx,
                  },
                    {
                      target: ["release_date"],
                      frames: [15, 100],
                      alpha: [0, 0],
                      translationX: $secondaryTextTranslationXInPx,
                    },
                    {
                      target: ["developer_name"],
                      frames: [15, 100],
                      alpha: [0, 0],
                      translationX: $secondaryTextTranslationXInPx,
                    },
                  {
                    target: ["cover"],
                    frames: [60],
                    alpha: 0,
                    translationX: $coverTranslationXInPx,
                    translationY: $coverTranslationYInPx,
                  },
                  {
                    target: ["like_button"],
                    frames: [60],
                    alpha: 0,
                    scaleX: 0,
                    scaleY: 0,
                  },
                  {
                    target: ["page_indicator"],
                    frames: [80],
                    translationX: $pageIndicatorTranslationXInPx,
                  }
                ]
              }
        }
    """.trimIndent()
}

@Composable
private fun calculateArtworksCollapsedHeight(): Dp {
    val statusBarHeightInDp = calculateStatusBarHeightInDp()
    val artworksHeight = 56.dp + statusBarHeightInDp

    return artworksHeight
}

@Composable
private fun calculateStatusBarHeightInDp(): Dp {
    val density = LocalDensity.current
    val context = LocalContext.current
    val statusBarHeight = context.statusBarHeight

    return with(density) { statusBarHeight.toDp() }
}
