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

import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.extensions.resultOrError
import com.paulrybitskyi.gamedge.common.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.common.ui.base.events.common.GeneralCommand
import com.paulrybitskyi.gamedge.common.ui.di.qualifiers.TransitionAnimationDuration
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.feature.info.domain.entities.GameImageType
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameImageUrlsUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameInfoUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.likes.ToggleGameLikeStateUseCase
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.companies.GameInfoCompanyUiModel
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.links.GameInfoLinkUiModel
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.main.GameInfoUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.relatedgames.GameInfoRelatedGameUiModel
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.videos.GameInfoVideoUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.paulrybitskyi.gamedge.core.R as CoreR

@HiltViewModel(assistedFactory = GameInfoViewModel.Factory::class)
@Suppress("LongParameterList")
internal class GameInfoViewModel @AssistedInject constructor(
    @TransitionAnimationDuration
    transitionAnimationDuration: Long,
    @Assisted
    private val destination: GameInfoDestination,
    private val useCases: GameInfoUseCases,
    private val uiModelMapper: GameInfoUiModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val stringProvider: StringProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger,
) : BaseViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(destination: GameInfoDestination): GameInfoViewModel
    }

    private var isObservingGameData = false

    private val _uiState = MutableStateFlow(GameInfoUiState(isLoading = false, game = null))

    private val currentUiState: GameInfoUiState
        get() = _uiState.value

    val uiState: StateFlow<GameInfoUiState> = _uiState.asStateFlow()

    init {
        observeGameInfo(resultEmissionDelay = transitionAnimationDuration)
    }

    private fun observeGameInfo(resultEmissionDelay: Long) {
        if (isObservingGameData) return

        viewModelScope.launch {
            observeGameInfoInternal(resultEmissionDelay)
        }
    }

    private suspend fun observeGameInfoInternal(resultEmissionDelay: Long) {
        useCases.getGameInfoUseCase.execute(GetGameInfoUseCase.Params(destination.gameId))
            .map(uiModelMapper::mapToUiModel)
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

    fun onArtworkClicked(artworkIndex: Int) {
        navigateToImageViewer(
            title = stringProvider.getString(CoreR.string.artwork),
            imageType = GameImageType.ARTWORK,
            initialPosition = artworkIndex,
        )
    }

    private fun navigateToImageViewer(
        title: String,
        imageType: GameImageType,
        initialPosition: Int = 0,
    ) {
        viewModelScope.launch {
            useCases.getGameImageUrlsUseCase.execute(
                GetGameImageUrlsUseCase.Params(
                    gameId = destination.gameId,
                    imageType = imageType,
                ),
            )
            .resultOrError()
            .onError {
                logger.error(logTag, "Failed to get the image urls of type = $imageType.", it)
                dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
            }
            .collect { imageUrls ->
                route(GameInfoRoute.ImageViewer(imageUrls, title, initialPosition))
            }
        }
    }

    fun onBackButtonClicked() {
        route(GameInfoRoute.Back)
    }

    fun onCoverClicked() {
        navigateToImageViewer(
            title = stringProvider.getString(CoreR.string.cover),
            imageType = GameImageType.COVER,
        )
    }

    fun onLikeButtonClicked() {
        viewModelScope.launch {
            useCases.toggleGameLikeStateUseCase
                .execute(ToggleGameLikeStateUseCase.Params(destination.gameId))
        }
    }

    fun onVideoClicked(video: GameInfoVideoUiModel) {
        openUrl(video.videoUrl)
    }

    fun onScreenshotClicked(screenshotIndex: Int) {
        navigateToImageViewer(
            title = stringProvider.getString(CoreR.string.screenshot),
            imageType = GameImageType.SCREENSHOT,
            initialPosition = screenshotIndex,
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
