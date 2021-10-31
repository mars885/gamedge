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

package com.paulrybitskyi.gamedge.feature.info

import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.applyWindowBottomInsetAsMargin
import com.paulrybitskyi.commons.ktx.showShortToast
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.commons.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Command
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.defaultWindowAnimationDuration
import com.paulrybitskyi.gamedge.commons.ui.observeIn
import com.paulrybitskyi.gamedge.core.urlopener.UrlOpener
import com.paulrybitskyi.gamedge.feature.info.databinding.FragmentGameInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
internal class GameInfoFragment : BaseFragment<
    FragmentGameInfoBinding,
    GameInfoViewModel,
    GameInfoNavigator
>(R.layout.fragment_game_info) {


    override val viewBinding by viewBinding(FragmentGameInfoBinding::bind)
    override val viewModel by viewModels<GameInfoViewModel>()

    @Inject lateinit var urlOpener: UrlOpener


    override fun onInit() {
        super.onInit()

        initGameInfoView()
    }


    private fun initGameInfoView() = with(viewBinding.gameInfoView) {
        applyWindowBottomInsetAsMargin()

        onArtworkClicked = viewModel::onArtworkClicked
        onBackButtonClicked = viewModel::onBackButtonClicked
        onCoverClicked = viewModel::onCoverClicked
        onLikeButtonClicked = viewModel::onLikeButtonClicked
        onVideoClicked = viewModel::onVideoClicked
        onScreenshotClicked = viewModel::onScreenshotClicked
        onLinkClicked = viewModel::onLinkClicked
        onCompanyClicked = viewModel::onCompanyClicked
        onRelatedGameClicked = viewModel::onRelatedGameClicked
    }


    override fun onBindViewModel() {
        super.onBindViewModel()

        observeUiState()
    }


    private fun observeUiState() {
        viewModel.uiState
            .onEach { viewBinding.gameInfoView.uiState = it }
            .observeIn(this)
    }


    override fun onLoadData() {
        super.onLoadData()

        viewModel.loadData(requireContext().defaultWindowAnimationDuration())
    }


    override fun onHandleCommand(command: Command) {
        super.onHandleCommand(command)

        when(command) {
            is GameInfoCommand.OpenUrl -> openUrl(command.url)
        }
    }


    private fun openUrl(url: String) {
        if(!urlOpener.openUrl(url, requireActivity())) {
            showShortToast(getString(R.string.url_opener_not_found))
        }
    }


    override fun onRoute(route: Route) {
        super.onRoute(route)

        when(route) {
            is GameInfoRoute.ImageViewer -> navigateToImageViewer(route)
            is GameInfoRoute.Info -> navigator.goToInfo(route.gameId)
            is GameInfoRoute.Back -> navigator.goBack()
        }
    }


    private fun navigateToImageViewer(route: GameInfoRoute.ImageViewer) {
        navigator.goToImageViewer(
            route.title,
            route.initialPosition,
            route.imageUrls
        )
    }


}
