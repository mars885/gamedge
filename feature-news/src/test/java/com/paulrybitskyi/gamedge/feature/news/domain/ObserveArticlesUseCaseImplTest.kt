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
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.domain.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.common.testing.domain.PAGINATION
import com.paulrybitskyi.gamedge.feature.news.DOMAIN_ARTICLES
import com.paulrybitskyi.gamedge.feature.news.domain.datastores.ArticlesLocalDataStore
import com.paulrybitskyi.gamedge.feature.news.domain.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.feature.news.domain.usecases.ObserveArticlesUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val USE_CASE_PARAMS = ObserveArticlesUseCase.Params(PAGINATION)

internal class ObserveArticlesUseCaseImplTest {

    @MockK private lateinit var articlesLocalDataStore: ArticlesLocalDataStore

    private lateinit var SUT: ObserveArticlesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = ObserveArticlesUseCaseImpl(
            articlesLocalDataStore = articlesLocalDataStore,
            dispatcherProvider = FakeDispatcherProvider(),
        )
    }

    @Test
    fun `Emits articles from local data store`() {
        runTest {
            every { articlesLocalDataStore.observeArticles(any()) } returns flowOf(DOMAIN_ARTICLES)

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitItem()).isEqualTo(DOMAIN_ARTICLES)
                awaitComplete()
            }
        }
    }
}
