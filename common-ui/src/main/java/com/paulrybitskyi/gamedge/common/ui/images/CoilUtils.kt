/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.common.ui.images

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest

private const val CROSSFADE_ANIMATION_DURATION = 200

@Composable
fun defaultImageRequest(
    data: Any?,
    builder: ImageRequest.Builder.() -> Unit,
): ImageRequest {
    return ImageRequest.Builder(LocalContext.current)
        .data(data)
        .crossfade(CROSSFADE_ANIMATION_DURATION)
        .apply(builder)
        .build()
}

fun ImageRequest.Builder.secondaryImage(
    @DrawableRes drawableResId: Int,
): ImageRequest.Builder = apply {
    placeholder(drawableResId)
    fallback(drawableResId)
    error(drawableResId)
}
