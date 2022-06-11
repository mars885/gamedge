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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.feature.info.widgets.header.approaches.MotionLayoutBasedOnJsonTransitionOnly
import com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks.GameInfoArtworkModel

internal const val CONSTRAINT_ID_ARTWORKS = "artworks"
internal const val CONSTRAINT_ID_ARTWORKS_SCRIM = "artworks_scrim"
internal const val CONSTRAINT_ID_BACK_BUTTON = "back_button"
internal const val CONSTRAINT_ID_PAGE_INDICATOR = "page_indicator"
internal const val CONSTRAINT_ID_BACKDROP = "backdrop"
internal const val CONSTRAINT_ID_COVER_SPACE = "cover_space"
internal const val CONSTRAINT_ID_COVER = "cover"
internal const val CONSTRAINT_ID_LIKE_BUTTON = "like_button"
internal const val CONSTRAINT_ID_FIRST_TITLE = "first_title"
internal const val CONSTRAINT_ID_SECOND_TITLE = "second_title"
internal const val CONSTRAINT_ID_RELEASE_DATE = "release_date"
internal const val CONSTRAINT_ID_DEVELOPER_NAME = "developer_name"
internal const val CONSTRAINT_ID_BOTTOM_BARRIER = "bottom_barrier"
internal const val CONSTRAINT_ID_RATING = "rating"
internal const val CONSTRAINT_ID_LIKE_COUNT = "like_count"
internal const val CONSTRAINT_ID_AGE_RATING = "age_rating"
internal const val CONSTRAINT_ID_GAME_CATEGORY = "game_category"
internal const val CONSTRAINT_ID_LIST = "list"

internal val COVER_SPACE = 40.dp
internal val INFO_ICON_SIZE = 34.dp

internal val BACKDROP_ELEVATION_EXPANDED = 2.dp
internal val BACKDROP_ELEVATION_COLLAPSED = 4.dp

internal enum class State {
    COLLAPSED,
    EXPANDED,
}

@Composable
internal fun GameInfoHeader(
    headerInfo: GameInfoHeaderModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    content: @Composable (Modifier) -> Unit = {},
) {
/*    MotionLayoutUsingSingleConstrainSet(
        headerInfo = headerInfo,
        onArtworkClicked = onArtworkClicked,
        onBackButtonClicked = onBackButtonClicked,
        onCoverClicked = onCoverClicked,
        onLikeButtonClicked = onLikeButtonClicked,
    )*/
    MotionLayoutBasedOnJsonTransitionOnly(
        headerInfo = headerInfo,
        onArtworkClicked = onArtworkClicked,
        onBackButtonClicked = onBackButtonClicked,
        onCoverClicked = onCoverClicked,
        onLikeButtonClicked = onLikeButtonClicked,
        content = content,
    )
/*    ConstraintLayoutSingleConstraintSet(
        headerInfo = headerInfo,
        onArtworkClicked = onArtworkClicked,
        onBackButtonClicked = onBackButtonClicked,
        onCoverClicked = onCoverClicked,
        onLikeButtonClicked = onLikeButtonClicked,
    )*/
/*    ConstraintLayoutUsingComposeAnimations(
        headerInfo = headerInfo,
        onArtworkClicked = onArtworkClicked,
        onBackButtonClicked = onBackButtonClicked,
        onCoverClicked = onCoverClicked,
        onLikeButtonClicked = onLikeButtonClicked,
    )*/
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
