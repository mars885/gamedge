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

package com.paulrybitskyi.gamedge.feature.news

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.showShortToast
import com.paulrybitskyi.gamedge.commons.ui.base.BaseComposeFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Command
import com.paulrybitskyi.gamedge.commons.ui.base.navigation.StubNavigator
import com.paulrybitskyi.gamedge.core.urlopener.UrlOpener
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNews
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GamingNewsFragment : BaseComposeFragment<GamingNewsViewModel, StubNavigator>() {

    override val viewBinding by viewBinding(FragmentGamingNewsBinding::bind)
    override val viewModel by viewModels<GamingNewsViewModel>()

    @Inject lateinit var urlOpener: UrlOpener

    @Composable
    override fun InitUi() {
        GamingNews(
            uiState = viewModel.uiState.collectAsState().value,
            onNewsItemClicked = viewModel::onNewsItemClicked,
            onRefreshRequested = viewModel::onRefreshRequested
        )
    }

    override fun onLoadData() {
        super.onLoadData()

        viewModel.loadData()
    }

    override fun onHandleCommand(command: Command) {
        super.onHandleCommand(command)

        when (command) {
            is GamingNewsCommand.OpenUrl -> openUrl(command.url)
        }
    }

    private fun openUrl(url: String) {
        if (!urlOpener.openUrl(url, requireActivity())) {
            showShortToast(getString(R.string.url_opener_not_found))
        }
    }
}
