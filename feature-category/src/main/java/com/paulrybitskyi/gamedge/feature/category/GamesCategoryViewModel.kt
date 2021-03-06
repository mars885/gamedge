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
import com.paulrybitskyi.gamedge.domain.commons.entities.nextLimitPage
import com.paulrybitskyi.gamedge.domain.commons.entities.nextOffsetPage
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.commons.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.feature.category.di.GamesCategoryKey
import com.paulrybitskyi.gamedge.feature.category.mapping.GamesCategoryUiStateFactory
import com.paulrybitskyi.gamedge.feature.category.widgets.GameCategoryModel
import com.paulrybitskyi.gamedge.feature.category.widgets.GamesCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val PARAM_GAMES_CATEGORY = "games_category"


@HiltViewModel
internal class GamesCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringProvider: StringProvider,
    private val useCases: GamesCategoryUseCases,
    private val uiStateFactory: GamesCategoryUiStateFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
): BaseViewModel() {


    private var isObservingGames = false
    private var isRefreshingGames = false
    private var hasMoreGamesToLoad = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()
    private var refreshGamesUseCaseParams = RefreshGamesUseCaseParams()

    private val gamesCategory: GamesCategory
    private val gamesCategoryKeyType: GamesCategoryKey.Type

    private var gamesObservingJob: Job? = null
    private var gamesRefreshingJob: Job? = null

    private val _toolbarTitle = MutableStateFlow("")
    private val _uiState = MutableStateFlow<GamesCategoryUiState>(GamesCategoryUiState.Empty)

    val toolbarTitle: StateFlow<String>
        get() = _toolbarTitle

    val uiState: StateFlow<GamesCategoryUiState>
        get() = _uiState


    init {
        gamesCategory = GamesCategory.valueOf(checkNotNull(savedStateHandle.get<String>(PARAM_GAMES_CATEGORY)))
        gamesCategoryKeyType = gamesCategory.toKeyType()

        _toolbarTitle.value = stringProvider.getString(gamesCategory.titleId)
    }


    fun loadData(resultEmissionDelay: Long) {
        observeGames(resultEmissionDelay)
        refreshGames()
    }


    private fun observeGames(resultEmissionDelay: Long = 0L) {
        if(isObservingGames) return

        gamesObservingJob = viewModelScope.launch {
            useCases.getObservableUseCase(gamesCategoryKeyType)
                .execute(observeGamesUseCaseParams)
                .map(uiStateFactory::createWithResultState)
                .flowOn(dispatcherProvider.computation)
                .onError {
                    logger.error(logTag, "Failed to observe ${gamesCategory.name} games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(uiStateFactory.createWithEmptyState())
                }
                .onStart {
                    isObservingGames = true
                    emit(uiStateFactory.createWithLoadingState())
                    delay(resultEmissionDelay)
                }
                .onCompletion { isObservingGames = false }
                .collect {
                    configureNextLoad(it)
                    _uiState.value = it
                }
        }
    }


    private fun configureNextLoad(uiState: GamesCategoryUiState) {
        if(uiState !is GamesCategoryUiState.Result) return

        val paginationLimit = observeGamesUseCaseParams.pagination.limit
        val itemCount = uiState.items.size

        hasMoreGamesToLoad = (paginationLimit == itemCount)
    }


    private fun refreshGames() {
        if(isRefreshingGames) return

        gamesRefreshingJob = viewModelScope.launch {
            useCases.getRefreshableUseCase(gamesCategoryKeyType)
                .execute(refreshGamesUseCaseParams)
                .resultOrError()
                .onError {
                    logger.error(logTag, "Failed to refresh ${gamesCategory.name} games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                }
                .onStart {
                    isRefreshingGames = true
                    _uiState.value = uiStateFactory.createWithLoadingState()
                }
                .onCompletion { isRefreshingGames = false }
                .collect()
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
        if(!hasMoreGamesToLoad) return

        viewModelScope.launch {
            fetchNextGamesBatch()
            observeNewGamesBatch()
        }
    }


    private suspend fun fetchNextGamesBatch() {
        refreshGamesUseCaseParams = refreshGamesUseCaseParams.copy(
            refreshGamesUseCaseParams.pagination.nextOffsetPage()
        )

        gamesRefreshingJob?.cancelAndJoin()
        refreshGames()
        gamesRefreshingJob?.join()
    }


    private suspend fun observeNewGamesBatch() {
        observeGamesUseCaseParams = observeGamesUseCaseParams.copy(
            observeGamesUseCaseParams.pagination.nextLimitPage()
        )

        gamesObservingJob?.cancelAndJoin()
        observeGames()
    }


}