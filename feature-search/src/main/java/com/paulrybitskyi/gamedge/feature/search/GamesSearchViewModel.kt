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
import com.paulrybitskyi.gamedge.domain.commons.entities.nextPage
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.usecases.SearchGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GamesSearchViewModel @Inject constructor(
    private val searchGamesUseCase: SearchGamesUseCase,
    private val gamesSearchUiStateFactory: GamesSearchUiStateFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
): BaseViewModel() {


    private var hasMoreDataToLoad = false

    private var searchQuery: String
        set(value) { useCaseParams = useCaseParams.copy(searchQuery = value) }
        get() = useCaseParams.searchQuery

    private var pagination: Pagination
        set(value) { useCaseParams = useCaseParams.copy(pagination = value) }
        get() = useCaseParams.pagination

    private var useCaseParams = SearchGamesUseCase.Params("")

    private var combinedPageResults: GamesUiState.Result? = null

    private val _gamesUiState = MutableStateFlow(createEmptyGamesUiState())

    val gamesUiState: StateFlow<GamesUiState>
        get() = _gamesUiState


    private fun createEmptyGamesUiState(): GamesUiState {
        return gamesSearchUiStateFactory.createWithEmptyState(searchQuery)
    }


    private fun searchGames() = viewModelScope.launch {
        if(searchQuery.isBlank()) {
            flowOf(createEmptyGamesUiState())
        } else {
            searchGamesUseCase.execute(useCaseParams)
                .map(::mapToGamesUiState)
                .flowOn(dispatcherProvider.computation)
                .onError {
                    logger.error(logTag, "Failed to load searched games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(createEmptyGamesUiState())
                }
                .onStart {
                    if(!isPaginationRelatedLoad()) {
                        emit(createEmptyGamesUiState())
                        // Intentional delay since the fragment does not seem
                        // to pick the empty state up
                        delay(10L)
                    }

                    emit(gamesSearchUiStateFactory.createWithLoadingState())
                }
                .map(::combinePageResults)
        }
        .collect {
            configureNextLoad(it)
            _gamesUiState.value = it
        }
    }


    private fun mapToGamesUiState(games: List<Game>): GamesUiState {
        return if(games.isEmpty()) {
            createEmptyGamesUiState()
        } else {
            gamesSearchUiStateFactory.createWithResultState(games)
        }
    }


    private fun isPaginationRelatedLoad(): Boolean {
        return (combinedPageResults != null)
    }


    private fun combinePageResults(uiState: GamesUiState): GamesUiState {
        if((uiState !is GamesUiState.Result) || (combinedPageResults == null)) {
            return uiState
        }

        val oldItems = checkNotNull(combinedPageResults).items
        val newItems = uiState.items

        return GamesUiState.Result(oldItems + newItems)
    }


    private fun configureNextLoad(uiState: GamesUiState) {
        if(uiState !is GamesUiState.Result) return

        combinedPageResults = uiState

        val paginationLimit = useCaseParams.pagination.limit
        val itemCount = uiState.items.size

        hasMoreDataToLoad = ((itemCount % paginationLimit) == 0)
    }


    fun onToolbarBackButtonClicked() {
        route(GamesSearchRoute.Back)
    }


    fun onSearchActionRequested(query: String) {
        if(query.isEmpty() || (searchQuery == query)) return

        searchQuery = query

        resetPagination()
        searchGames()
    }


    private fun resetPagination() {
        pagination = Pagination()
        combinedPageResults = null
    }


    fun onGameClicked(game: GameModel) {
        route(GamesSearchRoute.Info(game.id))
    }


    fun onBottomReached() {
        loadNextPageOfData()
    }


    private fun loadNextPageOfData() {
        if(!hasMoreDataToLoad) return

        pagination = pagination.nextPage()
        searchGames()
    }


}