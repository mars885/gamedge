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

package com.paulrybitskyi.gamedge.commons.ui

import android.view.View
import android.view.animation.DecelerateInterpolator

private const val DATA_SET_ANIMATION_DURATION = 500L
private val DATA_SET_ANIMATION_INTERPOLATOR = DecelerateInterpolator(@Suppress("MagicNumber") 1.5F)

fun View.fadeIn() {
    animate()
        .alpha(1f)
        .setDuration(DATA_SET_ANIMATION_DURATION)
        .setInterpolator(DATA_SET_ANIMATION_INTERPOLATOR)
        .start()
}

fun View.resetAnimation() {
    cancelActiveAnimations()
    alpha = 0f
}

fun View.cancelActiveAnimations() {
    clearAnimation()
    animate().cancel()
}
