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
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameCompaniesView
import com.paulrybitskyi.gamedge.feature.info.widgets.main.GameInfoAdapterItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.mapToCompanyModels
import com.paulrybitskyi.gamedge.feature.info.widgets.main.mapToInfoCompanyModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoCompanyModel

internal class GameInfoCompaniesItem(model: List<GameInfoCompanyModel>): AbstractItem<
    List<GameInfoCompanyModel>,
    GameInfoCompaniesItem.ViewHolder,
    NoDependencies
>(model) {


    override val itemId = GameInfoAdapterItem.COMPANIES.id


    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: NoDependencies
    ): ViewHolder {
        return ViewHolder(GameCompaniesView(parent.context))
    }


    override fun performBinding(viewHolder: ViewHolder, dependencies: NoDependencies) {
        viewHolder.bind(model)
    }


    internal class ViewHolder(
        private val view: GameCompaniesView
    ): RecyclerView.ViewHolder(view), HasListeners {

        fun bind(model: List<GameInfoCompanyModel>) {
            view.items = model.mapToCompanyModels()
        }

        fun setOnCompanyClickListener(onClick: (GameInfoCompanyModel) -> Unit) {
            view.onCompanyClicked = { onClick(it.mapToInfoCompanyModel()) }
        }

    }


}
