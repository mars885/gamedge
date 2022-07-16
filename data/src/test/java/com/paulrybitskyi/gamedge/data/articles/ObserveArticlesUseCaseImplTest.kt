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
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.commons.testing.DATA_ARTICLES
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.OBSERVE_ARTICLES_USE_CASE_PARAMS
import com.paulrybitskyi.gamedge.data.articles.datastores.ArticlesLocalDataStore
import com.paulrybitskyi.gamedge.data.articles.usecases.ObserveArticlesUseCaseImpl
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.ArticleMapper
import com.paulrybitskyi.gamedge.data.articles.usecases.commons.mapToDomainArticles
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class ObserveArticlesUseCaseImplTest {

    @MockK private lateinit var articlesLocalDataStore: ArticlesLocalDataStore

    private lateinit var articleMapper: ArticleMapper
    private lateinit var SUT: ObserveArticlesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        articleMapper = ArticleMapper()
        SUT = ObserveArticlesUseCaseImpl(
            articlesLocalDataStore = articlesLocalDataStore,
            dispatcherProvider = FakeDispatcherProvider(),
            articleMapper = articleMapper
        )
    }

    @Test
    fun `Emits articles from local data store`() {
        runTest {
            every { articlesLocalDataStore.observeArticles(any()) } returns flowOf(DATA_ARTICLES)

            SUT.execute(OBSERVE_ARTICLES_USE_CASE_PARAMS).test {
                assertThat(awaitItem()).isEqualTo(articleMapper.mapToDomainArticles(DATA_ARTICLES))
                awaitComplete()
            }
        }
    }
}
