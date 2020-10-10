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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategory
import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryItemChildModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryItemModel
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.commons.RefreshGamesUseCaseParams
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
        return discoveryUseCases.observeGamesUseCasesMap.getValue(category)
            .execute(observeGamesUseCaseParams)
            .map(discoveryItemGameModelMapper::mapToItemModels)
            .flowOn(dispatcherProvider.computation)
            .conflate()
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
            discoveryUseCases.refreshAllGamesUseCase
                .execute(refreshGamesUseCaseParams)
                .onError { onRefreshError(it) }
                .onStart { onRefreshStarted() }
                .onCompletion { onRefreshEnded() }
                .collect()
        }
    }


    private fun onRefreshError(error: Throwable) {
        logger.error(logTag, "Failed to refresh games.", error)

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