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

package com.paulrybitskyi.gamedge.feature.news.widgets

import androidx.compose.runtime.Immutable
import com.paulrybitskyi.gamedge.commons.ui.widgets.FiniteUiState

@Immutable
internal data class GamingNewsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val news: List<GamingNewsItemModel> = emptyList(),
)

internal val GamingNewsUiState.finiteUiState: FiniteUiState
    get() = when {
        isInEmptyState -> FiniteUiState.EMPTY
        isLoading -> FiniteUiState.LOADING
        isInSuccessState -> FiniteUiState.SUCCESS
        else -> error("Unknown gaming news UI state.")
    }

private val GamingNewsUiState.isInEmptyState: Boolean
    get() = (!isLoading && news.isEmpty())

private val GamingNewsUiState.isInSuccessState: Boolean
    get() = news.isNotEmpty()

internal fun GamingNewsUiState.toEmptyState(): GamingNewsUiState {
    return copy(isLoading = false, news = emptyList())
}

internal fun GamingNewsUiState.toLoadingState(): GamingNewsUiState {
    return copy(isLoading = true)
}

internal fun GamingNewsUiState.toSuccessState(
    news: List<GamingNewsItemModel>
): GamingNewsUiState {
    return copy(isLoading = false, news = news)
}

internal fun GamingNewsUiState.enableRefreshing(): GamingNewsUiState {
    return copy(isRefreshing = true)
}

internal fun GamingNewsUiState.disableRefreshing(): GamingNewsUiState {
    return copy(isRefreshing = false)
}
