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

import android.content.Context
import android.content.res.ColorStateList
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.InvalidationStrategy
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.MotionSceneScope
import androidx.constraintlayout.compose.Transition
import androidx.constraintlayout.compose.Visibility
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.paulrybitskyi.commons.ktx.getCompatDrawable
import com.paulrybitskyi.commons.ktx.onClick
import com.paulrybitskyi.commons.ktx.postAction
import com.paulrybitskyi.commons.ktx.statusBarHeight
import com.paulrybitskyi.gamedge.common.ui.clickable
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.theme.Spaces
import com.paulrybitskyi.gamedge.common.ui.theme.darkScrim
import com.paulrybitskyi.gamedge.common.ui.theme.lightScrim
import com.paulrybitskyi.gamedge.common.ui.theme.subtitle3
import com.paulrybitskyi.gamedge.common.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.common.ui.widgets.Info
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.header.artworks.Artworks
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.header.artworks.GameInfoArtworkUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language
import com.paulrybitskyi.gamedge.core.R as CoreR

private const val ConstraintSetNameExpanded = "expanded"
private const val ConstraintSetNameCollapsed = "collapsed"
private const val TransitionName = "fancy_transition"

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

private const val CustomAttributeTextColor = "text_color"

private val ScrimContentColor = Color.White

private val CoverSpace = 40.dp
private val InfoIconSize = 34.dp

private const val FirstTitleScaleCollapsed = 1.1f
private const val LikeButtonScaleCollapsed = 0f

private val ArtworksHeightExpanded = 240.dp
private val ArtworksHeightCollapsed = 56.dp

private val PageIndicatorDeltaXCollapsed = 60.dp
private val CoverDeltaXCollapsed = (-130).dp
private val CoverDeltaYCollapsed = (-60).dp
private val SecondaryTextDeltaXCollapsed = (-8).dp

private val AnimatableSaver = Saver(
    save = { animatable -> animatable.value },
    restore = ::Animatable,
)

