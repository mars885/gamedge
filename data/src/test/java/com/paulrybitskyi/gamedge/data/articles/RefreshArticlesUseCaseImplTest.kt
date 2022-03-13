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

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.paulrybitskyi.gamedge.commons.testing.DATA_ARTICLES
import com.paulrybitskyi.gamedge.commons.testing.DATA_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.REFRESH_ARTICLES_USE_CASE_PARAMS
import com.paulrybitskyi.gamedge.commons.testing.utils.coVerifyNotCalled
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
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class RefreshArticlesUseCaseImplTest {

    @MockK private lateinit var articlesLocalDataStore: ArticlesLocalDataStore
    @MockK private lateinit var articlesRemoteDataStore: ArticlesRemoteDataStore
    @MockK private lateinit var throttler: ArticlesRefreshingThrottler
    @MockK private lateinit var keyProvider: ArticlesRefreshingThrottlerKeyProvider

    private lateinit var articleMapper: ArticleMapper
    private lateinit var SUT: RefreshArticlesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        articleMapper = ArticleMapper()
        SUT = RefreshArticlesUseCaseImpl(
            articlesDataStores = ArticlesDataStores(
                local = articlesLocalDataStore,
                remote = articlesRemoteDataStore
            ),
            dispatcherProvider = FakeDispatcherProvider(),
            throttlerTools = ArticlesRefreshingThrottlerTools(
                throttler = throttler,
                keyProvider = keyProvider
            ),
            mappers = RefreshArticlesUseCaseMappers(
                article = articleMapper,
                error = ErrorMapper()
            )
        )

        every { keyProvider.provideArticlesKey(any()) } returns "key"
    }

    @Test
    fun `Emits remote articles when refresh is possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns true
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Ok(DATA_ARTICLES)

            SUT.execute(REFRESH_ARTICLES_USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(articleMapper.mapToDomainArticles(DATA_ARTICLES))
                awaitComplete()
            }
        }
    }

    @Test
    fun `Does not emit remote articles when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false

            SUT.execute(REFRESH_ARTICLES_USE_CASE_PARAMS).test {
                awaitComplete()
            }
        }
    }

    @Test
    fun `Saves remote articles into local data store when refresh is successful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns true
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Ok(DATA_ARTICLES)

            SUT.execute(REFRESH_ARTICLES_USE_CASE_PARAMS).firstOrNull()

            coVerify { articlesLocalDataStore.saveArticles(DATA_ARTICLES) }
        }
    }

    @Test
    fun `Does not save remote articles into local data store when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false

            SUT.execute(REFRESH_ARTICLES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { articlesLocalDataStore.saveArticles(any()) }
        }
    }

    @Test
    fun `Does not save remote articles into local data store when refresh is unsuccessful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Err(DATA_ERROR_UNKNOWN)

            SUT.execute(REFRESH_ARTICLES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { articlesLocalDataStore.saveArticles(any()) }
        }
    }

    @Test
    fun `Updates articles last refresh time when refresh is successful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns true
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Ok(DATA_ARTICLES)

            SUT.execute(REFRESH_ARTICLES_USE_CASE_PARAMS).firstOrNull()

            coVerify { throttler.updateArticlesLastRefreshTime(any()) }
        }
    }

    @Test
    fun `Does not update articles last refresh time when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false

            SUT.execute(REFRESH_ARTICLES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { throttler.updateArticlesLastRefreshTime(any()) }
        }
    }

    @Test
    fun `Does not update articles last refresh time when refresh is unsuccessful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Err(DATA_ERROR_UNKNOWN)

            SUT.execute(REFRESH_ARTICLES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { throttler.updateArticlesLastRefreshTime(any()) }
        }
    }
}
