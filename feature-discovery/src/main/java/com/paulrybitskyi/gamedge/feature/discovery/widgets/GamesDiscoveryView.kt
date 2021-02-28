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

package com.paulrybitskyi.gamedge.feature.discovery.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.SpacingItemDecorator
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.policies.LastItemExclusionPolicy
import com.paulrybitskyi.commons.recyclerview.utils.disableChangeAnimations
import com.paulrybitskyi.commons.recyclerview.utils.disableScrollbars
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.feature.discovery.R

internal class GamesDiscoveryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {


    private lateinit var discoveryAdapter: GamesDiscoveryAdapter

    private var adapterItems by observeChanges<List<GamesDiscoveryItem>>(emptyList()) { _, newItems ->
        discoveryAdapter.submitList(newItems)
    }

    var items by observeChanges<List<GamesDiscoveryItemModel>>(listOf()) { _, newValue ->
        adapterItems = newValue.toAdapterItems()
    }

    var onCategoryMoreButtonClicked: ((String) -> Unit)? = null
    var onCategoryGameClicked: ((GamesDiscoveryItemGameModel) -> Unit)? = null
    var onRefreshRequested: (() -> Unit)? = null


    init {
        initSwipeRefreshLayout()
        initRecyclerView(context)
    }


    private fun initSwipeRefreshLayout() {
        setColorSchemeColors(getColor(R.color.games_discovery_swipe_refresh_color))
        setOnRefreshListener {
            isRefreshing = false
            onRefreshRequested?.invoke()
        }
    }


    private fun initRecyclerView(context: Context) {
        RecyclerView(context)
            .apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
                overScrollMode = OVER_SCROLL_NEVER
                disableScrollbars()
                disableChangeAnimations()
                layoutManager = initLayoutManager(context)
                adapter = initAdapter(context)
                addItemDecoration(initItemDecorator())
            }
            .also(::addView)
    }


    private fun initLayoutManager(context: Context): LinearLayoutManager {
        return object : LinearLayoutManager(context) {

            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }

        }
    }


    private fun initAdapter(context: Context): GamesDiscoveryAdapter {
        return GamesDiscoveryAdapter(context)
            .apply { listenerBinder = ::bindListener }
            .also { discoveryAdapter = it }
    }


    private fun bindListener(item: GamesDiscoveryItem, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is GamesDiscoveryItem.ViewHolder) {
            with(viewHolder) {
                setOnMoreButtonClickListener { onCategoryMoreButtonClicked?.invoke(item.model.category) }
                setOnGameClickListener { onCategoryGameClicked?.invoke(it) }
            }
        }
    }


    private fun initItemDecorator(): SpacingItemDecorator {
        return SpacingItemDecorator(
            spacing = getDimensionPixelSize(R.dimen.games_discovery_decorator_spacing),
            sideFlags = SpacingItemDecorator.SIDE_BOTTOM,
            itemExclusionPolicy = LastItemExclusionPolicy()
        )
    }


    private fun List<GamesDiscoveryItemModel>.toAdapterItems(): List<GamesDiscoveryItem> {
        return map(::GamesDiscoveryItem)
    }


}