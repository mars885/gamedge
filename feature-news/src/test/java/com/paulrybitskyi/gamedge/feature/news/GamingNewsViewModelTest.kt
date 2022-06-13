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

package com.paulrybitskyi.gamedge.feature.news

import app.cash.turbine.test
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_ARTICLES
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.FakeErrorMapper
import com.paulrybitskyi.gamedge.commons.testing.FakeLogger
import com.paulrybitskyi.gamedge.commons.testing.MainCoroutineRule
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.domain.articles.DomainArticle
import com.paulrybitskyi.gamedge.domain.articles.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.domain.articles.usecases.RefreshArticlesUseCase
import com.paulrybitskyi.gamedge.feature.news.mapping.GamingNewsItemModelMapper
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsItemModel
import com.paulrybitskyi.gamedge.feature.news.widgets.finiteUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class GamingNewsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    @MockK private lateinit var observeArticlesUseCase: ObserveArticlesUseCase
    @MockK private lateinit var refreshArticlesUseCase: RefreshArticlesUseCase

    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamingNewsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        SUT = GamingNewsViewModel(
            observeArticlesUseCase = observeArticlesUseCase,
            refreshArticlesUseCase = refreshArticlesUseCase,
            gamingNewsItemModelMapper = FakeGamingNewsItemModelMapper(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
        )
    }

    @Test
    fun `Emits correct ui states when loading data`() {
        runTest {
            coEvery { observeArticlesUseCase.execute(any()) } returns flowOf(DOMAIN_ARTICLES)

            SUT.uiState.test {
                val emptyState = awaitItem()
                val loadingState = awaitItem()
                val resultState = awaitItem()

                assertThat(emptyState.finiteUiState).isEqualTo(FiniteUiState.EMPTY)
                assertThat(loadingState.finiteUiState).isEqualTo(FiniteUiState.LOADING)
                assertThat(resultState.finiteUiState).isEqualTo(FiniteUiState.SUCCESS)
                assertThat(resultState.news).hasSize(DOMAIN_ARTICLES.size)
            }
        }
    }

    @Test
    fun `Logs error when articles observing use case throws error`() {
        runTest {
            coEvery { observeArticlesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when articles observing use case throws error`() {
        runTest {
            coEvery { observeArticlesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Routes to search screen when search button is clicked`() {
        runTest {
            SUT.routeFlow.test {
                SUT.onSearchButtonClicked()

                assertThat(awaitItem()).isInstanceOf(GamingNewsRoute.Search::class.java)
            }
        }
    }

    @Test
    fun `Dispatches url opening command when clicking on news item`() {
        runTest {
            val itemModel = GamingNewsItemModel(
                id = 1,
                imageUrl = null,
                title = "",
                lede = "",
                publicationDate = "",
                siteDetailUrl = "site_detail_url",
            )

            SUT.commandFlow.test {
                SUT.onNewsItemClicked(itemModel)

                val command = awaitItem()

                assertThat(command).isInstanceOf(GamingNewsCommand.OpenUrl::class.java)
                assertThat((command as GamingNewsCommand.OpenUrl).url).isEqualTo(itemModel.siteDetailUrl)
            }
        }
    }

    @Test
    fun `Emits correct ui states when refreshing data`() {
        runTest {
            coEvery { refreshArticlesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_ARTICLES))

            advanceUntilIdle()

            SUT.uiState.test {
                SUT.onRefreshRequested()

                assertThat(awaitItem().isRefreshing).isFalse
                assertThat(awaitItem().isRefreshing).isTrue
                assertThat(awaitItem().isRefreshing).isFalse
            }
        }
    }

    private class FakeGamingNewsItemModelMapper : GamingNewsItemModelMapper {

        override fun mapToGamingNewsItemModel(article: DomainArticle): GamingNewsItemModel {
            return GamingNewsItemModel(
                id = article.id,
                imageUrl = null,
                title = article.title,
                lede = article.lede,
                publicationDate = "publication_date",
                siteDetailUrl = article.siteDetailUrl,
            )
        }
    }
}
