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

package com.paulrybitskyi.gamedge.feature.info.widgets.companies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams.WRAP_CONTENT
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.ktx.setHorizontalMargin
import com.paulrybitskyi.gamedge.commons.ui.base.rv.AbstractItem
import com.paulrybitskyi.gamedge.commons.ui.base.rv.HasListeners
import com.paulrybitskyi.gamedge.commons.ui.base.rv.NoDependencies
import com.paulrybitskyi.gamedge.core.utils.height
import com.paulrybitskyi.gamedge.core.utils.width
import com.paulrybitskyi.gamedge.feature.info.R

internal class GameCompanyItem(model: GameCompanyModel): AbstractItem<
    GameCompanyModel,
    GameCompanyItem.ViewHolder,
    NoDependencies
>(model) {


    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: NoDependencies
    ): ViewHolder {
        return ViewHolder(
            GameCompanyView(parent.context).apply {
                val margin = getDimensionPixelSize(R.dimen.game_companies_item_margin)

                RecyclerView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    .apply { setHorizontalMargin(margin) }
                    .also(::setLayoutParams)
            }
        )
    }


    override fun performBinding(viewHolder: ViewHolder, dependencies: NoDependencies) {
        viewHolder.bind(model)
    }


    internal class ViewHolder(
        private val view: GameCompanyView
    ): RecyclerView.ViewHolder(view), HasListeners {

        fun bind(model: GameCompanyModel) = with(view) {
            logoViewWidth = model.logoViewSize.width
            logoViewHeight = model.logoViewSize.height
            logoImageWidth = model.logoImageSize.width
            logoImageHeight = model.logoImageSize.height
            logoUrl = model.logoUrl
            name = model.name
            roles = model.roles
        }

        fun setOnCompanyClickListener(onClick: () -> Unit) {
            view.onCompanyClicked = onClick
        }

    }


}
