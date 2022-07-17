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

package com.paulrybitskyi.gamedge.feature.news.data.datastores.local.database

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.common.data.common.Pagination
import com.paulrybitskyi.gamedge.database.articles.DatabaseArticle
import com.paulrybitskyi.gamedge.database.articles.tables.ArticlesTable
import com.paulrybitskyi.gamedge.feature.news.data.DataArticle
import com.paulrybitskyi.gamedge.feature.news.data.datastores.local.ArticlesLocalDataStore
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
@BindType
internal class ArticlesDatabaseDataStore @Inject constructor(
    private val articlesTable: ArticlesTable,
    private val dispatcherProvider: DispatcherProvider,
    private val dbArticleMapper: DbArticleMapper
) : ArticlesLocalDataStore {

    override suspend fun saveArticles(articles: List<DataArticle>) {
        articlesTable.saveArticles(
            withContext(dispatcherProvider.computation) {
                dbArticleMapper.mapToDatabaseArticles(articles)
            }
        )
    }

    override fun observeArticles(pagination: Pagination): Flow<List<DataArticle>> {
        return articlesTable.observeArticles(
            offset = pagination.offset,
            limit = pagination.limit
        )
        .toDataArticlesFlow()
    }

    private fun Flow<List<DatabaseArticle>>.toDataArticlesFlow(): Flow<List<DataArticle>> {
        return distinctUntilChanged()
            .map(dbArticleMapper::mapToDataArticles)
            .flowOn(dispatcherProvider.computation)
    }
}
