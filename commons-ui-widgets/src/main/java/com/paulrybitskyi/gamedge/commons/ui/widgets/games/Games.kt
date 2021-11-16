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

package com.paulrybitskyi.gamedge.commons.ui.widgets.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.R

@Composable
fun Games(
    viewState: GamesViewState,
    onGameClicked: (GameModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorContentContainer))
    ) {
        if (viewState.isInLoadingState) {
            GamesLoadingState(Modifier.align(Alignment.Center))
        } else {
            when {
                viewState.isInEmptyState -> GamesEmptyState(
                    viewState = viewState,
                    modifier = Modifier.align(Alignment.Center)
                )
                viewState.isInSuccessState -> GamesSuccessState(
                    viewState = viewState,
                    modifier = Modifier.matchParentSize(),
                    onGameClicked = onGameClicked,
                    onBottomReached = onBottomReached,
                )
            }
        }
    }
}

@Composable
private fun GamesLoadingState(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = colorResource(R.color.colorProgressBar)
    )
}

@Composable
private fun GamesEmptyState(
    viewState: GamesViewState,
    modifier: Modifier,
) {
    Info(
        icon = painterResource(viewState.infoIconId),
        title = viewState.infoTitle,
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.games_info_view_horizontal_margin)
        ),
        iconColor = colorResource(R.color.colorInfoView),
        titleTextColor = colorResource(R.color.colorInfoView)
    )
}

@Composable
private fun GamesSuccessState(
    viewState: GamesViewState,
    modifier: Modifier,
    onGameClicked: (GameModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    RefreshableContent(
        isRefreshing = viewState.isInRefreshingState,
        modifier = modifier,
    ) {
        val games = viewState.games
        val lastIndex = games.lastIndex

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.games_decorator_spacing))
        ) {
            itemsIndexed(
                items = games,
                key = { _, game -> game.id }
            ) { index, game ->
                // todo double-check in the future
                if (index == lastIndex) {
                    LaunchedEffect(lastIndex) {
                        onBottomReached()
                    }
                }

                Game(
                    coverImageUrl = game.coverImageUrl,
                    name = game.name,
                    releaseDate = game.releaseDate,
                    developerName = game.developerName,
                    description = game.description,
                    onClick = { onGameClicked(game) },
                )
            }
        }
    }
}

@Composable
private fun RefreshableContent(
    isRefreshing: Boolean,
    modifier: Modifier,
    onRefreshRequested: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefreshRequested ?: {},
        modifier = modifier,
        swipeEnabled = false,
        indicator = { state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                contentColor = colorResource(R.color.games_swipe_refresh_color)
            )
        },
        content = content,
    )
}

@Preview
@Composable
internal fun GamesEmptyStatePreview() {
    Games(
        viewState = GamesViewState(
            isLoading = false,
            infoIconId = 0,
            infoTitle = "No Liked Games",
            games = emptyList(),
        ),
        onGameClicked = {},
        onBottomReached = {},
    )
}

@Preview
@Composable
internal fun GamesLoadingStatePreview() {
    Games(
        viewState = GamesViewState(
            isLoading = true,
            infoIconId = 0,
            infoTitle = "",
            games = emptyList(),
        ),
        onGameClicked = {},
        onBottomReached = {},
    )
}

@Preview
@Composable
internal fun GamesSuccessStatePreview() {
    val games = listOf(
        GameModel(
            id = 1,
            coverImageUrl = null,
            name = "Ghost of Tsushima: Director's Cut",
            releaseDate = "Aug 20, 2021 (2 months ago)",
            developerName = "Sucker Punch Productions",
            description = "Some very very very very very very very very very long description",
        ),
        GameModel(
            id = 2,
            coverImageUrl = null,
            name = "Forza Horizon 5",
            releaseDate = "Nov 09, 2021 (8 days ago)",
            developerName = "Playground Games",
            description = "Some very very very very very very very very very long description",
        ),
        GameModel(
            id = 3,
            coverImageUrl = null,
            name = "Outer Wilds: Echoes of the Eye",
            releaseDate = "Sep 28, 2021 (a month ago)",
            developerName = "Mobius Digital",
            description = "Some very very very very very very very very very long description",
        )
    )

    Games(
        viewState = GamesViewState(
            isLoading = false,
            infoIconId = 0,
            infoTitle = "",
            games = games,
        ),
        onGameClicked = {},
        onBottomReached = {},
    )
}
