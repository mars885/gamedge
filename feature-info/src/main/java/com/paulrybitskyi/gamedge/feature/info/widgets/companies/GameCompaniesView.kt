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

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.recyclerview.utils.disableAnimations
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.databinding.ViewGameCompaniesBinding

internal class GameCompaniesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private val binding = ViewGameCompaniesBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: GameCompaniesAdapter

    private var adapterItems by observeChanges<List<GameCompanyItem>>(emptyList()) { _, newItems ->
        adapter.submitList(newItems)
    }

    var items by observeChanges<List<GameCompanyModel>>(emptyList()) { _, newItems ->
        adapterItems = newItems.toAdapterItems()
    }

    var onCompanyClicked: ((GameCompanyModel) -> Unit)? = null


    init {
        initCard()
        initRecyclerView(context)
    }


    private fun initCard() {
        setBackgroundColor(getColor(R.color.game_companies_card_background_color))
        cardElevation = getDimension(R.dimen.game_companies_card_elevation)
    }


    private fun initRecyclerView(context: Context) = with(binding.recyclerView) {
        disableAnimations()

        layoutManager = initLayoutManager(context)
        adapter = initAdapter(context)
    }


    private fun initLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }


    private fun initAdapter(context: Context): GameCompaniesAdapter {
        return GameCompaniesAdapter(context)
            .apply { listenerBinder = ::bindListener }
            .also { adapter = it }
    }


    private fun bindListener(item: GameCompanyItem, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is GameCompanyItem.ViewHolder) {
            viewHolder.setOnCompanyClickListener { onCompanyClicked?.invoke(item.model) }
        }
    }


    private fun List<GameCompanyModel>.toAdapterItems(): List<GameCompanyItem> {
        return map(::GameCompanyItem)
    }


}