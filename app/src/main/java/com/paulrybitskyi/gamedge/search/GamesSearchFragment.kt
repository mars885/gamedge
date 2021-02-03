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

package com.paulrybitskyi.gamedge.search

import android.text.InputType
import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.applyWindowTopInsetAsPadding
import com.paulrybitskyi.commons.navigation.navController
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.base.BaseFragment
import com.paulrybitskyi.gamedge.base.events.Route
import com.paulrybitskyi.gamedge.dashboard.fragment.DashboardFragmentDirections
import com.paulrybitskyi.gamedge.databinding.FragmentGamesSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class GamesSearchFragment : BaseFragment<
    FragmentGamesSearchBinding,
    GamesSearchViewModel
>(R.layout.fragment_games_search) {


    override val viewBinding by viewBinding(FragmentGamesSearchBinding::bind)
    override val viewModel by viewModels<GamesSearchViewModel>()


    override fun onInit() {
        super.onInit()

        initSearchToolbar()
        initGamesView()
    }


    private fun initSearchToolbar() = with(viewBinding.searchToolbar) {
        applyWindowTopInsetAsPadding()

        inputType = (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
        hintText = getString(R.string.games_search_fragment_search_hint)
        onQueryChangeListener = viewModel::onQueryChanged
        onBackButtonClickListener = { viewModel.onToolbarBackButtonClicked() }
    }


    private fun initGamesView() = with(viewBinding.gamesView) {
        onGameClickListener = viewModel::onGameClicked
        onBottomReachListener = viewModel::onBottomReached
    }


    override fun onBindViewModel() = with(viewModel) {
        super.onBindViewModel()

        observeGamesUiState()
    }


    private fun GamesSearchViewModel.observeGamesUiState() {
        gamesUiState.observe(viewLifecycleOwner) {
            viewBinding.gamesView.uiState = it
        }
    }


    override fun onResume() {
        super.onResume()

        if(viewBinding.searchToolbar.isSearchQueryEmpty) {
            viewBinding.searchToolbar.showKeyboard(true)
        }
    }


    override fun onPause() {
        super.onPause()

        viewBinding.searchToolbar.hideKeyboard()
    }


    override fun onRoute(route: Route) {
        super.onRoute(route)

        when(route) {
            is GamesSearchRoute.Info -> navigateToInfoScreen(route.gameId)
            is GamesSearchRoute.Back -> navController.popBackStack()
        }
    }


    private fun navigateToInfoScreen(gameId: Int) {
        navController.navigate(DashboardFragmentDirections.actionInfoFragment(gameId))
    }


}