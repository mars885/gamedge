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
import com.paulrybitskyi.gamedge.commons.testing.*
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.*
import com.paulrybitskyi.gamedge.feature.discovery.di.GamesDiscoveryKey
import com.paulrybitskyi.gamedge.feature.discovery.mapping.GamesDiscoveryItemGameModelMapper
import com.paulrybitskyi.gamedge.feature.discovery.mapping.GamesDiscoveryItemModelFactoryImpl
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemGameModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class GamesDiscoveryViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK private lateinit var observePopularGamesUseCase: ObservePopularGamesUseCase
    @MockK private lateinit var refreshPopularGamesUseCase: RefreshPopularGamesUseCase

    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamesDiscoveryViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        SUT = GamesDiscoveryViewModel(
            useCases = setupUseCases(),
            itemModelFactory = GamesDiscoveryItemModelFactoryImpl(FakeStringProvider()),
            itemGameModelMapper = FakeGamesDiscoveryItemGameModelMapper(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger
        )
    }


    private fun setupUseCases(): GamesDiscoveryUseCases {
        return GamesDiscoveryUseCases(
            observeGamesUseCasesMap = mapOf(
                GamesDiscoveryKey.Type.POPULAR to observePopularGamesUseCase,
                GamesDiscoveryKey.Type.RECENTLY_RELEASED to mockk {
                    coEvery { execute(any()) } returns flowOf(DOMAIN_GAMES)
                },
                GamesDiscoveryKey.Type.COMING_SOON to mockk {
                    coEvery { execute(any()) } returns flowOf(DOMAIN_GAMES)
                },
                GamesDiscoveryKey.Type.MOST_ANTICIPATED to mockk {
                    coEvery { execute(any()) } returns flowOf(DOMAIN_GAMES)
                }
            ),
            refreshGamesUseCasesMap = mapOf(
                GamesDiscoveryKey.Type.POPULAR to refreshPopularGamesUseCase,
                GamesDiscoveryKey.Type.RECENTLY_RELEASED to mockk {
                    coEvery { execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))
                },
                GamesDiscoveryKey.Type.COMING_SOON to mockk {
                    coEvery { execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))
                },
                GamesDiscoveryKey.Type.MOST_ANTICIPATED to mockk {
                    coEvery { execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))
                }
            )
        )
    }


    @Test
    fun `Logs error when games observing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flow { throw Exception("error") }
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.loadData()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Logs error when games refreshing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flow { throw Exception("error") }

            SUT.loadData()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Dispatches toast showing command when games refreshing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { observePopularGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { refreshPopularGamesUseCase.execute(any()) } returns flow { throw Exception("error") }

            SUT.commandFlow.test {
                SUT.loadData()

                val command = expectItem()

                assertThat(command is GeneralCommand.ShowLongToast).isTrue
            }
        }
    }


    @Test
    fun `Routes to games category screen when more button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            SUT.routeFlow.test {
                SUT.onCategoryMoreButtonClicked("popular")

                val route = expectItem()

                assertThat(route is GamesDiscoveryRoute.Category).isTrue
                assertThat((route as GamesDiscoveryRoute.Category).category)
                    .isEqualTo("popular")
            }
        }
    }


    @Test
    fun `Routes to game info screen when game is clicked`() {
        mainCoroutineRule.runBlockingTest {
            val item = GamesDiscoveryItemGameModel(
                id = 1,
                title = "title",
                coverUrl = null
            )

            SUT.routeFlow.test {
                SUT.onCategoryGameClicked(item)

                val route = expectItem()

                assertThat(route is GamesDiscoveryRoute.Info).isTrue
                assertThat((route as GamesDiscoveryRoute.Info).gameId)
                    .isEqualTo(item.id)
            }
        }
    }


    private class FakeGamesDiscoveryItemGameModelMapper : GamesDiscoveryItemGameModelMapper {

        override fun mapToGameModel(game: DomainGame): GamesDiscoveryItemGameModel {
            return GamesDiscoveryItemGameModel(
                id = game.id,
                title = game.name,
                coverUrl = null
            )
        }

    }


}