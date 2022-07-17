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

package com.paulrybitskyi.gamedge.feature.likes.data

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.DATA_GAMES
import com.paulrybitskyi.gamedge.common.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.common.data.games.datastores.local.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.common.data.games.usecases.common.GameMapper
import com.paulrybitskyi.gamedge.common.data.games.usecases.common.mapToDomainGames
import com.paulrybitskyi.gamedge.common.domain.games.common.ObserveGamesUseCaseParams
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val OBSERVE_GAMES_USE_CASE_PARAMS = ObserveGamesUseCaseParams()

internal class ObserveLikedGamesUseCaseImplTest {

    @MockK private lateinit var likedGamesLocalDataStore: LikedGamesLocalDataStore

    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: ObserveLikedGamesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        gameMapper = GameMapper()
        SUT = ObserveLikedGamesUseCaseImpl(
            likedGamesLocalDataStore = likedGamesLocalDataStore,
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = gameMapper
        )
    }

    @Test
    fun `Emits liked games successfully`() {
        runTest {
            every { likedGamesLocalDataStore.observeLikedGames(any()) } returns flowOf(DATA_GAMES)

            SUT.execute(OBSERVE_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem()).isEqualTo(gameMapper.mapToDomainGames(DATA_GAMES))
                awaitComplete()
            }
        }
    }
}
