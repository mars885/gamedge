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

package com.paulrybitskyi.gamedge.feature.likes.presentation

import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.common.ui.base.events.common.GeneralCommand
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModel
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModelMapper
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.common.ui.widgets.games.mapToUiModels
import com.paulrybitskyi.gamedge.common.ui.widgets.games.toEmptyState
import com.paulrybitskyi.gamedge.common.ui.widgets.games.toLoadingState
import com.paulrybitskyi.gamedge.common.ui.widgets.games.toSuccessState
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.common.domain.common.entities.hasDefaultLimit
import com.paulrybitskyi.gamedge.common.domain.common.entities.nextLimit
import com.paulrybitskyi.gamedge.common.domain.games.common.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.feature.likes.R
import com.paulrybitskyi.gamedge.feature.likes.domain.ObserveLikedGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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

private const val SUBSEQUENT_EMISSION_DELAY = 500L

@HiltViewModel
internal class LikedGamesViewModel @Inject constructor(
    private val observeLikedGamesUseCase: ObserveLikedGamesUseCase,
    private val uiModelMapper: GameUiModelMapper,
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

    private val currentUiState: GamesUiState
        get() = _uiState.value

    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()

    init {
        observeGames()
    }

    private fun createDefaultUiState(): GamesUiState {
        return GamesUiState(
            isLoading = false,
            infoIconId = R.drawable.account_heart_outline,
            infoTitle = stringProvider.getString(R.string.liked_games_info_title),
            games = emptyList(),
        )
    }

    private fun observeGames() {
        if (isObservingGames) return

        gamesObservingJob = observeLikedGamesUseCase.execute(observeGamesUseCaseParams)
            .map(uiModelMapper::mapToUiModels)
            .flowOn(dispatcherProvider.computation)
            .map { games -> currentUiState.toSuccessState(games) }
            .onError {
                logger.error(logTag, "Failed to load liked games.", it)
                dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                emit(currentUiState.toEmptyState())
            }
            .onStart {
                isObservingGames = true
                emit(currentUiState.toLoadingState())

                // Delaying to give a sense of "loading" since progress indicators
                // do not have the time to fully show themselves
                if (isSubsequentEmission()) delay(SUBSEQUENT_EMISSION_DELAY)
            }
            .onCompletion { isObservingGames = false }
            .onEach { emittedUiState ->
                configureNextLoad(emittedUiState)
                _uiState.update { emittedUiState }
            }
            .launchIn(viewModelScope)
    }

    private fun isSubsequentEmission(): Boolean {
        return !observeGamesUseCaseParams.pagination.hasDefaultLimit()
    }

    private fun configureNextLoad(uiState: GamesUiState) {
        if (!uiState.hasLoadedNewGames()) return

        val paginationLimit = observeGamesUseCaseParams.pagination.limit
        val itemCount = uiState.games.size

        hasMoreGamesToLoad = (paginationLimit == itemCount)
    }

    private fun GamesUiState.hasLoadedNewGames(): Boolean {
        return (!isLoading && games.isNotEmpty())
    }

    fun onSearchButtonClicked() {
        route(LikedGamesRoute.Search)
    }

    fun onGameClicked(game: GameUiModel) {
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
