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

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.commons.testing.*
import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.GetSimilarGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.domain.commons.DomainPagination
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.gamedge.domain.commons.entities.Error
import com.paulrybitskyi.gamedge.domain.games.DomainCategory
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.RefreshSimilarGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.RefreshSimilarGamesUseCase.Params
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
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
        runBlockingTest {
            coEvery { refreshSimilarGamesUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAMES))

            assertThat(SUT.execute(GET_SIMILAR_GAMES_USE_CASE_PARAMS).first())
                .isEqualTo(DOMAIN_GAMES)
        }
    }


    @Test
    fun `Emits games from local data store if refresh use case does not emit`() {
        runBlockingTest {
            coEvery { refreshSimilarGamesUseCase.execute(any()) } returns flowOf()
            coEvery { gamesLocalDataStore.getSimilarGames(any(), any()) } returns DATA_GAMES

            assertThat(SUT.execute(GET_SIMILAR_GAMES_USE_CASE_PARAMS).first())
                .isEqualTo(gameMapper.mapToDomainGames(DATA_GAMES))
        }
    }


}