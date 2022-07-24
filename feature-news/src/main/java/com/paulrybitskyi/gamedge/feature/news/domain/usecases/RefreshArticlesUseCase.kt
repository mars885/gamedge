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

package com.paulrybitskyi.gamedge.feature.news.domain.usecases

import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.DomainResult
import com.paulrybitskyi.gamedge.common.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.common.domain.common.extensions.onEachSuccess
import com.paulrybitskyi.gamedge.common.domain.common.usecases.ObservableUseCase
import com.paulrybitskyi.gamedge.feature.news.domain.throttling.ArticlesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.feature.news.domain.datastores.ArticlesDataStores
import com.paulrybitskyi.gamedge.feature.news.domain.entities.Article
import com.paulrybitskyi.gamedge.feature.news.domain.usecases.RefreshArticlesUseCase.Params
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

internal interface RefreshArticlesUseCase : ObservableUseCase<Params, DomainResult<List<Article>>> {

    data class Params(val pagination: Pagination = Pagination())
}

@Singleton
@BindType
internal class RefreshArticlesUseCaseImpl @Inject constructor(
    private val articlesDataStores: ArticlesDataStores,
    private val dispatcherProvider: DispatcherProvider,
    private val throttlerTools: ArticlesRefreshingThrottlerTools,
) : RefreshArticlesUseCase {

    override fun execute(params: Params): Flow<DomainResult<List<Article>>> {
        val throttlerKey = throttlerTools.keyProvider.provideArticlesKey(params.pagination)

        return flow {
            if (throttlerTools.throttler.canRefreshArticles(throttlerKey)) {
                emit(articlesDataStores.remote.getArticles(params.pagination))
            }
        }
        .onEachSuccess { articles ->
            articlesDataStores.local.saveArticles(articles)
            throttlerTools.throttler.updateArticlesLastRefreshTime(throttlerKey)
        }
        .flowOn(dispatcherProvider.main)
    }
}
