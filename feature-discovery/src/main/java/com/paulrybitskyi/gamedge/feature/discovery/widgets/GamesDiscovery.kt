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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreview
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryCategory
import com.paulrybitskyi.gamedge.feature.discovery.R
import com.paulrybitskyi.gamedge.feature.discovery.titleId
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Intentional delay to keep the swipe refresh visible
// because as soon as it is let go, it disappears instantaneously.
private const val SWIPE_REFRESH_INTENTIONAL_DELAY = 300L

@Composable
internal fun GamesDiscovery(
    items: List<GamesDiscoveryItemModel>,
    onCategoryMoreButtonClicked: (String) -> Unit,
    onCategoryGameClicked: (GamesDiscoveryItemGameModel) -> Unit,
    onRefreshRequested: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorContentContainer)),
    ) {
        var isRefreshing by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true

                coroutineScope.launch {
                    delay(SWIPE_REFRESH_INTENTIONAL_DELAY)
                    onRefreshRequested()
                    isRefreshing = false
                }
            },
            modifier = Modifier.matchParentSize(),
            indicator = { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                    contentColor = colorResource(R.color.games_discovery_swipe_indicator_color),
                )
            },
            content = {
                GamesDiscoveryItems(
                    items = items,
                    onCategoryMoreButtonClicked = onCategoryMoreButtonClicked,
                    onCategoryGameClicked = onCategoryGameClicked,
                )
            },
        )
    }
}

@Composable
private fun GamesDiscoveryItems(
    items: List<GamesDiscoveryItemModel>,
    onCategoryMoreButtonClicked: (String) -> Unit,
    onCategoryGameClicked: (GamesDiscoveryItemGameModel) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.games_discovery_arrangement_spacing)),
    ) {
        items(items, key = GamesDiscoveryItemModel::title) { item ->
            GamesCategoryPreview(
                title = item.title,
                isProgressBarVisible = item.isProgressBarVisible,
                games = item.games.mapToCategoryItems(),
                onCategoryGameClicked = { onCategoryGameClicked(it.mapToDiscoveryItemGameModel()) },
                onCategoryMoreButtonClicked = { onCategoryMoreButtonClicked(item.category) },
            )
        }
    }
}

@Preview
@Composable
internal fun GamesDiscoverySuccessStatePreview() {
    val games = listOf(
        "Ghost of Tsushima: Director's Cut",
        "Outer Wilds: Echoes of the Eye",
        "Kena: Bridge of Spirits",
        "Forza Horizon 5",
    )
        .mapIndexed { index, gameTitle ->
            GamesDiscoveryItemGameModel(id = index, title = gameTitle, coverUrl = null)
        }

    val items = GamesDiscoveryCategory.values().map { category ->
        GamesDiscoveryItemModel(
            category = category.name,
            title = stringResource(category.titleId),
            isProgressBarVisible = true,
            games = games,
        )
    }

    GamesDiscovery(
        items = items,
        onCategoryMoreButtonClicked = {},
        onCategoryGameClicked = {},
        onRefreshRequested = {},
    )
}

@Preview
@Composable
internal fun GamesDiscoveryEmptyStatePreview() {
    val items = GamesDiscoveryCategory.values().map { category ->
        GamesDiscoveryItemModel(
            category = category.name,
            title = stringResource(category.titleId),
            isProgressBarVisible = true,
            games = emptyList(),
        )
    }

    GamesDiscovery(
        items = items,
        onCategoryMoreButtonClicked = {},
        onCategoryGameClicked = {},
        onRefreshRequested = {},
    )
}
