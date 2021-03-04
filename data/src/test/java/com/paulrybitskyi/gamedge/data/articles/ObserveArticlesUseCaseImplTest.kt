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

package com.paulrybitskyi.gamedge.data.articles

import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.articles.datastores.ArticlesLocalDataStore
import com.paulrybitskyi.gamedge.data.articles.usecases.ObserveArticlesUseCaseImpl
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.ArticleMapper
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.mapToDomainArticles
import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.domain.articles.DomainArticle
import com.paulrybitskyi.gamedge.domain.articles.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.domain.articles.usecases.RefreshArticlesUseCase
import com.paulrybitskyi.gamedge.domain.commons.DomainPagination
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test


private val DATA_ARTICLE = DataArticle(
    id = 1,
    title = "title",
    lede = "lede",
    imageUrls = emptyMap(),
    publicationDate = 500L,
    siteDetailUrl = "url"
)
private val DATA_ARTICLES = listOf(
    DATA_ARTICLE.copy(id = 1),
    DATA_ARTICLE.copy(id = 2),
    DATA_ARTICLE.copy(id = 3),
)
private val DOMAIN_ARTICLE = DomainArticle(
    id = 1,
    title = "title",
    lede = "lede",
    imageUrls = emptyMap(),
    publicationDate = 500L,
    siteDetailUrl = "url"
)
private val DOMAIN_ARTICLES = listOf(
    DOMAIN_ARTICLE.copy(id = 1),
    DOMAIN_ARTICLE.copy(id = 2),
    DOMAIN_ARTICLE.copy(id = 3),
)

private val DOMAIN_PAGINATION = DomainPagination(offset = 0, limit = 20)
private val DATA_PAGINATION = DataPagination(offset = 0, limit = 20)

private val USE_CASE_PARAMS = ObserveArticlesUseCase.Params(true, DOMAIN_PAGINATION)


internal class ObserveArticlesUseCaseImplTest {


    private lateinit var refreshArticlesUseCase: FakeRefreshArticlesUseCase
    private lateinit var articlesLocalDataStore: FakeArticlesLocalDataStore
    private lateinit var articleMapper: ArticleMapper
    private lateinit var SUT: ObserveArticlesUseCaseImpl


    @Before
    fun setup() {
        refreshArticlesUseCase = FakeRefreshArticlesUseCase()
        articlesLocalDataStore = FakeArticlesLocalDataStore()
        articleMapper = ArticleMapper()
        SUT = ObserveArticlesUseCaseImpl(
            refreshArticlesUseCase = refreshArticlesUseCase,
            articlesLocalDataStore = articlesLocalDataStore,
            dispatcherProvider = FakeDispatcherProvider(),
            articleMapper = articleMapper
        )
    }


    @Test
    fun `Verify that articles are refreshed when refresh is requested`() {
        runBlockingTest {
            refreshArticlesUseCase.shouldReturnArticles = true

            SUT.execute(USE_CASE_PARAMS)

            assertThat(refreshArticlesUseCase.isExecuted).isTrue
        }
    }


    @Test
    fun `Emits articles from local data store when refresh is requested`() {
        runBlockingTest {
            refreshArticlesUseCase.shouldReturnArticles = true
            articlesLocalDataStore.saveArticles(DATA_ARTICLES)

            assertThat(SUT.execute(USE_CASE_PARAMS).first())
                .isEqualTo(articleMapper.mapToDomainArticles(DATA_ARTICLES))
        }
    }


    @Test
    fun `Emits articles from local data store when refresh use cases emits empty flow`() {
        runBlockingTest {
            refreshArticlesUseCase.shouldEmitEmptyFlow = true
            articlesLocalDataStore.saveArticles(DATA_ARTICLES)

            assertThat(SUT.execute(USE_CASE_PARAMS).first())
                .isEqualTo(articleMapper.mapToDomainArticles(DATA_ARTICLES))
        }
    }


    @Test
    fun `Emits articles from local data store when refresh is not requested`() {
        runBlockingTest {
            articlesLocalDataStore.saveArticles(DATA_ARTICLES)

            assertThat(SUT.execute(USE_CASE_PARAMS.copy(refreshArticles = false)).first())
                .isEqualTo(articleMapper.mapToDomainArticles(DATA_ARTICLES))
        }
    }


    private class FakeRefreshArticlesUseCase : RefreshArticlesUseCase {

        var shouldReturnArticles = false
        var shouldEmitEmptyFlow = false

        var isExecuted = false

        override suspend fun execute(params: RefreshArticlesUseCase.Params): Flow<DomainResult<List<DomainArticle>>> {
            isExecuted = true

            return when {
                shouldReturnArticles -> flowOf(Ok(DOMAIN_ARTICLES))
                shouldEmitEmptyFlow -> flowOf()

                else -> throw IllegalStateException()
            }
        }

    }


    private class FakeArticlesLocalDataStore : ArticlesLocalDataStore {

        var articles = listOf<DataArticle>()

        override suspend fun saveArticles(articles: List<DataArticle>) {
            this.articles = articles
        }

        override suspend fun observeArticles(pagination: DataPagination): Flow<List<DataArticle>> {
            return flowOf(this.articles)
        }

    }


    private class FakeDispatcherProvider(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
        override val main: CoroutineDispatcher = testDispatcher,
        override val io: CoroutineDispatcher = testDispatcher,
        override val computation: CoroutineDispatcher = testDispatcher
    ) : DispatcherProvider


}