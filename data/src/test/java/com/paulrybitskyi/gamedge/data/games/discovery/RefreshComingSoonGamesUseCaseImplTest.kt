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

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.commons.testing.DATA_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.commons.testing.DATA_GAMES
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.REFRESH_GAMES_USE_CASE_PARAMS
import com.paulrybitskyi.gamedge.commons.testing.utils.coVerifyNotCalled
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.data.games.datastores.GamesDataStores
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.GamesRemoteDataStore
import com.paulrybitskyi.gamedge.data.games.discovery.utils.FakeGamesRefreshingThrottlerKeyProvider
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.RefreshGamesUseCaseMappers
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.data.games.usecases.commons.throttling.GamesRefreshingThrottler
import com.paulrybitskyi.gamedge.data.games.usecases.commons.throttling.GamesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.data.games.usecases.discovery.RefreshComingSoonGamesUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class RefreshComingSoonGamesUseCaseImplTest {

    @MockK private lateinit var gamesLocalDataStore: GamesLocalDataStore
    @MockK private lateinit var gamesRemoteDataStore: GamesRemoteDataStore
    @MockK private lateinit var throttler: GamesRefreshingThrottler

    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: RefreshComingSoonGamesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        gameMapper = GameMapper()
        SUT = RefreshComingSoonGamesUseCaseImpl(
            gamesDataStores = GamesDataStores(
                local = gamesLocalDataStore,
                remote = gamesRemoteDataStore
            ),
            dispatcherProvider = FakeDispatcherProvider(),
            throttlerTools = GamesRefreshingThrottlerTools(
                throttler = throttler,
                keyProvider = FakeGamesRefreshingThrottlerKeyProvider()
            ),
            mappers = RefreshGamesUseCaseMappers(
                game = gameMapper,
                error = ErrorMapper()
            )
        )
    }

    @Test
    fun `Emits remote games when refresh is possible`() {
        runTest {
            coEvery { throttler.canRefreshGames(any()) } returns true
            coEvery { gamesRemoteDataStore.getComingSoonGames(any()) } returns Ok(DATA_GAMES)

            SUT.execute(REFRESH_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(gameMapper.mapToDomainGames(DATA_GAMES))
                awaitComplete()
            }
        }
    }

    @Test
    fun `Does not emit remote games when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshGames(any()) } returns false

            SUT.execute(REFRESH_GAMES_USE_CASE_PARAMS).test {
                awaitComplete()
            }
        }
    }

    @Test
    fun `Saves remote games into local data store when refresh is successful`() {
        runTest {
            coEvery { throttler.canRefreshGames(any()) } returns true
            coEvery { gamesRemoteDataStore.getComingSoonGames(any()) } returns Ok(DATA_GAMES)

            SUT.execute(REFRESH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerify { gamesLocalDataStore.saveGames(DATA_GAMES) }
        }
    }

    @Test
    fun `Does not save remote games into local data store when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshGames(any()) } returns false

            SUT.execute(REFRESH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { gamesLocalDataStore.saveGames(any()) }
        }
    }

    @Test
    fun `Does not save remote games into local data store when refresh is unsuccessful`() {
        runTest {
            coEvery { throttler.canRefreshGames(any()) } returns false
            coEvery { gamesRemoteDataStore.getComingSoonGames(any()) } returns Err(DATA_ERROR_UNKNOWN)

            SUT.execute(REFRESH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { gamesLocalDataStore.saveGames(any()) }
        }
    }

    @Test
    fun `Updates games last refresh time when refresh is successful`() {
        runTest {
            coEvery { throttler.canRefreshGames(any()) } returns true
            coEvery { gamesRemoteDataStore.getComingSoonGames(any()) } returns Ok(DATA_GAMES)

            SUT.execute(REFRESH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerify { throttler.updateGamesLastRefreshTime(any()) }
        }
    }

    @Test
    fun `Does not update games last refresh time when refresh is not possible`() {
        runTest {
            coEvery { throttler.canRefreshGames(any()) } returns false

            SUT.execute(REFRESH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { throttler.updateGamesLastRefreshTime(any()) }
        }
    }

    @Test
    fun `Does not update games last refresh time when refresh is unsuccessful`() {
        runTest {
            coEvery { throttler.canRefreshGames(any()) } returns false
            coEvery { gamesRemoteDataStore.getComingSoonGames(any()) } returns Err(DATA_ERROR_UNKNOWN)

            SUT.execute(REFRESH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { throttler.updateGamesLastRefreshTime(any()) }
        }
    }
}
