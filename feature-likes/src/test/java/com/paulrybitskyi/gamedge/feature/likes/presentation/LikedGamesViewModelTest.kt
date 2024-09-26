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

package com.paulrybitskyi.gamedge.feature.likes.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.common.testing.FakeErrorMapper
import com.paulrybitskyi.gamedge.common.testing.FakeLogger
import com.paulrybitskyi.gamedge.common.testing.FakeStringProvider
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.common.testing.domain.MainCoroutineRule
import com.paulrybitskyi.gamedge.common.ui.base.events.common.GeneralCommand
import com.paulrybitskyi.gamedge.common.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModel
import com.paulrybitskyi.gamedge.common.ui.widgets.games.GameUiModelMapper
import com.paulrybitskyi.gamedge.common.ui.widgets.games.finiteUiState
import com.paulrybitskyi.gamedge.feature.likes.domain.ObserveLikedGamesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class LikedGamesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val observeLikedGamesUseCase = mockk<ObserveLikedGamesUseCase>(relaxed = true)

    private val logger = FakeLogger()
    private val SUT by lazy {
        LikedGamesViewModel(
            observeLikedGamesUseCase = observeLikedGamesUseCase,
            uiModelMapper = FakeGameUiModelMapper(),
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
            stringProvider = FakeStringProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
        )
    }

    @Test
    fun `Emits correct ui states when loading data`() {
        runTest {
            every { observeLikedGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.uiState.test {
                val emptyState = awaitItem()
                val loadingState = awaitItem()
                val resultState = awaitItem()

                assertThat(emptyState.finiteUiState).isEqualTo(FiniteUiState.Empty)
                assertThat(loadingState.finiteUiState).isEqualTo(FiniteUiState.Loading)
                assertThat(resultState.finiteUiState).isEqualTo(FiniteUiState.Success)
                assertThat(resultState.games).hasSize(DOMAIN_GAMES.size)
            }
        }
    }

    @Test
    fun `Logs error when liked games loading fails`() {
        runTest {
            every { observeLikedGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty()
        }
    }

    @Test
    fun `Dispatches toast showing command when liked games loading fails`() {
        runTest {
            every { observeLikedGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Navigates to search screen when search button is clicked`() {
        runTest {
            SUT.directionFlow.test {
                SUT.onSearchButtonClicked()

                assertThat(awaitItem()).isInstanceOf(LikedGamesDirection.Search::class.java)
            }
        }
    }

    @Test
    fun `Navigates to info screen when game is clicked`() {
        runTest {
            val game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "",
                releaseDate = "",
                developerName = null,
                description = null,
            )

            SUT.directionFlow.test {
                SUT.onGameClicked(game)

                val direction = awaitItem()

                assertThat(direction).isInstanceOf(LikedGamesDirection.Info::class.java)
                assertThat((direction as LikedGamesDirection.Info).gameId).isEqualTo(game.id)
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
                developerName = null,
                description = null,
            )
        }
    }
}
