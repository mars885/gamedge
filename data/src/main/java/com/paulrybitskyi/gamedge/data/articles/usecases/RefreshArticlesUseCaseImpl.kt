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
import com.paulrybitskyi.gamedge.core.utils.mapResult
import com.paulrybitskyi.gamedge.data.articles.datastores.ArticlesDataStores
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.RefreshArticlesUseCaseMappers
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.mapToDomainArticles
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.throttling.ArticlesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.data.commons.utils.onEachSuccess
import com.paulrybitskyi.gamedge.data.commons.utils.toDataPagination
import com.paulrybitskyi.gamedge.domain.articles.entities.Article
import com.paulrybitskyi.gamedge.domain.articles.usecases.RefreshArticlesUseCase
import com.paulrybitskyi.gamedge.domain.articles.usecases.RefreshArticlesUseCase.Params
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class RefreshArticlesUseCaseImpl @Inject constructor(
    private val articlesDataStores: ArticlesDataStores,
    private val dispatcherProvider: DispatcherProvider,
    private val throttlerTools: ArticlesRefreshingThrottlerTools,
    private val mappers: RefreshArticlesUseCaseMappers
) : RefreshArticlesUseCase {


    override suspend fun execute(params: Params): Flow<DomainResult<List<Article>>> {
        val throttlerKey = throttlerTools.keyProvider.provideArticlesKey(params.pagination)

        return flow {
            if(throttlerTools.throttler.canRefreshArticles(throttlerKey)) {
                emit(articlesDataStores.remote.getArticles(params.pagination.toDataPagination()))
            }
        }
        .onEachSuccess {
            articlesDataStores.local.saveArticles(it)
            throttlerTools.throttler.updateArticlesLastRefreshTime(throttlerKey)
        }
        .flowOn(dispatcherProvider.main)
        .mapResult(mappers.article::mapToDomainArticles, mappers.error::mapToDomainError)
        .flowOn(dispatcherProvider.computation)
    }


}