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
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModelMapper
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GamesViewState
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.mapToGameModels
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.domain.commons.entities.hasDefaultLimit
import com.paulrybitskyi.gamedge.domain.commons.entities.nextLimit
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveLikedGamesUseCase
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

private const val SUBSEQUENT_EMISSION_DELAY = 500L

@HiltViewModel
class LikedGamesViewModel @Inject constructor(
    private val observeLikedGamesUseCase: ObserveLikedGamesUseCase,
    private val likedGameModelMapper: GameModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val stringProvider: StringProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {

    private var isObservingGames = false
    private var hasMoreGamesToLoad = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()

    private var gamesObservingJob: Job? = null

    private val _uiState = MutableStateFlow(createDefaultUiState())

    private val currentUiState: GamesViewState
        get() = _uiState.value

    val uiState: StateFlow<GamesViewState>
        get() = _uiState

    private fun createDefaultUiState(): GamesViewState {
        return GamesViewState(
            isLoading = false,
            infoIconId = R.drawable.account_heart_outline,
            infoTitle = stringProvider.getString(R.string.liked_games_info_title),
            games = emptyList(),
        )
    }

    fun loadData() {
        observeGames()
    }

    private fun observeGames() {
        if (isObservingGames) return

        gamesObservingJob = viewModelScope.launch {
            observeLikedGamesUseCase.execute(observeGamesUseCaseParams)
                .map(likedGameModelMapper::mapToGameModels)
                .flowOn(dispatcherProvider.computation)
                .map { games -> currentUiState.copy(isLoading = false, games = games) }
                .onError {
                    logger.error(logTag, "Failed to load liked games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(currentUiState.copy(isLoading = false, games = emptyList()))
                }
                .onStart {
                    isObservingGames = true
                    emit(currentUiState.copy(isLoading = true))

                    // Delaying to give a sense of "loading" since progress indicators
                    // do not have the time to fully show themselves
                    if (isSubsequentEmission()) delay(SUBSEQUENT_EMISSION_DELAY)
                }
                .onCompletion { isObservingGames = false }
                .collect {
                    configureNextLoad(it)
                    _uiState.value = it
                }
        }
    }

    private fun isSubsequentEmission(): Boolean {
        return !observeGamesUseCaseParams.pagination.hasDefaultLimit()
    }

    private fun configureNextLoad(uiState: GamesViewState) {
        if (!uiState.hasLoadedNewGames()) return

        val paginationLimit = observeGamesUseCaseParams.pagination.limit
        val itemCount = uiState.games.size

        hasMoreGamesToLoad = (paginationLimit == itemCount)
    }

    private fun GamesViewState.hasLoadedNewGames(): Boolean {
        return (!isLoading && games.isNotEmpty())
    }

    fun onGameClicked(game: GameModel) {
        route(LikedGamesRoute.Info(game.id))
    }

    fun onBottomReached() {
        observeNewGamesBatch()
    }

    private fun observeNewGamesBatch() {
        if (!hasMoreGamesToLoad) return

        observeGamesUseCaseParams = observeGamesUseCaseParams.copy(
            observeGamesUseCaseParams.pagination.nextLimit()
        )

        viewModelScope.launch {
            gamesObservingJob?.cancelAndJoin()
            observeGames()
        }
    }
}
