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

package com.paulrybitskyi.gamedge.ui.likes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.videos.GamesUiState
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.commons.nextLimitPage
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveLikedGamesUseCase
import com.paulrybitskyi.gamedge.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.ui.commons.ErrorMapper
import com.paulrybitskyi.gamedge.ui.likes.mapping.LikedGamesUiStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LikedGamesViewModel @Inject constructor(
    private val observeLikedGamesUseCase: ObserveLikedGamesUseCase,
    private val likedGamesUiStateFactory: LikedGamesUiStateFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {


    private var isLoadingData = false
    private var hasMoreDataToLoad = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()

    private var dataLoadingJob: Job? = null

    private val _gamesUiState = MutableLiveData(likedGamesUiStateFactory.createWithEmptyState())

    val gamesUiState: LiveData<GamesUiState>
        get() = _gamesUiState


    fun loadData() {
        if(isLoadingData) return

        dataLoadingJob = viewModelScope.launch {
            observeLikedGamesUseCase.execute(observeGamesUseCaseParams)
                .map(likedGamesUiStateFactory::createWithResultState)
                .flowOn(dispatcherProvider.computation)
                .onError {
                    logger.error(logTag, "Failed to load liked games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(likedGamesUiStateFactory.createWithEmptyState())
                }
                .onStart {
                    isLoadingData = true
                    emit(likedGamesUiStateFactory.createWithLoadingState())
                }
                .onCompletion { isLoadingData = false }
                .collect {
                    configureNextLoad(it)
                    _gamesUiState.value = it
                }
        }
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