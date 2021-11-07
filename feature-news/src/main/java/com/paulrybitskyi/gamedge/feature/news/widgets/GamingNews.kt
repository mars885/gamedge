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
    onNewsItemClicked: ((GamingNewsItemModel) -> Unit)? = null,
    onRefreshRequested: (() -> Unit)? = null
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
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Info(
            icon = painterResource(R.drawable.newspaper_variant_outline),
            title = stringResource(R.string.gaming_news_info_view_title),
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.gaming_news_info_view_horizontal_margin)
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
    onRefreshRequested: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { onRefreshRequested?.invoke() },
        modifier = modifier,
        indicator = { state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                contentColor = colorResource(R.color.gaming_news_swipe_refresh_color)
            )
        },
        content = content,
    )
}

@Composable
private fun GamingNewsSuccessState(
    news: List<GamingNewsItemModel>,
    onNewsItemClicked: ((GamingNewsItemModel) -> Unit)? = null,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.gaming_news_decorator_spacing))
    ) {
        items(news, key = GamingNewsItemModel::id) { newItemModel ->
            GamingNewsItem(
                imageUrl = newItemModel.imageUrl,
                title = newItemModel.title,
                lede = newItemModel.lede,
                publicationDate = newItemModel.publicationDate,
                onClick = { onNewsItemClicked?.invoke(newItemModel) }
            )
        }
    }
}

@Preview
@Composable
internal fun GamingNewsEmptyStatePreview() {
    GamingNews(uiState = GamingNewsState())
}

@Preview
@Composable
internal fun GamingNewsLoadingStatePreview() {
    GamingNews(uiState = GamingNewsState(isLoading = true))
}

@Preview
@Composable
internal fun GamingNewsSuccessStatePreview() {
    GamingNews(
        uiState = GamingNewsState(
            news = listOf(
                GamingNewsItemModel(
                    id = 5,
                    imageUrl = null,
                    title = "Title",
                    lede = "Lede",
                    publicationDate = "3 mins ago",
                    siteDetailUrl = "url"
                )
            )
        )
    )
}
