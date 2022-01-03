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
import com.paulrybitskyi.gamedge.commons.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.commons.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.feature.discovery.mapping.GamesDiscoveryItemGameModelMapper
import com.paulrybitskyi.gamedge.feature.discovery.mapping.mapToGameModels
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemGameModel
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemModel
import com.paulrybitskyi.gamedge.feature.discovery.widgets.hideProgressBar
import com.paulrybitskyi.gamedge.feature.discovery.widgets.showProgressBar
import com.paulrybitskyi.gamedge.feature.discovery.widgets.toSuccessState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class GamesDiscoveryViewModel @Inject constructor(
    private val useCases: GamesDiscoveryUseCases,
    private val itemGameModelMapper: GamesDiscoveryItemGameModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val stringProvider: StringProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {

    private var isObservingGames = false
    private var isRefreshingGames = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()
    private var refreshGamesUseCaseParams = RefreshGamesUseCaseParams()

    private val _items = MutableStateFlow<List<GamesDiscoveryItemModel>>(listOf())

    private val currentItems: List<GamesDiscoveryItemModel>
        get() = _items.value

    val items: StateFlow<List<GamesDiscoveryItemModel>>
        get() = _items

    init {
        initDiscoveryItemsData()
    }

    private fun initDiscoveryItemsData() {
        _items.value = GamesDiscoveryCategory.values().map { category ->
            GamesDiscoveryItemModel(
                category = category.name,
                title = stringProvider.getString(category.titleId),
                isProgressBarVisible = false,
                games = emptyList(),
            )
        }
    }

    fun loadData() {
        observeGames()
        refreshGames()
    }

    private fun observeGames() {
        if (isObservingGames) return

        viewModelScope.launch {
            combine(
                flows = GamesDiscoveryCategory.values().map { observeGames(it) },
                transform = { it.toList() }
            )
            .map(currentItems::toSuccessState)
            .onError { logger.error(logTag, "Failed to observe games.", it) }
            .onStart { isObservingGames = true }
            .onCompletion { isObservingGames = false }
            .collect { _items.value = it }
        }
    }

    private suspend fun observeGames(category: GamesDiscoveryCategory): Flow<List<GamesDiscoveryItemGameModel>> {
        return useCases.getObservableUseCase(category.toKeyType())
            .execute(observeGamesUseCaseParams)
            .map(itemGameModelMapper::mapToGameModels)
            .flowOn(dispatcherProvider.computation)
    }

    private fun refreshGames() {
        if (isRefreshingGames) return

        viewModelScope.launch {
            combine(
                flows = GamesDiscoveryCategory.values().map { refreshGames(it) },
                transform = { it.toList() }
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
            .collect { _items.value = it }
        }
    }

    private suspend fun refreshGames(category: GamesDiscoveryCategory): Flow<List<Game>> {
        return useCases.getRefreshableUseCase(category.toKeyType())
            .execute(refreshGamesUseCaseParams)
            .resultOrError()
    }

    fun onCategoryMoreButtonClicked(category: String) {
        route(GamesDiscoveryRoute.Category(category))
    }

    fun onCategoryGameClicked(item: GamesDiscoveryItemGameModel) {
        route(GamesDiscoveryRoute.Info(gameId = item.id))
    }

    fun onRefreshRequested() {
        refreshGames()
    }
}
