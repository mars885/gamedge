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

package com.paulrybitskyi.gamedge.feature.likes

import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.commons.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.observeIn
import com.paulrybitskyi.gamedge.feature.likes.databinding.FragmentLikedGamesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LikedGamesFragment : BaseFragment<
    FragmentLikedGamesBinding,
    LikedGamesViewModel,
    LikedGamesNavigator
>(R.layout.fragment_liked_games) {


    override val viewBinding by viewBinding(FragmentLikedGamesBinding::bind)
    override val viewModel by viewModels<LikedGamesViewModel>()


    override fun onInit() {
        super.onInit()

        initGamesView()
    }


    private fun initGamesView() = with(viewBinding.gamesView) {
        onGameClicked = viewModel::onGameClicked
        onBottomReached = viewModel::onBottomReached
    }


    override fun onBindViewModel() {
        super.onBindViewModel()

        observeUiState()
    }


    private fun observeUiState() {
        viewModel.uiState
            .onEach { viewBinding.gamesView.uiState = it }
            .observeIn(this)
    }


    override fun onLoadData() {
        super.onLoadData()

        viewModel.loadData()
    }


    override fun onRoute(route: Route) {
        super.onRoute(route)

        when(route) {
            is LikedGamesRoute.Info -> navigator.navigateToInfo(route.gameId)
        }
    }


}