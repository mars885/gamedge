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

package com.paulrybitskyi.gamedge.feature.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.domain.commons.entities.nextLimit
import com.paulrybitskyi.gamedge.domain.commons.entities.nextOffset
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.commons.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.feature.category.di.GamesCategoryKey
import com.paulrybitskyi.gamedge.feature.category.widgets.GameCategoryModel
import com.paulrybitskyi.gamedge.feature.category.widgets.GamesCategoryViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val PARAM_GAMES_CATEGORY = "games_category"

@HiltViewModel
internal class GamesCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringProvider: StringProvider,
    private val useCases: GamesCategoryUseCases,
    private val gameCategoryModelMapper: GameCategoryModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {

    private var isObservingGames = false
    private var isRefreshingGames = false
    private var hasMoreGamesToLoad = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()
    private var refreshGamesUseCaseParams = RefreshGamesUseCaseParams()

    private val gamesCategory: GamesCategory
    private val gamesCategoryKeyType: GamesCategoryKey.Type

    private var gamesObservingJob: Job? = null
    private var gamesRefreshingJob: Job? = null

    private val _viewState = MutableStateFlow(createEmptyViewState())

    private val currentViewState: GamesCategoryViewState
        get() = _viewState.value

    val viewState: StateFlow<GamesCategoryViewState>
        get() = _viewState

    init {
        gamesCategory = GamesCategory.valueOf(checkNotNull(savedStateHandle.get<String>(PARAM_GAMES_CATEGORY)))
        gamesCategoryKeyType = gamesCategory.toKeyType()

        _viewState.value = currentViewState.copy(
            title = stringProvider.getString(gamesCategory.titleId)
        )
    }

    private fun createEmptyViewState(): GamesCategoryViewState {
        return GamesCategoryViewState(
            isLoading = false,
            title = "",
            games = emptyList(),
        )
    }

    fun loadData(resultEmissionDelay: Long) {
        observeGames(resultEmissionDelay)
        refreshGames()
    }

    private fun observeGames(resultEmissionDelay: Long = 0L) {
        if (isObservingGames) return

        gamesObservingJob = viewModelScope.launch {
            useCases.getObservableUseCase(gamesCategoryKeyType)
                .execute(observeGamesUseCaseParams)
                .map(gameCategoryModelMapper::mapToGameCategoryModels)
                .flowOn(dispatcherProvider.computation)
                .map { games -> currentViewState.copy(isLoading = false, games = games) }
                .onError {
                    logger.error(logTag, "Failed to observe ${gamesCategory.name} games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(currentViewState.copy(isLoading = false, games = emptyList()))
                }
                .onStart {
                    isObservingGames = true
                    emit(currentViewState.copy(isLoading = true))
                    delay(resultEmissionDelay)
                }
                .onCompletion { isObservingGames = false }
                .collect {
                    configureNextLoad(it)
                    _viewState.value = it
                }
        }
    }

    private fun configureNextLoad(viewState: GamesCategoryViewState) {
        if (!viewState.hasLoadedNewGames()) return

        val paginationLimit = observeGamesUseCaseParams.pagination.limit
        val gameCount = viewState.games.size

        hasMoreGamesToLoad = (paginationLimit == gameCount)
    }

    private fun GamesCategoryViewState.hasLoadedNewGames(): Boolean {
        return (!isLoading && games.isNotEmpty())
    }

    private fun refreshGames() {
        if (isRefreshingGames) return

        gamesRefreshingJob = viewModelScope.launch {
            useCases.getRefreshableUseCase(gamesCategoryKeyType)
                .execute(refreshGamesUseCaseParams)
                .resultOrError()
                .map { currentViewState }
                .onError {
                    logger.error(logTag, "Failed to refresh ${gamesCategory.name} games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                }
                .onStart {
                    isRefreshingGames = true
                    emit(currentViewState.copy(isLoading = true))
                }
                .onCompletion { isRefreshingGames = false }
                .collect { _viewState.value = it }
        }
    }

    fun onToolbarLeftButtonClicked() {
        route(GamesCategoryRoute.Back)
    }

    fun onGameClicked(game: GameCategoryModel) {
        route(GamesCategoryRoute.Info(game.id))
    }

    fun onBottomReached() {
        loadMoreGames()
    }

    private fun loadMoreGames() {
        if (!hasMoreGamesToLoad) return

        viewModelScope.launch {
            fetchNextGamesBatch()
            observeNewGamesBatch()
        }
    }

    private suspend fun fetchNextGamesBatch() {
        refreshGamesUseCaseParams = refreshGamesUseCaseParams.copy(
            refreshGamesUseCaseParams.pagination.nextOffset()
        )

        gamesRefreshingJob?.cancelAndJoin()
        refreshGames()
        gamesRefreshingJob?.join()
    }

    private suspend fun observeNewGamesBatch() {
        observeGamesUseCaseParams = observeGamesUseCaseParams.copy(
            observeGamesUseCaseParams.pagination.nextLimit()
        )

        gamesObservingJob?.cancelAndJoin()
        observeGames()
    }
}
