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

package com.paulrybitskyi.gamedge.feature.likes

import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.domain.commons.entities.DEFAULT_PAGE_SIZE
import com.paulrybitskyi.gamedge.domain.commons.entities.nextLimitPage
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveLikedGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val PAGINATION_LOAD_DELAY = 500L


@HiltViewModel
class LikedGamesViewModel @Inject constructor(
    private val observeLikedGamesUseCase: ObserveLikedGamesUseCase,
    private val uiStateFactory: LikedGamesUiStateFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {


    private var isLoadingData = false
    private var hasMoreDataToLoad = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()

    private var dataLoadingJob: Job? = null

    private val _uiState = MutableStateFlow(uiStateFactory.createWithEmptyState())

    val uiState: StateFlow<GamesUiState>
        get() = _uiState


    fun loadData() {
        if(isLoadingData) return

        dataLoadingJob = viewModelScope.launch {
            observeLikedGamesUseCase.execute(observeGamesUseCaseParams)
                .map(uiStateFactory::createWithResultState)
                .flowOn(dispatcherProvider.computation)
                .onError {
                    logger.error(logTag, "Failed to load liked games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(uiStateFactory.createWithEmptyState())
                }
                .onStart {
                    isLoadingData = true
                    emit(uiStateFactory.createWithLoadingState())

                    // Delaying to give a sense of "loading" since it's really fast without it
                    if(isPaginationRelatedLoad()) delay(PAGINATION_LOAD_DELAY)
                }
                .onCompletion { isLoadingData = false }
                .collect {
                    configureNextLoad(it)
                    _uiState.value = it
                }
        }
    }


    private fun isPaginationRelatedLoad(): Boolean {
        return (observeGamesUseCaseParams.pagination.limit != DEFAULT_PAGE_SIZE)
    }


    private fun configureNextLoad(uiState: GamesUiState) {
        if(uiState !is GamesUiState.Result) return

        val paginationLimit = observeGamesUseCaseParams.pagination.limit
        val itemCount = uiState.items.size

        hasMoreDataToLoad = (paginationLimit == itemCount)
    }


    fun onGameClicked(game: GameModel) {
        route(LikedGamesRoute.Info(game.id))
    }


    fun onBottomReached() {
        loadNextPageOfData()
    }


    private fun loadNextPageOfData() {
        if(!hasMoreDataToLoad) return

        observeGamesUseCaseParams = observeGamesUseCaseParams.copy(
            observeGamesUseCaseParams.pagination.nextLimitPage()
        )

        viewModelScope.launch {
            dataLoadingJob?.cancelAndJoin()
            loadData()
        }
    }


}