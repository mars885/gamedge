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

package com.paulrybitskyi.gamedge.feature.info.widgets.header

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.paulrybitskyi.commons.ktx.statusBarHeight
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme

@SuppressLint("Range")
@Composable
internal fun constructCollapsedConstraintSet(
    hasDefaultPlaceholderArtwork: Boolean = false,
): ConstraintSet {
    val density = LocalDensity.current
    val context = LocalContext.current
    val statusBarHeight = context.statusBarHeight
    val statusBarHeightInDp = with(density) { statusBarHeight.toDp() }
    val artworksHeight = 56.dp + statusBarHeightInDp
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
        val rating = createRefFor(CONSTRAINT_ID_RATING)
        val likeCount = createRefFor(CONSTRAINT_ID_LIKE_COUNT)
        val ageRating = createRefFor(CONSTRAINT_ID_AGE_RATING)
        val gameCategory = createRefFor(CONSTRAINT_ID_GAME_CATEGORY)
        val list = createRefFor(CONSTRAINT_ID_LIST)

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
            translationX = 60.dp
        }
        constrain(backdrop) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(artworks.bottom)
            bottom.linkTo(list.top)
            centerHorizontallyTo(parent)
            translationZ = BACKDROP_ELEVATION_COLLAPSED
        }
        constrain(coverSpace) {
            start.linkTo(parent.start)
            bottom.linkTo(artworks.bottom, coverSpaceMargin * 2)
        }
        constrain(cover) {
            top.linkTo(coverSpace.bottom)
            start.linkTo(parent.start, coverMarginStart)
            translationX = (-130).dp
            translationY = (-60).dp
        }
        constrain(likeButton) {
            top.linkTo(artworks.bottom)
            bottom.linkTo(artworks.bottom)
            end.linkTo(parent.end, likeBtnMarginEnd)
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
        }
        constrain(firstTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.top, statusBarHeightInDp)
            bottom.linkTo(artworks.bottom)
            start.linkTo(backButton.end, 30.dp)
            end.linkTo(parent.end, 25.dp)
            scaleX = 1.1f
            scaleY = 1.1f
        }
        constrain(secondTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(firstTitle.bottom)
            start.linkTo(cover.end, titleMarginStart)
            end.linkTo(parent.end, secondTitleMarginEnd)
            translationX = (-8).dp
            alpha = 0f
        }
        constrain(releaseDate) {
            width = Dimension.fillToConstraints
            top.linkTo(secondTitle.bottom, releaseDateMarginTop)
            start.linkTo(cover.end, releaseDateMarginHorizontal)
            end.linkTo(parent.end, releaseDateMarginHorizontal)
            translationX = (-8).dp
            alpha = 0f
        }
        constrain(developerName) {
            width = Dimension.fillToConstraints
            top.linkTo(releaseDate.bottom)
            start.linkTo(cover.end, developerNameMarginHorizontal)
            end.linkTo(parent.end, developerNameMarginHorizontal)
            translationX = (-8).dp
            alpha = 0f
        }
        constrain(rating) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, infoItemMarginBottom)
            bottom.linkTo(list.top, infoItemMarginBottom)
            linkTo(start = parent.start, end = likeCount.start, bias = 0.25f)
        }
        constrain(likeCount) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, infoItemMarginBottom)
            bottom.linkTo(list.top, infoItemMarginBottom)
            linkTo(start = rating.end, end = ageRating.start, bias = 0.25f)
        }
        constrain(ageRating) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, infoItemMarginBottom)
            bottom.linkTo(list.top, infoItemMarginBottom)
            linkTo(start = likeCount.end, end = gameCategory.start, bias = 0.25f)
        }
        constrain(gameCategory) {
            width = Dimension.fillToConstraints
            top.linkTo(artworks.bottom, infoItemMarginBottom)
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
