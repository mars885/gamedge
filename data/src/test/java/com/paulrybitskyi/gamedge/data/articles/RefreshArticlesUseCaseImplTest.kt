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

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.articles.datastores.ArticlesDataStores
import com.paulrybitskyi.gamedge.data.articles.datastores.ArticlesLocalDataStore
import com.paulrybitskyi.gamedge.data.articles.datastores.ArticlesRemoteDataStore
import com.paulrybitskyi.gamedge.data.articles.usecases.RefreshArticlesUseCaseImpl
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.ArticleMapper
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.RefreshArticlesUseCaseMappers
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.mapToDomainArticles
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.throttling.ArticlesRefreshingThrottler
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.throttling.ArticlesRefreshingThrottlerKeyProvider
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.throttling.ArticlesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.commons.DataResult
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.data.commons.entities.Error
import com.paulrybitskyi.gamedge.domain.articles.usecases.RefreshArticlesUseCase
import com.paulrybitskyi.gamedge.domain.commons.DomainPagination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
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

private val USE_CASE_PARAMS = RefreshArticlesUseCase.Params()


internal class RefreshArticlesUseCaseImplTest {


    private lateinit var articlesLocalDataStore: FakeArticlesLocalDataStore
    private lateinit var articlesRemoteDataStore: FakeArticlesRemoteDataStore
    private lateinit var throttler: FakeArticlesRefreshingThrottler
    private lateinit var articleMapper: ArticleMapper
    private lateinit var SUT: RefreshArticlesUseCaseImpl


    @Before
    fun setup() {
        articlesLocalDataStore = FakeArticlesLocalDataStore()
        articlesRemoteDataStore = FakeArticlesRemoteDataStore()
        throttler = FakeArticlesRefreshingThrottler()
        articleMapper = ArticleMapper()
        SUT = RefreshArticlesUseCaseImpl(
            articlesDataStores = ArticlesDataStores(
                local = articlesLocalDataStore,
                remote = articlesRemoteDataStore
            ),
            dispatcherProvider = FakeDispatcherProvider(),
            throttlerTools = ArticlesRefreshingThrottlerTools(
                throttler = throttler,
                keyProvider = FakeArticlesRefreshingThrottlerKeyProvider()
            ),
            mappers = RefreshArticlesUseCaseMappers(
                article = articleMapper,
                error = ErrorMapper()
            )
        )
    }


    @Test
    fun `Emits remote articles when refresh is possible`() {
        runBlockingTest {
            throttler.canRefreshArticles = true
            articlesRemoteDataStore.shouldReturnArticles = true

            assertEquals(
                articleMapper.mapToDomainArticles(DATA_ARTICLES),
                SUT.execute(USE_CASE_PARAMS).first().get()
            )
        }
    }


    @Test
    fun `Does not emit remote articles when refresh is not possible`() {
        runBlockingTest {
            throttler.canRefreshArticles = false

            var isEmptyFlow = false

            SUT.execute(USE_CASE_PARAMS)
                .onEmpty { isEmptyFlow = true }
                .firstOrNull()

            assertTrue(isEmptyFlow)
        }
    }


    @Test
    fun `Saves remote articles into local data store when refresh is successful`() {
        runBlockingTest {
            throttler.canRefreshArticles = true
            articlesRemoteDataStore.shouldReturnArticles = true

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            assertEquals(DATA_ARTICLES, articlesLocalDataStore.articles)
        }
    }


    @Test
    fun `Does not save remote articles into local data store when refresh is not possible`() {
        runBlockingTest {
            throttler.canRefreshArticles = false

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            assertTrue(articlesLocalDataStore.articles.isEmpty())
        }
    }


    @Test
    fun `Does not save remote articles into local data store when refresh is unsuccessful`() {
        runBlockingTest {
            throttler.canRefreshArticles = false
            articlesRemoteDataStore.shouldReturnError = true

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            assertTrue(articlesLocalDataStore.articles.isEmpty())
        }
    }


    @Test
    fun `Updates articles last refresh time when refresh is successful`() {
        runBlockingTest {
            throttler.canRefreshArticles = true
            articlesRemoteDataStore.shouldReturnArticles = true

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            assertTrue(throttler.areArticlesLastRefreshTimeUpdated)
        }
    }


    @Test
    fun `Does not update articles last refresh time when refresh is not possible`() {
        runBlockingTest {
            throttler.canRefreshArticles = false

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            assertFalse(throttler.areArticlesLastRefreshTimeUpdated)
        }
    }


    @Test
    fun `Does not update articles last refresh time when refresh is unsuccessful`() {
        runBlockingTest {
            throttler.canRefreshArticles = false
            articlesRemoteDataStore.shouldReturnError = true

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            assertFalse(throttler.areArticlesLastRefreshTimeUpdated)
        }
    }


    private class FakeArticlesLocalDataStore : ArticlesLocalDataStore {

        var articles = listOf<DataArticle>()

        override suspend fun saveArticles(articles: List<DataArticle>) {
            this.articles = articles
        }

        override suspend fun observeArticles(pagination: DataPagination): Flow<List<DataArticle>> {
            // no-op
            return flowOf()
        }

    }


    private class FakeArticlesRemoteDataStore : ArticlesRemoteDataStore {

        var shouldReturnArticles = false
        var shouldReturnError = false

        override suspend fun getArticles(pagination: DataPagination): DataResult<List<DataArticle>> {
            return when {
                shouldReturnArticles -> Ok(DATA_ARTICLES)
                shouldReturnError -> Err(Error.Unknown("error"))

                else -> throw IllegalStateException()
            }
        }

    }


    private class FakeDispatcherProvider(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
        override val main: CoroutineDispatcher = testDispatcher,
        override val io: CoroutineDispatcher = testDispatcher,
        override val computation: CoroutineDispatcher = testDispatcher
    ) : DispatcherProvider


    private class FakeArticlesRefreshingThrottler : ArticlesRefreshingThrottler {

        var canRefreshArticles = false
        var areArticlesLastRefreshTimeUpdated = false

        override suspend fun canRefreshArticles(key: String): Boolean {
            return canRefreshArticles
        }

        override suspend fun updateArticlesLastRefreshTime(key: String) {
            areArticlesLastRefreshTimeUpdated = true
        }

    }


    private class FakeArticlesRefreshingThrottlerKeyProvider : ArticlesRefreshingThrottlerKeyProvider {

        override fun provideArticlesKey(pagination: DomainPagination): String {
            return "key"
        }

    }


}