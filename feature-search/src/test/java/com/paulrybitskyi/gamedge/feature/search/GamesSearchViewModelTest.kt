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

package com.paulrybitskyi.gamedge.feature.search

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.FakeErrorMapper
import com.paulrybitskyi.gamedge.commons.testing.FakeLogger
import com.paulrybitskyi.gamedge.commons.testing.FakeStringProvider
import com.paulrybitskyi.gamedge.commons.testing.MainCoroutineRule
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModelMapper
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.finiteUiState
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.SearchGamesUseCase
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

internal class GamesSearchViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    @MockK private lateinit var searchGamesUseCase: SearchGamesUseCase

    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamesSearchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        SUT = GamesSearchViewModel(
            searchGamesUseCase = searchGamesUseCase,
            gameModelMapper = FakeGameModelMapper(),
            dispatcherProvider = FakeDispatcherProvider(),
            stringProvider = FakeStringProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
            savedStateHandle = setupSavedStateHandle(),
        )
    }

    private fun setupSavedStateHandle(): SavedStateHandle {
        return mockk(relaxed = true) {
            every { get<String>(any()) } returns ""
        }
    }

    @Test
    fun `Routes to previous screen when toolbar back button is clicked`() {
        runTest {
            SUT.routeFlow.test {
                SUT.onToolbarBackButtonClicked()

                assertThat(awaitItem()).isInstanceOf(GamesSearchRoute.Back::class.java)
            }
        }
    }

    @Test
    fun `Emits correct ui states when searching for games`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.uiState.test {
                SUT.onSearchActionRequested("god of war")

                val emptyState = awaitItem().gamesUiState
                val loadingState = awaitItem().gamesUiState
                val resultState = awaitItem().gamesUiState

                assertThat(emptyState.finiteUiState).isEqualTo(FiniteUiState.EMPTY)
                assertThat(loadingState.finiteUiState).isEqualTo(FiniteUiState.LOADING)
                assertThat(resultState.finiteUiState).isEqualTo(FiniteUiState.SUCCESS)
                assertThat(resultState.games).hasSize(DOMAIN_GAMES.size)
            }
        }
    }

    @Test
    fun `Does not emit ui states when search query is empty`() {
        runTest {
            SUT.uiState.test {
                SUT.onSearchActionRequested("")

                assertThat(awaitItem().gamesUiState.finiteUiState).isEqualTo(FiniteUiState.EMPTY)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Does not emit ui states when the current search query is provided`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            val gameQuery = "god of war"

            SUT.onSearchActionRequested(gameQuery)
            advanceUntilIdle()

            SUT.uiState.test {
                SUT.onSearchActionRequested(gameQuery)

                assertThat(awaitItem().gamesUiState.finiteUiState).isEqualTo(FiniteUiState.SUCCESS)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Emits empty ui state when blank search query is provided`() {
        runTest {
            SUT.uiState.test {
                SUT.onSearchActionRequested("   ")

                assertThat(awaitItem().gamesUiState.finiteUiState).isEqualTo(FiniteUiState.EMPTY)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Dispatches items clearing command when performing new search`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.uiState.test {
                SUT.onSearchActionRequested("god of war")

                assertThat(awaitItem().gamesUiState.games).isEmpty()
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Logs error when searching games use case throws error`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.onSearchActionRequested("god of war")
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when searching games use case throws error`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.commandFlow.test {
                SUT.onSearchActionRequested("god of war")

                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Routes to info screen when game is clicked`() {
        runTest {
            val gameModel = GameModel(
                id = 1,
                coverImageUrl = null,
                name = "",
                releaseDate = "",
                developerName = null,
                description = null,
            )

            SUT.routeFlow.test {
                SUT.onGameClicked(gameModel)

                val route = awaitItem()

                assertThat(route).isInstanceOf(GamesSearchRoute.Info::class.java)
                assertThat((route as GamesSearchRoute.Info).gameId).isEqualTo(gameModel.id)
            }
        }
    }

    private class FakeGameModelMapper : GameModelMapper {

        override fun mapToGameModel(game: DomainGame): GameModel {
            return GameModel(
                id = game.id,
                coverImageUrl = null,
                name = game.name,
                releaseDate = "release_date",
                developerName = "developer_name",
                description = "description",
            )
        }
    }
}
