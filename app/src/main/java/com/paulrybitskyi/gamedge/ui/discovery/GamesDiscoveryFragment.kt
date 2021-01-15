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

package com.paulrybitskyi.gamedge.ui.discovery

import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.navigation.navController
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.databinding.FragmentGamesDiscoveryBinding
import com.paulrybitskyi.gamedge.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.ui.base.events.Route
import com.paulrybitskyi.gamedge.ui.dashboard.fragment.DashboardFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class GamesDiscoveryFragment : BaseFragment<
    FragmentGamesDiscoveryBinding,
    GamesDiscoveryViewModel
>(R.layout.fragment_games_discovery) {


    override val viewBinding by viewBinding(FragmentGamesDiscoveryBinding::bind)
    override val viewModel by viewModels<GamesDiscoveryViewModel>()


    override fun onInit() {
        super.onInit()

        initDiscoveryView()
    }


    private fun initDiscoveryView() = with(viewBinding.discoveryView) {
        onCategoryMoreButtonClickListener = viewModel::onCategoryMoreButtonClicked
        onCategoryGameClickListener = viewModel::onCategoryGameClicked
        onRefreshListener = viewModel::onRefreshRequested
    }


    override fun onBindViewModel() = with(viewModel) {
        super.onBindViewModel()

        observeDiscoveryItems()
    }


    private fun GamesDiscoveryViewModel.observeDiscoveryItems() {
        discoveryItems.observe(viewLifecycleOwner) {
            viewBinding.discoveryView.items = it
        }
    }


    override fun onLoadData() {
        super.onLoadData()

        viewModel.loadData()
    }


    override fun onRoute(route: Route) {
        super.onRoute(route)

        when(route) {
            is GamesDiscoveryRoute.Info -> navigateToInfoScreen(route.gameId)
        }
    }


    private fun navigateToInfoScreen(gameId: Int) {
        navController.navigate(DashboardFragmentDirections.actionInfoFragment(gameId))
    }


}