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

package com.paulrybitskyi.gamedge.feature.category.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.device.info.screenMetrics
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.ktx.getFloat
import com.paulrybitskyi.commons.ktx.getInteger
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.ktx.makeGone
import com.paulrybitskyi.commons.ktx.makeInvisible
import com.paulrybitskyi.commons.ktx.makeVisible
import com.paulrybitskyi.commons.recyclerview.decorators.GridSpacingItemDecorator
import com.paulrybitskyi.commons.recyclerview.utils.addOnScrollListener
import com.paulrybitskyi.commons.recyclerview.utils.disableChangeAnimations
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.extensions.disableAfterAnimationEnds
import com.paulrybitskyi.gamedge.commons.ui.fadeIn
import com.paulrybitskyi.gamedge.commons.ui.resetAnimation
import com.paulrybitskyi.gamedge.feature.category.R
import com.paulrybitskyi.gamedge.feature.category.databinding.ViewGamesCategoryBinding
import kotlin.math.roundToInt

internal class GamesCategoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {


    private val gridSpanCount = getInteger(R.integer.games_category_grid_span_count)
    private val gridItemSpacing = getDimensionPixelSize(R.dimen.games_category_grid_item_spacing)
    private val itemHeightToWidthRatio = getFloat(R.integer.games_category_item_height_to_width_ratio)

    private val binding = ViewGamesCategoryBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: GamesCategoryAdapter

    private var adapterDeps by observeChanges(DEFAULT_ADAPTER_DEPS) { _, newValue ->
        adapter.dependencies = newValue
    }

    private var adapterItems by observeChanges<List<GameCategoryItem>>(emptyList()) { _, newItems ->
        adapter.submitList(newItems)
    }

    var uiState by observeChanges<GamesCategoryUiState>(GamesCategoryUiState.Empty) { _, newState ->
        handleUiStateChanged(newState)
    }

    var onGameClicked: ((GameCategoryModel) -> Unit)? = null
    var onBottomReached: (() -> Unit)? = null


    init {
        initSwipeRefreshLayout()
        initRecyclerView(context)
        initDefaults()
    }


    private fun initSwipeRefreshLayout() = with(binding.swipeRefreshLayout) {
        setColorSchemeColors(getColor(R.color.games_category_swipe_refresh_color))
        hideSwipeRefresh()
    }


    private fun initRecyclerView(context: Context) = with(binding.recyclerView) {
        disableChangeAnimations()
        layoutManager = initLayoutManager(context)
        adapter = initAdapter(context)
        adapterDeps = initAdapterDeps()
        addItemDecoration(initItemDecorator())
        addOnScrollListener(onBottomReached = { _, _ -> onBottomReached?.invoke() })

    }


    private fun initLayoutManager(context: Context): LinearLayoutManager {
        return GridLayoutManager(context, gridSpanCount)
    }


    private fun initAdapter(context: Context): GamesCategoryAdapter {
        return GamesCategoryAdapter(context)
            .apply { listenerBinder = ::bindListener }
            .also { adapter = it }
    }


    private fun bindListener(item: GameCategoryItem, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is GameCategoryItem.ViewHolder) {
            viewHolder.setOnGameClickListener { onGameClicked?.invoke(item.model) }
        }
    }


    private fun initAdapterDeps(): GamesCategoryAdapterDeps {
        val horizontalTotalSpacing = (gridItemSpacing * (gridSpanCount + 1))
        val screenWidth = (context.screenMetrics.width.sizeInPixels - horizontalTotalSpacing)
        val itemWidth = (screenWidth / gridSpanCount.toFloat())
        val itemHeight = (itemWidth * itemHeightToWidthRatio)

        return adapterDeps.copy(
            itemWidth = itemWidth.roundToInt(),
            itemHeight = itemHeight.roundToInt()
        )
    }


    private fun initItemDecorator(): GridSpacingItemDecorator {
        return GridSpacingItemDecorator(
            spacing = gridItemSpacing,
            spanCount = gridSpanCount,
            includeEdge = true
        )
    }


    private fun initDefaults() {
        uiState = uiState
    }


    private fun List<GameCategoryModel>.toAdapterItems(): List<GameCategoryItem> {
        return map(::GameCategoryItem)
    }


    private fun handleUiStateChanged(newState: GamesCategoryUiState) {
        when(newState) {
            is GamesCategoryUiState.Empty -> onEmptyUiStateSelected()
            is GamesCategoryUiState.Loading -> onLoadingStateSelected()
            is GamesCategoryUiState.Result -> onResultUiStateSelected(newState)
        }
    }


    private fun onEmptyUiStateSelected() {
        showInfoView()
        hideLoadingIndicators()
        hideRecyclerView()
    }


    private fun onLoadingStateSelected() {
        if(adapterItems.isNotEmpty()) {
            showSwipeRefresh()
        } else {
            showProgressBar()
            hideInfoView()
            hideRecyclerView()
        }
    }


    private fun onResultUiStateSelected(uiState: GamesCategoryUiState.Result) {
        adapterItems = uiState.items.toAdapterItems()

        showRecyclerView()
        hideInfoView()
        hideLoadingIndicators()
    }


    private fun showInfoView() = with(binding.infoView) {
        if(isVisible) return

        makeVisible()
        fadeIn()
    }


    private fun hideInfoView() = with(binding.infoView) {
        makeGone()
        resetAnimation()
    }


    private fun showProgressBar() = with(binding.progressBar) {
        makeVisible()
        fadeIn()
    }


    private fun hideProgressBar() = with(binding.progressBar) {
        makeGone()
        resetAnimation()
    }


    private fun showSwipeRefresh() = with(binding.swipeRefreshLayout) {
        isEnabled = true
        isRefreshing = true
    }


    private fun hideSwipeRefresh() = with(binding.swipeRefreshLayout) {
        isRefreshing = false
        disableAfterAnimationEnds()
    }


    private fun hideLoadingIndicators() {
        hideProgressBar()
        hideSwipeRefresh()
    }


    private fun showRecyclerView() = with(binding.recyclerView) {
        if(isVisible) return

        makeVisible()
        fadeIn()
    }


    private fun hideRecyclerView() = with(binding.recyclerView) {
        makeInvisible()
        resetAnimation()
    }


}
