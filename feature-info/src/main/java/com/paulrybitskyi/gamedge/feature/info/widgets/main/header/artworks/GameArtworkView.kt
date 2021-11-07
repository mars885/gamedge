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

package com.paulrybitskyi.gamedge.feature.info.widgets.main.header.artworks

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.paulrybitskyi.commons.ktx.getDrawable
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.image.loading.Config
import com.paulrybitskyi.gamedge.image.loading.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class GameArtworkView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var defaultBackgroundDrawable = checkNotNull(getDrawable(R.drawable.game_background_placeholder))

    var model by observeChanges<GameArtworkModel?>(null) { _, newModel ->
        newModel?.let(::onModelChanged)
    }

    @Inject lateinit var imageLoader: ImageLoader

    init {
        scaleType = ScaleType.CENTER_CROP
    }

    private fun onModelChanged(newModel: GameArtworkModel) {
        when (newModel) {
            is GameArtworkModel.DefaultImage -> loadDefaultImage()
            is GameArtworkModel.UrlImage -> loadUrlImage(newModel.url)
        }
    }

    private fun loadDefaultImage() {
        setImageDrawable(defaultBackgroundDrawable)
    }

    private fun loadUrlImage(url: String) {
        imageLoader.loadImage(
            Config.Builder()
                .fit()
                .centerCrop()
                .imageUrl(url)
                .target(this)
                .progressDrawable(defaultBackgroundDrawable)
                .errorDrawable(defaultBackgroundDrawable)
                .build()
        )
    }
}
