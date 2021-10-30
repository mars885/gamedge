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

package com.paulrybitskyi.gamedge.commons.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.ktx.getDrawable
import com.paulrybitskyi.commons.ktx.getString
import com.paulrybitskyi.commons.ktx.setHorizontalPadding
import com.paulrybitskyi.commons.ktx.views.setFontFamily
import com.paulrybitskyi.commons.ktx.views.setTextSizeInPx
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.image.loading.Config
import com.paulrybitskyi.gamedge.image.loading.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GameCoverView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    var isTitleVisible: Boolean = true
        set(value) {
            field = value
            titleTv.isVisible = value
        }

    var title: CharSequence
        set(value) { titleTv.text = value }
        get() = titleTv.text

    var imageUrl by observeChanges<String?>(null) { oldValue, newValue ->
        if((coverIv.drawable == null) || (oldValue != newValue)) loadImage(newValue)
    }

    private var defaultDrawable = checkNotNull(getDrawable(R.drawable.game_cover_placeholder))

    private lateinit var titleTv: AppCompatTextView
    private lateinit var coverIv: AppCompatImageView

    @Inject lateinit var imageLoader: ImageLoader


    init {
        initCard()
        initUi(context)
    }


    private fun initCard() {
        setCardBackgroundColor(getColor(R.color.game_cover_card_background_color))
        cardElevation = getDimension(R.dimen.game_cover_card_elevation)
        radius = getDimension(R.dimen.game_cover_card_corner_radius)
    }


    private fun initUi(context: Context) {
        initCoverImageView(context)
        initTitleTextView(context)
    }


    private fun initTitleTextView(context: Context) {
        titleTv = AppCompatTextView(context)
            .apply {
                layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
                setHorizontalPadding(getDimensionPixelSize(R.dimen.game_cover_title_horizontal_padding))
                setTextSizeInPx(getDimension(R.dimen.game_cover_title_text_size))
                setTextColor(getColor(R.color.game_cover_title_text_color))
                setFontFamily(getString(R.string.text_font_family_medium))
                gravity = Gravity.CENTER
            }
            .also(::addView)
    }


    private fun initCoverImageView(context: Context) {
        coverIv = AppCompatImageView(context)
            .apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            .also(::addView)
    }


    private fun loadImage(url: String?) {
        if(url == null) {
            showDefaultImage()
            return
        }

        imageLoader.loadImage(
            Config.Builder()
                .fit()
                .centerCrop()
                .imageUrl(url)
                .target(coverIv)
                .progressDrawable(defaultDrawable)
                .errorDrawable(defaultDrawable)
                .onStart(::onImageLoadingStarted)
                .onSuccess(::onImageLoadingSucceeded)
                .onFailure(::onImageLoadingFailed)
                .build()
        )
    }


    private fun showDefaultImage() {
        showTitle()
        coverIv.setImageDrawable(defaultDrawable)
    }


    private fun onImageLoadingStarted() {
        showTitle()
    }


    private fun onImageLoadingSucceeded() {
        hideTitle()
    }


    @Suppress("UNUSED_PARAMETER")
    private fun onImageLoadingFailed(error: Exception) {
        showTitle()
    }


    private fun showTitle() {
        if(!isTitleVisible) return

        titleTv.isVisible = true
    }


    private fun hideTitle() {
        if(!isTitleVisible) return

        titleTv.isVisible = false
    }


    fun disableRoundCorners() {
        radius = 0f
    }


}
