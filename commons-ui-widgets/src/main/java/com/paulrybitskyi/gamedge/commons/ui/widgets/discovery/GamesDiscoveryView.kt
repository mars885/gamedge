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

package com.paulrybitskyi.gamedge.commons.ui.widgets.discovery

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.ktx.makeGone
import com.paulrybitskyi.commons.ktx.makeVisible
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.SpacingItemDecorator
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.policies.LastItemExclusionPolicy
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.base.calculateDiff
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategory
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGamesDiscoveryBinding
import com.paulrybitskyi.gamedge.commons.ui.widgets.utils.fadeIn
import com.paulrybitskyi.gamedge.commons.ui.widgets.utils.resetAnimation

class GamesDiscoveryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val binding = ViewGamesDiscoveryBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: GamesDiscoveryAdapter

    private var adapterItems by observeChanges<List<GamesDiscoveryItem>>(emptyList()) { oldItems, newItems ->
        adapter.setItems(
            items = newItems,
            diff = calculateDiff(oldItems, newItems)
        )
    }

    var uiState by observeChanges<UiState>(UiState.Empty) { _, newState ->
        handleUiStateChange(newState)
    }

    var onCategoryMoreButtonClickListener: ((GamesCategory) -> Unit)? = null
    var onCategoryGameClickListener: (() -> Unit)? = null


    sealed class UiState {

        object Empty: UiState()

        object Loading: UiState()

        class Result(val items: List<GamesDiscoveryItemModel>): UiState()

    }


    init {
        initRecyclerView(context)
        initDefaults()
    }


    private fun initRecyclerView(context: Context) = with(binding.recyclerView) {
        layoutManager = initLayoutManager(context)
        adapter = initAdapter(context)
        addItemDecoration(initItemDecorator())
    }


    private fun initLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context)
    }


    private fun initAdapter(context: Context): GamesDiscoveryAdapter {
        return GamesDiscoveryAdapter(context)
            .apply { listenerBinder = ::bindListener }
            .also { adapter = it }
    }


    private fun bindListener(item: GamesDiscoveryItem, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is GamesDiscoveryItem.ViewHolder) {
            with(viewHolder) {
                setOnMoreButtonClickListener {
                    onCategoryMoreButtonClickListener?.invoke(item.model.category)
                }
                setOnGameClickListener {
                    onCategoryGameClickListener?.invoke()
                }
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


    private fun initDefaults() {
        uiState = uiState
    }


    private fun List<GamesDiscoveryItemModel>.toAdapterItems(): List<GamesDiscoveryItem> {
        return map(::GamesDiscoveryItem)
    }


    private fun handleUiStateChange(newState: UiState) {
        when(newState) {
            is UiState.Empty -> onEmptyUiStateSelected()
            is UiState.Loading -> onLoadingUiStateSelected()
            is UiState.Result -> onResultUiStateSelected(newState)
        }
    }


    private fun onEmptyUiStateSelected() {
        hideProgressBar()
        hideRecyclerView()
        showInfoView()
    }


    private fun onLoadingUiStateSelected() {
        hideInfoView()
        hideRecyclerView()
        showProgressBar()
    }


    private fun onResultUiStateSelected(uiState: UiState.Result) {
        adapterItems = uiState.items.toAdapterItems()

        hideProgressBar()
        hideInfoView()
        showRecyclerView()
    }


    private fun showProgressBar() = with(binding.progressBar) {
        makeVisible()
        fadeIn()
    }


    private fun hideProgressBar() = with(binding.progressBar) {
        makeGone()
        resetAnimation()
    }


    private fun showInfoView() = with(binding.infoView) {
        makeVisible()
        fadeIn()
    }


    private fun hideInfoView() = with(binding.infoView) {
        makeGone()
        resetAnimation()
    }


    private fun showRecyclerView() = with(binding.recyclerView) {
        makeVisible()
        fadeIn()
    }


    private fun hideRecyclerView() = with(binding.recyclerView) {
        makeGone()
        resetAnimation()
    }


}