@Composable
internal fun GameInfoAnimatableHeader(
    headerInfo: GameInfoHeaderUiModel,
    listState: LazyListState,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCoverClicked: () -> Unit,
    onLikeButtonClicked: () -> Unit,
    content: @Composable (Modifier, NestedScrollConnection) -> Unit,
) {
    val maxPx = 555f
    val minPx = 228f
    val progress = rememberSaveable(saver = AnimatableSaver) {
        Animatable(0f).apply {
            updateBounds(0f, 1f)
        }
    }
    var headerHeight by remember {
        mutableFloatStateOf(
            if (progress.value == 0f) maxPx else minPx
        )
    }
    val coroutineScope = rememberCoroutineScope()

    val colors = GamedgeTheme.colors
    val density = LocalDensity.current
    val artworks = headerInfo.artworks
    val isPageIndicatorVisible = remember(artworks) { artworks.size > 1 }
    val hasDefaultPlaceholderArtwork = remember(artworks) {
        artworks.size == 1 &&
        artworks.single() is GameInfoArtworkUiModel.DefaultImage
    }
    var selectedArtworkPage by remember { mutableIntStateOf(0) }
    var secondTitleText by rememberSaveable { mutableStateOf("") }
    val isSecondTitleVisible by remember {
        derivedStateOf {
            secondTitleText.isNotEmpty()
        }
    }
    val isArtworkInteractionEnabled by remember {
        derivedStateOf {
            progress.value < 0.01f
        }
    }
    val firstTitleOverflowMode by remember {
        derivedStateOf {
            if (progress.value < 0.95f) {
                TextOverflow.Clip
            } else {
                TextOverflow.Ellipsis
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .filterNot { it }
            .collect {
                val currentProgress = progress.value

                if (currentProgress != 0f && currentProgress != 1f) {
                    val newProgress = if (currentProgress < 0.5f) 0f else 1f
                    val duration = (300 + (1200 - 300) * 4 * currentProgress * (1 - currentProgress)).toInt()

                    launch {
                        progress.animateTo(
                            targetValue = newProgress,
                            animationSpec = tween(duration, easing = EaseInOut),
                            block = {
                                headerHeight = minPx + (1 - value) * (maxPx - minPx)
                            }
                        )
                    }
                }
            }
    }

    val nestedConnection = remember(listState, coroutineScope) {
        object : NestedScrollConnection {

            private fun consume(available: Offset): Offset {
                val height = headerHeight

                if (height + available.y > maxPx) {
                    headerHeight = maxPx
                    updateProgress()
                    return Offset(0f, maxPx - height)
                }

                if (height + available.y < minPx) {
                    headerHeight = minPx
                    updateProgress()
                    return Offset(0f, minPx - height)
                }

                headerHeight += available.y / 2
                updateProgress()

                return Offset(0f, available.y)
            }

            private fun updateProgress() {
                val newProgress = 1 - (headerHeight - minPx) / (maxPx - minPx)

                coroutineScope.launch {
                    progress.snapTo(newProgress)
                }
            }

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (listState.canScrollBackward) {
                    return Offset.Zero
                }

                return consume(available)
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                return if (available.y > 0 && !listState.canScrollBackward) {
                    consume(available)
                } else {
                    Offset.Zero
                }
            }
        }
    }

    MotionLayout(
        motionScene = rememberMotionScene(
            hasDefaultPlaceholderArtwork = hasDefaultPlaceholderArtwork,
            isSecondTitleVisible = isSecondTitleVisible,
        ),
        progress = progress.value,
        modifier = Modifier.fillMaxSize(),
        transitionName = TransitionName,
        invalidationStrategy = remember {
            InvalidationStrategy(
                onObservedStateChange = {
                    @Suppress("UNUSED_EXPRESSION")
                    headerInfo
                },
            )
        }
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
                    indication = ripple(
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
            tint = ScrimContentColor,
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
                color = ScrimContentColor,
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
                .clip(RectangleShape)
                .onGloballyPositioned {
/*                    Log.e("kimahri", "onGloballyPositioned, progress = $progress, size = ${it.size}")
                    if (progress == 0f) {
                        expandedHeight = it.size.height
                    }

                    if (progress == 1f) {
                        collapsedHeight = it.size.height
                    }
                    Log.e("wakka", "backdrop = ${it.size}")*/
                },
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

        // Animated selector drawables are not currently supported by the Jetpack Compose:
        // https://issuetracker.google.com/issues/212418566. However, since the link/unlike
        // animation is so gorgeous, it'd sad if we didn't use it, so we are using the legacy
        // View here to render it. Consider to migrate to the Jetpack Compose when the support
        // arrives.
        AndroidView(
            factory = { context ->
                LikeButton(context).apply {
                    supportBackgroundTintList = ColorStateList.valueOf(colors.secondary.toArgb())
                    size = FloatingActionButton.SIZE_NORMAL
                    setMaxImageSize(with(density) { 52.dp.toPx().toInt() })
                    setImageDrawable(context.getCompatDrawable(CoreR.drawable.heart_animated_selector))
                    supportImageTintList = ColorStateList.valueOf(colors.onSecondary.toArgb())
                    onClick { onLikeButtonClicked() }
                }
            },
            modifier = Modifier
                .layoutId(ConstraintIdLikeButton)
                .drawOnTop(),
            update = { view ->
                view.isLiked = headerInfo.isLiked
            },
        )

        Text(
            text = headerInfo.title,
            modifier = Modifier
                .layoutId(ConstraintIdFirstTitle)
                .drawOnTop(),
            color = customColor(ConstraintIdFirstTitle, CustomAttributeTextColor),
            overflow = firstTitleOverflowMode,
            maxLines = 1,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    val firstTitleWidth = textLayoutResult.size.width.toFloat()
                    val firstTitleOffset = Offset(firstTitleWidth, 0f)
                    val firstTitleVisibleTextEndIndex = textLayoutResult.getOffsetForPosition(firstTitleOffset) + 1

                    if (firstTitleVisibleTextEndIndex in headerInfo.title.indices) {
                        secondTitleText = headerInfo.title.substring(firstTitleVisibleTextEndIndex)
                    }
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
                .drawOnTop(),
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

        content(Modifier.layoutId(ConstraintIdList), nestedConnection)
    }
}

@Composable
private fun rememberMotionScene(
    hasDefaultPlaceholderArtwork: Boolean,
    isSecondTitleVisible: Boolean,
): MotionScene {
    val spaces = GamedgeTheme.spaces
    val artworksHeightInCollapsedState = calculateArtworksHeightInCollapsedState()
    val statusBarHeight = calculateStatusBarHeightInDp()
    val firstTitleColorInExpandedState = GamedgeTheme.colors.onPrimary
    val firstTitleColorInCollapsedState = ScrimContentColor

    return remember(
        spaces,
        artworksHeightInCollapsedState,
        statusBarHeight,
        firstTitleColorInExpandedState,
        firstTitleColorInCollapsedState
    ) {
        MotionScene {
            val refs = ConstraintLayoutRefs(this)

            addConstraintSet(
                constraintSet = constructExpandedConstraintSet(
                    refs = refs,
                    spaces = spaces,
                    hasDefaultPlaceholderArtwork = hasDefaultPlaceholderArtwork,
                    isSecondTitleVisible = isSecondTitleVisible,
                    firstTitleTextColor = firstTitleColorInExpandedState,
                ),
                name = ConstraintSetNameExpanded,
            )
            addConstraintSet(
                constraintSet = constructCollapsedConstraintSet(
                    refs = refs,
                    spaces = spaces,
                    hasDefaultPlaceholderArtwork = hasDefaultPlaceholderArtwork,
                    artworksHeight = artworksHeightInCollapsedState,
                    statusBarHeight = statusBarHeight,
                    firstTitleTextColor = firstTitleColorInCollapsedState,
                ),
                name = ConstraintSetNameCollapsed,
            )
            addTransition(
                transition = constructTransition(
                    refs = refs,
                    firstTitleColorInExpandedState = firstTitleColorInExpandedState,
                    firstTitleColorInCollapsedState = firstTitleColorInCollapsedState,
                ),
                name = TransitionName,
            )
        }
    }
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

private class ConstraintLayoutRefs(
    val artworks: ConstrainedLayoutReference,
    val artworksScrim: ConstrainedLayoutReference,
    val backButton: ConstrainedLayoutReference,
    val pageIndicator: ConstrainedLayoutReference,
    val backdrop: ConstrainedLayoutReference,
    val coverSpace: ConstrainedLayoutReference,
    val cover: ConstrainedLayoutReference,
    val likeButton: ConstrainedLayoutReference,
    val firstTitle: ConstrainedLayoutReference,
    val secondTitle: ConstrainedLayoutReference,
    val releaseDate: ConstrainedLayoutReference,
    val developerName: ConstrainedLayoutReference,
    val rating: ConstrainedLayoutReference,
    val likeCount: ConstrainedLayoutReference,
    val ageRating: ConstrainedLayoutReference,
    val gameCategory: ConstrainedLayoutReference,
    val list: ConstrainedLayoutReference,
) {
    constructor(motionSceneScope: MotionSceneScope): this(
        artworks = motionSceneScope.createRefFor(ConstraintIdArtworks),
        artworksScrim = motionSceneScope.createRefFor(ConstraintIdArtworksScrim),
        backButton = motionSceneScope.createRefFor(ConstraintIdBackButton),
        pageIndicator = motionSceneScope.createRefFor(ConstraintIdPageIndicator),
        backdrop = motionSceneScope.createRefFor(ConstraintIdBackdrop),
        coverSpace = motionSceneScope.createRefFor(ConstraintIdCoverSpace),
        cover = motionSceneScope.createRefFor(ConstraintIdCover),
        likeButton = motionSceneScope.createRefFor(ConstraintIdLikeButton),
        firstTitle = motionSceneScope.createRefFor(ConstraintIdFirstTitle),
        secondTitle = motionSceneScope.createRefFor(ConstraintIdSecondTitle),
        releaseDate = motionSceneScope.createRefFor(ConstraintIdReleaseDate),
        developerName = motionSceneScope.createRefFor(ConstraintIdDeveloperName),
        rating = motionSceneScope.createRefFor(ConstraintIdRating),
        likeCount = motionSceneScope.createRefFor(ConstraintIdLikeCount),
        ageRating = motionSceneScope.createRefFor(ConstraintIdAgeRating),
        gameCategory = motionSceneScope.createRefFor(ConstraintIdGameCategory),
        list = motionSceneScope.createRefFor(ConstraintIdList),
    )
}

private fun MotionSceneScope.constructExpandedConstraintSet(
    refs: ConstraintLayoutRefs,
    spaces: Spaces,
    hasDefaultPlaceholderArtwork: Boolean,
    isSecondTitleVisible: Boolean,
    firstTitleTextColor: Color,
): ConstraintSet {
    val pageIndicatorMargin = spaces.spacing_2_5
    val backdropElevation = spaces.spacing_0_5
    val coverSpaceMargin = CoverSpace
    val coverMarginStart = spaces.spacing_3_5
    val likeBtnMarginEnd = spaces.spacing_2_5
    val titleMarginStart = spaces.spacing_3_5
    val firstTitleMarginTop = titleMarginStart
    val firstTitleMarginEnd = spaces.spacing_1_0
    val secondTitleMarginEnd = spaces.spacing_3_5
    val releaseDateMarginTop = spaces.spacing_2_5
    val releaseDateMarginHorizontal = spaces.spacing_3_5
    val developerNameMarginHorizontal = spaces.spacing_3_5
    val bottomBarrierMargin = spaces.spacing_5_0
    val infoItemMarginBottom = spaces.spacing_3_5

    return ConstraintSet {
        val bottomBarrier = createBottomBarrier(
            refs.cover,
            refs.developerName,
            margin = bottomBarrierMargin,
        )

        constrain(refs.artworks) {
            width = Dimension.fillToConstraints
            height = Dimension.value(ArtworksHeightExpanded)
            top.linkTo(parent.top)
            centerHorizontallyTo(parent)
        }
        constrain(refs.artworksScrim) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            centerVerticallyTo(refs.artworks)
            centerHorizontallyTo(refs.artworks)
            visibility = if (hasDefaultPlaceholderArtwork) {
                Visibility.Gone
            } else {
                Visibility.Invisible
            }
        }
        constrain(refs.backButton) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }
        constrain(refs.pageIndicator) {
            top.linkTo(parent.top, pageIndicatorMargin)
            end.linkTo(parent.end, pageIndicatorMargin)
        }
        constrain(refs.backdrop) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom)
            bottom.linkTo(refs.list.top)
            centerHorizontallyTo(parent)
            translationZ = backdropElevation
        }
        constrain(refs.coverSpace) {
            start.linkTo(parent.start)
            bottom.linkTo(refs.artworks.bottom, coverSpaceMargin)
        }
        constrain(refs.cover) {
            top.linkTo(refs.coverSpace.bottom)
            start.linkTo(parent.start, coverMarginStart)
        }
        constrain(refs.likeButton) {
            top.linkTo(refs.artworks.bottom)
            bottom.linkTo(refs.artworks.bottom)
            end.linkTo(parent.end, likeBtnMarginEnd)
        }
        constrain(refs.firstTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom, firstTitleMarginTop)
            start.linkTo(refs.cover.end, titleMarginStart)
            end.linkTo(refs.likeButton.start, firstTitleMarginEnd)
            customColor(CustomAttributeTextColor, firstTitleTextColor)
        }
        constrain(refs.secondTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.firstTitle.bottom)
            start.linkTo(refs.cover.end, titleMarginStart)
            end.linkTo(parent.end, secondTitleMarginEnd)
            isVisible = isSecondTitleVisible
        }
        constrain(refs.releaseDate) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.secondTitle.bottom, releaseDateMarginTop, releaseDateMarginTop)
            start.linkTo(refs.cover.end, releaseDateMarginHorizontal)
            end.linkTo(parent.end, releaseDateMarginHorizontal)
        }
        constrain(refs.developerName) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.releaseDate.bottom)
            start.linkTo(refs.cover.end, developerNameMarginHorizontal)
            end.linkTo(parent.end, developerNameMarginHorizontal)
        }
        constrain(refs.rating) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(refs.list.top, infoItemMarginBottom)
            linkTo(start = parent.start, end = refs.likeCount.start, bias = 0.25f)
        }
        constrain(refs.likeCount) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(refs.list.top, infoItemMarginBottom)
            linkTo(start = refs.rating.end, end = refs.ageRating.start, bias = 0.25f)
        }
        constrain(refs.ageRating) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(refs.list.top, infoItemMarginBottom)
            linkTo(start = refs.likeCount.end, end = refs.gameCategory.start, bias = 0.25f)
        }
        constrain(refs.gameCategory) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            bottom.linkTo(refs.list.top, infoItemMarginBottom)
            linkTo(start = refs.ageRating.end, end = parent.end, bias = 0.25f)
        }
        constrain(refs.list) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(refs.rating.bottom)
            bottom.linkTo(parent.bottom)
            centerHorizontallyTo(parent)
        }
    }
}

