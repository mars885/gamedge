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

package com.paulrybitskyi.gamedge.feature.info.widgets.main

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.ktx.makeGone
import com.paulrybitskyi.commons.ktx.makeInvisible
import com.paulrybitskyi.commons.ktx.makeVisible
import com.paulrybitskyi.commons.recyclerview.decorators.spacing.SpacingItemDecorator
import com.paulrybitskyi.commons.recyclerview.utils.disableChangeAnimations
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.base.rv.Item
import com.paulrybitskyi.gamedge.commons.ui.base.rv.NoDependencies
import com.paulrybitskyi.gamedge.commons.ui.fadeIn
import com.paulrybitskyi.gamedge.commons.ui.resetAnimation
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.databinding.ViewGameInfoBinding
import com.paulrybitskyi.gamedge.feature.info.widgets.main.header.GameHeaderController
import com.paulrybitskyi.gamedge.feature.info.widgets.main.items.GameInfoCompaniesItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.items.GameInfoDetailsItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.items.GameInfoLinksItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.items.GameInfoRelatedGamesItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.items.GameInfoScreenshotsItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.items.GameInfoSummaryItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.items.GameInfoVideosItem
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoCompanyModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoLinkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoVideoModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.games.GameInfoRelatedGameModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class GameInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val binding = ViewGameInfoBinding.inflate(context.layoutInflater, this)

    private lateinit var headerController: GameHeaderController
    private lateinit var infoAdapter: GameInfoAdapter

    private var adapterItems by observeChanges<List<Item<*, NoDependencies>>>(emptyList()) { _, newItems ->
        infoAdapter.submitList(newItems)
    }

    var uiState by observeChanges<GameInfoUiState>(GameInfoUiState.Empty) { _, newState ->
        handleUiStateChange(newState)
    }

    @Inject lateinit var stringProvider: StringProvider

    var onArtworkClicked: ((Int) -> Unit)? = null
    var onBackButtonClicked: (() -> Unit)? = null
    var onCoverClicked: (() -> Unit)? = null
    var onLikeButtonClicked: (() -> Unit)? = null
    var onVideoClicked: ((GameInfoVideoModel) -> Unit)? = null
    var onScreenshotClicked: ((Int) -> Unit)? = null
    var onLinkClicked: ((GameInfoLinkModel) -> Unit)? = null
    var onCompanyClicked: ((GameInfoCompanyModel) -> Unit)? = null
    var onRelatedGameClicked: ((GameInfoRelatedGameModel) -> Unit)? = null


    init {
        initGameHeaderController(context)
        initRecyclerView(context)
        initDefaults()
    }


    private fun initGameHeaderController(context: Context) {
        GameHeaderController(context, binding, stringProvider)
            .apply {
                onArtworkClicked = {
                    this@GameInfoView.onArtworkClicked?.invoke(it)
                }

                onBackButtonClicked = {
                    this@GameInfoView.onBackButtonClicked?.invoke()
                }

                onCoverClicked = {
                    this@GameInfoView.onCoverClicked?.invoke()
                }

                onLikeButtonClicked = {
                    this@GameInfoView.onLikeButtonClicked?.invoke()
                }
            }
            .also { headerController = it }
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


    private fun initAdapter(context: Context): GameInfoAdapter {
        return GameInfoAdapter(context)
            .apply { listenerBinder = ::bindListener }
            .also { infoAdapter = it }
    }


    @Suppress("UNUSED_PARAMETER")
    private fun bindListener(item: Item<*, NoDependencies>, viewHolder: RecyclerView.ViewHolder) {
        when(viewHolder) {

            is GameInfoVideosItem.ViewHolder -> with(viewHolder) {
                setOnVideoClickListener { onVideoClicked?.invoke(it) }
            }

            is GameInfoScreenshotsItem.ViewHolder -> with(viewHolder) {
                setOnScreenshotClickListener { onScreenshotClicked?.invoke(it) }
            }

            is GameInfoLinksItem.ViewHolder -> with(viewHolder) {
                setOnLinkClickListener { onLinkClicked?.invoke(it) }
            }

            is GameInfoCompaniesItem.ViewHolder -> with(viewHolder) {
                setOnCompanyClickListener { onCompanyClicked?.invoke(it) }
            }

            is GameInfoRelatedGamesItem.ViewHolder -> with(viewHolder) {
                setOnGameClickListener { onRelatedGameClicked?.invoke(it) }
            }

        }
    }


    private fun initItemDecorator(): SpacingItemDecorator {
        return SpacingItemDecorator(
            spacing = getDimensionPixelSize(R.dimen.game_info_decorator_spacing),
            sideFlags = SpacingItemDecorator.SIDE_TOP
        )
    }


    private fun initDefaults() {
        uiState = uiState
    }


    private fun handleUiStateChange(newState: GameInfoUiState) {
        when(newState) {
            is GameInfoUiState.Empty -> onEmptyStateSelected()
            is GameInfoUiState.Loading -> onLoadingStateSelected()
            is GameInfoUiState.Result -> onResultStateSelected(newState)
        }
    }


    private fun onEmptyStateSelected() {
        showInfoView()
        hideProgressBar()
        hideMainView()
    }


    private fun onLoadingStateSelected() {
        showProgressBar()
        hideInfoView()
        hideMainView()
    }


    private fun onResultStateSelected(uiState: GameInfoUiState.Result) {
        bindModel(uiState.model)

        showMainView()
        hideInfoView()
        hideProgressBar()
    }


    private fun bindModel(model: GameInfoModel) {
        headerController.bindModel(model.headerModel)
        adapterItems = model.toAdapterItems()
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
    }


    private fun hideProgressBar() = with(binding.progressBar) {
        makeGone()
    }


    private fun showMainView() = with(binding.mainView) {
        if(isVisible) return

        makeVisible()
        fadeIn()
    }


    private fun hideMainView() = with(binding.mainView) {
        makeInvisible()
        resetAnimation()
    }


    private fun GameInfoModel.toAdapterItems(): List<Item<*, NoDependencies>> {
        return buildList {
            if(hasVideoModels) add(GameInfoVideosItem(videoModels))
            if(hasScreenshotUrls) add(GameInfoScreenshotsItem(screenshotUrls))
            if(hasSummary) add(GameInfoSummaryItem(checkNotNull(summary)))
            if(hasDetailsModel) add(GameInfoDetailsItem(checkNotNull(detailsModel)))
            if(hasLinkModels) add(GameInfoLinksItem(linkModels))
            if(hasCompanyModels) add(GameInfoCompaniesItem(companyModels))
            if(hasOtherCompanyGames) add(GameInfoRelatedGamesItem(checkNotNull(otherCompanyGames)))
            if(hasSimilarGames) add(GameInfoRelatedGamesItem(checkNotNull(similarGames)))
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        headerController.onAttachedToWindow()
    }


}
