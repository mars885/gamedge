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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
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
    uiState: GamingNewsUiState,
    onNewsItemClicked: ((GamingNewsItemModel) -> Unit)? = null,
    onRefreshRequested: (() -> Unit)? = null
) {
    /*
        7. AnimateVisibility fade.
        6. SwipeRefresh.
        7. SwipeRefresh for Info.
        5. Dump GamingNewsFragment?
        6. Toolbar bottom-elevation.
     */

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorContentContainer))
    ) {
        when(uiState) {
            is GamingNewsUiState.Empty -> GamingNewsEmptyState()
            is GamingNewsUiState.Loading -> GamingNewsLoadingState()
            is GamingNewsUiState.Result -> GamingNewsResultState(
                news = uiState.items,
                onNewsItemClicked = onNewsItemClicked,
                onRefreshRequested = onRefreshRequested
            )
        }
    }
}


@Composable
private fun BoxScope.GamingNewsEmptyState() {
    Info(
        icon = painterResource(R.drawable.newspaper_variant_outline),
        title = stringResource(R.string.gaming_news_info_view_title),
        modifier = Modifier
            .align(Alignment.Center)
            .padding(horizontal = dimensionResource(R.dimen.gaming_news_info_view_horizontal_margin)),
        iconColor = colorResource(R.color.colorInfoView),
        titleTextColor = colorResource(R.color.colorInfoView)
    )
}


@Composable
private fun BoxScope.GamingNewsLoadingState() {
    CircularProgressIndicator(
        modifier = Modifier.align(Alignment.Center),
        color = colorResource(R.color.colorProgressBar)
    )
}


@Composable
private fun BoxScope.GamingNewsResultState(
    news: List<GamingNewsItemModel>,
    onNewsItemClicked: ((GamingNewsItemModel) -> Unit)? = null,
    onRefreshRequested: (() -> Unit)? = null
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(false),
        onRefresh = { onRefreshRequested?.invoke() },
        modifier = Modifier.matchParentSize(),
        indicator = { state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                contentColor = colorResource(R.color.gaming_news_swipe_refresh_color)
            )
        }
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.gaming_news_decorator_spacing))) {
            items(news, key = GamingNewsItemModel::id) {
                GamingNewsItem(
                    imageUrl = it.imageUrl,
                    title = it.title,
                    lede = it.lede,
                    publicationDate = it.publicationDate,
                    onClick = { onNewsItemClicked?.invoke(it) }
                )
            }
        }
    }
}


@Preview
@Composable
private fun GamingNewsEmptyStatePreview() {
    GamingNews(uiState = GamingNewsUiState.Empty)
}


@Preview
@Composable
private fun GamingNewsLoadingStatePreview() {
    GamingNews(uiState = GamingNewsUiState.Loading)
}


@Preview
@Composable
private fun GamingNewsResultStatePreview() {
    GamingNews(
        uiState = GamingNewsUiState.Result(
            items = listOf(
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