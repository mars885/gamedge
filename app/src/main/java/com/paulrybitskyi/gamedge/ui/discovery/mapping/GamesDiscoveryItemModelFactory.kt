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

package com.paulrybitskyi.gamedge.ui.discovery.mapping

import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryCategory
import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryItemChildModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryItemModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryItemUiState
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import javax.inject.Inject


internal interface GamesDiscoveryItemModelFactory {

    fun createDefault(category: GamesDiscoveryCategory): GamesDiscoveryItemModel

    fun createCopyWithVisibleProgressBar(item: GamesDiscoveryItemModel): GamesDiscoveryItemModel

    fun createCopyWithHiddenProgressBar(item: GamesDiscoveryItemModel): GamesDiscoveryItemModel

    fun createCopyWithEmptyState(item: GamesDiscoveryItemModel): GamesDiscoveryItemModel

    fun createCopyWithResultState(
        item: GamesDiscoveryItemModel,
        children: List<GamesDiscoveryItemChildModel>
    ): GamesDiscoveryItemModel

}


@BindType(installIn = BindType.Component.ACTIVITY_RETAINED)
internal class GamesDiscoveryItemModelFactoryImpl @Inject constructor(
    private val stringProvider: StringProvider
) : GamesDiscoveryItemModelFactory {


    override fun createDefault(category: GamesDiscoveryCategory): GamesDiscoveryItemModel {
        return GamesDiscoveryItemModel(
            category = category,
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
        children: List<GamesDiscoveryItemChildModel>
    ): GamesDiscoveryItemModel {
        if(children.isEmpty()) return createCopyWithEmptyState(item)

        return item.copy(uiState = GamesDiscoveryItemUiState.Result(children))
    }


}