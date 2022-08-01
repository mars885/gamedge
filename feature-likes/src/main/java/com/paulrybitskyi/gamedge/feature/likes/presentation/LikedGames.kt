/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.feature.likes.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.paulrybitskyi.gamedge.common.ui.CommandsHandler
import com.paulrybitskyi.gamedge.common.ui.NavBarColorHandler
import com.paulrybitskyi.gamedge.common.ui.RoutesHandler
import com.paulrybitskyi.gamedge.common.ui.base.events.Route
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModel
import com.paulrybitskyi.gamedge.common.ui.widgets.games.Games
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.common.ui.widgets.toolbars.Toolbar
import com.paulrybitskyi.gamedge.feature.likes.R

@Composable
fun LikedGames(
    modifier: Modifier,
    onRoute: (Route) -> Unit,
) {
    LikedGames(
        viewModel = hiltViewModel(),
        modifier = modifier,
        onRoute = onRoute,
    )
}

@Composable
private fun LikedGames(
    viewModel: LikedGamesViewModel,
    modifier: Modifier,
    onRoute: (Route) -> Unit,
) {
    NavBarColorHandler()
    CommandsHandler(viewModel = viewModel)
    RoutesHandler(viewModel = viewModel, onRoute = onRoute)
    LikedGames(
        uiState = viewModel.uiState.collectAsState().value,
        onSearchButtonClicked = viewModel::onSearchButtonClicked,
        onGameClicked = viewModel::onGameClicked,
        onBottomReached = viewModel::onBottomReached,
        modifier = modifier,
    )
}

@Composable
private fun LikedGames(
    uiState: GamesUiState,
    onSearchButtonClicked: () -> Unit,
    onGameClicked: (GameUiModel) -> Unit,
    onBottomReached: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = stringResource(R.string.liked_games_toolbar_title),
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars,
                ),
                rightButtonIcon = painterResource(R.drawable.magnify),
                onRightButtonClick = onSearchButtonClicked,
            )
        },
    ) { paddingValues ->
        Games(
            uiState = uiState,
            modifier = Modifier.padding(paddingValues),
            onGameClicked = onGameClicked,
            onBottomReached = onBottomReached,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LikedGamesPreview() {
    GamedgeTheme {
        LikedGames(
            uiState = GamesUiState(
                isLoading = false,
                infoIconId = R.drawable.gamepad_variant_outline,
                infoTitle = "No Games\nNo Games",
                games = emptyList(),
            ),
            onSearchButtonClicked = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}
