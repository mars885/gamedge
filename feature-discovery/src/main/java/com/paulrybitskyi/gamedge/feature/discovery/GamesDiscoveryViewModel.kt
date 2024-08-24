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

package com.paulrybitskyi.gamedge.feature.discovery

import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.extensions.resultOrError
import com.paulrybitskyi.gamedge.common.domain.games.common.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.common.domain.games.common.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.common.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.common.ui.base.events.common.GeneralCommand
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.feature.discovery.mapping.GamesDiscoveryItemGameUiModelMapper
import com.paulrybitskyi.gamedge.feature.discovery.mapping.mapToUiModels
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemGameUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class GamesDiscoveryViewModel @Inject constructor(
    private val useCases: GamesDiscoveryUseCases,
    private val uiModelMapper: GamesDiscoveryItemGameUiModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val stringProvider: StringProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger,
) : BaseViewModel() {

    private var isObservingGames = false
    private var isRefreshingGames = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()
    private var refreshGamesUseCaseParams = RefreshGamesUseCaseParams()

    private val _items = MutableStateFlow<List<GamesDiscoveryItemUiModel>>(listOf())

    private val currentItems: List<GamesDiscoveryItemUiModel>
        get() = _items.value

    val items: StateFlow<List<GamesDiscoveryItemUiModel>> = _items.asStateFlow()

    init {
        initDiscoveryItemsData()
        observeGames()
        refreshGames()
    }

    private fun initDiscoveryItemsData() {
        _items.update {
            GamesDiscoveryCategory.entries.map { category ->
                GamesDiscoveryItemUiModel(
                    id = category.id,
                    categoryName = category.name,
                    title = stringProvider.getString(category.titleId),
                    isProgressBarVisible = false,
                    games = emptyList(),
                )
            }
        }
    }

    private fun observeGames() {
        if (isObservingGames) return

        combine(
            flows = GamesDiscoveryCategory.entries.map(::observeGames),
            transform = { it.toList() },
        )
        .map { games -> currentItems.toSuccessState(games) }
        .onError { logger.error(logTag, "Failed to observe games.", it) }
        .onStart { isObservingGames = true }
        .onCompletion { isObservingGames = false }
        .onEach { emittedItems -> _items.update { emittedItems } }
        .launchIn(viewModelScope)
    }

    private fun observeGames(category: GamesDiscoveryCategory): Flow<List<GamesDiscoveryItemGameUiModel>> {
        return useCases.getObservableUseCase(category.toKeyType())
            .execute(observeGamesUseCaseParams)
            .map(uiModelMapper::mapToUiModels)
            .flowOn(dispatcherProvider.computation)
    }

    private fun refreshGames() {
        if (isRefreshingGames) return

        combine(
            flows = GamesDiscoveryCategory.entries.map(::refreshGames),
            transform = { it.toList() },
        )
        .map { currentItems }
        .onError {
            logger.error(logTag, "Failed to refresh games.", it)
            dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
        }
        .onStart {
            isRefreshingGames = true
            emit(currentItems.showProgressBar())
        }
        .onCompletion {
            isRefreshingGames = false
            emit(currentItems.hideProgressBar())
        }
        .onEach { emittedItems -> _items.update { emittedItems } }
        .launchIn(viewModelScope)
    }

    private fun refreshGames(category: GamesDiscoveryCategory): Flow<List<Game>> {
        return useCases.getRefreshableUseCase(category.toKeyType())
            .execute(refreshGamesUseCaseParams)
            .resultOrError()
    }

    fun onSearchButtonClicked() {
        route(GamesDiscoveryRoute.Search)
    }

    fun onCategoryMoreButtonClicked(category: String) {
        route(GamesDiscoveryRoute.Category(category))
    }

    fun onCategoryGameClicked(item: GamesDiscoveryItemGameUiModel) {
        route(GamesDiscoveryRoute.Info(gameId = item.id))
    }

    fun onRefreshRequested() {
        refreshGames()
    }
}
