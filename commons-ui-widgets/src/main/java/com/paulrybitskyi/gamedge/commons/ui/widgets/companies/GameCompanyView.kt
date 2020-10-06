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

package com.paulrybitskyi.gamedge.commons.ui.widgets.companies

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGameCompanyBinding
import com.paulrybitskyi.gamedge.core.utils.*
import com.paulrybitskyi.gamedge.image.loading.Config
import com.paulrybitskyi.gamedge.image.loading.ImageLoader
import com.paulrybitskyi.gamedge.image.loading.Transformation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class GameCompanyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private val defaultImage = checkNotNull(getDrawable(R.drawable.game_landscape_placeholder))

    private val binding = ViewGameCompanyBinding.inflate(context.layoutInflater, this)

    var logoImageWidth = -1
    var logoImageHeight = -1

    var logoViewWidth: Int
        set(value) { binding.logoIv.layoutParamsWidth = value }
        get() = binding.logoIv.layoutParamsWidth

    var logoViewHeight: Int
        set(value) { binding.logoIv.layoutParamsHeight = value }
        get() = binding.logoIv.layoutParamsHeight

    var logoUrl by observeChanges<String?>(null) { _, newUrl ->
        if(newUrl != null) loadLogo(newUrl) else loadDefaultImage()
    }

    var name: CharSequence
        set(value) { binding.nameTv.text = value }
        get() = binding.nameTv.text

    var roles: CharSequence
        set(value) { binding.rolesTv.text = value }
        get() = binding.rolesTv.text

    private var logoScaleType: ImageView.ScaleType
        set(value) { binding.logoIv.scaleType = value }
        get() = binding.logoIv.scaleType

    @Inject lateinit var imageLoader: ImageLoader

    var onCompanyClickListener: (() -> Unit)? = null


    init {
        initCard()
    }


    private fun initCard() {
        setCardBackgroundColor(getColor(R.color.game_company_card_background_color))
        cardElevation = getDimension(R.dimen.game_company_card_elevation)
        radius = getDimension(R.dimen.game_company_card_corner_radius)
        onClick { onCompanyClickListener?.invoke() }
    }


    private fun loadLogo(url: String) {
        resetLogoScaleType()

        imageLoader.loadImage(
            Config.Builder()
                .resize(logoImageWidth, logoImageHeight)
                .imageUrl(url)
                .target(binding.logoIv)
                .progressDrawable(defaultImage)
                .errorDrawable(defaultImage)
                .transformation(LogoImageTransformation(logoViewWidth, logoViewHeight))
                .onSuccess(::onLogoLoadingSucceeded)
                .build()
        )
    }


    private fun resetLogoScaleType() {
        logoScaleType = ImageView.ScaleType.CENTER_CROP
    }


    private fun onLogoLoadingSucceeded() {
        logoScaleType = ImageView.ScaleType.CENTER_INSIDE
    }


    private fun loadDefaultImage() {
        resetLogoScaleType()

        binding.logoIv.setImageDrawable(defaultImage)
    }


}


private class LogoImageTransformation(
    private val logoViewWidth: Int,
    private val logoViewHeight: Int
) : Transformation {


    private companion object {

        private const val TARGET_BITMAP_SCALE_FACTOR = 0.85f

    }


    override val key = "logo: w - $logoViewWidth, h - $logoViewHeight"


    override fun transform(source: Bitmap): Bitmap {
        val targetBitmap = Bitmap
            .createBitmap(logoViewWidth, logoViewHeight, source.config)
            .apply { eraseColor(source.calculateFillColor()) }

        val targetCenterX = targetBitmap.centerX
        val targetCenterY = targetBitmap.centerY
        val sourceOffsetLeft = (targetCenterX - source.centerX)
        val sourceOffsetTop = (targetCenterY - source.centerY)

        return Canvas(targetBitmap)
            .apply {
                scale(
                    TARGET_BITMAP_SCALE_FACTOR,
                    TARGET_BITMAP_SCALE_FACTOR,
                    targetCenterX,
                    targetCenterY
                )

                drawBitmap(source, sourceOffsetLeft, sourceOffsetTop, null)
            }
            .also { source.recycle() }
            .let { targetBitmap }
    }


    private fun Bitmap.calculateFillColor(): Int {
        if(hasTransparentPixels()) return Color.WHITE


        var pixelColor: Int

        for(x in 10..centerX.toInt()) {
            for(y in 10..centerY.toInt()) {
                pixelColor = getPixel(x, y)

                if(pixelColor.isOpaque) return pixelColor
            }
        }

        return Color.WHITE
    }


}