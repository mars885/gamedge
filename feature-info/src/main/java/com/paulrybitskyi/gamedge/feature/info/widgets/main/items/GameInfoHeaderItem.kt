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
import com.paulrybitskyi.gamedge.commons.ui.base.rv.AbstractItem
import com.paulrybitskyi.gamedge.commons.ui.base.rv.HasListeners
import com.paulrybitskyi.gamedge.commons.ui.base.rv.NoDependencies
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameHeaderView
import com.paulrybitskyi.gamedge.feature.info.widgets.main.GameInfoAdapterItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.items.GameInfoHeaderItem.ViewHolder
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoHeaderModel

internal class GameInfoHeaderItem(model: GameInfoHeaderModel) : AbstractItem<
    GameInfoHeaderModel,
    ViewHolder,
    NoDependencies
>(model) {


    override val itemId = GameInfoAdapterItem.HEADER.id


    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: NoDependencies
    ): ViewHolder {
        return ViewHolder(
            GameHeaderView(parent.context).apply {
                applyWindowTopOffset()
            }
        )
    }


    override fun performBinding(viewHolder: ViewHolder, dependencies: NoDependencies) {
        viewHolder.bind(model)
    }


    internal class ViewHolder(
        private val view: GameHeaderView
    ): RecyclerView.ViewHolder(view), HasListeners {

        fun bind(model: GameInfoHeaderModel) = with(view) {
            backgroundImageModels = model.backgroundImageModels
            isLiked = model.isLiked
            coverImageUrl = model.coverImageUrl
            title = model.title
            releaseDate = model.releaseDate
            developerName = model.developerName
            rating = model.rating
            likeCount = model.likeCount
            ageRating = model.ageRating
            gameCategory = model.gameCategory
        }

        fun setOnBackButtonClickListener(listener: () -> Unit) {
            view.onBackButtonClickListener = listener
        }

        fun setOnLikeButtonClickListener(listener: () -> Unit) {
            view.onLikeButtonClickListener = listener
        }

    }


}