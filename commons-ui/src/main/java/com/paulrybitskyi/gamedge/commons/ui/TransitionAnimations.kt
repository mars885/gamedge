/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import kotlin.math.roundToInt

sealed interface TransitionAnimations {

    companion object {
        const val DEFAULT_ANIMATION_DURATION = 300
    }

    fun enter(): EnterTransition
    fun exit(): ExitTransition
    fun popEnter(): EnterTransition
    fun popExit(): ExitTransition
}

object OvershootScaling : TransitionAnimations {

    private const val FADING_ANIM_DURATION = 100

    private const val MIN_SCALE = 0.9f
    private const val MAX_SCALE = 1.1f

    private const val MIN_ALPHA = 0f
    private const val MAX_ALPHA = 0.9f

    private val overshootInterpolator = OvershootInterpolator()

    override fun enter(): EnterTransition {
        return scaleIn(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DEFAULT_ANIMATION_DURATION,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            initialScale = MAX_SCALE,
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = FADING_ANIM_DURATION,
                easing = LinearEasing,
            ),
            initialAlpha = MAX_ALPHA,
        )
    }

    override fun exit(): ExitTransition {
        return scaleOut(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DEFAULT_ANIMATION_DURATION,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            targetScale = MIN_SCALE,
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = FADING_ANIM_DURATION,
                easing = LinearEasing,
            ),
            targetAlpha = MAX_ALPHA,
        )
    }

    override fun popEnter(): EnterTransition {
        return scaleIn(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DEFAULT_ANIMATION_DURATION,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            initialScale = MIN_SCALE,
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = FADING_ANIM_DURATION,
                easing = LinearEasing,
            ),
            initialAlpha = MAX_ALPHA,
        )
    }

    override fun popExit(): ExitTransition {
        return scaleOut(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DEFAULT_ANIMATION_DURATION,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            targetScale = MAX_SCALE,
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = FADING_ANIM_DURATION,
                easing = LinearEasing,
            ),
            targetAlpha = MIN_ALPHA,
        )
    }
}

object HorizontalSliding : TransitionAnimations {

    private const val MIN_ALPHA = 0.8f
    private const val MAX_OFFSET_RATIO = 0.25f

    override fun enter(): EnterTransition {
        return slideInHorizontally(
            animationSpec = tween(TransitionAnimations.DEFAULT_ANIMATION_DURATION),
            initialOffsetX = ::calculateMinOffsetX,
        )
    }

    override fun exit(): ExitTransition {
        return fadeOut(
            animationSpec = tween(TransitionAnimations.DEFAULT_ANIMATION_DURATION),
            targetAlpha = MIN_ALPHA,
        ) + slideOutHorizontally(
            animationSpec = tween(TransitionAnimations.DEFAULT_ANIMATION_DURATION),
            targetOffsetX = ::calculateMaxOffsetX,
        )
    }

    override fun popEnter(): EnterTransition {
        return fadeIn(
            animationSpec = tween(TransitionAnimations.DEFAULT_ANIMATION_DURATION),
            initialAlpha = MIN_ALPHA,
        ) + slideInHorizontally(
            animationSpec = tween(TransitionAnimations.DEFAULT_ANIMATION_DURATION),
            initialOffsetX = ::calculateMaxOffsetX,
        )
    }

    override fun popExit(): ExitTransition {
        return slideOutHorizontally(
            animationSpec = tween(TransitionAnimations.DEFAULT_ANIMATION_DURATION),
            targetOffsetX = ::calculateMinOffsetX,
        )
    }

    private fun calculateMinOffsetX(fullWidth: Int): Int {
        return fullWidth
    }

    private fun calculateMaxOffsetX(fullWidth: Int): Int {
        return (-fullWidth * MAX_OFFSET_RATIO).roundToInt()
    }
}
