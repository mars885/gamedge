/*
 * Copyright 2021 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.feature.news.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.paulrybitskyi.commons.ktx.showShortToast
import com.paulrybitskyi.gamedge.common.ui.CommandsHandler
import com.paulrybitskyi.gamedge.common.ui.LocalUrlOpener
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.widgets.AnimatedContentContainer
import com.paulrybitskyi.gamedge.common.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.common.ui.widgets.GamedgeProgressIndicator
import com.paulrybitskyi.gamedge.common.ui.widgets.Info
import com.paulrybitskyi.gamedge.common.ui.widgets.RefreshableContent
import com.paulrybitskyi.gamedge.common.ui.widgets.toolbars.Toolbar
import com.paulrybitskyi.gamedge.feature.news.R
import com.paulrybitskyi.gamedge.feature.news.presentation.widgets.GamingNewsItem
import com.paulrybitskyi.gamedge.feature.news.presentation.widgets.GamingNewsItemUiModel
import com.paulrybitskyi.gamedge.core.R as CoreR

@Composable
fun GamingNewsScreen(modifier: Modifier) {
    GamingNewsScreen(
        viewModel = hiltViewModel(),
        modifier = modifier,
    )
}

@Composable
private fun GamingNewsScreen(
    viewModel: GamingNewsViewModel,
    modifier: Modifier,
) {
    val urlOpener = LocalUrlOpener.current
    val context = LocalContext.current

    CommandsHandler(viewModel = viewModel) { command ->
        when (command) {
            is GamingNewsCommand.OpenUrl -> {
                if (!urlOpener.openUrl(command.url, context)) {
                    context.showShortToast(context.getString(CoreR.string.url_opener_not_found))
                }
            }
        }
    }
    GamingNewsScreen(
        uiState = viewModel.uiState.collectAsState().value,
        onNewsItemClicked = viewModel::onNewsItemClicked,
        onRefreshRequested = viewModel::onRefreshRequested,
        modifier = modifier,
    )
}

@Composable
private fun GamingNewsScreen(
    uiState: GamingNewsUiState,
    onNewsItemClicked: (GamingNewsItemUiModel) -> Unit,
    onRefreshRequested: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        modifier = modifier,
        topBar = {
            Toolbar(title = stringResource(R.string.gaming_news_toolbar_title))
        },
    ) { paddingValues ->
        AnimatedContentContainer(
            finiteUiState = uiState.finiteUiState,
            modifier = Modifier.padding(paddingValues),
        ) { finiteUiState ->
            when (finiteUiState) {
                FiniteUiState.Loading -> {
                    LoadingState(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    RefreshableContent(
                        isRefreshing = uiState.isRefreshing,
                        modifier = Modifier.matchParentSize(),
                        onRefreshRequested = onRefreshRequested,
                    ) {
                        if (finiteUiState == FiniteUiState.Empty) {
                            EmptyState(modifier = Modifier.matchParentSize())
                        } else {
                            SuccessState(
                                news = uiState.news,
                                onNewsItemClicked = onNewsItemClicked,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    GamedgeProgressIndicator(modifier = modifier)
}

@Composable
private fun EmptyState(modifier: Modifier) {
    Column(
        // verticalScroll is to enable PullRefresh to work
        // when the screen is in empty state
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Info(
            icon = painterResource(CoreR.drawable.newspaper_variant_outline),
            title = stringResource(R.string.gaming_news_info_view_title),
            modifier = Modifier.padding(
                horizontal = GamedgeTheme.spaces.spacing_7_5,
            ),
        )
    }
}

@Composable
private fun SuccessState(
    news: List<GamingNewsItemUiModel>,
    onNewsItemClicked: (GamingNewsItemUiModel) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(GamedgeTheme.spaces.spacing_3_5),
    ) {
        items(items = news, key = GamingNewsItemUiModel::id) { itemModel ->
            GamingNewsItem(
                model = itemModel,
                onClick = { onNewsItemClicked(itemModel) },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun GamingNewsScreenSuccessStatePreview() {
    val news = listOf(
        GamingNewsItemUiModel(
            id = 1,
            imageUrl = "",
            title = "Halo Infinite Season 1 Will Run Until May 2022",
            lede = "Season 1 has been extended until May 2020, which " +
                    "might mean campaign co-op and Forge are coming even later than expected.",
            publicationDate = "3 mins ago",
            siteDetailUrl = "url",
        ),
        GamingNewsItemUiModel(
            id = 2,
            imageUrl = "",
            title = "Call of Duty: Vanguard's UK Launch Sales are Down 40% From Last Year",
            lede = "Call of Duty: Vanguard's launch sales are down about 40% compared to last year's " +
                "Call of Duty: Black Ops Cold War in the UK.",
            publicationDate = "an hour ago",
            siteDetailUrl = "url",
        ),
        GamingNewsItemUiModel(
            id = 3,
            imageUrl = null,
            title = "WoW Classic Season of Mastery: Full List of Changes",
            lede = "World of Warcraft Classic's first season is nearly here, and Blizzard has " +
                "detailed all the changes players can expect.",
            publicationDate = "2 hours ago",
            siteDetailUrl = "url",
        ),
    )

    GamedgeTheme {
        GamingNewsScreen(
            uiState = GamingNewsUiState(
                news = news,
            ),
            onNewsItemClicked = {},
            onRefreshRequested = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun GamingNewsScreenEmptyStatePreview() {
    GamedgeTheme {
        GamingNewsScreen(
            uiState = GamingNewsUiState(),
            onNewsItemClicked = {},
            onRefreshRequested = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun GamingNewsScreenLoadingStatePreview() {
    GamedgeTheme {
        GamingNewsScreen(
            uiState = GamingNewsUiState(isLoading = true),
            onNewsItemClicked = {},
            onRefreshRequested = {},
        )
    }
}
