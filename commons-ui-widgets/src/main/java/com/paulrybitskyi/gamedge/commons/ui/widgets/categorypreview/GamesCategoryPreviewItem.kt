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

package com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.ktx.onClick
import com.paulrybitskyi.commons.ktx.setHorizontalMargin
import com.paulrybitskyi.gamedge.commons.ui.base.rv.AbstractItem
import com.paulrybitskyi.gamedge.commons.ui.base.rv.HasListeners
import com.paulrybitskyi.gamedge.commons.ui.base.rv.HasUniqueIdentifier
import com.paulrybitskyi.gamedge.commons.ui.base.rv.NoDependencies
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCoverView
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreviewItem.ViewHolder

internal class GamesCategoryPreviewItem(model: GamesCategoryPreviewItemModel) : AbstractItem<
    GamesCategoryPreviewItemModel,
    ViewHolder,
    NoDependencies
>(model), HasUniqueIdentifier<Int> {


    override val uniqueIdentifier: Int
        get() = model.id


    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: NoDependencies
    ): ViewHolder {
        return ViewHolder(
            GameCoverView(parent.context).apply {
                val width = getDimensionPixelSize(R.dimen.games_category_preview_cover_item_width)
                val height = getDimensionPixelSize(R.dimen.games_category_preview_cover_item_height)
                val margin = getDimensionPixelSize(R.dimen.games_category_preview_cover_item_margin)

                RecyclerView.LayoutParams(width, height)
                    .apply { setHorizontalMargin(margin) }
                    .also(::setLayoutParams)
            }
        )
    }


    override fun performBinding(viewHolder: ViewHolder, dependencies: NoDependencies) {
        viewHolder.bind(model)
    }


    internal class ViewHolder(
        private val view: GameCoverView
    ): RecyclerView.ViewHolder(view), HasListeners {

        fun bind(model: GamesCategoryPreviewItemModel) = with(view) {
            title = model.title
            imageUrl = model.coverUrl
        }

        fun setOnGameClickListener(onClick: () -> Unit) {
            view.onClick { onClick() }
        }

    }


}