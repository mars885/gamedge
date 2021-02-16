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

import androidx.hilt.Assisted
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
import com.paulrybitskyi.gamedge.domain.commons.entities.nextPage
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
    stringProvider: StringProvider,
    private val categoryUseCases: GamesCategoryUseCases,
    private val gamesCategoryUiStateFactory: GamesCategoryUiStateFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger,
    @Assisted private val savedStateHandle: SavedStateHandle
): BaseViewModel() {


    private var isLoadingData = false
    private var isRefreshingData = false
    private var hasMoreDataToLoad = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()
    private var refreshGamesUseCaseParams = RefreshGamesUseCaseParams()

    private val gamesCategory: GamesCategory
    private val gamesCategoryKeyType: GamesCategoryKey.Type

    private var dataLoadingJob: Job? = null
    private var dataRefreshingJob: Job? = null

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
        loadGames(resultEmissionDelay)
        refreshGames()
    }


    private fun loadGames(resultEmissionDelay: Long = 0L) {
        if(isLoadingData) return

        dataLoadingJob = viewModelScope.launch {
            categoryUseCases.getObservableUseCase(gamesCategoryKeyType)
                .execute(observeGamesUseCaseParams)
                .map(gamesCategoryUiStateFactory::createWithResultState)
                .flowOn(dispatcherProvider.computation)
                .onError {
                    logger.error(logTag, "Failed to load ${gamesCategory.name} games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(gamesCategoryUiStateFactory.createWithEmptyState())
                }
                .onStart {
                    isLoadingData = true
                    emit(gamesCategoryUiStateFactory.createWithLoadingState())
                    delay(resultEmissionDelay)
                }
                .onCompletion { isLoadingData = false }
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

        hasMoreDataToLoad = (paginationLimit == itemCount)
    }


    private fun refreshGames() {
        if(isRefreshingData) return

        dataRefreshingJob = viewModelScope.launch {
            categoryUseCases.getRefreshableUseCase(gamesCategoryKeyType)
                .execute(refreshGamesUseCaseParams)
                .resultOrError()
                .onError {
                    logger.error(logTag, "Failed to refresh ${gamesCategory.name} games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                }
                .onStart { isRefreshingData = true }
                .onCompletion { isRefreshingData = false }
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
        displayNextGamesPage()
    }


    private fun displayNextGamesPage() {
        if(!hasMoreDataToLoad) return

        viewModelScope.launch {
            downloadNextGamesPage()
            loadNextGamesPage()
        }
    }


    private suspend fun downloadNextGamesPage() {
        refreshGamesUseCaseParams = refreshGamesUseCaseParams.copy(
            refreshGamesUseCaseParams.pagination.nextPage()
        )

        dataLoadingJob?.cancelAndJoin()
        _uiState.value = gamesCategoryUiStateFactory.createWithLoadingState()
        refreshGames()
        dataRefreshingJob?.join()
    }


    private fun loadNextGamesPage() {
        observeGamesUseCaseParams = observeGamesUseCaseParams.copy(
            observeGamesUseCaseParams.pagination.nextLimitPage()
        )

        loadGames()
    }


}