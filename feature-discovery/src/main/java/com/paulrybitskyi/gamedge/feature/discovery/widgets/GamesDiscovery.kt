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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.RefreshableContent
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreview
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryCategory
import com.paulrybitskyi.gamedge.feature.discovery.titleId
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Intentional delay to keep the swipe refresh visible
// because as soon as it is let go, it disappears instantaneously.
private const val SWIPE_REFRESH_INTENTIONAL_DELAY = 300L

@Composable
internal fun GamesDiscovery(
    items: List<GamesDiscoveryItemModel>,
    onCategoryMoreButtonClicked: (category: String) -> Unit,
    onCategoryGameClicked: (GamesDiscoveryItemGameModel) -> Unit,
    onRefreshRequested: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = GamedgeTheme.colors.background,
    ) {
        Box(Modifier.fillMaxSize()) {
            var isRefreshing by remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()

            RefreshableContent(
                isRefreshing = isRefreshing,
                modifier = Modifier.matchParentSize(),
                onRefreshRequested = {
                    isRefreshing = true

                    coroutineScope.launch {
                        delay(SWIPE_REFRESH_INTENTIONAL_DELAY)
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
}

@Composable
private fun CategoryPreviewItems(
    items: List<GamesDiscoveryItemModel>,
    onCategoryMoreButtonClicked: (category: String) -> Unit,
    onCategoryGameClicked: (GamesDiscoveryItemGameModel) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(GamedgeTheme.spaces.spacing_3_5),
    ) {
        items(items, key = GamesDiscoveryItemModel::id) { item ->
            val categoryGames = remember(item.games) {
                item.games.mapToCategoryItems()
            }

            GamesCategoryPreview(
                title = item.title,
                isProgressBarVisible = item.isProgressBarVisible,
                games = categoryGames,
                onCategoryGameClicked = { onCategoryGameClicked(it.mapToDiscoveryItemGameModel()) },
                onCategoryMoreButtonClicked = { onCategoryMoreButtonClicked(item.categoryName) },
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
            onCategoryMoreButtonClicked = {},
            onCategoryGameClicked = {},
            onRefreshRequested = {},
        )
    }
}

@Preview
@Composable
internal fun GamesDiscoveryEmptyStatePreview() {
    val items = GamesDiscoveryCategory.values().map { category ->
        GamesDiscoveryItemModel(
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
            onCategoryMoreButtonClicked = {},
            onCategoryGameClicked = {},
            onRefreshRequested = {},
        )
    }
}
