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

package com.paulrybitskyi.gamedge.feature.info.presentation

import androidx.compose.runtime.Immutable
import com.paulrybitskyi.gamedge.common.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.main.GameInfoUiModel

@Immutable
internal data class GameInfoUiState(
    val isLoading: Boolean,
    val game: GameInfoUiModel?,
)

internal val GameInfoUiState.finiteUiState: FiniteUiState
    get() = when {
        isInEmptyState -> FiniteUiState.Empty
        isInLoadingState -> FiniteUiState.Loading
        isInSuccessState -> FiniteUiState.Success
        else -> error("Unknown game info UI state.")
    }

private val GameInfoUiState.isInEmptyState: Boolean
    get() = (!isLoading && (game == null))

private val GameInfoUiState.isInLoadingState: Boolean
    get() = (isLoading && (game == null))

private val GameInfoUiState.isInSuccessState: Boolean
    get() = (game != null)

internal fun GameInfoUiState.toEmptyState(): GameInfoUiState {
    return copy(isLoading = false, game = null)
}

internal fun GameInfoUiState.toLoadingState(): GameInfoUiState {
    return copy(isLoading = true)
}

internal fun GameInfoUiState.toSuccessState(game: GameInfoUiModel): GameInfoUiState {
    return copy(isLoading = false, game = game)
}
