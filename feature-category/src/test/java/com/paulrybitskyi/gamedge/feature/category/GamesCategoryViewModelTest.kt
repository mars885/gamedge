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
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.FakeErrorMapper
import com.paulrybitskyi.gamedge.commons.testing.FakeLogger
import com.paulrybitskyi.gamedge.commons.testing.FakeStringProvider
import com.paulrybitskyi.gamedge.commons.testing.MainCoroutineRule
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObservePopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.RefreshPopularGamesUseCase
import com.paulrybitskyi.gamedge.feature.category.di.GamesCategoryKey
import com.paulrybitskyi.gamedge.feature.category.widgets.GameCategoryModelUiMapper
import com.paulrybitskyi.gamedge.feature.category.widgets.GameCategoryUiModel
import com.paulrybitskyi.gamedge.feature.category.widgets.finiteUiState
import com.paulrybitskyi.gamedge.feature.category.widgets.isRefreshing
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Provider

internal class GamesCategoryViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    @MockK private lateinit var observePopularGamesUseCase: ObservePopularGamesUseCase
    @MockK private lateinit var refreshPopularGamesUseCase: RefreshPopularGamesUseCase

    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamesCategoryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        SUT = GamesCategoryViewModel(
            savedStateHandle = setupSavedStateHandle(),
            stringProvider = FakeStringProvider(),
            transitionAnimationDuration = 0L,
            useCases = setupUseCases(),
            gameCategoryModelMapper = FakeGameCategoryModelUiMapper(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
        )
    }

    private fun setupSavedStateHandle(): SavedStateHandle {
        return mockk(relaxed = true) {
            every { get<String>(any()) } returns GamesCategory.POPULAR.name
        }
    }

    private fun setupUseCases(): GamesCategoryUseCases {
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

    @Test
    fun `Emits toolbar title when initialized`() {
        runTest {
            SUT.uiState.test {
                assertThat(awaitItem().title).isNotEmpty
            }
        }
    }

    @Test
    fun `Emits correct ui states when observing games`() {
        runTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.uiState.test {
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Empty)
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Loading)
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Logs error when games observing use case throws error`() {
        runTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when games observing use case throws error`() {
        runTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Emits correct ui states when refreshing games`() {
        runTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.uiState.test {
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Empty)
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Loading)
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Success)
                assertThat(awaitItem().isRefreshing).isTrue
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Logs error when games refreshing use case throws error`() {
        runTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when games refreshing use case throws error`() {
        runTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Routes to previous screen when toolbar left button is clicked`() {
        runTest {
            SUT.routeFlow.test {
                SUT.onToolbarLeftButtonClicked()

                assertThat(awaitItem()).isInstanceOf(GamesCategoryRoute.Back::class.java)
            }
        }
    }

    @Test
    fun `Routes to game info screen when game is clicked`() {
        runTest {
            val game = GameCategoryUiModel(
                id = 1,
                title = "title",
                coverUrl = null
            )

            SUT.routeFlow.test {
                SUT.onGameClicked(game)

                val route = awaitItem()

                assertThat(route).isInstanceOf(GamesCategoryRoute.Info::class.java)
                assertThat((route as GamesCategoryRoute.Info).gameId).isEqualTo(game.id)
            }
        }
    }

    private class FakeGameCategoryModelUiMapper : GameCategoryModelUiMapper {

        override fun mapToUiModel(game: DomainGame): GameCategoryUiModel {
            return GameCategoryUiModel(
                id = game.id,
                title = game.name,
                coverUrl = null,
            )
        }
    }
}
