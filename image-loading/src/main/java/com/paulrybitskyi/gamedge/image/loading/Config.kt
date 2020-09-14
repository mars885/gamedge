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

package com.paulrybitskyi.gamedge.image.loading

import android.widget.ImageView

class Config private constructor(
    val shouldCenterInside: Boolean,
    val shouldCenterCrop: Boolean,
    val targetWidth: Int,
    val targetHeight: Int,
    val imageUrl: String,
    val destination: ImageView
) {


    internal val hasTargetSize: Boolean
        get() = ((targetWidth > 0) && (targetHeight > 0))


    class Builder {

        private var shouldCenterInside: Boolean = false
        private var shouldCenterCrop: Boolean = false
        private var targetWidth: Int = 0
        private var targetHeight: Int = 0
        private var imageUrl: String = ""
        private var destination: ImageView? = null

        fun centerCrop() = apply { shouldCenterCrop = true }

        fun centerInside() = apply { shouldCenterInside = true }

        fun resize(targetWidth: Int, targetHeight: Int) = apply {
            require(targetWidth > 0) { "The width must be larger tha 0." }
            require(targetHeight > 0) { "The height must be larger than 0." }

            this.targetWidth = targetWidth
            this.targetHeight = targetHeight
        }

        fun imageUrl(imageUrl: String) = apply {
            require(imageUrl.isNotBlank()) { "The image url is blank" }

            this.imageUrl = imageUrl
        }

        fun destination(destination: ImageView) = apply { this.destination = destination }

        fun build(): Config {
            check(imageUrl.isNotEmpty()) { "Image url is not set." }
            check(destination != null) { "Destination ImageView is not set." }

            return Config(
                shouldCenterInside = shouldCenterInside,
                shouldCenterCrop = shouldCenterCrop,
                targetWidth = targetWidth,
                targetHeight = targetHeight,
                imageUrl = imageUrl,
                destination = checkNotNull(destination)
            )
        }

    }


}