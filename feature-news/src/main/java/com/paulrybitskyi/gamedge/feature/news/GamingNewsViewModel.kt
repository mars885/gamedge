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
import com.paulrybitskyi.gamedge.feature.news.mapping.GamingNewsItemUiModelMapper
import com.paulrybitskyi.gamedge.feature.news.mapping.mapToUiModels
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsItemUiModel
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsUiState
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

private const val MAX_ARTICLE_COUNT = 100
private const val ARTICLES_REFRESH_INITIAL_DELAY = 500L
private const val ARTICLES_REFRESH_DEFAULT_DELAY = 1000L

@HiltViewModel
internal class GamingNewsViewModel @Inject constructor(
    private val observeArticlesUseCase: ObserveArticlesUseCase,
    private val refreshArticlesUseCase: RefreshArticlesUseCase,
    private val uiModelMapper: GamingNewsItemUiModelMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {

    private var isObservingArticles = false

    private lateinit var observerUseCaseParams: ObserveArticlesUseCase.Params
    private lateinit var refresherUseCaseParams: RefreshArticlesUseCase.Params

    private val _uiState = MutableStateFlow(GamingNewsUiState())

    private val currentUiState: GamingNewsUiState
        get() = _uiState.value

    val uiState: StateFlow<GamingNewsUiState> = _uiState.asStateFlow()

    init {
        initUseCaseParams()
        observeArticles()
        refreshArticles(isFirstRefresh = true)
    }

    private fun initUseCaseParams() {
        val pagination = Pagination(limit = MAX_ARTICLE_COUNT)

        observerUseCaseParams = ObserveArticlesUseCase.Params(pagination)
        refresherUseCaseParams = RefreshArticlesUseCase.Params(pagination)
    }

    private fun observeArticles() {
        if (isObservingArticles) return

        observeArticlesUseCase.execute(observerUseCaseParams)
            .map(uiModelMapper::mapToUiModels)
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
            .onEach { emittedUiState -> _uiState.update { emittedUiState } }
            .launchIn(viewModelScope)
    }

    fun onSearchButtonClicked() {
        route(GamingNewsRoute.Search)
    }

    fun onNewsItemClicked(model: GamingNewsItemUiModel) {
        dispatchCommand(GamingNewsCommand.OpenUrl(model.siteDetailUrl))
    }

    fun onRefreshRequested() {
        if (!currentUiState.isRefreshing) {
            refreshArticles(isFirstRefresh = false)
        }
    }

    private fun refreshArticles(isFirstRefresh: Boolean) {
        refreshArticlesUseCase.execute(refresherUseCaseParams)
            .resultOrError()
            .map { currentUiState }
            .onError {
                logger.error(logTag, "Failed to refresh articles.", it)
                dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(it)))
            }
            .onStart {
                // Adding the delay on the first refresh to wait until the cached
                // articles are loaded and not overload the UI with loaders
                if (isFirstRefresh) {
                    delay(ARTICLES_REFRESH_INITIAL_DELAY)
                }

                emit(currentUiState.enableRefreshing())
                // Adding a delay to prevent the SwipeRefresh from disappearing quickly
                delay(ARTICLES_REFRESH_DEFAULT_DELAY)
            }
            .onCompletion {
                emit(currentUiState.disableRefreshing())
            }
            .onEach { emittedUiState -> _uiState.update { emittedUiState } }
            .launchIn(viewModelScope)
    }
}
