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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layoutId
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
import androidx.constraintlayout.compose.KeyAttributeScope
import androidx.constraintlayout.compose.KeyPositionScope
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
import com.paulrybitskyi.gamedge.core.R as CoreR

private const val AutoTransitionAnimationDurationMin = 300
private const val AutoTransitionAnimationDurationMax = 1_200

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

private const val CustomAttributeTextColor = "text_color"

private val ScrimContentColor = Color.White

private val CoverSpace = 40.dp
private val InfoIconSize = 34.dp

private const val FirstTitleScaleExpanded = 1f
private const val FirstTitleScaleCollapsed = 1.1f
private const val LikeButtonScaleCollapsed = 0f

private val ArtworksHeightExpanded = 240.dp
private val ArtworksHeightCollapsed = 56.dp

private val PageIndicatorDeltaXCollapsed = 60.dp
private val CoverDeltaXCollapsed = (-130).dp
private val CoverDeltaYCollapsed = (-40).dp

private enum class State(val progress: Float) {
    Expanded(progress = 0f),
    Collapsed(progress = 1f);

    companion object {

        fun fromProgressOrNull(progress: Float): State? {
            return entries.firstOrNull { state -> state.progress == progress }
        }
    }
}

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
    content: @Composable (Modifier) -> Unit,
) {
    // minHeaderHeight = ArtworksHeightInCollapsedState +
    val minPx = 228f
    val maxPx = 555f


    val colors = GamedgeTheme.colors
    val density = LocalDensity.current
    val progress = rememberSaveable(saver = AnimatableSaver) {
        Animatable(State.Expanded.progress)
    }
    var headerHeight by remember {
        mutableFloatStateOf(
            if (progress.value == State.Expanded.progress) maxPx else minPx
        )
    }
    val coroutineScope = rememberCoroutineScope()
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
    val isInCollapsedState by remember {
        derivedStateOf {
            State.fromProgressOrNull(progress.value) == State.Collapsed
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .filterNot { isScrolling -> isScrolling }
            .collect {
                val currentProgress = progress.value

                if (State.fromProgressOrNull(currentProgress) == null) {
                    val newState = if (currentProgress < 0.5f) State.Expanded else State.Collapsed
                    val duration = calculateAutoTransitionDuration(currentProgress)

                    launch {
                        progress.animateTo(
                            targetValue = newState.progress,
                            animationSpec = tween(durationMillis = duration, easing = EaseInOut),
                            block = {
                                headerHeight = calculateHeaderHeightGivenProgress(
                                    progress = value,
                                    minHeaderHeight = minPx,
                                    maxHeaderHeight = maxPx,
                                )
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
                val newProgress = calculateProgressGivenHeaderHeight(
                    headerHeight = headerHeight,
                    minHeaderHeight = minPx,
                    maxHeaderHeight = maxPx,
                )

                coroutineScope.launch {
                    progress.snapTo(newProgress)
                }
            }

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return if (listState.canScrollBackward) {
                    Offset.Zero
                } else {
                    consume(available)
                }
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

    Column {
        MotionLayout(
            motionScene = rememberMotionScene(
                hasDefaultPlaceholderArtwork = hasDefaultPlaceholderArtwork,
                isSecondTitleVisible = isSecondTitleVisible,
            ),
            progress = progress.value,
            modifier = Modifier
                .fillMaxWidth()
                .drawOnTop(),
            transitionName = TransitionName,
            invalidationStrategy = remember {
                InvalidationStrategy(
                    onObservedStateChange = @Suppress("UNUSED_EXPRESSION") {
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
                        // Disabling the ripple because it cripples the animation a bit
                        rippleColor = colors.secondary.toArgb()
                        // Disabling the shadow to avoid it being clipped when animating to collapsed state
                        // (especially can be seen on the light theme)
                        compatElevation = 0f
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
                // When restoring state, customColor function returns invalid color (black color
                // when in collapsed state), so a little fix here to set the correct color
                color = if (isInCollapsedState) {
                    ScrimContentColor
                } else {
                    customColor(ConstraintIdFirstTitle, CustomAttributeTextColor)
                },
                overflow = firstTitleOverflowMode,
                maxLines = 1,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow && secondTitleText.isEmpty()) {
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

            Text(
                text = secondTitleText,
                modifier = Modifier
                    .layoutId(ConstraintIdSecondTitle)
                    .drawOnTop(),
                color = GamedgeTheme.colors.onPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = GamedgeTheme.typography.h6,
            )

            Text(
                text = headerInfo.releaseDate,
                modifier = Modifier
                    .layoutId(ConstraintIdReleaseDate)
                    .drawOnTop(),
                color = GamedgeTheme.colors.onSurface,
                style = GamedgeTheme.typography.subtitle3,
            )

            if (headerInfo.hasDeveloperName) {
                Text(
                    text = checkNotNull(headerInfo.developerName),
                    modifier = Modifier
                        .layoutId(ConstraintIdDeveloperName)
                        .drawOnTop(),
                    color = GamedgeTheme.colors.onSurface,
                    style = GamedgeTheme.typography.subtitle3,
                )
            }

            val infoItemModifier = Modifier
                .drawOnTop()
                .padding(vertical = GamedgeTheme.spaces.spacing_3_5)

            Info(
                icon = painterResource(CoreR.drawable.star_circle_outline),
                title = headerInfo.rating,
                modifier = infoItemModifier.layoutId(ConstraintIdRating),
                iconSize = InfoIconSize,
                titleTextStyle = GamedgeTheme.typography.caption,
            )
            Info(
                icon = painterResource(CoreR.drawable.account_heart_outline),
                title = headerInfo.likeCount,
                modifier = infoItemModifier.layoutId(ConstraintIdLikeCount),
                iconSize = InfoIconSize,
                titleTextStyle = GamedgeTheme.typography.caption,
            )
            Info(
                icon = painterResource(CoreR.drawable.age_rating_outline),
                title = headerInfo.ageRating,
                modifier = infoItemModifier.layoutId(ConstraintIdAgeRating),
                iconSize = InfoIconSize,
                titleTextStyle = GamedgeTheme.typography.caption,
            )
            Info(
                icon = painterResource(CoreR.drawable.shape_outline),
                title = headerInfo.gameCategory,
                modifier = infoItemModifier.layoutId(ConstraintIdGameCategory),
                iconSize = InfoIconSize,
                titleTextStyle = GamedgeTheme.typography.caption,
            )
        }

        content(Modifier.nestedScroll(nestedConnection))
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
        hasDefaultPlaceholderArtwork,
        isSecondTitleVisible,
        spaces,
        artworksHeightInCollapsedState,
        statusBarHeight,
        firstTitleColorInExpandedState,
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
                    isSecondTitleVisible = isSecondTitleVisible,
                    artworksHeight = artworksHeightInCollapsedState,
                    statusBarHeight = statusBarHeight,
                    firstTitleTextColor = firstTitleColorInCollapsedState,
                ),
                name = ConstraintSetNameCollapsed,
            )
            addTransition(
                transition = constructTransition(
                    refs = refs,
                    isSecondTitleVisible = isSecondTitleVisible,
                    firstTitleColorInExpandedState = firstTitleColorInExpandedState,
                    firstTitleColorInCollapsedState = firstTitleColorInCollapsedState,
                ),
                name = TransitionName,
            )
        }
    }
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
    val textHorizontalMargin = spaces.spacing_3_5
    val firstTitleMarginTop = textHorizontalMargin
    val firstTitleMarginEnd = spaces.spacing_1_0
    val releaseDateMarginTop = spaces.spacing_2_5
    val bottomBarrierMargin = spaces.spacing_1_5

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
            bottom.linkTo(refs.rating.bottom)
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
            start.linkTo(refs.cover.end, textHorizontalMargin)
            end.linkTo(refs.likeButton.start, firstTitleMarginEnd)
            setScale(FirstTitleScaleExpanded)
            customColor(CustomAttributeTextColor, firstTitleTextColor)
        }
        constrain(refs.secondTitle) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.firstTitle.bottom)
            start.linkTo(refs.cover.end, textHorizontalMargin)
            end.linkTo(parent.end, textHorizontalMargin)
            isVisible = isSecondTitleVisible
        }
        constrain(refs.releaseDate) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.secondTitle.bottom, releaseDateMarginTop, releaseDateMarginTop)
            start.linkTo(refs.cover.end, textHorizontalMargin)
            end.linkTo(parent.end, textHorizontalMargin)
        }
        constrain(refs.developerName) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.releaseDate.bottom)
            start.linkTo(refs.cover.end, textHorizontalMargin)
            end.linkTo(parent.end, textHorizontalMargin)
        }
        constrain(refs.rating) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            linkTo(start = parent.start, end = refs.likeCount.start, bias = 0.25f)
        }
        constrain(refs.likeCount) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            linkTo(start = refs.rating.end, end = refs.ageRating.start, bias = 0.25f)
        }
        constrain(refs.ageRating) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            linkTo(start = refs.likeCount.end, end = refs.gameCategory.start, bias = 0.25f)
        }
        constrain(refs.gameCategory) {
            width = Dimension.fillToConstraints
            top.linkTo(bottomBarrier)
            linkTo(start = refs.ageRating.end, end = parent.end, bias = 0.25f)
        }
    }
}

