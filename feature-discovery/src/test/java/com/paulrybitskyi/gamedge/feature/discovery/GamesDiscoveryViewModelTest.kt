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

package com.paulrybitskyi.gamedge.feature.discovery

import app.cash.turbine.test
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.FakeErrorMapper
import com.paulrybitskyi.gamedge.commons.testing.FakeLogger
import com.paulrybitskyi.gamedge.commons.testing.FakeStringProvider
import com.paulrybitskyi.gamedge.commons.testing.MainCoroutineRule
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.ObservePopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.RefreshPopularGamesUseCase
import com.paulrybitskyi.gamedge.feature.discovery.di.GamesDiscoveryKey
import com.paulrybitskyi.gamedge.feature.discovery.mapping.GamesDiscoveryItemGameUiModelMapper
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemGameUiModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class GamesDiscoveryViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val observePopularGamesUseCase = mockk<ObservePopularGamesUseCase>(relaxed = true)
    private val refreshPopularGamesUseCase = mockk<RefreshPopularGamesUseCase>(relaxed = true)

    private val logger = FakeLogger()
    private val SUT by lazy {
        GamesDiscoveryViewModel(
            useCases = setupUseCases(),
            uiModelMapper = FakeGamesDiscoveryItemGameUiModelMapper(),
            dispatcherProvider = FakeDispatcherProvider(),
            stringProvider = FakeStringProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger
        )
    }

    private fun setupUseCases(): GamesDiscoveryUseCases {
        return GamesDiscoveryUseCases(
            observeGamesUseCasesMap = mapOf(
                GamesDiscoveryKey.Type.POPULAR to observePopularGamesUseCase,
                GamesDiscoveryKey.Type.RECENTLY_RELEASED to mockk {
                    every { execute(any()) } returns flowOf(DOMAIN_GAMES)
                },
                GamesDiscoveryKey.Type.COMING_SOON to mockk {
                    every { execute(any()) } returns flowOf(DOMAIN_GAMES)
                },
                GamesDiscoveryKey.Type.MOST_ANTICIPATED to mockk {
                    every { execute(any()) } returns flowOf(DOMAIN_GAMES)
                }
            ),
            refreshGamesUseCasesMap = mapOf(
                GamesDiscoveryKey.Type.POPULAR to refreshPopularGamesUseCase,
                GamesDiscoveryKey.Type.RECENTLY_RELEASED to mockk {
                    every { execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))
                },
                GamesDiscoveryKey.Type.COMING_SOON to mockk {
                    every { execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))
                },
                GamesDiscoveryKey.Type.MOST_ANTICIPATED to mockk {
                    every { execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))
                }
            )
        )
    }

    @Test
    fun `Logs error when games observing use case throws error`() {
        runTest {
            every { observePopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }
            every { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty()
        }
    }

    @Test
    fun `Logs error when games refreshing use case throws error`() {
        runTest {
            every { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            every { refreshPopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

            SUT
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty()
        }
    }

    @Test
    fun `Dispatches toast showing command when games refreshing use case throws error`() {
        runTest {
            every { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            every { refreshPopularGamesUseCase.execute(any()) } returns flow { throw IllegalStateException("error") }

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

                assertThat(awaitItem()).isInstanceOf(GamesDiscoveryRoute.Search::class.java)
            }
        }
    }

    @Test
    fun `Routes to games category screen when more button is clicked`() {
        runTest {
            val categoryName = GamesDiscoveryCategory.POPULAR.name

            SUT.routeFlow.test {
                SUT.onCategoryMoreButtonClicked(categoryName)

                val route = awaitItem()

                assertThat(route).isInstanceOf(GamesDiscoveryRoute.Category::class.java)
                assertThat((route as GamesDiscoveryRoute.Category).category).isEqualTo(categoryName)
            }
        }
    }

    @Test
    fun `Routes to game info screen when game is clicked`() {
        runTest {
            val item = GamesDiscoveryItemGameUiModel(
                id = 1,
                title = "title",
                coverUrl = null
            )

            SUT.routeFlow.test {
                SUT.onCategoryGameClicked(item)

                val route = awaitItem()

                assertThat(route).isInstanceOf(GamesDiscoveryRoute.Info::class.java)
                assertThat((route as GamesDiscoveryRoute.Info).gameId).isEqualTo(item.id)
            }
        }
    }

    private class FakeGamesDiscoveryItemGameUiModelMapper : GamesDiscoveryItemGameUiModelMapper {

        override fun mapToUiModel(game: DomainGame): GamesDiscoveryItemGameUiModel {
            return GamesDiscoveryItemGameUiModel(
                id = game.id,
                title = game.name,
                coverUrl = null
            )
        }
    }
}
