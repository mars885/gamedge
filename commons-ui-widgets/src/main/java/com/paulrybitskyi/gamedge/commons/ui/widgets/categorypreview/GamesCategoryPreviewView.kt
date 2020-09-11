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

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.SpacingItemDecorator
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.base.calculateDiff
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGamesCategoryPreviewBinding

class GamesCategoryPreviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    var title: CharSequence
        set(value) { binding.titleTv.text = value }
        get() = binding.titleTv.text

    private val binding = ViewGamesCategoryPreviewBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: GamesCategoryPreviewAdapter

    private var adapterItems by observeChanges<List<GamesCategoryPreviewItem>>(emptyList()) { oldItems, newItems ->
        adapter.setItems(
            items = newItems,
            diff = calculateDiff(oldItems, newItems)
        )
    }

    var items by observeChanges<List<GamesCategoryPreviewItemModel>>(emptyList()) { _, newItems ->
        adapterItems = newItems.toAdapterItems()
    }

    var onMoreButtonClickListener: (() -> Unit)? = null
    var onGameClickListener: (() -> Unit)? = null


    init {
        initCard()
        initMoreButton()
        initRecyclerView(context)
    }


    private fun initCard() {
        setBackgroundColor(getColor(R.color.games_category_preview_view_card_background_color))
        cardElevation = getDimension(R.dimen.games_category_preview_card_elevation)
    }


    private fun initMoreButton() {
        binding.moreBtn.setOnClickListener {
            onMoreButtonClickListener?.invoke()
        }
    }


    private fun initRecyclerView(context: Context) = with(binding.recyclerView) {
        layoutManager = initLayoutManager(context)
        adapter = initAdapter(context)
    }


    private fun initLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }


    private fun initAdapter(context: Context): GamesCategoryPreviewAdapter {
        return GamesCategoryPreviewAdapter(context)
            .apply { listenerBinder = ::bindListener }
            .also { adapter = it }
    }


    private fun bindListener(item: GamesCategoryPreviewItem, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is GamesCategoryPreviewItem.ViewHolder) {
            viewHolder.setOnGameClickListener {
                onGameClickListener?.invoke()
            }
        }
    }


    private fun List<GamesCategoryPreviewItemModel>.toAdapterItems(): List<GamesCategoryPreviewItem> {
        return map(::GamesCategoryPreviewItem)
    }


}