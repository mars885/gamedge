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

package com.paulrybitskyi.gamedge.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.statusBarsPadding
import com.paulrybitskyi.gamedge.commons.ui.OnLifecycleEvent
import com.paulrybitskyi.gamedge.commons.ui.widgets.SearchToolbar
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.Games
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GamesUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val KEYBOARD_POPUP_INTENTIONAL_DELAY = 300L

@Composable
internal fun GamesSearch(
    uiState: GamesSearchUiState,
    onSearchActionRequested: (String) -> Unit,
    onBackButtonClicked: () -> Unit,
    onClearButtonClicked: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onGameClicked: (GameModel) -> Unit,
    onBottomReached: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize()) {
        SearchToolbar(
            queryText = uiState.queryText,
            placeholderText = stringResource(R.string.games_search_toolbar_hint),
            modifier = Modifier.statusBarsPadding(),
            focusRequester = focusRequester,
            onQueryChanged = onQueryChanged,
            onSearchActionRequested = { query ->
                focusManager.clearFocus(force = true)
                onSearchActionRequested(query)
            },
            onBackButtonClicked = onBackButtonClicked,
            onClearButtonClicked = onClearButtonClicked,
        )

        Games(
            uiState = uiState.gamesUiState,
            onGameClicked = onGameClicked,
            onBottomReached = onBottomReached,
        )

        OnLifecycleEvent(
            onResume = {
                if (uiState.queryText.isEmpty()) {
                    // On subsequent openings of this screen from the background, simply calling
                    // focusRequester.requestFocus() does not make the keyboard visible. The
                    // workaround is to add small delay and call keyboardController.show() as well.
                    coroutineScope.launch {
                        delay(KEYBOARD_POPUP_INTENTIONAL_DELAY)
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                }
            },
        )
    }
}

@Preview
@Composable
internal fun GamesSearchSuccessStatePreview() {
    GamesSearch(
        uiState = GamesSearchUiState(
            queryText = "God of War",
            gamesUiState = GamesUiState(
                isLoading = false,
                infoIconId = R.drawable.magnify,
                infoTitle = "",
                games = listOf(
                    GameModel(
                        id = 1,
                        coverImageUrl = null,
                        name = "God of War",
                        releaseDate = "Apr 20, 2018 (3 years ago)",
                        developerName = "SIE Santa Monica Studio",
                        description = "Very very very very very very very very very " +
                            "very very very very very very very very long description",
                    ),
                    GameModel(
                        id = 2,
                        coverImageUrl = null,
                        name = "God of War II",
                        releaseDate = "Mar 13, 2007 (14 years ago)",
                        developerName = "SIE Santa Monica Studio",
                        description = "Very very very very very very very very very " +
                            "very very very very very very very very long description",
                    ),
                    GameModel(
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
        onSearchActionRequested = {},
        onBackButtonClicked = {},
        onClearButtonClicked = {},
        onQueryChanged = {},
        onGameClicked = {},
        onBottomReached = {},
    )
}

@Preview
@Composable
internal fun GamesSearchEmptyStatePreview() {
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
        onSearchActionRequested = {},
        onBackButtonClicked = {},
        onClearButtonClicked = {},
        onQueryChanged = {},
        onGameClicked = {},
        onBottomReached = {},
    )
}

@Preview
@Composable
internal fun GamesSearchLoadingStatePreview() {
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
        onSearchActionRequested = {},
        onBackButtonClicked = {},
        onClearButtonClicked = {},
        onQueryChanged = {},
        onGameClicked = {},
        onBottomReached = {},
    )
}
