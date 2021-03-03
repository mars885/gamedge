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

package com.paulrybitskyi.gamedge.database

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.articles.DataArticle
import com.paulrybitskyi.gamedge.data.commons.Pagination
import com.paulrybitskyi.gamedge.database.articles.DatabaseArticle
import com.paulrybitskyi.gamedge.database.articles.datastores.ArticleMapper
import com.paulrybitskyi.gamedge.database.articles.datastores.ArticlesDatabaseDataStore
import com.paulrybitskyi.gamedge.database.articles.datastores.mapToDatabaseArticles
import com.paulrybitskyi.gamedge.database.articles.tables.ArticlesTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


internal val DATA_ARTICLE = DataArticle(
    id = 1,
    title = "title",
    lede = "lede",
    imageUrls = emptyMap(),
    publicationDate = 500L,
    siteDetailUrl = "url"
)
internal val DATA_ARTICLES = listOf(
    DATA_ARTICLE.copy(id = 1),
    DATA_ARTICLE.copy(id = 2),
    DATA_ARTICLE.copy(id = 3),
)


internal class ArticlesDatabaseDataStoreTest {


    private lateinit var articlesTable: FakeArticlesTable
    private lateinit var articleMapper: FakeArticleMapper
    private lateinit var articlesDbDataStore: ArticlesDatabaseDataStore


    @Before
    fun setup() {
        articlesTable = FakeArticlesTable()
        articleMapper = FakeArticleMapper()
        articlesDbDataStore = ArticlesDatabaseDataStore(
            articlesTable = articlesTable,
            dispatcherProvider = FakeDispatcherProvider(),
            articleMapper = articleMapper
        )
    }


    @Test
    fun `Saves articles to table successfully`() = runBlockingTest {
        articlesDbDataStore.saveArticles(DATA_ARTICLES)

        assertEquals(
            articleMapper.mapToDatabaseArticles(DATA_ARTICLES),
            articlesTable.articles
        )
    }


    @Test
    fun `Emits articles successfully`() = runBlockingTest {
        articlesDbDataStore.saveArticles(DATA_ARTICLES)

        val articles = articlesDbDataStore.observeArticles(Pagination(offset = 0, limit = 20)).first()

        assertEquals(DATA_ARTICLES, articles)
    }


    private class FakeArticlesTable : ArticlesTable {

        var articles = listOf<DatabaseArticle>()

        override suspend fun saveArticles(articles: List<DatabaseArticle>) {
            this.articles = articles
        }

        override fun observeArticles(offset: Int, limit: Int): Flow<List<DatabaseArticle>> {
            return flowOf(this.articles)
        }

    }


    private class FakeDispatcherProvider(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
        override val main: CoroutineDispatcher = testDispatcher,
        override val io: CoroutineDispatcher = testDispatcher,
        override val computation: CoroutineDispatcher = testDispatcher
    ) : DispatcherProvider


    private class FakeArticleMapper : ArticleMapper {

        override fun mapToDatabaseArticle(dataArticle: DataArticle): DatabaseArticle {
            return DatabaseArticle(
                id = dataArticle.id,
                title = dataArticle.title,
                lede = dataArticle.lede,
                imageUrls = "",
                publicationDate = dataArticle.publicationDate,
                siteDetailUrl = dataArticle.siteDetailUrl
            )
        }

        override fun mapToDataArticle(databaseArticle: DatabaseArticle): DataArticle {
            return DataArticle(
                id = databaseArticle.id,
                title = databaseArticle.title,
                lede = databaseArticle.lede,
                imageUrls = emptyMap(),
                publicationDate = databaseArticle.publicationDate,
                siteDetailUrl = databaseArticle.siteDetailUrl
            )
        }

    }


}