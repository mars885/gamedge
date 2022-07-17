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

package com.paulrybitskyi.gamedge.feature.info.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.common.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.common.ui.base.events.common.GeneralCommand
import com.paulrybitskyi.gamedge.common.ui.di.qualifiers.TransitionAnimationDuration
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.factories.ImageViewerGameUrlFactory
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.combine
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.common.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.common.domain.games.entities.Company
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.domain.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.GetGameUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.likes.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.likes.ToggleGameLikeStateUseCase
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.main.GameInfoUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.companies.GameInfoCompanyUiModel
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.links.GameInfoLinkUiModel
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.videos.GameInfoVideoUiModel
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.relatedgames.GameInfoRelatedGameUiModel
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.main.GameInfoUiState
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.main.toEmptyState
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.main.toLoadingState
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.main.toSuccessState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val PARAM_GAME_ID = "game-id"

@HiltViewModel
@Suppress("LongParameterList")
internal class GameInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @TransitionAnimationDuration
    transitionAnimationDuration: Long,
    private val useCases: GameInfoUseCases,
    private val uiModelMapper: GameInfoUiModelMapper,
    private val gameUrlFactory: ImageViewerGameUrlFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val stringProvider: StringProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {

    private var isObservingGameData = false

    private val gameId = checkNotNull(savedStateHandle.get<Int>(PARAM_GAME_ID))

    private val relatedGamesUseCasePagination = Pagination()

    private val _uiState = MutableStateFlow(GameInfoUiState(isLoading = false, game = null))

    private val currentUiState: GameInfoUiState
        get() = _uiState.value

    val uiState: StateFlow<GameInfoUiState> = _uiState.asStateFlow()

    init {
        observeGameData(resultEmissionDelay = transitionAnimationDuration)
    }

    private fun observeGameData(resultEmissionDelay: Long) {
        if (isObservingGameData) return

        viewModelScope.launch {
            observeGameDataInternal(resultEmissionDelay)
        }
    }

    private suspend fun observeGameDataInternal(resultEmissionDelay: Long) {
        getGame()
            .flatMapConcat { game ->
                combine(
                    flowOf(game),
                    observeGameLikeState(game),
                    getCompanyGames(game),
                    getSimilarGames(game)
                )
            }
            .map { (game, isGameLiked, companyGames, similarGames) ->
                uiModelMapper.mapToUiModel(
                    game,
                    isGameLiked,
                    companyGames,
                    similarGames
                )
            }
            .flowOn(dispatcherProvider.computation)
            .map { game -> currentUiState.toSuccessState(game) }
            .onError {
                logger.error(logTag, "Failed to load game info data.", it)
                dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                emit(currentUiState.toEmptyState())
            }
            .onStart {
                isObservingGameData = true
                emit(currentUiState.toLoadingState())
                delay(resultEmissionDelay)
            }
            .onCompletion { isObservingGameData = false }
            .collect { emittedUiState -> _uiState.update { emittedUiState } }
    }

    private suspend fun getGame(): Flow<Game> {
        return useCases.getGameUseCase
            .execute(GetGameUseCase.Params(gameId))
            .resultOrError()
    }

    private fun observeGameLikeState(game: Game): Flow<Boolean> {
        return useCases.observeGameLikeStateUseCase
            .execute(ObserveGameLikeStateUseCase.Params(game.id))
    }

    private suspend fun getCompanyGames(game: Game): Flow<List<Game>> {
        val company = game.developerCompany
            ?.takeIf(Company::hasDevelopedGames)
            ?: return flowOf(emptyList())

        return useCases.getCompanyDevelopedGamesUseCase
            .execute(GetCompanyDevelopedGamesUseCase.Params(company, relatedGamesUseCasePagination))
    }

    private suspend fun getSimilarGames(game: Game): Flow<List<Game>> {
        if (!game.hasSimilarGames) return flowOf(emptyList())

        return useCases.getSimilarGamesUseCase
            .execute(GetSimilarGamesUseCase.Params(game, relatedGamesUseCasePagination))
    }

    fun onArtworkClicked(artworkIndex: Int) {
        navigateToImageViewer(
            title = stringProvider.getString(R.string.artwork),
            initialPosition = artworkIndex,
            gameImageUrlsProvider = gameUrlFactory::createArtworkImageUrls
        )
    }

    private fun navigateToImageViewer(
        title: String,
        initialPosition: Int = 0,
        gameImageUrlsProvider: (Game) -> List<String>
    ) {
        viewModelScope.launch {
            val game = getGame()
                .onError {
                    logger.error(logTag, "Failed to get the game.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                }
                .firstOrNull()
                ?: return@launch

            val imageUrls = gameImageUrlsProvider(game)
                .takeIf(List<String>::isNotEmpty)
                ?: return@launch

            route(GameInfoRoute.ImageViewer(title, initialPosition, imageUrls))
        }
    }

    fun onBackButtonClicked() {
        route(GameInfoRoute.Back)
    }

    fun onCoverClicked() {
        navigateToImageViewer(
            title = stringProvider.getString(R.string.cover),
            gameImageUrlsProvider = { game ->
                gameUrlFactory.createCoverImageUrl(game)
                    ?.let(::listOf)
                    ?: emptyList()
            }
        )
    }

    fun onLikeButtonClicked() {
        viewModelScope.launch {
            useCases.toggleGameLikeStateUseCase
                .execute(ToggleGameLikeStateUseCase.Params(gameId))
        }
    }

    fun onVideoClicked(video: GameInfoVideoUiModel) {
        openUrl(video.videoUrl)
    }

    fun onScreenshotClicked(screenshotIndex: Int) {
        navigateToImageViewer(
            title = stringProvider.getString(R.string.screenshot),
            initialPosition = screenshotIndex,
            gameImageUrlsProvider = gameUrlFactory::createScreenshotImageUrls
        )
    }

    fun onLinkClicked(link: GameInfoLinkUiModel) {
        openUrl(link.url)
    }

    fun onCompanyClicked(company: GameInfoCompanyUiModel) {
        openUrl(company.websiteUrl)
    }

    private fun openUrl(url: String) {
        dispatchCommand(GameInfoCommand.OpenUrl(url))
    }

    fun onRelatedGameClicked(game: GameInfoRelatedGameUiModel) {
        route(GameInfoRoute.Info(gameId = game.id))
    }
}
