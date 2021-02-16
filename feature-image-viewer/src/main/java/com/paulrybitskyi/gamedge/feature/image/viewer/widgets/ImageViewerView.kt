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

package com.paulrybitskyi.gamedge.feature.image.viewer.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.MarginPageTransformer
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.extensions.recyclerView
import com.paulrybitskyi.gamedge.commons.ui.extensions.registerOnPageChangeCallback
import com.paulrybitskyi.gamedge.feature.image.viewer.R
import com.paulrybitskyi.gamedge.feature.image.viewer.databinding.ViewImageViewerBinding


private const val VIEW_PAGER_OFFSCREEN_LIMIT = 1


internal class ImageViewerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {


    private val binding = ViewImageViewerBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: ImageViewerAdapter

    private var adapterItems by observeChanges<List<ImageViewerItem>>(emptyList()) { _, newItems ->
        adapter.submitList(newItems) {
            selectedPosition = selectedPosition
        }
    }

    var toolbarTitle: CharSequence
        set(value) { binding.toolbar.titleText = value }
        get() = binding.toolbar.titleText

    var selectedPosition by observeChanges(0) { _, newValue ->
        onSelectedPositionChanged(newValue)
    }

    var imageUrls by observeChanges<List<String>>(emptyList()) { _, newItems ->
        adapterItems = newItems.map(::ImageViewerItem)
    }

    var onToolbarLeftBtnClicked: (() -> Unit)? = null
    var onToolbarRightBtnClicked: (() -> Unit)? = null
    var onPageChanged: ((Int) -> Unit)? = null


    init {
        initToolbar()
        initViewPager(context)
    }


    private fun initToolbar() = with(binding.toolbar) {
        removeElevation()
        applyWindowTopInsetAsMargin()
        onLeftButtonClickListener = { onToolbarLeftBtnClicked?.invoke() }
        onRightButtonClickListener = { onToolbarRightBtnClicked?.invoke() }
    }


    @SuppressLint("WrongConstant")
    private fun initViewPager(context: Context) = with(binding.viewPager) {
        recyclerView?.overScrollMode = OVER_SCROLL_NEVER
        offscreenPageLimit = VIEW_PAGER_OFFSCREEN_LIMIT
        setPageTransformer(MarginPageTransformer(getDimensionPixelSize(R.dimen.image_viewer_page_margin)))
        registerOnPageChangeCallback(onPageSelected = { onPageChanged?.invoke(it) })
        adapter = initAdapter(context)
    }


    private fun initAdapter(context: Context): ImageViewerAdapter {
        return ImageViewerAdapter(context)
            .also { adapter = it }
    }


    private fun onSelectedPositionChanged(newPosition: Int) {
        if(binding.viewPager.currentItem == newPosition) return

        binding.viewPager.setCurrentItem(newPosition, false)
    }


    fun isCurrentImageScaled(): Boolean {
        val imageViewerItemView = getCurrentImageViewerItemView()
        val isScaled = (imageViewerItemView?.isScaled == true)

        return isScaled
    }


    fun resetCurrentImageScale() {
        getCurrentImageViewerItemView()?.resetScale(animate = true)
    }


    private fun getCurrentImageViewerItemView(): ImageViewerItemView? {
        if(binding.viewPager.adapter == null) return null
        if(binding.viewPager.adapter!!.itemCount == 0) return null

        val recyclerView = binding.viewPager.recyclerView
        val viewHolder = recyclerView?.findViewHolderForAdapterPosition(selectedPosition)
        val imageViewerItemView = (viewHolder?.itemView as? ImageViewerItemView)

        return imageViewerItemView
    }


}