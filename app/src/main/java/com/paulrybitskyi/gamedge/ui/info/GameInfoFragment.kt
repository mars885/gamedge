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

package com.paulrybitskyi.gamedge.ui.info

import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.showShortToast
import com.paulrybitskyi.commons.navigation.navController
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.core.urlopener.UrlOpenerFactory
import com.paulrybitskyi.gamedge.databinding.FragmentGameInfoBinding
import com.paulrybitskyi.gamedge.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.ui.base.events.Command
import com.paulrybitskyi.gamedge.ui.base.events.Route
import com.paulrybitskyi.gamedge.utils.defaultWindowAnimationDuration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class GameInfoFragment : BaseFragment<
    FragmentGameInfoBinding,
    GameInfoViewModel
>(R.layout.fragment_game_info) {


    override val viewBinding by viewBinding(FragmentGameInfoBinding::bind)
    override val viewModel by viewModels<GameInfoViewModel>()

    @Inject lateinit var urlOpenerFactory: UrlOpenerFactory


    override fun onInit() {
        super.onInit()

        initGameInfoView()
    }


    private fun initGameInfoView() = with(viewBinding.gameInfoView) {
        onBackButtonClickListener = viewModel::onBackButtonClicked
        onLikeButtonClickListener = viewModel::onLikeButtonClicked
        onVideoClickListener = viewModel::onVideoClicked
        onLinkClickListener = viewModel::onLinkClicked
        onCompanyClickListener = viewModel::onCompanyClicked
        onRelatedGameClickListener = viewModel::onRelatedGameClicked
    }


    override fun onBindViewModel() = with(viewModel) {
        super.onBindViewModel()

        observeInfoUiState()
    }


    private fun GameInfoViewModel.observeInfoUiState() {
        infoUiState.observe(viewLifecycleOwner) {
            viewBinding.gameInfoView.uiState = it
        }
    }


    override fun onLoadData() {
        super.onLoadData()

        viewModel.loadData(requireContext().defaultWindowAnimationDuration())
    }


    override fun onHandleCommand(command: Command) {
        super.onHandleCommand(command)

        when(command) {
            is GameInfoCommands.OpenUrl -> openUrl(command.url)
        }
    }


    private fun openUrl(url: String) {
        val activityContext = requireActivity()

        urlOpenerFactory.getUrlOpener(url, activityContext)
            ?.openUrl(url, activityContext)
            ?: showShortToast(getString(R.string.url_opener_not_found))
    }


    override fun onRoute(route: Route) {
        super.onRoute(route)

        when(route) {
            is GameInfoRoutes.Info -> navigateToInfoScreen(route.gameId)
            is GameInfoRoutes.Back -> navController.popBackStack()
        }
    }


    private fun navigateToInfoScreen(gameId: Int) {
        navController.navigate(GameInfoFragmentDirections.actionInfoFragment(gameId))
    }


}