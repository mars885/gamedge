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

package com.paulrybitskyi.gamedge.ui.discovery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategory
import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryItemChildModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryItemModel
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.domain.entities.Game
import com.paulrybitskyi.gamedge.domain.usecases.games.commons.GamesObserverParams
import com.paulrybitskyi.gamedge.domain.usecases.games.commons.GamesRefresherParams
import com.paulrybitskyi.gamedge.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.ui.base.events.commons.GeneralCommands
import com.paulrybitskyi.gamedge.ui.discovery.mapping.GamesDiscoveryItemGameModelMapper
import com.paulrybitskyi.gamedge.ui.discovery.mapping.GamesDiscoveryItemModelFactory
import com.paulrybitskyi.gamedge.ui.discovery.mapping.mapToItemModels
import com.paulrybitskyi.gamedge.utils.ErrorMapper
import com.paulrybitskyi.gamedge.utils.extensions.nonNullValue
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class GamesDiscoveryViewModel @ViewModelInject constructor(
    private val discoveryUseCasesMap: Map<GamesCategory, GamesDiscoveryUseCases>,
    private val discoveryItemModelFactory: GamesDiscoveryItemModelFactory,
    private val discoveryItemGameModelMapper: GamesDiscoveryItemGameModelMapper,
    private val errorMapper: ErrorMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val logger: Logger
) : BaseViewModel() {


    private var isLoadingData = false
    private var isRefreshingData = false

    private var gamesObserverParams = GamesObserverParams()
    private var gamesRefresherParams = GamesRefresherParams()

    private val _discoveryItems = MutableLiveData<List<GamesDiscoveryItemModel>>(listOf())

    val discoveryItems: LiveData<List<GamesDiscoveryItemModel>>
        get() = _discoveryItems


    init {
        initDiscoveryItemsData()
    }


    private fun initDiscoveryItemsData() {
        _discoveryItems.value = GamesCategory.values().map(discoveryItemModelFactory::createDefault)
    }


    fun loadData() {
        loadGames()
        refreshGames()
    }


    private fun loadGames() {
        if(isLoadingData) return

        viewModelScope.launch {
            combine(
                flows = GamesCategory.values().map { loadGames(it) },
                transform = { it.toList() }
            )
            .map { _discoveryItems.nonNullValue.withResultState(it) }
            .onError { logger.error(logTag, "Failed to load games.", it) }
            .onStart { isLoadingData = true }
            .onCompletion { isLoadingData = false }
            .collect(_discoveryItems::setValue)
        }
    }


    private suspend fun loadGames(category: GamesCategory): Flow<List<GamesDiscoveryItemChildModel>> {
        return discoveryUseCasesMap.getValue(category)
            .observeGamesUseCase.execute(gamesObserverParams)
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
            combine(
                flows = GamesCategory.values().map { refreshGames(it) },
                transform = { it.toList() }
            )
            .onError { onRefreshError(it) }
            .onStart { onRefreshStarted() }
            .onCompletion { onRefreshEnded() }
            .collect()
        }
    }


    private suspend fun refreshGames(category: GamesCategory): Flow<List<Game>> {
        return discoveryUseCasesMap.getValue(category)
            .refreshGamesUseCase.execute(gamesRefresherParams)
            .resultOrError()
    }


    private fun onRefreshError(error: Throwable) {
        dispatchCommand(GeneralCommands.ShowLongToast(errorMapper.mapToMessage(error)))
    }


    private fun onRefreshStarted() {
        isRefreshingData = true
        _discoveryItems.value = _discoveryItems.nonNullValue.withVisibleProgressBar()
    }


    private fun onRefreshEnded() {
        isRefreshingData = false
        _discoveryItems.value = _discoveryItems.nonNullValue.withHiddenProgressBar()
    }


    fun onCategoryMoreButtonClicked(category: GamesCategory) {
        //todo
    }


    fun onCategoryGameClicked() {
        //todo
    }


    fun onRefreshRequested() {
        refreshGames()
    }


}