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

package com.paulrybitskyi.gamedge.feature.info.widgets.videos

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.databinding.ViewGameVideoBinding
import com.paulrybitskyi.gamedge.image.loading.Config
import com.paulrybitskyi.gamedge.image.loading.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class GameVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private val defaultImage = checkNotNull(getDrawable(R.drawable.game_landscape_placeholder))

    private val binding = ViewGameVideoBinding.inflate(context.layoutInflater, this)

    var thumbnailHeight: Int
        set(value) { binding.thumbnailIv.layoutParamsHeight = value }
        get() = binding.thumbnailIv.layoutParamsHeight

    var thumbnailUrl by observeChanges<String?>(null) { _, newItem ->
        newItem?.let(::loadThumbnail)
    }

    var title: CharSequence
        set(value) {
            binding.titleTv.isVisible = value.isNotBlank()
            binding.titleTv.text = value
        }
        get() = binding.titleTv.text

    @Inject lateinit var imageLoader: ImageLoader

    var onVideoClicked: (() -> Unit)? = null


    init {
        initCard()
    }


    private fun initCard() {
        setCardBackgroundColor(getColor(R.color.game_video_card_background_color))
        cardElevation = getDimension(R.dimen.game_video_card_elevation)
        radius = getDimension(R.dimen.game_video_card_corner_radius)
        onClick { onVideoClicked?.invoke() }
    }


    private fun loadThumbnail(url: String) {
        imageLoader.loadImage(
            Config.Builder()
                .fit()
                .imageUrl(url)
                .target(binding.thumbnailIv)
                .progressDrawable(defaultImage)
                .errorDrawable(defaultImage)
                .build()
        )
    }


}