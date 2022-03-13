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

package com.paulrybitskyi.gamedge.feature.likes

import app.cash.turbine.test
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.FakeErrorMapper
import com.paulrybitskyi.gamedge.commons.testing.FakeLogger
import com.paulrybitskyi.gamedge.commons.testing.MainCoroutineRule
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveLikedGamesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class LikedGamesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK private lateinit var observeLikedGamesUseCase: ObserveLikedGamesUseCase

    private lateinit var logger: FakeLogger
    private lateinit var SUT: LikedGamesViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        SUT = LikedGamesViewModel(
            observeLikedGamesUseCase = observeLikedGamesUseCase,
            uiStateFactory = FakeUiStateFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger
        )
    }

    @Test
    fun `Emits correct ui states when loading data`() {
        runTest {
            coEvery { observeLikedGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.uiState.test {
                SUT.loadData()

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
    fun `Logs error when liked games loading fails`() {
        runTest {
            coEvery { observeLikedGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.loadData()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when liked games loading fails`() {
        runTest {
            coEvery { observeLikedGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.commandFlow.test {
                SUT.loadData()

                assertThat(awaitItem() is GeneralCommand.ShowLongToast).isTrue
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
                description = null
            )

            SUT.routeFlow.test {
                SUT.onGameClicked(gameModel)

                val route = awaitItem()

                assertThat(route is LikedGamesRoute.Info).isTrue
                assertThat((route as LikedGamesRoute.Info).gameId).isEqualTo(gameModel.id)
            }
        }
    }

    private class FakeUiStateFactory : LikedGamesUiStateFactory {

        override fun createWithEmptyState(): GamesUiState {
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
                        developerName = null,
                        description = null
                    )
                }
            )
        }
    }
}
