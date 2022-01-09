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

package com.paulrybitskyi.gamedge.feature.news

import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.domain.articles.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.domain.articles.usecases.RefreshArticlesUseCase
import com.paulrybitskyi.gamedge.domain.commons.entities.Pagination
import com.paulrybitskyi.gamedge.feature.news.mapping.GamingNewsItemModelMapper
import com.paulrybitskyi.gamedge.feature.news.mapping.mapToGamingNewsItemModels
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsItemModel
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsState
import com.paulrybitskyi.gamedge.feature.news.widgets.disableRefreshing
import com.paulrybitskyi.gamedge.feature.news.widgets.enableRefreshing
import com.paulrybitskyi.gamedge.feature.news.widgets.toEmptyState
import com.paulrybitskyi.gamedge.feature.news.widgets.toLoadingState
import com.paulrybitskyi.gamedge.feature.news.widgets.toSuccessState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val MAX_ARTICLE_COUNT = 100
private const val ARTICLES_REFRESH_DELAY = 1000L

@HiltViewModel
class GamingNewsViewModel @Inject constructor(
    private val observeArticlesUseCase: ObserveArticlesUseCase,
    private val refreshArticlesUseCase: RefreshArticlesUseCase,
    private val gamingNewsItemModelMapper: GamingNewsItemModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {

    private var isObservingArticles = false

    private var observerUseCaseParams: ObserveArticlesUseCase.Params
    private var refresherUseCaseParams: RefreshArticlesUseCase.Params

    private val _uiState = MutableStateFlow(GamingNewsState())

    private val currentUiState: GamingNewsState
        get() = _uiState.value

    val uiState: StateFlow<GamingNewsState>
        get() = _uiState

    init {
        val pagination = Pagination(limit = MAX_ARTICLE_COUNT)

        observerUseCaseParams = ObserveArticlesUseCase.Params(pagination)
        refresherUseCaseParams = RefreshArticlesUseCase.Params(pagination)
    }

    fun loadData() {
        observeArticles()
        refreshArticles()
    }

    private fun observeArticles() {
        if (isObservingArticles) return

        viewModelScope.launch {
            observeArticlesUseCase.execute(observerUseCaseParams)
                .map(gamingNewsItemModelMapper::mapToGamingNewsItemModels)
                .flowOn(dispatcherProvider.computation)
                .map { news -> currentUiState.toSuccessState(news) }
                .onError {
                    logger.error(logTag, "Failed to load articles.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                    emit(currentUiState.toEmptyState())
                }
                .onStart {
                    isObservingArticles = true
                    emit(currentUiState.toLoadingState())
                }
                .onCompletion { isObservingArticles = false }
                .collect { _uiState.value = it }
        }
    }

    fun onNewsItemClicked(model: GamingNewsItemModel) {
        dispatchCommand(GamingNewsCommand.OpenUrl(model.siteDetailUrl))
    }

    fun onRefreshRequested() {
        if (!currentUiState.isRefreshing) {
            refreshArticles()
        }
    }

    private fun refreshArticles() {
        viewModelScope.launch {
            refreshArticlesUseCase.execute(refresherUseCaseParams)
                .resultOrError()
                .map { currentUiState }
                .onError {
                    logger.error(logTag, "Failed to refresh articles.", it)
                    dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
                }
                .onStart {
                    emit(currentUiState.enableRefreshing())
                    // Adding a delay to prevent the SwipeRefresh from disappearing quickly
                    delay(ARTICLES_REFRESH_DELAY)
                }
                .onCompletion {
                    emit(currentUiState.disableRefreshing())
                }
                .collect { _uiState.value = it }
        }
    }
}
