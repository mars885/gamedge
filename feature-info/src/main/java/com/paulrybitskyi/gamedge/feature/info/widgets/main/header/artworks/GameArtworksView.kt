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

package com.paulrybitskyi.gamedge.feature.info.widgets.main.header.artworks

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.paulrybitskyi.commons.ktx.views.recyclerView
import com.paulrybitskyi.commons.ktx.views.registerOnPageChangeCallback
import com.paulrybitskyi.commons.utils.observeChanges

internal class GameArtworksView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var isScrollingEnabled: Boolean
        set(value) { viewPager.isUserInputEnabled = value }
        get() = viewPager.isUserInputEnabled

    var isArtworkClickEnabled = true

    private lateinit var adapter: GameArtworksAdapter

    private var adapterItems by observeChanges<List<GameArtworkItem>>(emptyList()) { _, newItems ->
        adapter.submitList(newItems)
    }

    var artworkModels by observeChanges<List<GameArtworkModel>>(emptyList()) { _, newItems ->
        adapterItems = newItems.map(::GameArtworkItem)
    }

    private lateinit var viewPager: ViewPager2

    var onArtworkChanged: ((Int) -> Unit)? = null
    var onArtworkClicked: ((Int) -> Unit)? = null

    init {
        initViewPager(context)
    }

    private fun initViewPager(context: Context) {
        viewPager = ViewPager2(context)
            .apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                recyclerView?.overScrollMode = OVER_SCROLL_NEVER
                registerOnPageChangeCallback(onPageSelected = { onArtworkChanged?.invoke(it) })
                adapter = initAdapter(context)
            }
            .also(::addView)
    }

    private fun initAdapter(context: Context): GameArtworksAdapter {
        return GameArtworksAdapter(context)
            .apply { listenerBinder = ::bindListener }
            .also { adapter = it }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun bindListener(item: GameArtworkItem, viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is GameArtworkItem.ViewHolder) {
            viewHolder.setOnArtworkClickListener {
                if (isArtworkClickEnabled) {
                    onArtworkClicked?.invoke(viewPager.currentItem)
                }
            }
        }
    }
}
