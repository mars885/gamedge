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

package com.paulrybitskyi.gamedge.feature.discovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.paulrybitskyi.gamedge.commons.ui.base.BaseComposeFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscovery
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamesDiscoveryFragment : BaseComposeFragment<
    GamesDiscoveryViewModel,
    GamesDiscoveryNavigator
>() {

    override val viewModel by viewModels<GamesDiscoveryViewModel>()

    override fun getContent() = @Composable {
        GamesDiscovery(
            items = viewModel.items.collectAsState().value,
            onCategoryMoreButtonClicked = viewModel::onCategoryMoreButtonClicked,
            onCategoryGameClicked = viewModel::onCategoryGameClicked,
            onRefreshRequested = viewModel::onRefreshRequested
        )
    }

    override fun onLoadData() {
        super.onLoadData()

        viewModel.loadData()
    }

    override fun onRoute(route: Route) {
        super.onRoute(route)

        when (route) {
            is GamesDiscoveryRoute.Category -> navigator.goToCategory(route.category)
            is GamesDiscoveryRoute.Info -> navigator.goToInfo(route.gameId)
        }
    }
}
