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

package com.paulrybitskyi.gamedge.commons.ui.widgets.discovery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.gamedge.commons.ui.widgets.base.AbstractItem
import com.paulrybitskyi.gamedge.commons.ui.widgets.base.HasListeners
import com.paulrybitskyi.gamedge.commons.ui.widgets.base.HasUniqueIdentifier
import com.paulrybitskyi.gamedge.commons.ui.widgets.base.NoDependencies
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreviewView
import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryItem.ViewHolder

internal class GamesDiscoveryItem(model: GamesDiscoveryItemModel) : AbstractItem<
    GamesDiscoveryItemModel,
    ViewHolder,
    NoDependencies
>(model), HasUniqueIdentifier<String> {


    override val uniqueIdentifier: String
        get() = model.title


    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: NoDependencies
    ): ViewHolder {
        return ViewHolder(GamesCategoryPreviewView(parent.context))
    }


    override fun performBinding(viewHolder: ViewHolder, dependencies: NoDependencies) {
        viewHolder.bind(model)
    }


    internal class ViewHolder(
        private val view: GamesCategoryPreviewView
    ): RecyclerView.ViewHolder(view), HasListeners {

        fun bind(model: GamesDiscoveryItemModel) = with(view) {
            title = model.title
            isProgressBarVisible = model.isProgressBarVisible
            uiState = model.uiState.mapToCategoryUiState()
        }

        fun setOnMoreButtonClickListener(listener: () -> Unit) {
            view.onMoreButtonClickListener = listener
        }

        fun setOnGameClickListener(listener: (GamesDiscoveryItemChildModel) -> Unit) {
            view.onGameClickListener = {
                listener(it.mapToDiscoveryItemChildModel())
            }
        }

    }


}