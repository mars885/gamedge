/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.commons.ui.widgets.header

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGameHeaderBinding

class GameHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private val iconColor =  getColor(R.color.game_header_like_btn_icon_color)
    private val unlikedIcon = getColoredDrawable(R.drawable.heart_outline, iconColor)
    private val likedIcon = getColoredDrawable(R.drawable.heart, iconColor)

    var isLiked: Boolean = false
        set(value) {
            field = value
            binding.likeBtn.setImageDrawable(if(value) likedIcon else unlikedIcon)
        }

    private var isSecondTitleVisible: Boolean
        set(value) { binding.secondTitleTv.isVisible = value }
        get() = binding.secondTitleTv.isVisible

    private var isDeveloperNameEnabled: Boolean
        set(value) { binding.developerNameTv.isEnabled = value }
        get() = binding.developerNameTv.isEnabled

    private var isDeveloperNameVisible: Boolean
        set(value) { binding.developerNameTv.isVisible = (value && isDeveloperNameEnabled) }
        get() = binding.developerNameTv.isVisible

    var coverImageUrl: String? = null
        set(value) {
            field = value
            binding.coverView.imageUrl = value
        }

    var title by observeChanges("") { oldTitle, newTitle ->
        onTitleChanged(oldTitle, newTitle)
    }

    var releaseDate: CharSequence
        set(value) { binding.releaseDateTv.text = value }
        get() = binding.releaseDateTv.text

    var developerName: CharSequence?
        set(value) {
            binding.developerNameTv.text = value
            isDeveloperNameVisible = (value != null)
        }
        get() = binding.developerNameTv.text

    var rating: CharSequence
        set(value) { binding.ratingIv.titleText = value }
        get() = binding.ratingIv.titleText

    var likeCount: CharSequence
        set(value) { binding.likeCountIv.titleText = value }
        get() = binding.likeCountIv.titleText

    var ageRating: CharSequence
        set(value) { binding.ageRatingIv.titleText = value }
        get() = binding.ageRatingIv.titleText

    var gameCategory: CharSequence
        set(value) { binding.gameCategoryIv.titleText = value }
        get() = binding.gameCategoryIv.titleText

    var backgroundImageModels by observeChanges<List<GameHeaderImageModel>>(emptyList()) { oldItems, newItems ->
        if(oldItems != newItems) binding.artworksView.artworkModels = newItems.mapToGameArtworkModels()
    }

    private val binding = ViewGameHeaderBinding.inflate(context.layoutInflater, this)

    var onBackButtonClickListener: (() -> Unit)? = null
    var onLikeButtonClickListener: (() -> Unit)? = null


    init {
        initCard()
        initBackButton()
        initCoverView()
        initLikeButton()
    }


    private fun initCard() {
        setBackgroundColor(getColor(R.color.game_header_card_background_color))
        cardElevation = getDimension(R.dimen.game_header_card_elevation)
    }


    private fun initBackButton() {
        binding.backBtnIv.onClick {
            onBackButtonClickListener?.invoke()
        }
    }


    private fun initCoverView() {
        binding.coverView.isTitleVisible = false
    }


    private fun initLikeButton() {
        binding.likeBtn.onClick {
            onLikeButtonClickListener?.invoke()
        }
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
                secondTitleTv.doOnPreDraw {
                    val isSecondTitleOneLiner = (binding.secondTitleTv.lineCount == 1)

                    isDeveloperNameEnabled = isSecondTitleOneLiner
                    isDeveloperNameVisible = isSecondTitleOneLiner
                }
            }
        }
    }


    fun applyWindowTopOffset() {
        binding.artworksView.applyWindowTopOffset()
        binding.backBtnIv.applyWindowTopInsetAsMargin()
    }


}