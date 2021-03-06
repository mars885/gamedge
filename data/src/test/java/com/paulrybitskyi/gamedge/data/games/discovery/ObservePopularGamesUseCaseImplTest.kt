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

package com.paulrybitskyi.gamedge.data.games.discovery

import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.data.games.usecases.discovery.ObservePopularGamesUseCaseImpl
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.commons.testing.DATA_GAMES
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
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

internal class ObservePopularGamesUseCaseImplTest {


    @MockK private lateinit var gamesLocalDataStore: GamesLocalDataStore

    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: ObservePopularGamesUseCaseImpl


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        gameMapper = GameMapper()
        SUT = ObservePopularGamesUseCaseImpl(
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = gameMapper
        )
    }


    @Test
    fun `Emits games successfully`() {
        runBlockingTest {
            coEvery { gamesLocalDataStore.observePopularGames(any()) } returns flowOf(DATA_GAMES)

            assertThat(SUT.execute(ObserveGamesUseCaseParams()).first())
                .isEqualTo(gameMapper.mapToDomainGames(DATA_GAMES))
        }
    }


}