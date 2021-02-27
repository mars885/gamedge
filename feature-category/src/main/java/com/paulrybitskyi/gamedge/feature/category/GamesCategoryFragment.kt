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

package com.paulrybitskyi.gamedge.feature.category

import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.applyWindowBottomInsetAsMargin
import com.paulrybitskyi.commons.ktx.applyWindowTopInsetAsPadding
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.commons.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.defaultWindowAnimationDuration
import com.paulrybitskyi.gamedge.commons.ui.observeIn
import com.paulrybitskyi.gamedge.feature.category.databinding.FragmentGamesCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
internal class GamesCategoryFragment : BaseFragment<
    FragmentGamesCategoryBinding,
    GamesCategoryViewModel,
    GamesCategoryNavigator
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
        applyWindowBottomInsetAsMargin()
        onGameClickListener = viewModel::onGameClicked
        onBottomReachListener = viewModel::onBottomReached
    }


    override fun onBindViewModel() {
        super.onBindViewModel()

        observeToolbarTitle()
        observeUiState()
    }


    private fun observeToolbarTitle() {
        viewModel.toolbarTitle
            .onEach { viewBinding.toolbar.titleText = it }
            .observeIn(this)
    }


    private fun observeUiState() {
        viewModel.uiState
            .onEach { viewBinding.gamesCategoryView.uiState = it }
            .observeIn(this)
    }


    override fun onLoadData() {
        super.onLoadData()

        viewModel.loadData(requireContext().defaultWindowAnimationDuration())
    }


    override fun onRoute(route: Route) {
        super.onRoute(route)

        when(route) {
            is GamesCategoryRoute.Info -> navigator.navigateToInfo(route.gameId)
            is GamesCategoryRoute.Back -> navigator.navigateBack()
        }
    }


}