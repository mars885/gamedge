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

package com.paulrybitskyi.gamedge.feature.info.widgets.screenshots

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.getDrawable
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.image.loading.Config
import com.paulrybitskyi.gamedge.image.loading.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class GameScreenshotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private val defaultImage = checkNotNull(getDrawable(R.drawable.game_landscape_placeholder))

    private lateinit var imageView: AppCompatImageView

    @Inject lateinit var imageLoader: ImageLoader


    init {
        initCard()
        initImageView(context)
    }


    private fun initCard() {
        setCardBackgroundColor(getColor(R.color.game_screenshot_card_background_color))
        cardElevation = getDimension(R.dimen.game_screenshot_card_elevation)
        radius = getDimension(R.dimen.game_screenshot_card_corner_radius)
    }


    private fun initImageView(context: Context) {
        imageView = AppCompatImageView(context)
            .apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            .also(::addView)
    }


    fun loadImage(url: String) {
        imageLoader.loadImage(
            Config.Builder()
                .fit()
                .imageUrl(url)
                .target(imageView)
                .progressDrawable(defaultImage)
                .errorDrawable(defaultImage)
                .build()
        )
    }


}