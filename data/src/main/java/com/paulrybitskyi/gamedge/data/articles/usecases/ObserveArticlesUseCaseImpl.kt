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

package com.paulrybitskyi.gamedge.data.articles.usecases

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.data.articles.datastores.ArticlesLocalDataStore
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.ArticleMapper
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.mapToDomainArticles
import com.paulrybitskyi.gamedge.data.commons.utils.toDataPagination
import com.paulrybitskyi.gamedge.domain.articles.entities.Article
import com.paulrybitskyi.gamedge.domain.articles.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.domain.articles.usecases.ObserveArticlesUseCase.Params
import com.paulrybitskyi.gamedge.domain.articles.usecases.RefreshArticlesUseCase
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class ObserveArticlesUseCaseImpl @Inject constructor(
    private val refreshArticlesUseCase: RefreshArticlesUseCase,
    private val articlesLocalDataStore: ArticlesLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val articleMapper: ArticleMapper
) : ObserveArticlesUseCase {


    override suspend fun execute(params: Params): Flow<List<Article>> {
        return refreshArticles(params)
            .flatMapConcat { observeArticles(params) }
    }


    private suspend fun refreshArticles(params: Params): Flow<List<Article>> {
        val refreshUseCaseParams = RefreshArticlesUseCase.Params(params.pagination)

        return refreshArticlesUseCase.execute(refreshUseCaseParams)
            .resultOrError()
    }


    private suspend fun observeArticles(params: Params): Flow<List<Article>> {
        return articlesLocalDataStore
            .observeArticles(params.pagination.toDataPagination())
            .map(articleMapper::mapToDomainArticles)
            .flowOn(dispatcherProvider.computation)
    }


}