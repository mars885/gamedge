/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.base.BaseViewModel
import com.paulrybitskyi.gamedge.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ErrorMapper
import com.paulrybitskyi.gamedge.commons.ui.widgets.news.GamingNewsItemModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.news.GamingNewsUiState
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.domain.articles.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.domain.commons.entities.Pagination
import com.paulrybitskyi.gamedge.news.mapping.GamingNewsUiStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GamingNewsViewModel @Inject constructor(
    private val observeArticlesUseCase: ObserveArticlesUseCase,
    private val gamingNewsUiStateFactory: GamingNewsUiStateFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {


    private companion object {

        private const val PAGINATION_LIMIT = 100

    }


    private var isLoadingData = false

    private var useCaseParams: ObserveArticlesUseCase.Params

    private var dataLoadingJob: Job? = null

    private val _newsUiState = MutableLiveData<GamingNewsUiState>(GamingNewsUiState.Empty)

    val newsUiState: LiveData<GamingNewsUiState>
        get() = _newsUiState


    init {
        Pagination(limit = PAGINATION_LIMIT)
            .let(ObserveArticlesUseCase::Params)
            .also { useCaseParams = it }
    }


    fun loadData() {
        if(isLoadingData) return

        dataLoadingJob = viewModelScope.launch {
            observeArticlesUseCase.execute(useCaseParams)
                .map(gamingNewsUiStateFactory::createWithResultState)
                .flowOn(dispatcherProvider.computation)
                .onError {
                    logger.error(logTag, "Failed to load articles.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(gamingNewsUiStateFactory.createWithEmptyState())
                }
                .onStart {
                    isLoadingData = true
                    emit(gamingNewsUiStateFactory.createWithLoadingState())
                }
                .onCompletion { isLoadingData = false }
                .collect(_newsUiState::setValue)
        }
    }


    fun onNewsItemClicked(model: GamingNewsItemModel) {
        dispatchCommand(GamingNewsCommand.OpenUrl(model.siteDetailUrl))
    }


    fun onRefreshRequested() {
        viewModelScope.launch {
            dataLoadingJob?.cancelAndJoin()
            loadData()
        }
    }


}