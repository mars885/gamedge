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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.common.domain.common.entities.nextOffset
import com.paulrybitskyi.gamedge.common.domain.common.extensions.resultOrError
import com.paulrybitskyi.gamedge.common.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.common.ui.base.events.common.GeneralCommand
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModel
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModelMapper
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.common.ui.widgets.games.mapToUiModels
import com.paulrybitskyi.gamedge.common.ui.widgets.games.toSuccessState
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.feature.search.R
import com.paulrybitskyi.gamedge.feature.search.domain.SearchGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.paulrybitskyi.gamedge.core.R as CoreR

private const val KEY_CURRENT_SEARCH_QUERY = "current_search_query"
private const val KEY_CONFIRMED_SEARCH_QUERY = "confirmed_search_query"

@HiltViewModel
internal class GamesSearchViewModel @Inject constructor(
    private val searchGamesUseCase: SearchGamesUseCase,
    private val uiModelMapper: GameUiModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val stringProvider: StringProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private var hasMoreGamesToLoad = false

    private var currentSearchQuery by observeChanges("") { _, newQuery ->
        _uiState.update { it.copy(queryText = newQuery) }
        savedStateHandle.set(KEY_CURRENT_SEARCH_QUERY, newQuery)
    }

    private var confirmedSearchQuery by observeChanges("") { _, newQuery ->
        useCaseParams = useCaseParams.copy(searchQuery = newQuery)
        savedStateHandle.set(KEY_CONFIRMED_SEARCH_QUERY, newQuery)
    }

    private var pagination: Pagination
        set(value) {
            useCaseParams = useCaseParams.copy(pagination = value)
        }
        get() = useCaseParams.pagination

    private var useCaseParams = SearchGamesUseCase.Params(searchQuery = "")

    private var allLoadedGames = emptyList<GameUiModel>()

    private val _uiState = MutableStateFlow(createGamesSearchEmptyUiState())

    private val currentUiState: GamesSearchUiState
        get() = _uiState.value

    val uiState: StateFlow<GamesSearchUiState> = _uiState.asStateFlow()

    init {
        restoreState()
    }

    private fun restoreState() {
        if (savedStateHandle.contains(KEY_CURRENT_SEARCH_QUERY)) {
            currentSearchQuery = checkNotNull(savedStateHandle.get(KEY_CURRENT_SEARCH_QUERY))
        }

        val restoredConfirmedSearchQuery = savedStateHandle.get<String>(KEY_CONFIRMED_SEARCH_QUERY)

        if (restoredConfirmedSearchQuery == currentSearchQuery) {
            onSearchConfirmed(checkNotNull(savedStateHandle.get(KEY_CONFIRMED_SEARCH_QUERY)))
        }
    }

    private fun createGamesSearchEmptyUiState(): GamesSearchUiState {
        return GamesSearchUiState(
            queryText = confirmedSearchQuery,
            gamesUiState = createGamesEmptyUiState(),
        )
    }

    private fun createGamesEmptyUiState(): GamesUiState {
        return GamesUiState(
            isLoading = false,
            infoIconId = CoreR.drawable.magnify,
            infoTitle = getUiStateInfoTitle(),
            games = emptyList(),
        )
    }

    private fun getUiStateInfoTitle(): String {
        return if (confirmedSearchQuery.isBlank()) {
            stringProvider.getString(R.string.games_search_info_title_default)
        } else {
            stringProvider.getString(
                R.string.games_search_info_title_empty,
                confirmedSearchQuery,
            )
        }
    }

    fun onToolbarBackButtonClicked() {
        route(GamesSearchRoute.Back)
    }

    fun onToolbarClearButtonClicked() {
        _uiState.update { it.copy(queryText = "") }
    }

    fun onQueryChanged(newQueryText: String) {
        currentSearchQuery = newQueryText
    }

    fun onSearchConfirmed(query: String) {
        if (query.isEmpty() || (confirmedSearchQuery == query)) return

        confirmedSearchQuery = query

        resetPagination()
        searchGames()
    }

    private fun resetPagination() {
        pagination = Pagination()
        allLoadedGames = emptyList()
    }

    private fun searchGames() = viewModelScope.launch {
        if (confirmedSearchQuery.isBlank()) {
            flowOf(createGamesEmptyUiState())
        } else {
            searchGamesUseCase.execute(useCaseParams)
                .resultOrError()
                .map(uiModelMapper::mapToUiModels)
                .flowOn(dispatcherProvider.computation)
                .map { games ->
                    currentUiState.gamesUiState.toSuccessState(
                        infoTitle = getUiStateInfoTitle(),
                        games = games,
                    )
                }
                .onError {
                    logger.error(logTag, "Failed to search games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(createGamesEmptyUiState())
                }
                .onStart {
                    val games = if (isPerformingNewSearch()) {
                        emptyList()
                    } else {
                        currentUiState.gamesUiState.games
                    }

                    emit(currentUiState.gamesUiState.toLoadingState(games))
                }
                .map(::combineWithAlreadyLoadedGames)
        }
        .collect { emittedUiState ->
            configureNextLoad(emittedUiState)
            updateTotalGamesResult(emittedUiState)
            _uiState.update { it.copy(gamesUiState = emittedUiState) }
        }
    }

    private fun isPerformingNewSearch(): Boolean {
        return allLoadedGames.isEmpty()
    }

    private fun combineWithAlreadyLoadedGames(gamesUiState: GamesUiState): GamesUiState {
        if (!gamesUiState.hasLoadedNewGames() || allLoadedGames.isEmpty()) {
            return gamesUiState
        }

        val oldGames = allLoadedGames
        val newGames = gamesUiState.games

        // The reason for distinctBy is because IGDB API, unfortunately, returns sometimes
        // duplicate entries. This causes Compose to throw the following error:
        // - java.lang.IllegalArgumentException: Key 389 was already used. If you are using
        // - LazyColumn/Row please make sure you provide a unique key for each item.
        // We do indeed provide game's ID as key ID for each composable inside LazyColumn
        // to improve performance in some cases. To fix that crash, we are filtering
        // duplicate entries using .distinctBy extension.
        val totalGames = (oldGames + newGames).distinctBy(GameUiModel::id)

        return gamesUiState.toSuccessState(totalGames)
    }

    private fun GamesUiState.hasLoadedNewGames(): Boolean {
        return (!isLoading && games.isNotEmpty())
    }

    private fun configureNextLoad(gamesUiState: GamesUiState) {
        if (!gamesUiState.hasLoadedNewGames()) return

        val paginationLimit = useCaseParams.pagination.limit
        val gameCount = gamesUiState.games.size

        hasMoreGamesToLoad = ((gameCount % paginationLimit) == 0)
    }

    private fun updateTotalGamesResult(gamesUiState: GamesUiState) {
        if (!gamesUiState.hasLoadedNewGames()) return

        allLoadedGames = gamesUiState.games
    }

    fun onGameClicked(game: GameUiModel) {
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
