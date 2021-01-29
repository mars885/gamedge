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

package com.paulrybitskyi.gamedge.ui.category

import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.applyWindowTopInsetAsPadding
import com.paulrybitskyi.commons.navigation.navController
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.databinding.FragmentGamesCategoryBinding
import com.paulrybitskyi.gamedge.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.ui.base.events.Route
import com.paulrybitskyi.gamedge.ui.dashboard.fragment.DashboardFragmentDirections
import com.paulrybitskyi.gamedge.utils.defaultWindowAnimationDuration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class GamesCategoryFragment : BaseFragment<
    FragmentGamesCategoryBinding,
    GamesCategoryViewModel
>(R.layout.fragment_games_category) {


    override val viewBinding by viewBinding(FragmentGamesCategoryBinding::bind)
    override val viewModel by viewModels<GamesCategoryViewModel>()


    override fun onInit() {
        super.onInit()

        initToolbar()
        initGamesCategoryView()
    }


    private fun initToolbar() = with(viewBinding.toolbar) {
        applyWindowTopInsetAsPadding()
        onLeftButtonClickListener = { viewModel.onToolbarLeftButtonClicked() }
    }


    private fun initGamesCategoryView() = with(viewBinding.gamesCategoryView) {
        onGameClickListener = viewModel::onGameClicked
        onBottomReachListener = viewModel::onBottomReached
    }


    override fun onBindViewModel() = with(viewModel) {
        super.onBindViewModel()

        observeToolbarTitle()
        observeGamesCategoryUiState()
    }


    private fun GamesCategoryViewModel.observeToolbarTitle() {
        toolbarTitle.observe(viewLifecycleOwner) {
            viewBinding.toolbar.titleText = it
        }
    }


    private fun GamesCategoryViewModel.observeGamesCategoryUiState() {
        uiState.observe(viewLifecycleOwner) {
            viewBinding.gamesCategoryView.uiState = it
        }
    }


    override fun onLoadData() {
        super.onLoadData()

        viewModel.loadData(requireContext().defaultWindowAnimationDuration())
    }


    override fun onRoute(route: Route) {
        super.onRoute(route)

        when(route) {
            is GamesCategoryRoute.Info -> navigateToInfoScreen(route.gameId)
            is GamesCategoryRoute.Back -> navController.popBackStack()
        }
    }


    private fun navigateToInfoScreen(gameId: Int) {
        navController.navigate(DashboardFragmentDirections.actionInfoFragment(gameId))
    }


}