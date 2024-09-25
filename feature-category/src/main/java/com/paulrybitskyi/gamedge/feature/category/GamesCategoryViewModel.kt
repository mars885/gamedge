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

import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.entities.nextLimit
import com.paulrybitskyi.gamedge.common.domain.common.entities.nextOffset
import com.paulrybitskyi.gamedge.common.domain.common.extensions.resultOrError
import com.paulrybitskyi.gamedge.common.domain.games.common.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.common.domain.games.common.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.common.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.common.ui.base.events.common.GeneralCommand
import com.paulrybitskyi.gamedge.common.ui.di.qualifiers.TransitionAnimationDuration
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.feature.category.widgets.GameCategoryUiModelMapper
import com.paulrybitskyi.gamedge.feature.category.widgets.mapToUiModels
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GamesCategoryViewModel.Factory::class)
internal class GamesCategoryViewModel @AssistedInject constructor(
    stringProvider: StringProvider,
    @TransitionAnimationDuration
    transitionAnimationDuration: Long,
    @Assisted
    private val destination: GamesCategoryDestination,
    private val useCases: GamesCategoryUseCases,
    private val uiModelMapper: GameCategoryUiModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger,
) : BaseViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(destination: GamesCategoryDestination): GamesCategoryViewModel
    }

    private var isObservingGames = false
    private var isRefreshingGames = false
    private var hasMoreGamesToLoad = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()
    private var refreshGamesUseCaseParams = RefreshGamesUseCaseParams()

    private val gamesCategory = GamesCategory.valueOf(destination.category)
    private val gamesCategoryKeyType = gamesCategory.toKeyType()

    private var gamesObservingJob: Job? = null
    private var gamesRefreshingJob: Job? = null

    private val _uiState = MutableStateFlow(createEmptyUiState())

    private val currentUiState: GamesCategoryUiState
        get() = _uiState.value

    val uiState: StateFlow<GamesCategoryUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(title = stringProvider.getString(gamesCategory.titleId))
        }

        observeGames(resultEmissionDelay = transitionAnimationDuration)
        refreshGames(resultEmissionDelay = transitionAnimationDuration)
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

        gamesObservingJob = useCases.getObservableUseCase(gamesCategoryKeyType)
            .execute(observeGamesUseCaseParams)
            .map(uiModelMapper::mapToUiModels)
            .flowOn(dispatcherProvider.computation)
            .map { games -> currentUiState.toSuccessState(games) }
            .onError {
                logger.error(logTag, "Failed to observe ${gamesCategory.name} games.", it)
                dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                emit(currentUiState.toEmptyState())
            }
            .onStart {
                isObservingGames = true
                delay(resultEmissionDelay)
            }
            .onCompletion { isObservingGames = false }
            .onEach { emittedUiState ->
                configureNextLoad(emittedUiState)
                _uiState.update { emittedUiState }
            }
            .launchIn(viewModelScope)
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

    private fun refreshGames(resultEmissionDelay: Long = 0L) {
        if (isRefreshingGames) return

        gamesRefreshingJob = useCases.getRefreshableUseCase(gamesCategoryKeyType)
            .execute(refreshGamesUseCaseParams)
            .resultOrError()
            .map { currentUiState }
            .onError {
                logger.error(logTag, "Failed to refresh ${gamesCategory.name} games.", it)
                dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
            }
            .onStart {
                isRefreshingGames = true
                emit(currentUiState.enableLoading())
                // Show loading state for some time since it can be too quick
                delay(resultEmissionDelay)
            }
            .onCompletion {
                isRefreshingGames = false
                // Delay disabling loading to avoid quick state changes like
                // empty, loading, empty, success
                delay(resultEmissionDelay)
                emit(currentUiState.disableLoading())
            }
            .onEach { emittedUiState -> _uiState.update { emittedUiState } }
            .launchIn(viewModelScope)
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
            refreshGamesUseCaseParams.pagination.nextOffset(),
        )

        gamesRefreshingJob?.cancelAndJoin()
        refreshGames()
        gamesRefreshingJob?.join()
    }

    private suspend fun observeNewGamesBatch() {
        observeGamesUseCaseParams = observeGamesUseCaseParams.copy(
            observeGamesUseCaseParams.pagination.nextLimit(),
        )

        gamesObservingJob?.cancelAndJoin()
        observeGames()
    }
}
