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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.recyclerview.utils.disableAnimations
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGamesCategoryPreviewBinding
import com.paulrybitskyi.gamedge.commons.ui.widgets.utils.fadeIn
import com.paulrybitskyi.gamedge.commons.ui.widgets.utils.resetAnimation

class GamesCategoryPreviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    var isMoreButtonVisible: Boolean
        set(value) { binding.moreBtn.isVisible = value }
        get() = binding.moreBtn.isVisible

    var isProgressBarVisible: Boolean
        set(value) { binding.progressBar.isVisible = value }
        get() = binding.progressBar.isVisible

    var topBarMargin: Int
        set(value) { binding.topBarBarrier.margin = value }
        get() = binding.topBarBarrier.margin

    var title: CharSequence
        set(value) { binding.titleTv.text = value }
        get() = binding.titleTv.text

    private val binding = ViewGamesCategoryPreviewBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: GamesCategoryPreviewAdapter

    private var adapterItems by observeChanges<List<GamesCategoryPreviewItem>>(emptyList()) { _, newItems ->
        adapter.submitList(newItems)
    }

    var uiState by observeChanges<GamesCategoryPreviewUiState>(GamesCategoryPreviewUiState.Empty) { _, newState ->
        handleUiStateChange(newState)
    }

    var onMoreButtonClickListener: (() -> Unit)? = null
    var onGameClickListener: ((GamesCategoryPreviewItemModel) -> Unit)? = null


    init {
        initCard()
        initMoreButton()
        initRecyclerView(context)
    }


    private fun initCard() {
        setBackgroundColor(getColor(R.color.games_category_preview_card_background_color))
        cardElevation = getDimension(R.dimen.games_category_preview_card_elevation)
    }


    private fun initMoreButton() {
        binding.moreBtn.setOnClickListener {
            onMoreButtonClickListener?.invoke()
        }
    }


    private fun initRecyclerView(context: Context) = with(binding.recyclerView) {
        disableAnimations()

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
                onGameClickListener?.invoke(item.model)
            }
        }
    }


    private fun List<GamesCategoryPreviewItemModel>.toAdapterItems(): List<GamesCategoryPreviewItem> {
        return map(::GamesCategoryPreviewItem)
    }


    private fun handleUiStateChange(newState: GamesCategoryPreviewUiState) {
        when(newState) {
            is GamesCategoryPreviewUiState.Empty -> onEmptyUiStateSelected()
            is GamesCategoryPreviewUiState.Result -> onResultUiStateSelected(newState)
        }
    }


    private fun onEmptyUiStateSelected() {
        showInfoView()
    }


    private fun onResultUiStateSelected(uiState: GamesCategoryPreviewUiState.Result) {
        adapterItems = uiState.items.toAdapterItems()

        hideInfoView()
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


}