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

import androidx.appcompat.content.res.AppCompatResources
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.clickable
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.feature.info.R

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

@Composable
internal fun GameHeader(
    headerInfo: GameInfoHeaderModel,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
) {
    val artworks = headerInfo.artworks
    val isPageIndicatorVisible by remember(artworks) {
        // DerivedStateOf?
        mutableStateOf(artworks.size > 1)
    }
    var selectedArtworkPage by remember { mutableStateOf(0) }
    var isSecondTitleVisible by remember { mutableStateOf(false) }
    var secondTitleText by remember { mutableStateOf("") }

    ConstraintLayout(
        constraintSet = constructExpandedConstraintSet(),
        modifier = Modifier.fillMaxWidth(),
    ) {
        GameArtworks(
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
                .background(
                    color = colorResource(
                        R.color.game_info_header_artworks_scrim_bg_color_expanded
                    ),
                ),
        )

        Icon(
            painter = painterResource(R.drawable.arrow_left),
            contentDescription = null,
            modifier = Modifier
                .layoutId(CONSTRAINT_ID_BACK_BUTTON)
                .statusBarsPadding()
                .size(dimensionResource(R.dimen.game_info_header_back_button_size))
                .clickable(
                    indication = rememberRipple(
                        bounded = false,
                        radius = dimensionResource(R.dimen.game_info_header_back_button_ripple_radius),
                    ),
                    onClick = onBackButtonClicked,
                )
                .padding(dimensionResource(R.dimen.game_info_header_back_button_drawable_bg_padding))
                .background(
                    color = colorResource(
                        R.color.game_info_header_back_button_drawable_middleground_color
                    ),
                    shape = CircleShape,
                )
                .padding(dimensionResource(R.dimen.game_info_header_back_button_drawable_fog_padding)),
            tint = colorResource(R.color.game_info_header_back_button_icon_color),
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
                        color = colorResource(R.color.game_info_header_page_indicator_background_color),
                        shape = RoundedCornerShape(
                            dimensionResource(R.dimen.game_info_header_page_indicator_background_radius),
                        )
                    )
                    .padding(
                        vertical = dimensionResource(R.dimen.game_info_header_page_indicator_vertical_padding),
                        horizontal = dimensionResource(R.dimen.game_info_header_page_indicator_horizontal_padding),
                    ),
                color = colorResource(R.color.game_info_header_page_indicator_text_color),
                fontSize = textSizeResource(R.dimen.game_info_header_page_indicator_text_size),
            )
        }

        Box(
            Modifier
                .layoutId(CONSTRAINT_ID_BACKDROP)
                .shadow(
                    elevation = dimensionResource(R.dimen.game_info_header_backdrop_elevation_expanded),
                    shape = RectangleShape,
                    clip = false,
                )
                .background(
                    color = colorResource(R.color.game_info_header_backdrop_background_color),
                    shape = RectangleShape,
                )
                .clip(RectangleShape),
        )

        Spacer(
            Modifier
                .layoutId(CONSTRAINT_ID_COVER_SPACE)
                .height(dimensionResource(R.dimen.game_info_header_cover_space)),
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
            backgroundColor = colorResource(R.color.game_info_header_like_btn_color),
        ) {
            val drawable = AppCompatResources.getDrawable(
                LocalContext.current,
                R.drawable.heart_animated_selector
            )

            Icon(
                painter = rememberDrawablePainter(drawable),
                contentDescription = null,
                modifier = Modifier.size(
                    dimensionResource(R.dimen.game_info_header_like_btn_max_image_size),
                ),
                tint = colorResource(R.color.game_info_header_like_btn_icon_color),
            )
        }

        Text(
            text = headerInfo.title,
            modifier = Modifier.layoutId(CONSTRAINT_ID_FIRST_TITLE),
            color = colorResource(R.color.game_info_header_title_text_color),
            fontSize = textSizeResource(R.dimen.game_info_header_title_text_size),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            onTextLayout = { textLayoutResult ->
                if (!textLayoutResult.hasVisualOverflow) {
                    isSecondTitleVisible = false
                } else {
                    val firstTitleWidth = textLayoutResult.size.width.toFloat()
                    val firstTitleOffset = Offset(firstTitleWidth, 0f)
                    val firstTitleVisibleTextEndIndex = textLayoutResult.getOffsetForPosition(firstTitleOffset) + 1

                    secondTitleText = headerInfo.title.substring(firstTitleVisibleTextEndIndex)
                    isSecondTitleVisible = true
                }
            },
        )

        Box(Modifier.layoutId(CONSTRAINT_ID_SECOND_TITLE)) {
            if (isSecondTitleVisible) {
                // Remove font padding once https://issuetracker.google.com/issues/171394808
                // is implemented (includeFontPadding="false" in XML)
                Text(
                    text = secondTitleText,
                    color = colorResource(R.color.game_info_header_title_text_color),
                    fontSize = textSizeResource(R.dimen.game_info_header_title_text_size),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Text(
            text = headerInfo.releaseDate,
            modifier = Modifier.layoutId(CONSTRAINT_ID_RELEASE_DATE),
            color = colorResource(R.color.game_info_header_release_date_text_color),
            fontSize = textSizeResource(R.dimen.game_info_header_release_date_text_size),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
        )

        if (headerInfo.hasDeveloperName) {
            Text(
                text = checkNotNull(headerInfo.developerName),
                modifier = Modifier.layoutId(CONSTRAINT_ID_DEVELOPER_NAME),
                color = colorResource(R.color.game_info_header_developer_name_text_color),
                fontSize = textSizeResource(R.dimen.game_info_header_developer_name_text_size),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
            )
        }

        Info(
            icon = painterResource(R.drawable.star_circle_outline),
            title = headerInfo.rating,
            modifier = Modifier.layoutId(CONSTRAINT_ID_RATING),
            iconSize = dimensionResource(R.dimen.game_info_header_info_view_icon_size),
            iconColor = colorResource(R.color.game_info_header_info_view_icon_color),
            titleTextSize = textSizeResource(R.dimen.game_info_header_info_view_title_text_size),
            titleTextColor = colorResource(R.color.game_info_header_info_view_title_color),
        )
        Info(
            icon = painterResource(R.drawable.account_heart_outline),
            title = headerInfo.likeCount,
            modifier = Modifier.layoutId(CONSTRAINT_ID_LIKE_COUNT),
            iconSize = dimensionResource(R.dimen.game_info_header_info_view_icon_size),
            iconColor = colorResource(R.color.game_info_header_info_view_icon_color),
            titleTextSize = textSizeResource(R.dimen.game_info_header_info_view_title_text_size),
            titleTextColor = colorResource(R.color.game_info_header_info_view_title_color),
        )
        Info(
            icon = painterResource(R.drawable.age_rating_outline),
            title = headerInfo.ageRating,
            modifier = Modifier.layoutId(CONSTRAINT_ID_AGE_RATING),
            iconSize = dimensionResource(R.dimen.game_info_header_info_view_icon_size),
            iconColor = colorResource(R.color.game_info_header_info_view_icon_color),
            titleTextSize = textSizeResource(R.dimen.game_info_header_info_view_title_text_size),
            titleTextColor = colorResource(R.color.game_info_header_info_view_title_color),
        )
        Info(
            icon = painterResource(R.drawable.shape_outline),
            title = headerInfo.gameCategory,
            modifier = Modifier.layoutId(CONSTRAINT_ID_GAME_CATEGORY),
            iconSize = dimensionResource(R.dimen.game_info_header_info_view_icon_size),
            iconColor = colorResource(R.color.game_info_header_info_view_icon_color),
            titleTextSize = textSizeResource(R.dimen.game_info_header_info_view_title_text_size),
            titleTextColor = colorResource(R.color.game_info_header_info_view_title_color),
        )
    }
}

@Composable
private fun constructExpandedConstraintSet(): ConstraintSet {
    val artworksHeight = dimensionResource(R.dimen.game_info_header_artworks_height_expanded)
    val pageIndicatorMargin = dimensionResource(R.dimen.game_info_header_page_indicator_margin)
    val coverSpaceMargin = dimensionResource(R.dimen.game_info_header_cover_space)
    val coverWidth = dimensionResource(R.dimen.game_info_header_cover_width)
    val coverHeight = dimensionResource(R.dimen.game_info_header_cover_height)
    val coverMarginStart = dimensionResource(R.dimen.game_info_header_cover_margin_start)
    val likeBtnMarginEnd = dimensionResource(R.dimen.game_info_header_like_btn_margin_end)
    val titleMarginStart = dimensionResource(R.dimen.game_info_header_title_margin_start)
    val firstTitleMarginTop = dimensionResource(R.dimen.game_info_header_title_margin_start)
    val firstTitleMarginEnd = dimensionResource(R.dimen.game_info_header_first_title_margin_end_expanded)
    val secondTitleMarginEnd = dimensionResource(R.dimen.game_info_header_second_title_margin_end)
    val releaseDateMarginTop = dimensionResource(R.dimen.game_info_header_release_date_margin_top)
    val releaseDateMarginHorizontal = dimensionResource(R.dimen.game_info_header_release_date_horizontal_margin)
    val developerNameMarginHorizontal = dimensionResource(R.dimen.game_info_header_developer_name_horizontal_margin)
    val bottomBarrierMargin = dimensionResource(R.dimen.game_info_header_bottom_barrier_margin)
    val infoItemMarginBottom = dimensionResource(R.dimen.game_info_header_info_view_vertical_margin)

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
            width = Dimension.value(coverWidth)
            height = Dimension.value(coverHeight)
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
private fun GameArtworks(
    artworks: List<GameArtworkModel>,
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
    ) { page ->
        GameArtwork(
            artwork = artworks[page],
            onArtworkClicked = { onArtworkClicked(page) },
        )
    }
}

@Composable
private fun GameArtwork(
    artwork: GameArtworkModel,
    onArtworkClicked: () -> Unit,
) {
    val contentScale = ContentScale.Crop
    val painter = when (artwork) {
        is GameArtworkModel.DefaultImage -> painterResource(R.drawable.game_background_placeholder)
        is GameArtworkModel.UrlImage -> rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(artwork.url)
                .fallback(R.drawable.game_background_placeholder)
                .placeholder(R.drawable.game_background_placeholder)
                .error(R.drawable.game_background_placeholder)
                .crossfade(CROSSFADE_ANIMATION_DURATION)
                .build(),
            contentScale = contentScale,
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
        contentScale = contentScale,
    )
}

@Preview
@Composable
internal fun GameHeaderPreview() {
    GameHeader(
        headerInfo = GameInfoHeaderModel(
            artworks = listOf(GameArtworkModel.DefaultImage),
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
