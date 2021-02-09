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

package com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.paulrybitskyi.commons.ktx.applyWindowTopInsetAsMargin
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.ktx.setListener
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.cancelActiveAnimations
import com.paulrybitskyi.gamedge.commons.ui.extensions.recyclerView
import com.paulrybitskyi.gamedge.commons.ui.extensions.registerOnPageChangeCallback
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.databinding.ViewGameArtworksBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val PAGE_INDICATOR_FADING_DURATION = 500L
private const val PAGE_INDICATOR_FADING_OUT_TRANSITION_TIME = 3000L

private val PAGE_INDICATOR_FADING_INTERPOLATOR = LinearInterpolator()


@AndroidEntryPoint
internal class GameArtworksView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    private val isPageIndicatorEnabled: Boolean
        get() = (adapterItems.size > 1)

    private val binding = ViewGameArtworksBinding.inflate(context.layoutInflater, this)

    private lateinit var adapter: GameArtworksAdapter

    private var adapterItems by observeChanges<List<GameArtworkItem>>(emptyList()) { _, newItems ->
        adapter.submitList(newItems)
    }

    var artworkModels by observeChanges<List<GameArtworkModel>>(emptyList()) { _, newItems ->
        adapterItems = newItems.map(::GameArtworkItem)
    }

    private val animationHandler = Handler()
    private val fadeOutPageIndicatorAction = Runnable(::fadeOutPageIndicator)

    @Inject lateinit var stringProvider: StringProvider


    init {
        initViewPager(context)
    }


    private fun initViewPager(context: Context) = with(binding.viewPager) {
        recyclerView?.overScrollMode = OVER_SCROLL_NEVER
        registerOnPageChangeCallback(onPageSelected = ::onArtworkPageChanged)
        adapter = initAdapter(context)
    }


    private fun onArtworkPageChanged(position: Int) {
        if(!isPageIndicatorEnabled) return

        updatePageIndicatorPosition(position)
        updatePageIndicatorAnimation()
    }


    private fun updatePageIndicatorPosition(newPosition: Int) {
        val oneIndexedPosition = (newPosition + 1)

        binding.pageIndicatorTv.text = stringProvider.getString(
            R.string.game_artworks_page_indicator_template,
            oneIndexedPosition,
            adapterItems.size
        )
    }


    private fun updatePageIndicatorAnimation() {
        animationHandler.removeCallbacks(fadeOutPageIndicatorAction)

        fadeInPageIndicator()
    }


    private fun initAdapter(context: Context): GameArtworksAdapter {
        return GameArtworksAdapter(context)
            .also { adapter = it }
    }


    fun applyWindowTopOffset() {
        binding.pageIndicatorTv.applyWindowTopInsetAsMargin()
    }


    private fun fadeInPageIndicator() = with(binding.pageIndicatorTv) {
        cancelActiveAnimations()
        isVisible = true

        animate().alpha(1f)
            .setDuration(PAGE_INDICATOR_FADING_DURATION)
            .setInterpolator(PAGE_INDICATOR_FADING_INTERPOLATOR)
            .setListener(onEnd = { onPageIndicatorFadedIn() })
            .start()
    }


    private fun onPageIndicatorFadedIn() = with(animationHandler) {
        removeCallbacks(fadeOutPageIndicatorAction)
        postDelayed(
            fadeOutPageIndicatorAction,
            PAGE_INDICATOR_FADING_OUT_TRANSITION_TIME
        )
    }


    private fun fadeOutPageIndicator() = with(binding.pageIndicatorTv) {
        animate().alpha(0f)
            .setDuration(PAGE_INDICATOR_FADING_DURATION)
            .setInterpolator(PAGE_INDICATOR_FADING_INTERPOLATOR)
            .setListener(onEnd = { isVisible = false })
            .start()
    }


    override fun onDetachedFromWindow() {
        if(isPageIndicatorEnabled) cleanUpPageIndicatorAnimations()

        super.onDetachedFromWindow()
    }


    private fun cleanUpPageIndicatorAnimations() {
        animationHandler.removeCallbacks(fadeOutPageIndicatorAction)

        with(binding.pageIndicatorTv) {
            cancelActiveAnimations()
            alpha = 0f
            isVisible = false
        }
    }


}