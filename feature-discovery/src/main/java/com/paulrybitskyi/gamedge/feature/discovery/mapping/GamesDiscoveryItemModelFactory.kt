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

package com.paulrybitskyi.gamedge.feature.discovery.mapping

import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryCategory
import com.paulrybitskyi.gamedge.feature.discovery.titleId
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemGameModel
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemModel
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemUiState
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface GamesDiscoveryItemModelFactory {

    fun createDefault(category: GamesDiscoveryCategory): GamesDiscoveryItemModel
    fun createCopyWithVisibleProgressBar(item: GamesDiscoveryItemModel): GamesDiscoveryItemModel
    fun createCopyWithHiddenProgressBar(item: GamesDiscoveryItemModel): GamesDiscoveryItemModel
    fun createCopyWithEmptyState(item: GamesDiscoveryItemModel): GamesDiscoveryItemModel
    fun createCopyWithResultState(
        item: GamesDiscoveryItemModel,
        games: List<GamesDiscoveryItemGameModel>
    ): GamesDiscoveryItemModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GamesDiscoveryItemModelFactoryImpl @Inject constructor(
    private val stringProvider: StringProvider
) : GamesDiscoveryItemModelFactory {

    override fun createDefault(category: GamesDiscoveryCategory): GamesDiscoveryItemModel {
        return GamesDiscoveryItemModel(
            category = category.name,
            title = stringProvider.getString(category.titleId),
            isProgressBarVisible = false,
            uiState = GamesDiscoveryItemUiState.Empty
        )
    }

    override fun createCopyWithVisibleProgressBar(item: GamesDiscoveryItemModel): GamesDiscoveryItemModel {
        return item.copy(isProgressBarVisible = true)
    }

    override fun createCopyWithHiddenProgressBar(item: GamesDiscoveryItemModel): GamesDiscoveryItemModel {
        return item.copy(isProgressBarVisible = false)
    }

    override fun createCopyWithEmptyState(item: GamesDiscoveryItemModel): GamesDiscoveryItemModel {
        return item.copy(uiState = GamesDiscoveryItemUiState.Empty)
    }

    override fun createCopyWithResultState(
        item: GamesDiscoveryItemModel,
        games: List<GamesDiscoveryItemGameModel>
    ): GamesDiscoveryItemModel {
        if (games.isEmpty()) return createCopyWithEmptyState(item)

        return item.copy(uiState = GamesDiscoveryItemUiState.Result(games))
    }
}
