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
import com.paulrybitskyi.gamedge.commons.testing.*
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.domain.articles.DomainArticle
import com.paulrybitskyi.gamedge.domain.articles.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.feature.news.mapping.GamingNewsUiStateFactory
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsItemModel
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class GamingNewsViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK private lateinit var observeArticlesUseCase: ObserveArticlesUseCase

    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamingNewsViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        SUT = GamingNewsViewModel(
            observeArticlesUseCase = observeArticlesUseCase,
            uiStateFactory = FakeGamingNewsUiStateFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger
        )
    }


    @Test
    fun `Emits correct ui states when loading data`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observeArticlesUseCase.execute(any()) } returns flowOf(DOMAIN_ARTICLES)

            SUT.uiState.test {
                SUT.loadData()

                val emptyState = awaitItem()
                val loadingState = awaitItem()
                val resultState = awaitItem()

                assertThat(emptyState is GamingNewsUiState.Empty).isTrue
                assertThat(loadingState is GamingNewsUiState.Loading).isTrue
                assertThat(resultState is GamingNewsUiState.Result).isTrue
                assertThat((resultState as GamingNewsUiState.Result).items).hasSize(DOMAIN_ARTICLES.size)
            }
        }
    }


    @Test
    fun `Logs error when articles observing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observeArticlesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.loadData()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Dispatches toast showing command when articles observing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observeArticlesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.commandFlow.test {
                SUT.loadData()

                assertThat(awaitItem() is GeneralCommand.ShowLongToast).isTrue
            }
        }
    }


    @Test
    fun `Dispatches url opening command when clicking on news item`() {
        mainCoroutineRule.runBlockingTest {
            val itemModel = GamingNewsItemModel(
                id = 1,
                imageUrl = null,
                title = "",
                lede = "",
                publicationDate = "",
                siteDetailUrl = "site_detail_url"
            )

            SUT.commandFlow.test {
                SUT.onNewsItemClicked(itemModel)

                val command = awaitItem()

                assertThat(command is GamingNewsCommand.OpenUrl).isTrue
                assertThat((command as GamingNewsCommand.OpenUrl).url).isEqualTo(itemModel.siteDetailUrl)
            }
        }
    }


    @Test
    fun `Emits correct ui states when refreshing data`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observeArticlesUseCase.execute(any()) } returns flowOf(DOMAIN_ARTICLES)

            SUT.uiState.test {
                SUT.onRefreshRequested()

                val emptyState = awaitItem()
                val loadingState = awaitItem()
                val resultState = awaitItem()

                assertThat(emptyState is GamingNewsUiState.Empty).isTrue
                assertThat(loadingState is GamingNewsUiState.Loading).isTrue
                assertThat(resultState is GamingNewsUiState.Result).isTrue
                assertThat((resultState as GamingNewsUiState.Result).items).hasSize(DOMAIN_ARTICLES.size)
            }
        }
    }


    private class FakeGamingNewsUiStateFactory : GamingNewsUiStateFactory {

        override fun createWithEmptyState(): GamingNewsUiState {
            return GamingNewsUiState.Empty
        }

        override fun createWithLoadingState(): GamingNewsUiState {
            return GamingNewsUiState.Loading
        }

        override fun createWithResultState(articles: List<DomainArticle>): GamingNewsUiState {
            return GamingNewsUiState.Result(
                articles.map {
                    GamingNewsItemModel(
                        id = it.id,
                        imageUrl = null,
                        title = it.title,
                        lede = it.lede,
                        publicationDate = "publication_date",
                        siteDetailUrl = it.siteDetailUrl
                    )
                }
            )
        }

    }


}
