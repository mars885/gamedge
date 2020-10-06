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

package com.paulrybitskyi.gamedge.commons.ui.widgets.screenshots

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.recyclerview.utils.disableAnimations
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGameScreenshotsBinding

class GameScreenshotsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private val binding = ViewGameScreenshotsBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: GameScreenshotsAdapter

    private var adapterItems by observeChanges<List<GameScreenshotItem>>(emptyList()) { _, newItems ->
        adapter.submitList(newItems)
    }

    var items by observeChanges<List<String>>(emptyList()) { _, newItems ->
        adapterItems = newItems.toAdapterItems()
    }


    init {
        initCard()
        initRecyclerView(context)
    }


    private fun initCard() {
        setBackgroundColor(getColor(R.color.game_screenshots_card_background_color))
        cardElevation = getDimension(R.dimen.game_screenshots_card_elevation)
    }


    private fun initRecyclerView(context: Context) = with(binding.recyclerView) {
        disableAnimations()

        layoutManager = initLayoutManager(context)
        adapter = initAdapter(context)
    }


    private fun initLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }


    private fun initAdapter(context: Context): GameScreenshotsAdapter {
        return GameScreenshotsAdapter(context)
            .also { adapter = it }
    }


    private fun List<String>.toAdapterItems(): List<GameScreenshotItem> {
        return map(::GameScreenshotItem)
    }


}