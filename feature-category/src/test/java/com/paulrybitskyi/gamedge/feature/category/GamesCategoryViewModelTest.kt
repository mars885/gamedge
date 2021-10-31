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

package com.paulrybitskyi.gamedge.feature.category

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.commons.testing.*
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObservePopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.RefreshPopularGamesUseCase
import com.paulrybitskyi.gamedge.feature.category.di.GamesCategoryKey
import com.paulrybitskyi.gamedge.feature.category.mapping.GamesCategoryUiStateFactory
import com.paulrybitskyi.gamedge.feature.category.widgets.GameCategoryModel
import com.paulrybitskyi.gamedge.feature.category.widgets.GamesCategoryUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Provider

@Suppress("TooGenericExceptionThrown")
internal class GamesCategoryViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK private lateinit var observePopularGamesUseCase: ObservePopularGamesUseCase
    @MockK private lateinit var refreshPopularGamesUseCase: RefreshPopularGamesUseCase

    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamesCategoryViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        SUT = GamesCategoryViewModel(
            stringProvider = FakeStringProvider(),
            useCases = setupUseCases(),
            uiStateFactory = FakeGamesCategoryUiStateFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
            savedStateHandle = setupSavedStateHandle()
        )
    }


    private fun setupUseCases(): GamesCategoryUseCases {
        coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
        coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

        return GamesCategoryUseCases(
            observeGamesUseCasesMap = mapOf(
                GamesCategoryKey.Type.POPULAR to Provider { observePopularGamesUseCase },
                GamesCategoryKey.Type.RECENTLY_RELEASED to Provider(::mockk),
                GamesCategoryKey.Type.COMING_SOON to Provider(::mockk),
                GamesCategoryKey.Type.MOST_ANTICIPATED to Provider(::mockk)
            ),
            refreshGamesUseCasesMap = mapOf(
                GamesCategoryKey.Type.POPULAR to Provider { refreshPopularGamesUseCase },
                GamesCategoryKey.Type.RECENTLY_RELEASED to Provider(::mockk),
                GamesCategoryKey.Type.COMING_SOON to Provider(::mockk),
                GamesCategoryKey.Type.MOST_ANTICIPATED to Provider(::mockk)
            )
        )
    }


    private fun setupSavedStateHandle(): SavedStateHandle {
        return mockk(relaxed = true) {
            every { get<String>(any()) } returns GamesCategory.POPULAR.name
        }
    }


    @Test
    fun `Emits toolbar title when initialized`() {
        mainCoroutineRule.runBlockingTest {
            SUT.toolbarTitle.test {
                assertThat(awaitItem()).isNotEmpty
            }
        }
    }


    @Test
    fun `Emits correct ui states when observing games`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.uiState.test {
                SUT.loadData(resultEmissionDelay = 0L)

                assertThat(awaitItem() is GamesCategoryUiState.Empty).isTrue
                assertThat(awaitItem() is GamesCategoryUiState.Loading).isTrue
                assertThat(awaitItem() is GamesCategoryUiState.Result).isTrue

                cancelAndIgnoreRemainingEvents()
            }
        }
    }


    @Test
    fun `Logs error when games observing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flow { throw Exception("error") }
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.loadData(resultEmissionDelay = 0L)

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Dispatches toast showing command when games observing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flow { throw Exception("error") }
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.commandFlow.test {
                SUT.loadData(resultEmissionDelay = 0L)

                assertThat(awaitItem() is GeneralCommand.ShowLongToast).isTrue
            }
        }
    }


    @Test
    fun `Emits correct ui states when refreshing games`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.uiState.test {
                SUT.loadData(resultEmissionDelay = 0L)

                assertThat(awaitItem() is GamesCategoryUiState.Empty).isTrue
                assertThat(awaitItem() is GamesCategoryUiState.Loading).isTrue
                assertThat(awaitItem() is GamesCategoryUiState.Result).isTrue
                assertThat(awaitItem() is GamesCategoryUiState.Loading).isTrue

                cancelAndIgnoreRemainingEvents()
            }
        }
    }


    @Test
    fun `Logs error when games refreshing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flow { throw Exception("error") }

            SUT.loadData(resultEmissionDelay = 0L)

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Dispatches toast showing command when games refreshing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flow { throw Exception("error") }

            SUT.commandFlow.test {
                SUT.loadData(resultEmissionDelay = 0L)

                assertThat(awaitItem() is GeneralCommand.ShowLongToast).isTrue
            }
        }
    }


    @Test
    fun `Routes to previous screen when toolbar left button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            SUT.routeFlow.test {
                SUT.onToolbarLeftButtonClicked()

                assertThat(awaitItem() is GamesCategoryRoute.Back).isTrue
            }
        }
    }


    @Test
    fun `Routes to game info screen when game is clicked`() {
        mainCoroutineRule.runBlockingTest {
            val game = GameCategoryModel(
                id = 1,
                title = "title",
                coverUrl = null
            )

            SUT.routeFlow.test {
                SUT.onGameClicked(game)

                val route = awaitItem()

                assertThat(route is GamesCategoryRoute.Info).isTrue
                assertThat((route as GamesCategoryRoute.Info).gameId).isEqualTo(game.id)
            }
        }
    }


    private class FakeGamesCategoryUiStateFactory : GamesCategoryUiStateFactory {

        override fun createWithEmptyState(): GamesCategoryUiState {
            return GamesCategoryUiState.Empty
        }

        override fun createWithLoadingState(): GamesCategoryUiState {
            return GamesCategoryUiState.Loading
        }

        override fun createWithResultState(games: List<DomainGame>): GamesCategoryUiState {
            return GamesCategoryUiState.Result(
                games.map {
                    GameCategoryModel(
                        id = it.id,
                        title = it.name,
                        coverUrl = null
                    )
                }
            )
        }

    }


}
