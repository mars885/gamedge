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

package com.paulrybitskyi.gamedge.feature.info.domain

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAME
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.common.domain.common.DomainException
import com.paulrybitskyi.gamedge.common.testing.domain.MainCoroutineRule
import com.paulrybitskyi.gamedge.feature.info.GAME_INFO
import com.paulrybitskyi.gamedge.feature.info.INVOLVED_COMPANY
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameInfoUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameInfoUseCaseImpl
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.likes.ObserveGameLikeStateUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val USE_CASE_PARAMS = GetGameInfoUseCase.Params(gameId = 0)

internal class GetGameInfoUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK private lateinit var getGameUseCase: GetGameUseCase
    @MockK private lateinit var observeGameLikeStateUseCase: ObserveGameLikeStateUseCase
    @MockK private lateinit var getCompanyDevelopedGamesUseCase: GetCompanyDevelopedGamesUseCase
    @MockK private lateinit var getSimilarGamesUseCase: GetSimilarGamesUseCase

    private lateinit var SUT: GetGameInfoUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = GetGameInfoUseCaseImpl(
            getGameUseCase = getGameUseCase,
            observeGameLikeStateUseCase = observeGameLikeStateUseCase,
            getCompanyDevelopedGamesUseCase = getCompanyDevelopedGamesUseCase,
            getSimilarGamesUseCase = getSimilarGamesUseCase,
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
        )
    }

    @Test
    fun `Emits game info successfully`() {
        runTest {
            val game = DOMAIN_GAME.copy(
                involvedCompanies = listOf(INVOLVED_COMPANY.copy(isDeveloper = true)),
                similarGames = listOf(1, 2, 3),
            )
            val expectedGameInfo = GAME_INFO.copy(game = game)

            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(game))
            every { observeGameLikeStateUseCase.execute(any()) } returns flowOf(true)
            coEvery { getCompanyDevelopedGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)
            coEvery { getSimilarGamesUseCase.execute(any()) } returns flowOf(DOMAIN_GAMES)

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitItem()).isEqualTo(expectedGameInfo)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Emits error when game retrieval fails`() {
        runTest {
            coEvery { getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_UNKNOWN))

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitError()).isInstanceOf(DomainException::class.java)
            }
        }
    }

    @Test
    fun `Emits game info with empty company games`() {
        runTest {
            val game = DOMAIN_GAME.copy(involvedCompanies = emptyList())

            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(game))
            every { observeGameLikeStateUseCase.execute(any()) } returns flowOf(true)

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitItem().companyGames).isEmpty()
                awaitComplete()
            }
        }
    }

    @Test
    fun `Emits game info with empty similar games`() {
        runTest {
            val game = DOMAIN_GAME.copy(similarGames = emptyList())

            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(game))
            every { observeGameLikeStateUseCase.execute(any()) } returns flowOf(true)

            SUT.execute(USE_CASE_PARAMS).test {
                assertThat(awaitItem().similarGames).isEmpty()
                awaitComplete()
            }
        }
    }
}
