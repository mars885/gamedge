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

package com.paulrybitskyi.gamedge.feature.discovery.widgets

import androidx.compose.runtime.Immutable

@Immutable
internal data class GamesDiscoveryItemModel(
    val id: Int,
    val categoryName: String,
    val title: String,
    val isProgressBarVisible: Boolean,
    val games: List<GamesDiscoveryItemGameModel>,
)

internal fun List<GamesDiscoveryItemModel>.toSuccessState(
    games: List<List<GamesDiscoveryItemGameModel>>
): List<GamesDiscoveryItemModel> {
    return mapIndexed { index, itemModel ->
        itemModel.copy(games = games[index])
    }
}

internal fun List<GamesDiscoveryItemModel>.showProgressBar(): List<GamesDiscoveryItemModel> {
    return map { itemModel -> itemModel.copy(isProgressBarVisible = true) }
}

internal fun List<GamesDiscoveryItemModel>.hideProgressBar(): List<GamesDiscoveryItemModel> {
    return map { itemModel -> itemModel.copy(isProgressBarVisible = false) }
}
