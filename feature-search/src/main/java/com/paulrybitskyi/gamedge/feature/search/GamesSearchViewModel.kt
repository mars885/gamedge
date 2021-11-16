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
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.domain.commons.entities.Pagination
import com.paulrybitskyi.gamedge.domain.commons.entities.nextOffset
import com.paulrybitskyi.gamedge.domain.games.entities.Game
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
    private val uiStateFactory: GamesSearchUiStateFactory,
    private val dispatcherProvider: DispatcherProvider,
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

    private var totalGamesResult: GamesUiState.Result? = null

    private val _uiState = MutableStateFlow(createEmptyGamesUiState())

    val uiState: StateFlow<GamesUiState>
        get() = _uiState

    init {
        onSearchActionRequested(savedStateHandle.get(KEY_SEARCH_QUERY) ?: "")
    }

    private fun createEmptyGamesUiState(): GamesUiState {
        return uiStateFactory.createWithEmptyState(searchQuery)
    }

    fun onToolbarBackButtonClicked() {
        route(GamesSearchRoute.Back)
    }

    fun onSearchActionRequested(query: String) {
        if (query.isEmpty() || (searchQuery == query)) return

        searchQuery = query

        resetPagination()
        searchGames()
    }

    private fun resetPagination() {
        pagination = Pagination()
        totalGamesResult = null
    }

    private fun searchGames() = viewModelScope.launch {
        if (searchQuery.isBlank()) {
            flowOf(createEmptyGamesUiState())
        } else {
            searchGamesUseCase.execute(useCaseParams)
                .map(::mapToUiState)
                .flowOn(dispatcherProvider.computation)
                .onError {
                    logger.error(logTag, "Failed to search games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(createEmptyGamesUiState())
                }
                .onStart {
                    if (isPerformingNewSearch()) {
                        dispatchCommand(GamesSearchCommand.ClearItems)
                    }

                    emit(uiStateFactory.createWithLoadingState())
                }
                .map(::aggregateResults)
        }
        .collect {
            configureNextLoad(it)
            updateTotalGamesResult(it)
            _uiState.value = it
        }
    }

    private fun mapToUiState(games: List<Game>): GamesUiState {
        return if (games.isEmpty()) {
            createEmptyGamesUiState()
        } else {
            uiStateFactory.createWithResultState(games)
        }
    }

    private fun isPerformingNewSearch(): Boolean {
        return (totalGamesResult == null)
    }

    private fun aggregateResults(uiState: GamesUiState): GamesUiState {
        if ((uiState !is GamesUiState.Result) || (totalGamesResult == null)) {
            return uiState
        }

        val oldItems = checkNotNull(totalGamesResult).items
        val newItems = uiState.items

        return GamesUiState.Result(oldItems + newItems)
    }

    private fun configureNextLoad(uiState: GamesUiState) {
        if (uiState !is GamesUiState.Result) return

        val paginationLimit = useCaseParams.pagination.limit
        val itemCount = uiState.items.size

        hasMoreGamesToLoad = ((itemCount % paginationLimit) == 0)
    }

    private fun updateTotalGamesResult(uiState: GamesUiState) {
        if (uiState !is GamesUiState.Result) return

        totalGamesResult = uiState
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
