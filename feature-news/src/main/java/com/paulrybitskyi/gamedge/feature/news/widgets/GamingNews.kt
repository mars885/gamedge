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

package com.paulrybitskyi.gamedge.feature.news.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.feature.news.R

@Composable
internal fun GamingNews(
    uiState: GamingNewsState,
    onNewsItemClicked: (GamingNewsItemModel) -> Unit,
    onRefreshRequested: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorContentContainer))
    ) {
        if (uiState.isLoading) {
            GamingNewsLoadingState(Modifier.align(Alignment.Center))
        } else {
            RefreshableContent(
                isRefreshing = uiState.isRefreshing,
                modifier = Modifier.matchParentSize(),
                onRefreshRequested = onRefreshRequested,
            ) {
                when {
                    uiState.isInEmptyState -> GamingNewsEmptyState(Modifier.matchParentSize())
                    uiState.isInSuccessState -> GamingNewsSuccessState(
                        news = uiState.news,
                        onNewsItemClicked = onNewsItemClicked,
                    )
                }
            }
        }
    }
}

@Composable
private fun GamingNewsLoadingState(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = colorResource(R.color.colorProgressBar)
    )
}

@Composable
private fun GamingNewsEmptyState(modifier: Modifier) {
    Column(
        // verticalScroll is to enable SwipeRefresh to work
        // when the screen is in empty state
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Info(
            icon = painterResource(R.drawable.newspaper_variant_outline),
            title = stringResource(R.string.gaming_news_info_view_title),
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.gaming_news_info_view_horizontal_padding)
            ),
            iconColor = colorResource(R.color.colorInfoView),
            titleTextColor = colorResource(R.color.colorInfoView)
        )
    }
}

@Composable
private fun RefreshableContent(
    isRefreshing: Boolean,
    modifier: Modifier,
    onRefreshRequested: () -> Unit,
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefreshRequested,
        modifier = modifier,
        indicator = { state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                contentColor = colorResource(R.color.gaming_news_swipe_indicator_color)
            )
        },
        content = content,
    )
}

@Composable
private fun GamingNewsSuccessState(
    news: List<GamingNewsItemModel>,
    onNewsItemClicked: (GamingNewsItemModel) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.gaming_news_arrangement_spacing))
    ) {
        items(news, key = GamingNewsItemModel::id) { newItemModel ->
            GamingNewsItem(
                imageUrl = newItemModel.imageUrl,
                title = newItemModel.title,
                lede = newItemModel.lede,
                publicationDate = newItemModel.publicationDate,
                onClick = { onNewsItemClicked(newItemModel) }
            )
        }
    }
}

@Preview
@Composable
internal fun GamingNewsEmptyStatePreview() {
    GamingNews(
        uiState = GamingNewsState(),
        onNewsItemClicked = {},
        onRefreshRequested = {}
    )
}

@Preview
@Composable
internal fun GamingNewsLoadingStatePreview() {
    GamingNews(
        uiState = GamingNewsState(isLoading = true),
        onNewsItemClicked = {},
        onRefreshRequested = {}
    )
}

@Preview
@Composable
internal fun GamingNewsSuccessStatePreview() {
    val news = listOf(
        GamingNewsItemModel(
            id = 1,
            imageUrl = "",
            title = "Halo Infinite Season 1 Will Run Until May 2022",
            lede = "Season 1 has been extended until May 2020, which " +
                    "might mean campaign co-op and Forge are coming even later than expected.",
            publicationDate = "3 mins ago",
            siteDetailUrl = "url",
        ),
        GamingNewsItemModel(
            id = 2,
            imageUrl = "",
            title = "Call of Duty: Vanguard's UK Launch Sales are Down 40% From Last Year",
            lede = "Call of Duty: Vanguard's launch sales are down about 40% compared to last year's " +
                "Call of Duty: Black Ops Cold War in the UK.",
            publicationDate = "an hour ago",
            siteDetailUrl = "url",
        ),
        GamingNewsItemModel(
            id = 3,
            imageUrl = null,
            title = "WoW Classic Season of Mastery: Full List of Changes",
            lede = "World of Warcraft Classic's first season is nearly here, and Blizzard has " +
                "detailed all the changes players can expect.",
            publicationDate = "2 hours ago",
            siteDetailUrl = "url",
        ),
    )

    GamingNews(
        uiState = GamingNewsState(
            news = news,
        ),
        onNewsItemClicked = {},
        onRefreshRequested = {}
    )
}