private fun MotionSceneScope.constructCollapsedConstraintSet(
    refs: ConstraintLayoutRefs,
    spaces: Spaces,
    hasDefaultPlaceholderArtwork: Boolean,
    isSecondTitleVisible: Boolean,
    artworksHeight: Dp,
    statusBarHeight: Dp,
    firstTitleTextColor: Color,
): ConstraintSet {
    val pageIndicatorMargin = spaces.spacing_2_5
    val backdropElevation = spaces.spacing_1_0
    val coverSpaceMargin = CoverSpace
    val coverMarginStart = spaces.spacing_3_5
    val likeBtnMarginEnd = spaces.spacing_2_5
    val textHorizontalMargin = spaces.spacing_3_5
    val firstTitleMarginStart = spaces.spacing_7_5
    val firstTitleMarginEnd = spaces.spacing_6_0
    val releaseDateMarginTop = spaces.spacing_2_5

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
            centerVerticallyTo(refs.rating)
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
            // We need to set it to Gone to avoid the cover taking up the vertical space,
            // which increases the size of the header in collapsed state
            visibility = Visibility.Gone
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
            start.linkTo(refs.firstTitle.start)
            end.linkTo(parent.end, textHorizontalMargin)
            isVisible = isSecondTitleVisible
            alpha = 0f
        }
        constrain(refs.releaseDate) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.secondTitle.bottom, releaseDateMarginTop, releaseDateMarginTop)
            start.linkTo(refs.firstTitle.start)
            end.linkTo(parent.end, textHorizontalMargin)
            alpha = 0f
        }
        constrain(refs.developerName) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.releaseDate.bottom)
            start.linkTo(refs.firstTitle.start)
            end.linkTo(parent.end, textHorizontalMargin)
            alpha = 0f
        }
        constrain(refs.rating) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom)
            linkTo(start = parent.start, end = refs.likeCount.start, bias = 0.25f)
        }
        constrain(refs.likeCount) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom)
            linkTo(start = refs.rating.end, end = refs.ageRating.start, bias = 0.25f)
        }
        constrain(refs.ageRating) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom)
            linkTo(start = refs.likeCount.end, end = refs.gameCategory.start, bias = 0.25f)
        }
        constrain(refs.gameCategory) {
            width = Dimension.fillToConstraints
            top.linkTo(refs.artworks.bottom)
            linkTo(start = refs.ageRating.end, end = parent.end, bias = 0.25f)
        }
    }
}

