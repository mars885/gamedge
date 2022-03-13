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

package com.paulrybitskyi.gamedge.data.games

import app.cash.turbine.test
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.commons.testing.DATA_GAMES
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.GET_SIMILAR_GAMES_USE_CASE_PARAMS
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.GetSimilarGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.domain.games.usecases.RefreshSimilarGamesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class GetSimilarGamesUseCaseImplTest {

    @MockK private lateinit var refreshSimilarGamesUseCase: RefreshSimilarGamesUseCase
    @MockK private lateinit var gamesLocalDataStore: GamesLocalDataStore

    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: GetSimilarGamesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        gameMapper = GameMapper()
        SUT = GetSimilarGamesUseCaseImpl(
            refreshSimilarGamesUseCase = refreshSimilarGamesUseCase,
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = gameMapper
        )
    }

    @Test
    fun `Emits games that refresh use case successfully emits`() {
        runTest {
            coEvery { refreshSimilarGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            SUT.execute(GET_SIMILAR_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem()).isEqualTo(DOMAIN_GAMES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Emits games from local data store if refresh use case does not emit`() {
        runTest {
            coEvery { refreshSimilarGamesUseCase.execute(any()) } returns flowOf()
            coEvery { gamesLocalDataStore.getSimilarGames(any(), any()) } returns DATA_GAMES

            SUT.execute(GET_SIMILAR_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem()).isEqualTo(gameMapper.mapToDomainGames(DATA_GAMES))
                awaitComplete()
            }
        }
    }
}
