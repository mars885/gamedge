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

package com.paulrybitskyi.gamedge.feature.search.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.common.testing.FakeErrorMapper
import com.paulrybitskyi.gamedge.common.testing.FakeLogger
import com.paulrybitskyi.gamedge.common.testing.FakeStringProvider
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.common.testing.domain.MainCoroutineRule
import com.paulrybitskyi.gamedge.common.ui.base.events.common.GeneralCommand
import com.paulrybitskyi.gamedge.common.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModel
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModelMapper
import com.paulrybitskyi.gamedge.common.ui.widgets.games.finiteUiState
import com.paulrybitskyi.gamedge.feature.search.domain.SearchGamesUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class GamesSearchViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val searchGamesUseCase = mockk<SearchGamesUseCase>(relaxed = true)

    private val logger = FakeLogger()
    private val SUT by lazy {
        GamesSearchViewModel(
            searchGamesUseCase = searchGamesUseCase,
            uiModelMapper = FakeGameUiModelMapper(),
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
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
    fun `Query text is cleared from UI state when clear button is clicked`() {
        runTest {
            val query = "query"

            SUT.onQueryChanged(query)

            SUT.uiState.test {
                SUT.onToolbarClearButtonClicked()

                assertThat(awaitItem().queryText).isEqualTo(query)
                assertThat(awaitItem().queryText).isEmpty()
            }
        }
    }

    @Test
    fun `Query text of UI state is synced with query text entered by user`() {
        runTest {
            val query = "Shadow of the Colossus"

            SUT.uiState.test {
                SUT.onQueryChanged(query)

                assertThat(awaitItem().queryText).isEmpty()
                assertThat(awaitItem().queryText).isEqualTo(query)
            }
        }
    }

    @Test
    fun `Emits correct ui states when searching for games`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.uiState.test {
                SUT.onSearchConfirmed("god of war")

                val emptyState = awaitItem().gamesUiState
                val loadingState = awaitItem().gamesUiState
                val resultState = awaitItem().gamesUiState

                assertThat(emptyState.finiteUiState).isEqualTo(FiniteUiState.Empty)
                assertThat(loadingState.finiteUiState).isEqualTo(FiniteUiState.Loading)
                assertThat(resultState.finiteUiState).isEqualTo(FiniteUiState.Success)
                assertThat(resultState.games).hasSize(DOMAIN_GAMES.size)
            }
        }
    }

    @Test
    fun `Does not emit ui states when search query is empty`() {
        runTest {
            SUT.uiState.test {
                SUT.onSearchConfirmed("")

                assertThat(awaitItem().gamesUiState.finiteUiState).isEqualTo(FiniteUiState.Empty)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Does not emit ui states when the current search query is provided`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            val gameQuery = "god of war"

            SUT.onSearchConfirmed(gameQuery)
            advanceUntilIdle()

            SUT.uiState.test {
                SUT.onSearchConfirmed(gameQuery)

                assertThat(awaitItem().gamesUiState.finiteUiState).isEqualTo(FiniteUiState.Success)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Emits empty ui state when blank search query is provided`() {
        runTest {
            SUT.uiState.test {
                SUT.onSearchConfirmed("   ")

                assertThat(awaitItem().gamesUiState.finiteUiState).isEqualTo(FiniteUiState.Empty)
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Dispatches items clearing command when performing new search`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.uiState.test {
                SUT.onSearchConfirmed("god of war")

                assertThat(awaitItem().gamesUiState.games).isEmpty()
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Logs error when searching games use case throws error`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_UNKNOWN))

            SUT.onSearchConfirmed("god of war")
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty()
        }
    }

    @Test
    fun `Dispatches toast showing command when searching games use case throws error`() {
        runTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_UNKNOWN))

            SUT.commandFlow.test {
                SUT.onSearchConfirmed("god of war")

                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Routes to info screen when game is clicked`() {
        runTest {
            val game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "",
                releaseDate = "",
                developerName = null,
                description = null,
            )

            SUT.routeFlow.test {
                SUT.onGameClicked(game)

                val route = awaitItem()

                assertThat(route).isInstanceOf(GamesSearchRoute.Info::class.java)
                assertThat((route as GamesSearchRoute.Info).gameId).isEqualTo(game.id)
            }
        }
    }

    private class FakeGameUiModelMapper : GameUiModelMapper {

        override fun mapToUiModel(game: Game): GameUiModel {
            return GameUiModel(
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
