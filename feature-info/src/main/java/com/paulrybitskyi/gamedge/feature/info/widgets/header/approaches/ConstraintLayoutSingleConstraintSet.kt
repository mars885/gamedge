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

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.insets.statusBarsPadding
import com.paulrybitskyi.gamedge.commons.ui.clickable
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
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
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_PAGE_INDICATOR
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_RATING
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_RELEASE_DATE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.CONSTRAINT_ID_SECOND_TITLE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.COVER_SPACE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeaderModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.INFO_ICON_SIZE
import com.paulrybitskyi.gamedge.feature.info.widgets.header.State
import com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks.Artworks
import com.paulrybitskyi.gamedge.feature.info.widgets.header.constructCollapsedConstraintSet
import com.paulrybitskyi.gamedge.feature.info.widgets.header.constructExpandedConstraintSet

@Composable
internal fun ConstraintLayoutSingleConstraintSet(
    headerInfo: GameInfoHeaderModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
) {
    val artworks = headerInfo.artworks
    val isPageIndicatorVisible by remember(artworks) { mutableStateOf(artworks.size > 1) }
    var selectedArtworkPage by remember { mutableStateOf(0) }
    var secondTitleText by rememberSaveable { mutableStateOf("") }
    val isSecondTitleVisible by remember {
        derivedStateOf {
            secondTitleText.isNotEmpty()
        }
    }
    var state by remember { mutableStateOf(State.EXPANDED) }

    ConstraintLayout(
        constraintSet = if (state == State.EXPANDED) {
            constructExpandedConstraintSet()
        } else {
            constructCollapsedConstraintSet()
        },
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
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_RATING)
                .clickable {
                    state = if (state == State.EXPANDED) State.COLLAPSED else State.EXPANDED
                },
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
