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

package com.paulrybitskyi.gamedge.feature.image.viewer.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.feature.image.viewer.R
import com.paulrybitskyi.gamedge.feature.image.viewer.databinding.ViewImageViewerItewBinding
import com.paulrybitskyi.gamedge.image.loading.Config
import com.paulrybitskyi.gamedge.image.loading.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val DEFAULT_MIN_SCALE = 1f
private const val DEFAULT_MID_SCALE = 1.75f
private const val DEFAULT_MAX_SCALE = 3f

@AndroidEntryPoint
internal class ImageViewerItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewImageViewerItewBinding.inflate(context.layoutInflater, this)

    private var isInfoViewVisible: Boolean
        set(value) { binding.infoView.isVisible = value }
        get() = binding.infoView.isVisible

    private var infoViewTitleText: CharSequence
        set(value) { binding.infoView.titleText = value }
        get() = binding.infoView.titleText

    val isScaled: Boolean
        get() = (binding.photoView.scale != DEFAULT_MIN_SCALE)

    var imageUrl by observeChanges<String?>(null) { _, newModel ->
        newModel?.let(::loadUrlImage)
    }

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var stringProvider: StringProvider
    @Inject lateinit var networkStateProvider: NetworkStateProvider

    init {
        initPhotoView()
    }

    private fun initPhotoView() = with(binding.photoView) {
        minimumScale = DEFAULT_MIN_SCALE
        mediumScale = DEFAULT_MID_SCALE
        maximumScale = DEFAULT_MAX_SCALE
    }

    private fun loadUrlImage(url: String) {
        imageLoader.loadImage(
            Config.Builder()
                .fit()
                .centerInside()
                .imageUrl(url)
                .target(binding.photoView)
                .onSuccess(::onImageLoadingSucceeded)
                .onFailure(::onImageLoadingFailed)
                .build()
        )
    }

    private fun onImageLoadingSucceeded() {
        isInfoViewVisible = false
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onImageLoadingFailed(error: Exception) {
        infoViewTitleText = stringProvider.getString(
            if (!networkStateProvider.isNetworkAvailable) {
                R.string.error_no_network_message
            } else {
                R.string.error_unknown_message
            }
        )

        isInfoViewVisible = true
    }

    fun resetScale(animate: Boolean) {
        binding.photoView.setScale(DEFAULT_MIN_SCALE, animate)
    }
}
