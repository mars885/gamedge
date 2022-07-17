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

package com.paulrybitskyi.gamedge.feature.search.data

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.data.common.ErrorMapper
import com.paulrybitskyi.gamedge.common.data.games.datastores.GamesDataStores
import com.paulrybitskyi.gamedge.common.data.games.datastores.local.GamesLocalDataStore
import com.paulrybitskyi.gamedge.common.data.games.datastores.remote.GamesRemoteDataStore
import com.paulrybitskyi.gamedge.common.data.games.usecases.common.GameMapper
import com.paulrybitskyi.gamedge.common.data.games.usecases.common.mapToDomainGames
import com.paulrybitskyi.gamedge.common.testing.DATA_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.common.testing.DATA_GAMES
import com.paulrybitskyi.gamedge.common.testing.DOMAIN_PAGINATION
import com.paulrybitskyi.gamedge.common.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.common.testing.utils.coVerifyNotCalled
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.feature.search.domain.SearchGamesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val SEARCH_GAMES_USE_CASE_PARAMS = SearchGamesUseCase.Params(
    searchQuery = "search_query",
    pagination = DOMAIN_PAGINATION,
)

internal class SearchGamesUseCaseImplTest {

    @MockK private lateinit var gamesLocalDataStore: GamesLocalDataStore
    @MockK private lateinit var gamesRemoteDataStore: GamesRemoteDataStore
    @MockK private lateinit var networkStateProvider: NetworkStateProvider

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var gameMapper: GameMapper
    private lateinit var errorMapper: ErrorMapper
    private lateinit var SUT: SearchGamesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        testDispatcher = UnconfinedTestDispatcher()
        gameMapper = GameMapper()
        errorMapper = ErrorMapper()
        SUT = SearchGamesUseCaseImpl(
            gamesDataStores = GamesDataStores(
                local = gamesLocalDataStore,
                remote = gamesRemoteDataStore,
            ),
            dispatcherProvider = FakeDispatcherProvider(testDispatcher),
            networkStateProvider = networkStateProvider,
            gameMapper = gameMapper,
            errorMapper = errorMapper,
        )
    }

    @Test
    fun `Emits from remote data store when network is on`() {
        runTest(testDispatcher) {
            every { networkStateProvider.isNetworkAvailable } returns true
            coEvery { gamesRemoteDataStore.searchGames(any(), any()) } returns Ok(DATA_GAMES)

            SUT.execute(SEARCH_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(gameMapper.mapToDomainGames(DATA_GAMES))
                awaitComplete()
            }
        }
    }

    @Test
    fun `Saves remote games into local data store when network is on & request succeeds`() {
        runTest(testDispatcher) {
            every { networkStateProvider.isNetworkAvailable } returns true
            coEvery { gamesRemoteDataStore.searchGames(any(), any()) } returns Ok(DATA_GAMES)

            SUT.execute(SEARCH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerify { gamesLocalDataStore.saveGames(DATA_GAMES) }
        }
    }

    @Test
    fun `Does not save remote games into local data store when network is on & request fails`() {
        runTest(testDispatcher) {
            every { networkStateProvider.isNetworkAvailable } returns true
            coEvery { gamesRemoteDataStore.searchGames(any(), any()) } returns Err(DATA_ERROR_UNKNOWN)

            SUT.execute(SEARCH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { gamesLocalDataStore.saveGames(any()) }
        }
    }

    @Test
    fun `Emits from local data store when network is off`() {
        runTest(testDispatcher) {
            every { networkStateProvider.isNetworkAvailable } returns false
            coEvery { gamesLocalDataStore.searchGames(any(), any()) } returns DATA_GAMES

            SUT.execute(SEARCH_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(gameMapper.mapToDomainGames(DATA_GAMES))
                awaitComplete()
            }
        }
    }
}
