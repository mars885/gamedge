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

package com.paulrybitskyi.gamedge.feature.news.data.datastores.database

import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.database.articles.DatabaseArticle
import com.paulrybitskyi.gamedge.database.articles.tables.ArticlesTable
import com.paulrybitskyi.gamedge.feature.news.domain.datastores.ArticlesLocalDataStore
import com.paulrybitskyi.gamedge.feature.news.domain.entities.Article
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class ArticlesDatabaseDataStore @Inject constructor(
    private val articlesTable: ArticlesTable,
    private val dispatcherProvider: DispatcherProvider,
    private val dbArticleMapper: DbArticleMapper,
) : ArticlesLocalDataStore {

    override suspend fun saveArticles(articles: List<Article>) {
        articlesTable.saveArticles(
            withContext(dispatcherProvider.computation) {
                dbArticleMapper.mapToDatabaseArticles(articles)
            },
        )
    }

    override fun observeArticles(pagination: Pagination): Flow<List<Article>> {
        return articlesTable.observeArticles(
            offset = pagination.offset,
            limit = pagination.limit,
        )
        .toDataArticlesFlow()
    }

    private fun Flow<List<DatabaseArticle>>.toDataArticlesFlow(): Flow<List<Article>> {
        return distinctUntilChanged()
            .map(dbArticleMapper::mapToDomainArticles)
            .flowOn(dispatcherProvider.computation)
    }
}
