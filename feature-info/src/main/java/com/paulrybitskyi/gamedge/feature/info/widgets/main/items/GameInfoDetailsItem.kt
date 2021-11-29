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

package com.paulrybitskyi.gamedge.feature.info.widgets.main.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.gamedge.commons.ui.base.rv.AbstractItem
import com.paulrybitskyi.gamedge.commons.ui.base.rv.NoDependencies
import com.paulrybitskyi.gamedge.feature.info.widgets.GameDetailsView
import com.paulrybitskyi.gamedge.feature.info.widgets.main.GameInfoAdapterItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoDetailsModel

internal class GameInfoDetailsItem(model: GameInfoDetailsModel) : AbstractItem<
    GameInfoDetailsModel,
    GameInfoDetailsItem.ViewHolder,
    NoDependencies
>(model) {

    override val itemId = GameInfoAdapterItem.DETAILS.id

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: NoDependencies
    ): ViewHolder {
        return ViewHolder(GameDetailsView(parent.context))
    }

    override fun performBinding(viewHolder: ViewHolder, dependencies: NoDependencies) {
        viewHolder.bind(model)
    }

    internal class ViewHolder(
        private val view: GameDetailsView
    ) : RecyclerView.ViewHolder(view) {

        fun bind(model: GameInfoDetailsModel) = with(view) {
            genresText = model.genresText
            platformsText = model.platformsText
            modesText = model.modesText
            playerPerspectivesText = model.playerPerspectivesText
            themesText = model.themesText
        }
    }
}
