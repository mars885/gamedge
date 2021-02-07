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

package com.paulrybitskyi.gamedge.commons.ui.widgets.news

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.SpacingItemDecorator
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.policies.LastItemExclusionPolicy
import com.paulrybitskyi.commons.recyclerview.utils.disableChangeAnimations
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.fadeIn
import com.paulrybitskyi.gamedge.commons.ui.resetAnimation
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGamingNewsBinding

class GamingNewsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {


    private val binding = ViewGamingNewsBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: GamingNewsAdapter

    private var adapterItems by observeChanges<List<GamingNewsItem>>(emptyList()) { _, newItems ->
        adapter.submitList(newItems) {
            binding.recyclerView.invalidateItemDecorations()
        }
    }

    var uiState by observeChanges<GamingNewsUiState>(GamingNewsUiState.Empty) { _, newState ->
        handleUiStateChange(newState)
    }

    var onNewsItemClickListener: ((GamingNewsItemModel) -> Unit)? = null
    var onRefreshListener: (() -> Unit)? = null


    init {
        initSwipeRefreshLayout()
        initRecyclerView(context)
        initDefaults()
    }


    private fun initSwipeRefreshLayout() = with(binding.swipeRefreshLayout) {
        setColorSchemeColors(getColor(R.color.gaming_news_swipe_refresh_color))
        setOnRefreshListener { onRefreshListener?.invoke() }
    }


    private fun initRecyclerView(context: Context) = with(binding.recyclerView) {
        disableChangeAnimations()
        layoutManager = initLayoutManager(context)
        adapter = initAdapter(context)
        addItemDecoration(initItemDecorator())
    }


    private fun initLayoutManager(context: Context): LinearLayoutManager {
        return object : LinearLayoutManager(context) {

            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }

        }
    }


    private fun initAdapter(context: Context): GamingNewsAdapter {
        return GamingNewsAdapter(context)
            .apply { listenerBinder = ::bindListener }
            .also { adapter = it }
    }


    private fun bindListener(item: GamingNewsItem, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is GamingNewsItem.ViewHolder) {
            viewHolder.setOnNewsItemClickListener { onNewsItemClickListener?.invoke(item.model) }
        }
    }


    private fun initItemDecorator(): SpacingItemDecorator {
        return SpacingItemDecorator(
            spacing = getDimensionPixelSize(R.dimen.gaming_news_decorator_spacing),
            sideFlags = SpacingItemDecorator.SIDE_BOTTOM,
            itemExclusionPolicy = LastItemExclusionPolicy()
        )
    }


    private fun initDefaults() {
        uiState = uiState
    }


    private fun List<GamingNewsItemModel>.toAdapterItems(): List<GamingNewsItem> {
        return map(::GamingNewsItem)
    }


    private fun handleUiStateChange(newState: GamingNewsUiState) {
        when(newState) {
            is GamingNewsUiState.Empty -> onEmptyUiStateSelected()
            is GamingNewsUiState.Loading -> onLoadingStateSelected()
            is GamingNewsUiState.Result -> onResultUiStateSelected(newState)
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


    private fun onResultUiStateSelected(uiState: GamingNewsUiState.Result) {
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
        isRefreshing = true
    }


    private fun hideSwipeRefresh() = with(binding.swipeRefreshLayout) {
        isRefreshing = false
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