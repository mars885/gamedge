/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModelMapper
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GamesViewState
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.mapToGameModels
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.domain.commons.entities.Pagination
import com.paulrybitskyi.gamedge.domain.commons.entities.nextOffset
import com.paulrybitskyi.gamedge.domain.games.usecases.SearchGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val KEY_SEARCH_QUERY = "search_query"

@HiltViewModel
internal class GamesSearchViewModel @Inject constructor(
    private val searchGamesUseCase: SearchGamesUseCase,
    private val gameModelMapper: GameModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val stringProvider: StringProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private var hasMoreGamesToLoad = false

    private var searchQuery: String
        set(value) {
            useCaseParams = useCaseParams.copy(searchQuery = value)
            savedStateHandle.set(KEY_SEARCH_QUERY, value)
        }
        get() = useCaseParams.searchQuery

    private var pagination: Pagination
        set(value) { useCaseParams = useCaseParams.copy(pagination = value) }
        get() = useCaseParams.pagination

    private var useCaseParams = SearchGamesUseCase.Params(searchQuery = "")

    private var allLoadedGames = emptyList<GameModel>()

    private val _uiState = MutableStateFlow(createGamesSearchEmptyUiState())

    private val currentUiState: GamesSearchViewState
        get() = _uiState.value

    val uiState: StateFlow<GamesSearchViewState>
        get() = _uiState

    init {
        onSearchActionRequested(savedStateHandle.get(KEY_SEARCH_QUERY) ?: "")
    }

    private fun createGamesSearchEmptyUiState(): GamesSearchViewState {
        return GamesSearchViewState(
            queryText = searchQuery,
            gamesViewState = createGamesEmptyUiState(),
        )
    }

    private fun createGamesEmptyUiState(): GamesViewState {
        val title = if (searchQuery.isBlank()) {
            stringProvider.getString(R.string.games_search_fragment_info_title_default)
        } else {
            stringProvider.getString(
                R.string.games_search_fragment_info_title_empty,
                searchQuery
            )
        }

        return GamesViewState(
            isLoading = false,
            infoIconId = R.drawable.magnify,
            infoTitle = title,
            games = emptyList(),
        )
    }

    fun onToolbarBackButtonClicked() {
        route(GamesSearchRoute.Back)
    }

    fun onToolbarClearButtonClicked() {
        _uiState.value = currentUiState.copy(queryText = "")
    }

    fun onQueryChanged(newQueryText: String) {
        _uiState.value = currentUiState.copy(queryText = newQueryText)
    }

    fun onSearchActionRequested(query: String) {
        if (query.isEmpty() || (searchQuery == query)) return

        searchQuery = query

        resetPagination()
        searchGames()
    }

    private fun resetPagination() {
        pagination = Pagination()
        allLoadedGames = emptyList()
    }

    private fun searchGames() = viewModelScope.launch {
        if (searchQuery.isBlank()) {
            flowOf(createGamesEmptyUiState())
        } else {
            searchGamesUseCase.execute(useCaseParams)
                .map(gameModelMapper::mapToGameModels)
                .flowOn(dispatcherProvider.computation)
                .map { games -> currentUiState.gamesViewState.copy(isLoading = false, games = games) }
                .onError {
                    logger.error(logTag, "Failed to search games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(createGamesEmptyUiState())
                }
                .onStart {
                    val games = if (isPerformingNewSearch()) {
                        emptyList()
                    } else {
                        currentUiState.gamesViewState.games
                    }

                    emit(currentUiState.gamesViewState.copy(isLoading = true, games = games))
                }
                .map(::combineWithAlreadyLoadedGames)
        }
        .collect {
            configureNextLoad(it)
            updateTotalGamesResult(it)
            _uiState.value = currentUiState.copy(gamesViewState = it)
        }
    }

    private fun isPerformingNewSearch(): Boolean {
        return allLoadedGames.isEmpty()
    }

    private fun combineWithAlreadyLoadedGames(gamesViewState: GamesViewState): GamesViewState {
        if (!gamesViewState.hasLoadedNewGames() || allLoadedGames.isEmpty()) {
            return gamesViewState
        }

        val oldGames = allLoadedGames
        val newGames = gamesViewState.games

        // The reason for distinctBy is because IGDB API, unfortunately, returns sometimes
        // duplicate entries. This causes Compose to throw the following error:
        // - java.lang.IllegalArgumentException: Key 389 was already used. If you are using
        // - LazyColumn/Row please make sure you provide a unique key for each item.
        // We do indeed provide game's ID as key ID for each composable inside LazyColumn
        // to improve performance in some cases. To fix that crash, we are filtering
        // duplicate entries using .distinctBy extension.
        val totalGames = (oldGames + newGames).distinctBy(GameModel::id)

        return gamesViewState.copy(games = totalGames)
    }

    private fun GamesViewState.hasLoadedNewGames(): Boolean {
        return (!isLoading && games.isNotEmpty())
    }

    private fun configureNextLoad(gamesViewState: GamesViewState) {
        if (!gamesViewState.hasLoadedNewGames()) return

        val paginationLimit = useCaseParams.pagination.limit
        val gameCount = gamesViewState.games.size

        hasMoreGamesToLoad = ((gameCount % paginationLimit) == 0)
    }

    private fun updateTotalGamesResult(gamesViewState: GamesViewState) {
        if (!gamesViewState.hasLoadedNewGames()) return

        allLoadedGames = gamesViewState.games
    }

    fun onGameClicked(game: GameModel) {
        route(GamesSearchRoute.Info(game.id))
    }

    fun onBottomReached() {
        loadMoreGames()
    }

    private fun loadMoreGames() {
        if (!hasMoreGamesToLoad) return

        pagination = pagination.nextOffset()
        searchGames()
    }
}
