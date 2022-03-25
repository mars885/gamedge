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

package com.paulrybitskyi.gamedge.commons.ui.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource

private const val ANIMATION_DURATION = 500

@Composable
fun AnimatedContentContainer(
    finiteUiState: FiniteUiState,
    exitTransition: ExitTransition = fadeOut(animationSpec = tween(ANIMATION_DURATION)),
    enterTransition: EnterTransition = fadeIn(animationSpec = tween(ANIMATION_DURATION)),
    content: @Composable BoxScope.(FiniteUiState) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorContentContainer))
    ) {
        AnimatedContent(
            targetState = finiteUiState,
            transitionSpec = {
                val finalExitTransition = when (initialState) {
                    FiniteUiState.EMPTY,
                    FiniteUiState.LOADING -> ExitTransition.None
                    FiniteUiState.SUCCESS -> exitTransition
                }
                val finalEnterTransition = when (targetState) {
                    FiniteUiState.LOADING -> EnterTransition.None
                    FiniteUiState.EMPTY,
                    FiniteUiState.SUCCESS -> enterTransition
                }

                finalEnterTransition with finalExitTransition
            },
        ) { targetUiState ->
            Box(Modifier.fillMaxSize()) {
                content(targetUiState)
            }
        }
    }
}

enum class FiniteUiState {
    EMPTY,
    LOADING,
    SUCCESS,
}