private fun MotionSceneScope.constructCollapsedConstraintSet(
    refs: ConstraintLayoutRefs,
    spaces: Spaces,
    hasDefaultPlaceholderArtwork: Boolean,
    artworksHeight: Dp,
    statusBarHeight: Dp,
    firstTitleTextColor: Color,
): ConstraintSet {
    val pageIndicatorMargin = spaces.spacing_2_5
    val backdropElevation = spaces.spacing_1_0
    val coverSpaceMargin = CoverSpace
    val coverMarginStart = spaces.spacing_3_5
    val likeBtnMarginEnd = spaces.spacing_2_5
    val titleMarginStart = spaces.spacing_3_5
    val firstTitleMarginStart = spaces.spacing_7_5
    val firstTitleMarginEnd = spaces.spacing_6_0
    val secondTitleMarginEnd = spaces.spacing_3_5
    val releaseDateMarginTop = spaces.spacing_2_5
    val releaseDateMarginHorizontal = spaces.spacing_3_5
    val developerNameMarginHorizontal = spaces.spacing_3_5
    val infoItemVerticalMargin = spaces.spacing_3_5

    return ConstraintSet {
        constrain(refs.artworks) {
            width = Dimension.fillToConstraints
            height = Dimension.value(artworksHeight)
            top.linkTo(parent.top)
            bottom.linkTo(refs.backdrop.top)
            centerHorizontallyTo(parent)
        }
        constrain(refs.artworksScrim) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            centerVerticallyTo(refs.artworks)
            centerHorizontallyTo(refs.artworks)
            visibility = if (hasDefaultPlaceholderArtwork) {
                Visibility.Gone
            } else {
                Visibility.Visible
            }
        }
        constrain(refs.backButton) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }
        constrain(refs.pageIndicator) {
            top.linkTo(parent.top, pageIndicatorMargin)
            end.linkTo(parent.end, pageIndicatorMargin)
            translationX = PageIndicatorDeltaXCollapsed
        }
        constrain(refs.backdrop) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom)
            bottom.linkTo(refs.list.top)
            centerHorizontallyTo(parent)
            translationZ = backdropElevation
        }
        constrain(refs.coverSpace) {
            start.linkTo(parent.start)
            bottom.linkTo(refs.artworks.bottom, coverSpaceMargin)
        }
        constrain(refs.cover) {
            top.linkTo(refs.coverSpace.bottom)
            start.linkTo(parent.start, coverMarginStart)
            translationX = CoverDeltaXCollapsed
            translationY = CoverDeltaYCollapsed
        }
        constrain(refs.likeButton) {
            top.linkTo(refs.artworks.bottom)
            bottom.linkTo(refs.artworks.bottom)
            end.linkTo(parent.end, likeBtnMarginEnd)
            alpha = 0f
            setScale(LikeButtonScaleCollapsed)
        }
        constrain(refs.firstTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.top, statusBarHeight)
            bottom.linkTo(refs.artworks.bottom)
            start.linkTo(refs.backButton.end, firstTitleMarginStart)
            end.linkTo(parent.end, firstTitleMarginEnd)
            setScale(FirstTitleScaleCollapsed)
            customColor(CustomAttributeTextColor, firstTitleTextColor)
        }
        constrain(refs.secondTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.firstTitle.bottom)
            start.linkTo(refs.cover.end, titleMarginStart)
            end.linkTo(parent.end, secondTitleMarginEnd)
            alpha = 0f
            translationX = SecondaryTextDeltaXCollapsed
        }
        constrain(refs.releaseDate) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.secondTitle.bottom, releaseDateMarginTop, releaseDateMarginTop)
            start.linkTo(refs.cover.end, releaseDateMarginHorizontal)
            end.linkTo(parent.end, releaseDateMarginHorizontal)
            alpha = 0f
            translationX = SecondaryTextDeltaXCollapsed
        }
        constrain(refs.developerName) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.releaseDate.bottom)
            start.linkTo(refs.cover.end, developerNameMarginHorizontal)
            end.linkTo(parent.end, developerNameMarginHorizontal)
            alpha = 0f
            translationX = SecondaryTextDeltaXCollapsed
        }
        constrain(refs.rating) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom, infoItemVerticalMargin)
            bottom.linkTo(refs.list.top, infoItemVerticalMargin)
            linkTo(start = parent.start, end = refs.likeCount.start, bias = 0.25f)
        }
        constrain(refs.likeCount) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom, infoItemVerticalMargin)
            bottom.linkTo(refs.list.top, infoItemVerticalMargin)
            linkTo(start = refs.rating.end, end = refs.ageRating.start, bias = 0.25f)
        }
        constrain(refs.ageRating) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom, infoItemVerticalMargin)
            bottom.linkTo(refs.list.top, infoItemVerticalMargin)
            linkTo(start = refs.likeCount.end, end = refs.gameCategory.start, bias = 0.25f)
        }
        constrain(refs.gameCategory) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom, infoItemVerticalMargin)
            bottom.linkTo(refs.list.top, infoItemVerticalMargin)
            linkTo(start = refs.ageRating.end, end = parent.end, bias = 0.25f)
        }
        constrain(refs.list) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(refs.rating.bottom)
            bottom.linkTo(parent.bottom)
            centerHorizontallyTo(parent)
        }
    }
}

