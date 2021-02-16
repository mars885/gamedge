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
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.commons.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.feature.discovery.mapping.GamesDiscoveryItemGameModelMapper
import com.paulrybitskyi.gamedge.feature.discovery.mapping.GamesDiscoveryItemModelFactory
import com.paulrybitskyi.gamedge.feature.discovery.mapping.mapToItemModels
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemChildModel
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesDiscoveryViewModel @Inject constructor(
    private val discoveryUseCases: GamesDiscoveryUseCases,
    private val discoveryItemModelFactory: GamesDiscoveryItemModelFactory,
    private val discoveryItemGameModelMapper: GamesDiscoveryItemGameModelMapper,
    private val errorMapper: ErrorMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val logger: Logger
) : BaseViewModel() {


    private var isLoadingData = false
    private var isRefreshingData = false

    private var observeGamesUseCaseParams = ObserveGamesUseCaseParams()
    private var refreshGamesUseCaseParams = RefreshGamesUseCaseParams()

    private val _discoveryItems = MutableStateFlow<List<GamesDiscoveryItemModel>>(listOf())

    val discoveryItems: StateFlow<List<GamesDiscoveryItemModel>>
        get() = _discoveryItems


    init {
        initDiscoveryItemsData()
    }


    private fun initDiscoveryItemsData() {
        _discoveryItems.value = GamesDiscoveryCategory.values().map(discoveryItemModelFactory::createDefault)
    }


    fun loadData() {
        loadGames()
        refreshGames()
    }


    private fun loadGames() {
        if(isLoadingData) return

        viewModelScope.launch {
            combine(
                flows = GamesDiscoveryCategory.values().map { loadGames(it) },
                transform = { it.toList() }
            )
            .map { _discoveryItems.value.withResultState(it) }
            .onError { logger.error(logTag, "Failed to load games.", it) }
            .onStart { isLoadingData = true }
            .onCompletion { isLoadingData = false }
            .collect { _discoveryItems.value = it }
        }
    }


    private suspend fun loadGames(category: GamesDiscoveryCategory): Flow<List<GamesDiscoveryItemChildModel>> {
        return discoveryUseCases.observeGamesUseCasesMap.getValue(category.toKeyType())
            .execute(observeGamesUseCaseParams)
            .map(discoveryItemGameModelMapper::mapToItemModels)
            .flowOn(dispatcherProvider.computation)
    }


    private fun List<GamesDiscoveryItemModel>.withResultState(
        children: List<List<GamesDiscoveryItemChildModel>>
    ): List<GamesDiscoveryItemModel> {
        return mapIndexed { index, itemModel ->
            discoveryItemModelFactory.createCopyWithResultState(itemModel, children[index])
        }
    }


    private fun List<GamesDiscoveryItemModel>.withVisibleProgressBar(): List<GamesDiscoveryItemModel> {
        return map(discoveryItemModelFactory::createCopyWithVisibleProgressBar)
    }


    private fun List<GamesDiscoveryItemModel>.withHiddenProgressBar(): List<GamesDiscoveryItemModel> {
        return map(discoveryItemModelFactory::createCopyWithHiddenProgressBar)
    }


    private fun refreshGames() {
        if(isRefreshingData) return

        viewModelScope.launch {
            discoveryUseCases.refreshAllDiscoverableGamesUseCase
                .execute(refreshGamesUseCaseParams)
                .onError {
                    logger.error(logTag, "Failed to refresh games.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                }
                .onStart {
                    isRefreshingData = true
                    _discoveryItems.value = _discoveryItems.value.withVisibleProgressBar()
                }
                .onCompletion {
                    isRefreshingData = false
                    _discoveryItems.value = _discoveryItems.value.withHiddenProgressBar()
                }
                .collect()
        }
    }


    fun onCategoryMoreButtonClicked(category: String) {
        route(GamesDiscoveryRoute.Category(category))
    }


    fun onCategoryGameClicked(item: GamesDiscoveryItemChildModel) {
        route(GamesDiscoveryRoute.Info(gameId = item.id))
    }


    fun onRefreshRequested() {
        refreshGames()
    }


}