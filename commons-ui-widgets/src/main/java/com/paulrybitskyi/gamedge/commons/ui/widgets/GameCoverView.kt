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
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.ktx.views.setFontFamily
import com.paulrybitskyi.commons.ktx.views.setTextSizeInPx
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


    private var isTitleVisible: Boolean
        set(value) { titleTv.isVisible = value }
        get() = titleTv.isVisible

    var title: CharSequence
        set(value) { titleTv.text = value }
        get() = titleTv.text

    private var defaultDrawable = checkNotNull(getDrawable(R.drawable.game_cover))

    private lateinit var titleTv: TextView
    private lateinit var coverIv: ImageView

    @Inject lateinit var imageLoader: ImageLoader


    init {
        initCard()
        initUi(context)
    }


    private fun initCard() {
        setCardBackgroundColor(getColor(R.color.game_cover_view_card_background_color))
        cardElevation = getDimension(R.dimen.game_cover_card_elevation)
        radius = getDimension(R.dimen.game_cover_card_corner_radius)
    }


    private fun initUi(context: Context) {
        initCoverImageView(context)
        initTitleTextView(context)
    }


    private fun initTitleTextView(context: Context) {
        titleTv = TextView(context)
            .apply {
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
                setHorizontalPadding(getDimensionPixelSize(R.dimen.game_cover_title_horizontal_padding))
                setTextSizeInPx(getDimension(R.dimen.game_cover_title_text_size))
                setTextColor(getColor(R.color.game_cover_view_title_text_color))
                setFontFamily(getString(R.string.text_font_family_medium))
                gravity = Gravity.CENTER
            }
            .also(::addView)
    }


    private fun initCoverImageView(context: Context) {
        coverIv = ImageView(context)
            .apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            .also(::addView)
    }


    fun loadImage(url: String?) {
        if(url == null) {
            showDefaultImage()
            return
        }

        imageLoader.loadImage(
            Config.Builder()
                .fit()
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
        isTitleVisible = true
        coverIv.setImageDrawable(defaultDrawable)
    }


    private fun onImageLoadingStarted() {
        isTitleVisible = true
    }


    private fun onImageLoadingSucceeded() {
        isTitleVisible = false
    }


    private fun onImageLoadingFailed(error: Exception) {
        isTitleVisible = true
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        //imageLoader.cancelRequests(coverIv)
    }


}