private fun MotionSceneScope.constructTransition(
    refs: ConstraintLayoutRefs,
    firstTitleColorInExpandedState: Color,
    firstTitleColorInCollapsedState: Color,
): Transition {
    return Transition(from = ConstraintSetNameExpanded, to = ConstraintSetNameCollapsed) {
        keyAttributes(refs.secondTitle) {
            frame(frame = 15) {
                alpha = 0f
                translationX = SecondaryTextDeltaXCollapsed
            }
        }
        keyAttributes(refs.releaseDate) {
            frame(frame = 15) {
                alpha = 0f
                translationX = SecondaryTextDeltaXCollapsed
            }
        }
        keyAttributes(refs.developerName) {
            frame(frame = 15) {
                alpha = 0f
                translationX = SecondaryTextDeltaXCollapsed
            }
        }
        keyAttributes(refs.cover) {
            frame(frame = 50) {
                alpha = 0f
                translationX = CoverDeltaXCollapsed
                translationY = CoverDeltaYCollapsed
            }
        }
        keyAttributes(refs.firstTitle) {
            frame(frame = 40) {
                customColor(CustomAttributeTextColor, firstTitleColorInExpandedState)
            }
            frame(frame = 60) {
                customColor(CustomAttributeTextColor, firstTitleColorInCollapsedState)
            }
        }
        keyAttributes(refs.likeButton) {
            frame(frame = 60) {
                alpha = 0f
                scaleX = 0f
                scaleY = 0f
            }
        }
        keyAttributes(refs.pageIndicator) {
            frame(frame = 80) {
                translationX = PageIndicatorDeltaXCollapsed
            }
        }

/*        onSwipe = OnSwipe(
            anchor = refs.list,
            side = SwipeSide.Top,
            direction = SwipeDirection.Up,
            onTouchUp = SwipeTouchUp.AutoComplete,
            mode = SwipeMode.Velocity,
        )*/
    }
}

private class LikeButton(context: Context) : FloatingActionButton(context) {

    private companion object {
        const val STATE_CHECKED = android.R.attr.state_checked
        const val STATE_CHECKED_ON = (STATE_CHECKED * 1)
        const val STATE_CHECKED_OFF = (STATE_CHECKED * -1)
    }

    var isLiked: Boolean
        set(value) {
            // Just calling setImageState() directly doesn't work, so we need
            // to postpone it just a bit.
            postAction {
                setImageState(intArrayOf(if (value) STATE_CHECKED_ON else STATE_CHECKED_OFF), true)
            }
        }
        get() = drawableState.contains(STATE_CHECKED_ON)
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
private fun constructTransitionJson(): String {
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
          onSwipe: {
              direction: "up",
              touchUp: "autocomplete",
              anchor: "$ConstraintIdList",
              side: "top",
              mode: "velocity",
          },
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

private fun Modifier.drawOnTop(): Modifier {
    return zIndex(Float.MAX_VALUE)
}
