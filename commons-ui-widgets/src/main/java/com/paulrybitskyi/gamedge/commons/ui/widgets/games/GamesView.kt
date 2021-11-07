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

package com.paulrybitskyi.gamedge.commons.ui.widgets.games

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.ktx.getDrawable
import com.paulrybitskyi.commons.ktx.getString
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.ktx.makeGone
import com.paulrybitskyi.commons.ktx.makeInvisible
import com.paulrybitskyi.commons.ktx.makeVisible
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.SpacingItemDecorator
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.policies.LastItemExclusionPolicy
import com.paulrybitskyi.commons.recyclerview.utils.addOnScrollListener
import com.paulrybitskyi.commons.recyclerview.utils.disableChangeAnimations
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.extensions.disableAfterAnimationEnds
import com.paulrybitskyi.gamedge.commons.ui.fadeIn
import com.paulrybitskyi.gamedge.commons.ui.resetAnimation
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGamesBinding

class GamesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewGamesBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: GamesAdapter

    private var adapterItems by observeChanges<List<GameItem>>(emptyList()) { _, newItems ->
        adapter.submitList(newItems) {
            binding.recyclerView.invalidateItemDecorations()
        }
    }

    var uiState by observeChanges(createDefaultUiState()) { _, newState ->
        handleUiStateChange(newState)
    }

    var onGameClicked: ((GameModel) -> Unit)? = null
    var onBottomReached: (() -> Unit)? = null

    init {
        initSwipeRefreshLayout()
        initRecyclerView(context)
        initDefaults()
    }

    private fun initSwipeRefreshLayout() = with(binding.swipeRefreshLayout) {
        setColorSchemeColors(getColor(R.color.games_swipe_refresh_color))
        hideSwipeRefresh()
    }

    private fun initRecyclerView(context: Context) = with(binding.recyclerView) {
        disableChangeAnimations()
        layoutManager = initLayoutManager(context)
        adapter = initAdapter(context)
        addItemDecoration(initItemDecorator())
        addOnScrollListener(onBottomReached = { _, _ -> onBottomReached?.invoke() })
    }

    private fun initLayoutManager(context: Context): LinearLayoutManager {
        return object : LinearLayoutManager(context) {

            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
        }
    }

    private fun initAdapter(context: Context): GamesAdapter {
        return GamesAdapter(context)
            .apply { listenerBinder = ::bindListener }
            .also { adapter = it }
    }

    private fun bindListener(item: GameItem, viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is GameItem.ViewHolder) {
            viewHolder.setOnGameClickListener { onGameClicked?.invoke(item.model) }
        }
    }

    private fun initItemDecorator(): SpacingItemDecorator {
        return SpacingItemDecorator(
            spacing = getDimensionPixelSize(R.dimen.games_decorator_spacing),
            sideFlags = SpacingItemDecorator.SIDE_BOTTOM,
            itemExclusionPolicy = LastItemExclusionPolicy()
        )
    }

    private fun initDefaults() {
        uiState = uiState
    }

    private fun createDefaultUiState(): GamesUiState {
        return GamesUiState.Empty(
            iconId = R.drawable.gamepad_variant_outline,
            title = getString(R.string.games_info_view_title)
        )
    }

    fun clearItems() {
        adapterItems = emptyList()
    }

    private fun List<GameModel>.toAdapterItems(): List<GameItem> {
        return map(::GameItem)
    }

    private fun handleUiStateChange(newState: GamesUiState) {
        when (newState) {
            is GamesUiState.Empty -> onEmptyUiStateSelected(newState)
            is GamesUiState.Loading -> onLoadingStateSelected()
            is GamesUiState.Result -> onResultUiStateSelected(newState)
        }
    }

    private fun onEmptyUiStateSelected(uiState: GamesUiState.Empty) {
        showInfoView(uiState)
        hideLoadingIndicators()
        hideRecyclerView()
    }

    private fun onLoadingStateSelected() {
        if (adapterItems.isNotEmpty()) {
            showSwipeRefresh()
        } else {
            showProgressBar()
            hideInfoView()
            hideRecyclerView()
        }
    }

    private fun onResultUiStateSelected(uiState: GamesUiState.Result) {
        adapterItems = uiState.items.toAdapterItems()

        showRecyclerView()
        hideInfoView()
        hideLoadingIndicators()
    }

    private fun showInfoView(uiState: GamesUiState.Empty) = with(binding.infoView) {
        icon = getDrawable(uiState.iconId)
        titleText = uiState.title

        if (isVisible) return

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
        if (isVisible) return

        makeVisible()
        fadeIn()
    }

    private fun hideRecyclerView() = with(binding.recyclerView) {
        makeInvisible()
        resetAnimation()
    }
}
