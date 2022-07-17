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

package com.paulrybitskyi.gamedge.common.ui.widgets.games

import androidx.compose.runtime.Immutable
import com.paulrybitskyi.gamedge.common.ui.widgets.FiniteUiState

@Immutable
data class GamesUiState(
    val isLoading: Boolean,
    val infoIconId: Int,
    val infoTitle: String,
    val games: List<GameUiModel>,
)

val GamesUiState.finiteUiState: FiniteUiState
    get() = when {
        isInEmptyState -> FiniteUiState.Empty
        isInLoadingState -> FiniteUiState.Loading
        isInSuccessState -> FiniteUiState.Success
        else -> error("Unknown games UI state.")
    }

private val GamesUiState.isInEmptyState: Boolean
    get() = (!isLoading && games.isEmpty())

private val GamesUiState.isInLoadingState: Boolean
    get() = (isLoading && games.isEmpty())

private val GamesUiState.isInSuccessState: Boolean
    get() = games.isNotEmpty()

val GamesUiState.isRefreshing: Boolean
    get() = (isLoading && games.isNotEmpty())

fun GamesUiState.toEmptyState(): GamesUiState {
    return copy(isLoading = false, games = emptyList())
}

fun GamesUiState.toLoadingState(): GamesUiState {
    return copy(isLoading = true)
}

fun GamesUiState.toSuccessState(games: List<GameUiModel>): GamesUiState {
    return copy(isLoading = false, games = games)
}
