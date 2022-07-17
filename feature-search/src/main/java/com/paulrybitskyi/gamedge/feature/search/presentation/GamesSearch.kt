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

package com.paulrybitskyi.gamedge.feature.search.presentation

import android.content.res.Configuration
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.paulrybitskyi.gamedge.common.ui.CommandsHandler
import com.paulrybitskyi.gamedge.common.ui.NavBarColorHandler
import com.paulrybitskyi.gamedge.common.ui.RoutesHandler
import com.paulrybitskyi.gamedge.common.ui.OnLifecycleEvent
import com.paulrybitskyi.gamedge.common.ui.base.events.Route
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.common.ui.widgets.toolbars.SearchToolbar
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModel
import com.paulrybitskyi.gamedge.common.ui.widgets.games.Games
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.common.ui.widgets.games.finiteUiState
import com.paulrybitskyi.gamedge.feature.search.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val KeyboardPopupIntentionalDelay = 300L

@Composable
fun GamesSearch(onRoute: (Route) -> Unit) {
    GamesSearch(
        viewModel = hiltViewModel(),
        onRoute = onRoute,
    )
}

@Composable
private fun GamesSearch(
    viewModel: GamesSearchViewModel,
    onRoute: (Route) -> Unit,
) {
    NavBarColorHandler()
    CommandsHandler(viewModel = viewModel)
    RoutesHandler(viewModel = viewModel, onRoute = onRoute)
    GamesSearch(
        uiState = viewModel.uiState.collectAsState().value,
        onSearchConfirmed = viewModel::onSearchConfirmed,
        onBackButtonClicked = viewModel::onToolbarBackButtonClicked,
        onClearButtonClicked = viewModel::onToolbarClearButtonClicked,
        onQueryChanged = viewModel::onQueryChanged,
        onGameClicked = viewModel::onGameClicked,
        onBottomReached = viewModel::onBottomReached,
    )
}

@Composable
private fun GamesSearch(
    uiState: GamesSearchUiState,
    onSearchConfirmed: (query: String) -> Unit,
    onBackButtonClicked: () -> Unit,
    onClearButtonClicked: () -> Unit,
    onQueryChanged: (query: String) -> Unit,
    onGameClicked: (GameUiModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            SearchToolbar(
                queryText = uiState.queryText,
                placeholderText = stringResource(R.string.games_search_toolbar_hint),
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars,
                ),
                focusRequester = focusRequester,
                onQueryChanged = onQueryChanged,
                onSearchConfirmed = { query ->
                    focusManager.clearFocus(force = true)
                    onSearchConfirmed(query)
                },
                onBackButtonClicked = onBackButtonClicked,
                onClearButtonClicked = onClearButtonClicked,
            )
        },
    ) {
        Games(
            uiState = uiState.gamesUiState,
            onGameClicked = onGameClicked,
            onBottomReached = onBottomReached,
        )

        OnLifecycleEvent(
            onResume = {
                if (uiState.gamesUiState.finiteUiState == FiniteUiState.Empty) {
                    // On subsequent openings of this screen from the background,
                    // simply calling focusRequester.requestFocus() does not make
                    // the keyboard visible. The workaround is to add small delay
                    // and call keyboardController.show() as well.
                    coroutineScope.launch {
                        delay(KeyboardPopupIntentionalDelay)
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                }
            },
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesSearchSuccessStatePreview() {
    GamedgeTheme {
        GamesSearch(
            uiState = GamesSearchUiState(
                queryText = "God of War",
                gamesUiState = GamesUiState(
                    isLoading = false,
                    infoIconId = R.drawable.magnify,
                    infoTitle = "",
                    games = listOf(
                        GameUiModel(
                            id = 1,
                            coverImageUrl = null,
                            name = "God of War",
                            releaseDate = "Apr 20, 2018 (3 years ago)",
                            developerName = "SIE Santa Monica Studio",
                            description = "Very very very very very very very very very " +
                                    "very very very very very very very very long description",
                        ),
                        GameUiModel(
                            id = 2,
                            coverImageUrl = null,
                            name = "God of War II",
                            releaseDate = "Mar 13, 2007 (14 years ago)",
                            developerName = "SIE Santa Monica Studio",
                            description = "Very very very very very very very very very " +
                                    "very very very very very very very very long description",
                        ),
                        GameUiModel(
                            id = 3,
                            coverImageUrl = null,
                            name = "God of War II HD",
                            releaseDate = "Oct 02, 2010 (11 years ago)",
                            developerName = "Bluepoint Games",
                            description = "Very very very very very very very very very " +
                                    "very very very very very very very very long description",
                        ),
                    ),
                )
            ),
            onSearchConfirmed = {},
            onBackButtonClicked = {},
            onClearButtonClicked = {},
            onQueryChanged = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesSearchEmptyStatePreview() {
    GamedgeTheme {
        GamesSearch(
            uiState = GamesSearchUiState(
                queryText = "God of War",
                gamesUiState = GamesUiState(
                    isLoading = false,
                    infoIconId = R.drawable.magnify,
                    infoTitle = "No games found for \n\"God of War\"",
                    games = emptyList(),
                )
            ),
            onSearchConfirmed = {},
            onBackButtonClicked = {},
            onClearButtonClicked = {},
            onQueryChanged = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GamesSearchLoadingStatePreview() {
    GamedgeTheme {
        GamesSearch(
            uiState = GamesSearchUiState(
                queryText = "God of War",
                gamesUiState = GamesUiState(
                    isLoading = true,
                    infoIconId = R.drawable.magnify,
                    infoTitle = "",
                    games = emptyList(),
                )
            ),
            onSearchConfirmed = {},
            onBackButtonClicked = {},
            onClearButtonClicked = {},
            onQueryChanged = {},
            onGameClicked = {},
            onBottomReached = {},
        )
    }
}
