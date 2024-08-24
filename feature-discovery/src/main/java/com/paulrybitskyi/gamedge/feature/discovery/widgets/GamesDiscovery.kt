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

package com.paulrybitskyi.gamedge.feature.discovery.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.paulrybitskyi.gamedge.common.ui.CommandsHandler
import com.paulrybitskyi.gamedge.common.ui.RoutesHandler
import com.paulrybitskyi.gamedge.common.ui.base.events.Route
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.widgets.RefreshableContent
import com.paulrybitskyi.gamedge.common.ui.widgets.categorypreview.GamesCategoryPreview
import com.paulrybitskyi.gamedge.common.ui.widgets.toolbars.Toolbar
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryCategory
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryViewModel
import com.paulrybitskyi.gamedge.feature.discovery.R
import com.paulrybitskyi.gamedge.feature.discovery.titleId
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.paulrybitskyi.gamedge.core.R as CoreR

// Intentional delay to keep the pull refresh visible
// because as soon as it is let go, it disappears instantaneously.
private const val PullRefreshIntentionalDelay = 300L

@Composable
fun GamesDiscovery(
    modifier: Modifier,
    onRoute: (Route) -> Unit,
) {
    GamesDiscovery(
        viewModel = hiltViewModel(),
        modifier = modifier,
        onRoute = onRoute,
    )
}

@Composable
private fun GamesDiscovery(
    viewModel: GamesDiscoveryViewModel,
    modifier: Modifier,
    onRoute: (Route) -> Unit,
) {
    CommandsHandler(viewModel = viewModel)
    RoutesHandler(viewModel = viewModel, onRoute = onRoute)
    GamesDiscovery(
        items = viewModel.items.collectAsState().value,
        onCategoryMoreButtonClicked = viewModel::onCategoryMoreButtonClicked,
        onSearchButtonClicked = viewModel::onSearchButtonClicked,
        onCategoryGameClicked = viewModel::onCategoryGameClicked,
        onRefreshRequested = viewModel::onRefreshRequested,
        modifier = modifier,
    )
}

@Composable
private fun GamesDiscovery(
    items: List<GamesDiscoveryItemUiModel>,
    onSearchButtonClicked: () -> Unit,
    onCategoryMoreButtonClicked: (category: String) -> Unit,
    onCategoryGameClicked: (GamesDiscoveryItemGameUiModel) -> Unit,
    onRefreshRequested: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        modifier = modifier,
        topBar = {
            Toolbar(
                title = stringResource(R.string.games_discovery_toolbar_title),
                rightButtonIcon = painterResource(CoreR.drawable.magnify),
                onRightButtonClick = onSearchButtonClicked,
            )
        },
    ) { paddingValues ->
        var isRefreshing by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        RefreshableContent(
            isRefreshing = isRefreshing,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onRefreshRequested = {
                isRefreshing = true

                coroutineScope.launch {
                    delay(PullRefreshIntentionalDelay)
                    onRefreshRequested()
                    isRefreshing = false
                }
            },
        ) {
            CategoryPreviewItems(
                items = items,
                onCategoryMoreButtonClicked = onCategoryMoreButtonClicked,
                onCategoryGameClicked = onCategoryGameClicked,
            )
        }
    }
}

@Composable
private fun CategoryPreviewItems(
    items: List<GamesDiscoveryItemUiModel>,
    onCategoryMoreButtonClicked: (category: String) -> Unit,
    onCategoryGameClicked: (GamesDiscoveryItemGameUiModel) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(GamedgeTheme.spaces.spacing_3_5),
    ) {
        items(items = items, key = GamesDiscoveryItemUiModel::id) { item ->
            val categoryGames = remember(item.games) {
                item.games.mapToCategoryUiModels()
            }

            GamesCategoryPreview(
                title = item.title,
                isProgressBarVisible = item.isProgressBarVisible,
                games = categoryGames,
                onCategoryGameClicked = { onCategoryGameClicked(it.mapToDiscoveryUiModel()) },
                onCategoryMoreButtonClicked = { onCategoryMoreButtonClicked(item.categoryName) },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun GamesDiscoverySuccessStatePreview() {
    val games = listOf(
        "Ghost of Tsushima: Director's Cut",
        "Outer Wilds: Echoes of the Eye",
        "Kena: Bridge of Spirits",
        "Forza Horizon 5",
    )
    .mapIndexed { index, gameTitle ->
        GamesDiscoveryItemGameUiModel(id = index, title = gameTitle, coverUrl = null)
    }

    val items = GamesDiscoveryCategory.entries.map { category ->
        GamesDiscoveryItemUiModel(
            id = category.id,
            categoryName = category.name,
            title = stringResource(category.titleId),
            isProgressBarVisible = true,
            games = games,
        )
    }

    GamedgeTheme {
        GamesDiscovery(
            items = items,
            onSearchButtonClicked = {},
            onCategoryMoreButtonClicked = {},
            onCategoryGameClicked = {},
            onRefreshRequested = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun GamesDiscoveryEmptyStatePreview() {
    val items = GamesDiscoveryCategory.entries.map { category ->
        GamesDiscoveryItemUiModel(
            id = category.id,
            categoryName = category.name,
            title = stringResource(category.titleId),
            isProgressBarVisible = true,
            games = emptyList(),
        )
    }

    GamedgeTheme {
        GamesDiscovery(
            items = items,
            onSearchButtonClicked = {},
            onCategoryMoreButtonClicked = {},
            onCategoryGameClicked = {},
            onRefreshRequested = {},
        )
    }
}
