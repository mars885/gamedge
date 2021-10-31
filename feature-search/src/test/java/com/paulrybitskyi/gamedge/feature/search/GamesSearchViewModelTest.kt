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
import com.paulrybitskyi.gamedge.commons.testing.*
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.SearchGamesUseCase
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

internal class GamesSearchViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK private lateinit var searchGamesUseCase: SearchGamesUseCase

    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamesSearchViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        SUT = GamesSearchViewModel(
            searchGamesUseCase = searchGamesUseCase,
            uiStateFactory = FakeGamesSearchUiStateFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
            savedStateHandle = setupSavedStateHandle()
        )
    }


    private fun setupSavedStateHandle(): SavedStateHandle {
        return mockk(relaxed = true) {
            every { get<String>(any()) } returns ""
        }
    }


    @Test
    fun `Routes to previous screen when toolbar back button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            SUT.routeFlow.test {
                SUT.onToolbarBackButtonClicked()

                assertThat(awaitItem() is GamesSearchRoute.Back).isTrue
            }
        }
    }


    @Test
    fun `Emits correct ui states when searching for games`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.uiState.test {
                SUT.onSearchActionRequested("god of war")

                val emptyState = awaitItem()
                val loadingState = awaitItem()
                val resultState = awaitItem()

                assertThat(emptyState is GamesUiState.Empty).isTrue
                assertThat(loadingState is GamesUiState.Loading).isTrue
                assertThat(resultState is GamesUiState.Result).isTrue
                assertThat((resultState as GamesUiState.Result).items).hasSize(DOMAIN_GAMES.size)
            }
        }
    }


    @Test
    fun `Does not emit ui states when search query is empty`() {
        mainCoroutineRule.runBlockingTest {
            SUT.uiState.test {
                SUT.onSearchActionRequested("")

                assertThat(awaitItem() is GamesUiState.Empty).isTrue
                expectNoEvents()
            }
        }
    }


    @Test
    fun `Does not emit ui states when the current search query is provided`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.onSearchActionRequested("god of war")

            SUT.uiState.test {
                SUT.onSearchActionRequested("god of war")

                assertThat(awaitItem() is GamesUiState.Result).isTrue
                expectNoEvents()
            }
        }
    }


    @Test
    fun `Emits empty ui state when blank search query is provided`() {
        mainCoroutineRule.runBlockingTest {
            SUT.uiState.test {
                SUT.onSearchActionRequested("   ")

                assertThat(awaitItem() is GamesUiState.Empty).isTrue
                expectNoEvents()
            }
        }
    }


    @Test
    fun `Dispatches items clearing command when performing new search`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.commandFlow.test {
                SUT.onSearchActionRequested("god of war")

                assertThat(awaitItem() is GamesSearchCommand.ClearItems).isTrue
            }
        }
    }


    @Test
    fun `Logs error when searching games use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.onSearchActionRequested("god of war")

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Dispatches toast showing command when searching games use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { searchGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.commandFlow.test {
                SUT.onSearchActionRequested("god of war")

                assertThat(awaitItem() is GamesSearchCommand.ClearItems).isTrue
                assertThat(awaitItem() is GeneralCommand.ShowLongToast).isTrue
            }
        }
    }


    @Test
    fun `Routes to info screen when game is clicked`() {
        mainCoroutineRule.runBlockingTest {
            val gameModel = GameModel(
                id = 1,
                coverImageUrl = null,
                name = "",
                releaseDate = "",
                developerName = null,
                description = null
            )

            SUT.routeFlow.test {
                SUT.onGameClicked(gameModel)

                val route = awaitItem()

                assertThat(route is GamesSearchRoute.Info).isTrue
                assertThat((route as GamesSearchRoute.Info).gameId).isEqualTo(gameModel.id)
            }
        }
    }


    private class FakeGamesSearchUiStateFactory : GamesSearchUiStateFactory {

        override fun createWithEmptyState(searchQuery: String): GamesUiState {
            return GamesUiState.Empty(iconId = -1, title = "title")
        }

        override fun createWithLoadingState(): GamesUiState {
            return GamesUiState.Loading
        }

        override fun createWithResultState(games: List<DomainGame>): GamesUiState {
            return GamesUiState.Result(
                games.map {
                    GameModel(
                        id = it.id,
                        coverImageUrl = null,
                        name = it.name,
                        releaseDate = "release_date",
                        developerName = "developer_name",
                        description = "description"
                    )
                }
            )
        }

    }


}
