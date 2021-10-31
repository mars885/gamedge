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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.gamedge.commons.ui.base.rv.AbstractItem
import com.paulrybitskyi.gamedge.commons.ui.base.rv.HasListeners
import com.paulrybitskyi.gamedge.commons.ui.base.rv.HasUniqueIdentifier
import com.paulrybitskyi.gamedge.commons.ui.base.rv.NoDependencies

internal class GamingNewsItem(model: GamingNewsItemModel) : AbstractItem<
    GamingNewsItemModel,
    GamingNewsItem.ViewHolder,
    NoDependencies
>(model), HasUniqueIdentifier<Int> {


    override val uniqueIdentifier: Int
        get() = model.id


    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: NoDependencies
    ): ViewHolder {
        return ViewHolder(GamingNewsItemView(parent.context))
    }


    override fun performBinding(viewHolder: ViewHolder, dependencies: NoDependencies) {
        viewHolder.bind(model)
    }


    internal class ViewHolder(
        private val view: GamingNewsItemView
    ): RecyclerView.ViewHolder(view), HasListeners {

        fun bind(model: GamingNewsItemModel) = with(view) {
            imageUrl = model.imageUrl
            title = model.title
            lede = model.lede
            publicationDate = model.publicationDate
        }

        fun setOnNewsItemClickListener(onClick: () -> Unit) {
            view.onNewsItemClicked = onClick
        }

    }


}
