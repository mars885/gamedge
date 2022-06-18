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
import com.paulrybitskyi.gamedge.commons.ui.di.qualifiers.TransitionAnimationDuration
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
import com.paulrybitskyi.gamedge.feature.category.widgets.GameCategoryModelUiMapper
import com.paulrybitskyi.gamedge.feature.category.widgets.GameCategoryUiModel
import com.paulrybitskyi.gamedge.feature.category.widgets.GamesCategoryUiState
import com.paulrybitskyi.gamedge.feature.category.widgets.mapToUiModels
import com.paulrybitskyi.gamedge.feature.category.widgets.toEmptyState
import com.paulrybitskyi.gamedge.feature.category.widgets.toLoadingState
import com.paulrybitskyi.gamedge.feature.category.widgets.toSuccessState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PARAM_GAMES_CATEGORY = "category"

@HiltViewModel
internal class GamesCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringProvider: StringProvider,
    @TransitionAnimationDuration
    transitionAnimationDuration: Long,
    private val useCases: GamesCategoryUseCases,
    private val gameCategoryModelUiMapper: GameCategoryModelUiMapper,
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

    private val _uiState = MutableStateFlow(createEmptyUiState())

    private val currentUiState: GamesCategoryUiState
        get() = _uiState.value

    val uiState: StateFlow<GamesCategoryUiState>
        get() = _uiState

    init {
        gamesCategory = GamesCategory.valueOf(checkNotNull(savedStateHandle.get<String>(PARAM_GAMES_CATEGORY)))
        gamesCategoryKeyType = gamesCategory.toKeyType()

        _uiState.update {
            it.copy(title = stringProvider.getString(gamesCategory.titleId))
        }

        observeGames(resultEmissionDelay = transitionAnimationDuration)
        refreshGames()
    }

    private fun createEmptyUiState(): GamesCategoryUiState {
        return GamesCategoryUiState(
            isLoading = false,
            title = "",
            games = emptyList(),
        )
    }

    private fun observeGames(resultEmissionDelay: Long = 0L) {
        if (isObservingGames) return

        gamesObservingJob = viewModelScope.launch {
            useCases.getObservableUseCase(gamesCategoryKeyType)
                .execute(observeGamesUseCaseParams)
                .map(gameCategoryModelUiMapper::mapToUiModels)
                .flowOn(dispatcherProvider.computation)
                .map { games -> currentUiState.toSuccessState(games) }
                .onError {
                    logger.error(logTag, "Failed to observe ${gamesCategory.name} games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(currentUiState.toEmptyState())
                }
                .onStart {
                    isObservingGames = true
                    emit(currentUiState.toLoadingState())
                    delay(resultEmissionDelay)
                }
                .onCompletion { isObservingGames = false }
                .collect { emittedUiState ->
                    configureNextLoad(emittedUiState)
                    _uiState.update { emittedUiState }
                }
        }
    }

    private fun configureNextLoad(uiState: GamesCategoryUiState) {
        if (!uiState.hasLoadedNewGames()) return

        val paginationLimit = observeGamesUseCaseParams.pagination.limit
        val gameCount = uiState.games.size

        hasMoreGamesToLoad = (paginationLimit == gameCount)
    }

    private fun GamesCategoryUiState.hasLoadedNewGames(): Boolean {
        return (!isLoading && games.isNotEmpty())
    }

    private fun refreshGames() {
        if (isRefreshingGames) return

        gamesRefreshingJob = viewModelScope.launch {
            useCases.getRefreshableUseCase(gamesCategoryKeyType)
                .execute(refreshGamesUseCaseParams)
                .resultOrError()
                .map { currentUiState }
                .onError {
                    logger.error(logTag, "Failed to refresh ${gamesCategory.name} games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                }
                .onStart {
                    isRefreshingGames = true
                    emit(currentUiState.toLoadingState())
                }
                .onCompletion { isRefreshingGames = false }
                .collect { emittedUiState -> _uiState.update { emittedUiState } }
        }
    }

    fun onToolbarLeftButtonClicked() {
        route(GamesCategoryRoute.Back)
    }

    fun onGameClicked(game: GameCategoryUiModel) {
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
