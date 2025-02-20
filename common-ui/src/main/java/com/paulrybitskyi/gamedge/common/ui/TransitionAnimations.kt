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

package com.paulrybitskyi.gamedge.common.ui

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
        const val DefaultAnimationDuration = 300
    }

    fun enter(): EnterTransition
    fun exit(): ExitTransition
    fun popEnter(): EnterTransition
    fun popExit(): ExitTransition
}

data object OvershootScaling : TransitionAnimations {

    private const val FadingAnimationDuration = 100

    private const val MinScale = 0.9f
    private const val MaxScale = 1.1f

    private const val MinAlpha = 0f
    private const val MaxAlpha = 0.9f

    private val overshootInterpolator = OvershootInterpolator()

    override fun enter(): EnterTransition {
        return scaleIn(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            initialScale = MaxScale,
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = FadingAnimationDuration,
                easing = LinearEasing,
            ),
            initialAlpha = MaxAlpha,
        )
    }

    override fun exit(): ExitTransition {
        return scaleOut(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            targetScale = MinScale,
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = FadingAnimationDuration,
                easing = LinearEasing,
            ),
            targetAlpha = MaxAlpha,
        )
    }

    override fun popEnter(): EnterTransition {
        return scaleIn(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            initialScale = MinScale,
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = FadingAnimationDuration,
                easing = LinearEasing,
            ),
            initialAlpha = MaxAlpha,
        )
    }

    override fun popExit(): ExitTransition {
        return scaleOut(
            animationSpec = tween(
                durationMillis = TransitionAnimations.DefaultAnimationDuration,
                easing = Easing(overshootInterpolator::getInterpolation),
            ),
            targetScale = MaxScale,
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = FadingAnimationDuration,
                easing = LinearEasing,
            ),
            targetAlpha = MinAlpha,
        )
    }
}

data object HorizontalSliding : TransitionAnimations {

    private const val MinAlpha = 0.8f
    private const val MaxOffsetRatio = 0.25f

    override fun enter(): EnterTransition {
        return slideInHorizontally(
            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
            initialOffsetX = HorizontalSliding::calculateMinOffsetX,
        )
    }

    override fun exit(): ExitTransition {
        return fadeOut(
            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
            targetAlpha = MinAlpha,
        ) + slideOutHorizontally(
            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
            targetOffsetX = HorizontalSliding::calculateMaxOffsetX,
        )
    }

    override fun popEnter(): EnterTransition {
        return fadeIn(
            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
            initialAlpha = MinAlpha,
        ) + slideInHorizontally(
            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
            initialOffsetX = HorizontalSliding::calculateMaxOffsetX,
        )
    }

    override fun popExit(): ExitTransition {
        return slideOutHorizontally(
            animationSpec = tween(TransitionAnimations.DefaultAnimationDuration),
            targetOffsetX = HorizontalSliding::calculateMinOffsetX,
        )
    }

    private fun calculateMinOffsetX(fullWidth: Int): Int {
        return fullWidth
    }

    private fun calculateMaxOffsetX(fullWidth: Int): Int {
        return (-fullWidth * MaxOffsetRatio).roundToInt()
    }
}