private fun MotionSceneScope.constructTransition(
    refs: ConstraintLayoutRefs,
    isSecondTitleVisible: Boolean,
    firstTitleColorInExpandedState: Color,
    firstTitleColorInCollapsedState: Color,
): Transition {
    return Transition(from = ConstraintSetNameExpanded, to = ConstraintSetNameCollapsed) {
        // Don't scale the first title until the secondary texts (second title,
        // release date and developer name) is gone
        keyAttributes(refs.firstTitle) {
            frame(frame = 15) {
                setScale(FirstTitleScaleExpanded)
            }
        }
        keyAttributes(refs.secondTitle) {
            frame(frame = 15) {
                alpha = 0f
            }
        }
        keyAttributes(refs.releaseDate) {
            frame(frame = 15) {
                alpha = 0f
            }
        }
        keyAttributes(refs.developerName) {
            frame(frame = 15) {
                alpha = 0f
            }
        }
        keyAttributes(refs.cover) {
            frame(frame = 50) {
                alpha = 0f
                translationX = CoverDeltaXCollapsed
                translationY = CoverDeltaYCollapsed
            }
        }
        keyPositions(refs.cover) {
            frame(frame = 50) {
                setSizePercentage(0f)
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

        if (isSecondTitleVisible) {
            // To prevent the first title overlapping with the like button
            keyPositions(refs.firstTitle) {
                frame(frame = 60) {
                    percentWidth = 0.5f
                }
            }
        }

        keyAttributes(refs.likeButton) {
            frame(frame = 60) {
                alpha = 0f
                setScale(LikeButtonScaleCollapsed)
            }
        }
        keyAttributes(refs.pageIndicator) {
            frame(frame = 80) {
                translationX = PageIndicatorDeltaXCollapsed
            }
        }
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

private fun KeyAttributeScope.setScale(scale: Float) {
    scaleX = scale
    scaleY = scale
}

private fun KeyPositionScope.setSizePercentage(percentage: Float) {
    percentWidth = percentage
    percentHeight = percentage
}

private fun Modifier.drawOnTop(): Modifier {
    return zIndex(Float.MAX_VALUE)
}

/**
 * Calculates a duration for the auto transition in the following way:
 * - for progress that is zero, the duration is minimal (0f -> min)
 * - for progress that is half way, the duration is maximal (0.5f - max)
 * - for progress that is one, the duration is minimal (1f - min)
 **/
private fun calculateAutoTransitionDuration(progress: Float): Int {
    val minDuration = AutoTransitionAnimationDurationMin
    val maxDuration = AutoTransitionAnimationDurationMax

    return (minDuration + (maxDuration - minDuration) * 4 * progress * (1 - progress)).toInt()
}

private fun calculateHeaderHeightGivenProgress(
    progress: Float,
    minHeaderHeight: Float,
    maxHeaderHeight: Float,
): Float {
    return minHeaderHeight + (1 - progress) * (maxHeaderHeight - minHeaderHeight)
}

private fun calculateProgressGivenHeaderHeight(
    headerHeight: Float,
    minHeaderHeight: Float,
    maxHeaderHeight: Float,
): Float {
    return 1 - (headerHeight - minHeaderHeight) / (maxHeaderHeight - minHeaderHeight)
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
