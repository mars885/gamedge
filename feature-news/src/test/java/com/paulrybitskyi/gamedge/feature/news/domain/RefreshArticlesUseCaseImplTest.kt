/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.feature.news.domain

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.common.testing.domain.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.common.testing.domain.MainCoroutineRule
import com.paulrybitskyi.gamedge.common.testing.domain.coVerifyNotCalled
import com.paulrybitskyi.gamedge.feature.news.DOMAIN_ARTICLES
import com.paulrybitskyi.gamedge.feature.news.domain.datastores.ArticlesDataStores
import com.paulrybitskyi.gamedge.feature.news.domain.datastores.ArticlesLocalDataStore
import com.paulrybitskyi.gamedge.feature.news.domain.datastores.ArticlesRemoteDataStore
import com.paulrybitskyi.gamedge.feature.news.domain.throttling.ArticlesRefreshingThrottler
import com.paulrybitskyi.gamedge.feature.news.domain.throttling.ArticlesRefreshingThrottlerKeyProvider
import com.paulrybitskyi.gamedge.feature.news.domain.throttling.ArticlesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.feature.news.domain.usecases.RefreshArticlesUseCase
import com.paulrybitskyi.gamedge.feature.news.domain.usecases.RefreshArticlesUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val USE_CASE_PARAMS = RefreshArticlesUseCase.Params()

internal class RefreshArticlesUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK private lateinit var articlesLocalDataStore: ArticlesLocalDataStore
    @MockK private lateinit var articlesRemoteDataStore: ArticlesRemoteDataStore
    @MockK private lateinit var throttler: ArticlesRefreshingThrottler
    @MockK private lateinit var keyProvider: ArticlesRefreshingThrottlerKeyProvider

    private lateinit var SUT: RefreshArticlesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

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
        )

        every { keyProvider.provideArticlesKey(any()) } returns "key"
    }

    @Test
    fun `Emits remote articles when refresh is possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns true
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Ok(DOMAIN_ARTICLES)

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(DOMAIN_ARTICLES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Does not emit remote articles when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false

            SUT.execute(USE_CASE_PARAMS).test {
                awaitComplete()
            }
        }
    }

    @Test
    fun `Saves remote articles into local data store when refresh is successful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns true
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Ok(DOMAIN_ARTICLES)

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerify { articlesLocalDataStore.saveArticles(DOMAIN_ARTICLES) }
        }
    }

    @Test
    fun `Does not save remote articles into local data store when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { articlesLocalDataStore.saveArticles(any()) }
        }
    }

    @Test
    fun `Does not save remote articles into local data store when refresh is unsuccessful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Err(DOMAIN_ERROR_UNKNOWN)

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { articlesLocalDataStore.saveArticles(any()) }
        }
    }

    @Test
    fun `Updates articles last refresh time when refresh is successful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns true
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Ok(DOMAIN_ARTICLES)

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerify { throttler.updateArticlesLastRefreshTime(any()) }
        }
    }

    @Test
    fun `Does not update articles last refresh time when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { throttler.updateArticlesLastRefreshTime(any()) }
        }
    }

    @Test
    fun `Does not update articles last refresh time when refresh is unsuccessful`() {
        runTest {
            coEvery { throttler.canRefreshArticles(any()) } returns false
            coEvery { articlesRemoteDataStore.getArticles(any()) } returns Err(DOMAIN_ERROR_UNKNOWN)

            SUT.execute(USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { throttler.updateArticlesLastRefreshTime(any()) }
        }
    }
}
