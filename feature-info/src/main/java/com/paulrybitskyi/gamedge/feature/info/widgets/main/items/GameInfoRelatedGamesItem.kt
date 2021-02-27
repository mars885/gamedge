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
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.gamedge.commons.ui.base.rv.AbstractItem
import com.paulrybitskyi.gamedge.commons.ui.base.rv.HasListeners
import com.paulrybitskyi.gamedge.commons.ui.base.rv.NoDependencies
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreviewUiState
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreviewView
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.main.GameInfoAdapterItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.items.GameInfoRelatedGamesItem.ViewHolder
import com.paulrybitskyi.gamedge.feature.info.widgets.main.mapToCategoryModels
import com.paulrybitskyi.gamedge.feature.info.widgets.main.mapToInfoRelatedGameModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.games.GameInfoRelatedGameModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.games.GameInfoRelatedGamesModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.games.GameInfoRelatedGamesType

internal class GameInfoRelatedGamesItem(model: GameInfoRelatedGamesModel) : AbstractItem<
    GameInfoRelatedGamesModel,
    ViewHolder,
    NoDependencies
>(model) {


    override val itemId: Long
        get() = when(model.type) {
            GameInfoRelatedGamesType.OTHER_COMPANY_GAMES -> GameInfoAdapterItem.OTHER_COMPANY_GAMES.id
            GameInfoRelatedGamesType.SIMILAR_GAMES -> GameInfoAdapterItem.SIMILAR_GAMES.id
        }


    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: NoDependencies
    ): ViewHolder {
        return ViewHolder(
            GamesCategoryPreviewView(parent.context).apply {
                isProgressBarVisible = false
                isMoreButtonVisible = false
                topBarMargin = getDimensionPixelSize(R.dimen.game_info_related_games_top_bar_margin)
            }
        )
    }


    override fun performBinding(viewHolder: ViewHolder, dependencies: NoDependencies) {
        viewHolder.bind(model)
    }


    internal class ViewHolder(
        private val view: GamesCategoryPreviewView
    ): RecyclerView.ViewHolder(view), HasListeners {

        fun bind(model: GameInfoRelatedGamesModel) = with(view) {
            title = model.title
            uiState = GamesCategoryPreviewUiState.Result(model.items.mapToCategoryModels())
        }

        fun setOnGameClickListener(onClick: (GameInfoRelatedGameModel) -> Unit) {
            view.onGameClicked = { onClick(it.mapToInfoRelatedGameModel()) }
        }

    }


}