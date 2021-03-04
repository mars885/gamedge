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

import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.domain.articles.DomainArticle
import com.paulrybitskyi.gamedge.domain.articles.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.feature.news.mapping.GamingNewsUiStateFactory
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsItemModel
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private val DOMAIN_ARTICLE = DomainArticle(
    id = 1,
    title = "Article",
    lede = "lede",
    imageUrls = emptyMap(),
    publicationDate = 0L,
    siteDetailUrl = ""
)
private val DOMAIN_ARTICLES = listOf(
    DOMAIN_ARTICLE.copy(id = 1),
    DOMAIN_ARTICLE.copy(id = 2),
    DOMAIN_ARTICLE.copy(id = 3)
)


internal class GamingNewsViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var observeArticlesUseCase: FakeObserveArticlesUseCase
    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamingNewsViewModel


    @Before
    fun setup() {
        observeArticlesUseCase = FakeObserveArticlesUseCase()
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
            observeArticlesUseCase.shouldReturnArticles = true

            val uiStates = mutableListOf<GamingNewsUiState>()
            val uiStateJob = launch { SUT.uiState.toList(uiStates) }

            SUT.loadData()

            assertThat(uiStates[0] is GamingNewsUiState.Empty).isTrue
            assertThat(uiStates[1] is GamingNewsUiState.Loading).isTrue
            assertThat(uiStates[2] is GamingNewsUiState.Result).isTrue
            assertThat((uiStates[2] as GamingNewsUiState.Result).items)
                .hasSize(DOMAIN_ARTICLES.size)

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Logs error when articles observing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            observeArticlesUseCase.shouldThrowError = true

            SUT.loadData()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Dispatches toast showing command when articles observing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            observeArticlesUseCase.shouldThrowError = true

            SUT.loadData()

            val command = SUT.commandFlow.first()

            assertThat(command is GeneralCommand.ShowLongToast).isTrue
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

            SUT.onNewsItemClicked(itemModel)

            val command = SUT.commandFlow.first()

            assertThat(command is GamingNewsCommand.OpenUrl).isTrue
            assertThat((command as GamingNewsCommand.OpenUrl).url)
                .isEqualTo(itemModel.siteDetailUrl)
        }
    }


    @Test
    fun `Emits correct ui states when refreshing data`() {
        mainCoroutineRule.runBlockingTest {
            observeArticlesUseCase.shouldReturnArticles = true

            val uiStates = mutableListOf<GamingNewsUiState>()
            val uiStateJob = launch { SUT.uiState.toList(uiStates) }

            SUT.onRefreshRequested()

            assertThat(uiStates[0] is GamingNewsUiState.Empty).isTrue
            assertThat(uiStates[1] is GamingNewsUiState.Loading).isTrue
            assertThat(uiStates[2] is GamingNewsUiState.Result).isTrue
            assertThat((uiStates[2] as GamingNewsUiState.Result).items)
                .hasSize(DOMAIN_ARTICLES.size)

            uiStateJob.cancel()
        }
    }


    private class FakeObserveArticlesUseCase : ObserveArticlesUseCase {

        var shouldReturnArticles = false
        var shouldThrowError = false

        override suspend fun execute(params: ObserveArticlesUseCase.Params): Flow<List<DomainArticle>> {
            return when {
                shouldReturnArticles -> flowOf(DOMAIN_ARTICLES)
                shouldThrowError -> flow { throw Exception("error") }

                else -> throw IllegalStateException()
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
                        publicationDate = "pub_date",
                        siteDetailUrl = it.siteDetailUrl
                    )
                }
            )
        }

    }


    private class FakeDispatcherProvider(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
        override val main: CoroutineDispatcher = testDispatcher,
        override val io: CoroutineDispatcher = testDispatcher,
        override val computation: CoroutineDispatcher = testDispatcher
    ) : DispatcherProvider


    private class FakeErrorMapper : ErrorMapper {

        override fun mapToMessage(error: Throwable): String {
            return "error"
        }

    }


    private class FakeLogger : Logger {

        var errorMessage = ""

        override fun info(tag: String, message: String, throwable: Throwable?) {}
        override fun debug(tag: String, message: String, throwable: Throwable?) {}
        override fun warning(tag: String, message: String, throwable: Throwable?) {}

        override fun error(tag: String, message: String, throwable: Throwable?) {
            errorMessage = message
        }

    }


}