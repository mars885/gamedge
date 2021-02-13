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

package com.paulrybitskyi.gamedge.feature.info.widgets.main.header

import android.content.Context
import android.text.TextUtils
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.extensions.addTransitionListener
import com.paulrybitskyi.gamedge.commons.ui.extensions.updateConstraintSets
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.databinding.ViewGameInfoBinding
import com.paulrybitskyi.gamedge.feature.info.widgets.main.mapToGameArtworkModels
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoHeaderModel

internal class GameHeaderController(
    context: Context,
    private val binding: ViewGameInfoBinding
) {


    private val iconColor =  context.getCompatColor(R.color.game_info_header_like_btn_icon_color)
    private val unlikedIcon = context.getColoredDrawable(R.drawable.heart_outline, iconColor)
    private val likedIcon = context.getColoredDrawable(R.drawable.heart, iconColor)

    private val hasDefaultBackgroundImage: Boolean
        get() = (
            (backgroundImageModels.size == 1) &&
            (backgroundImageModels.single() is GameHeaderImageModel.DefaultImage)
        )

    private var isLiked: Boolean = false
        set(value) {
            field = value
            binding.likeBtn.setImageDrawable(if(value) likedIcon else unlikedIcon)
        }

    private var isSecondTitleVisible: Boolean
        set(value) { binding.secondTitleTv.isVisible = value }
        get() = binding.secondTitleTv.isVisible

    private var isDeveloperNameVisible: Boolean
        set(value) { binding.developerNameTv.isVisible = value }
        get() = binding.developerNameTv.isVisible

    private var coverImageUrl: String? = null
        set(value) {
            field = value
            binding.coverView.imageUrl = value
        }

    private var title by observeChanges("") { oldTitle, newTitle ->
        onTitleChanged(oldTitle, newTitle)
    }

    private var releaseDate: CharSequence
        set(value) { binding.releaseDateTv.text = value }
        get() = binding.releaseDateTv.text

    private var developerName: CharSequence?
        set(value) {
            binding.developerNameTv.text = value
            isDeveloperNameVisible = (value != null)
        }
        get() = binding.developerNameTv.text

    private var rating: CharSequence
        set(value) { binding.ratingIv.titleText = value }
        get() = binding.ratingIv.titleText

    private var likeCount: CharSequence
        set(value) { binding.likeCountIv.titleText = value }
        get() = binding.likeCountIv.titleText

    private var ageRating: CharSequence
        set(value) { binding.ageRatingIv.titleText = value }
        get() = binding.ageRatingIv.titleText

    private var gameCategory: CharSequence
        set(value) { binding.gameCategoryIv.titleText = value }
        get() = binding.gameCategoryIv.titleText

    private var backgroundImageModels by observeChanges<List<GameHeaderImageModel>>(emptyList()) { _, newItems ->
        disableScrimConstraintIfNeeded()

        binding.artworksView.artworkModels = newItems.mapToGameArtworkModels()
    }

    var onBackButtonClickListener: (() -> Unit)? = null
    var onLikeButtonClickListener: (() -> Unit)? = null


    init {
        initMotionLayout()
        initArtworksView()
        initBackButton()
        initCoverView()
        initLikeButton()
    }


    private fun initMotionLayout() {
        // Fixes a weird behavior where interacting with one UI element actually
        // interacts with another one (e.g., swiping and clicking on the "Details"
        // card causes items of the "Screenshots" card to scroll)
        // https://stackoverflow.com/questions/59504422/motionlayout-problems-with-children-intercepting-touch-events
        binding.mainView.isInteractionEnabled = false

        initMotionLayoutListener()
        initMotionLayoutInsets()
    }


    private fun initMotionLayoutListener() {
        binding.mainView.addTransitionListener(
            onTransitionStarted =  { startId, _ ->
                if(startId == R.id.expanded) {
                    binding.artworksView.hidePageIndicator()
                }
            },
            onTransitionChange = { _, _, progress ->
                if((binding.firstTitleTv.ellipsize != TextUtils.TruncateAt.END) && (progress > 0.95f)) {
                    binding.firstTitleTv.ellipsize = TextUtils.TruncateAt.END
                }

                if((binding.firstTitleTv.ellipsize != null) && (progress < 0.95f)) {
                    binding.firstTitleTv.ellipsize = null
                }
            },
            onTransitionCompleted = {
                binding.artworksView.isScrollingEnabled = (binding.mainView.progress == 0f)
            }
        )
    }


    private fun initMotionLayoutInsets() = with(binding.mainView) {
        // Traditional inset applying does not work with views that
        // are direct children of MotionLayout. Have to manually update
        // the constraint sets within it.
        doOnApplyWindowInsets(DimensionSnapshotType.MARGINS) { _, insets, _ ->
            updateConstraintSets { id, set ->
                set.setMargin(R.id.backBtnIv, ConstraintSet.TOP, insets.systemWindowInsetTop)

                if(id == R.id.collapsed) {
                    val toolbarHeight = getDimensionPixelSize(R.dimen.toolbar_height)
                    val statusBarHeight = insets.systemWindowInsetTop
                    val totalHeight = (toolbarHeight + statusBarHeight)

                    set.setMargin(R.id.firstTitleTv, ConstraintSet.TOP, statusBarHeight)
                    set.constrainHeight(R.id.artworksView, totalHeight)
                }
            }
        }
    }


    private fun initArtworksView() {
        binding.artworksView.applyWindowTopInset()
    }


    private fun initBackButton() = with(binding.backBtnIv) {
        onClick { onBackButtonClickListener?.invoke() }
    }


    private fun initCoverView() = with(binding.coverView) {
        cardElevation = getDimension(R.dimen.game_info_header_backdrop_elevation)
        isTitleVisible = false
    }


    private fun initLikeButton() {
        binding.likeBtn.onClick { onLikeButtonClickListener?.invoke() }
    }


    private fun disableScrimConstraintIfNeeded() {
        if(!hasDefaultBackgroundImage) return

        binding.mainView.updateConstraintSets { _, constraintSet ->
            constraintSet.clear(R.id.artworksScrimView)
        }

        binding.artworksScrimView.makeGone()
    }


    private fun onTitleChanged(oldTitle: String, newTitle: String) {
        if((oldTitle == newTitle) || newTitle.isBlank()) return

        val firstTitleTv = binding.firstTitleTv
        val secondTitleTv = binding.secondTitleTv

        firstTitleTv.text = newTitle

        firstTitleTv.doOnPreDraw {
            if(firstTitleTv.lineCount == 1) {
                isSecondTitleVisible = false
            } else {
                val firstTitleWidth = firstTitleTv.width.toFloat()
                val firstTitleVisibleTextEndIndex = (firstTitleTv.getOffsetForPosition(firstTitleWidth, 0f) + 1)
                val secondTitleText = newTitle.substring(firstTitleVisibleTextEndIndex)

                secondTitleTv.text = secondTitleText
            }
        }
    }


    fun bindModel(model: GameInfoHeaderModel) {
        isLiked = model.isLiked

        if(backgroundImageModels != model.backgroundImageModels) {
            backgroundImageModels = model.backgroundImageModels
        }

        if(coverImageUrl != model.coverImageUrl) coverImageUrl = model.coverImageUrl
        if(title != model.title) title = model.title
        if(releaseDate != model.releaseDate) releaseDate = model.releaseDate
        if(developerName != model.developerName) developerName = model.developerName
        if(rating != model.rating) rating = model.rating
        if(likeCount != model.likeCount) likeCount = model.likeCount
        if(ageRating != model.ageRating) ageRating = model.ageRating
        if(gameCategory != model.gameCategory) gameCategory = model.gameCategory
    }


}