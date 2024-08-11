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

package com.paulrybitskyi.gamedge.feature.search.domain

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.domain.games.datastores.GamesDataStores
import com.paulrybitskyi.gamedge.common.domain.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.common.domain.games.datastores.GamesRemoteDataStore
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.common.testing.domain.MainCoroutineRule
import com.paulrybitskyi.gamedge.common.testing.domain.PAGINATION
import com.paulrybitskyi.gamedge.common.testing.domain.coVerifyNotCalled
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val SEARCH_GAMES_USE_CASE_PARAMS = SearchGamesUseCase.Params(
    searchQuery = "search_query",
    pagination = PAGINATION,
)

internal class SearchGamesUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK private lateinit var gamesLocalDataStore: GamesLocalDataStore
    @MockK private lateinit var gamesRemoteDataStore: GamesRemoteDataStore
    @MockK private lateinit var networkStateProvider: NetworkStateProvider

    private lateinit var SUT: SearchGamesUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        SUT = SearchGamesUseCaseImpl(
            gamesDataStores = GamesDataStores(
                local = gamesLocalDataStore,
                remote = gamesRemoteDataStore,
            ),
            dispatcherProvider = mainCoroutineRule.dispatcherProvider,
            networkStateProvider = networkStateProvider,
        )
    }

    @Test
    fun `Emits from remote data store when network is on`() {
        runTest {
            every { networkStateProvider.isNetworkAvailable } returns true
            coEvery { gamesRemoteDataStore.searchGames(any(), any()) } returns Ok(DOMAIN_GAMES)

            SUT.execute(SEARCH_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(DOMAIN_GAMES)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Saves remote games into local data store when network is on & request succeeds`() {
        runTest {
            every { networkStateProvider.isNetworkAvailable } returns true
            coEvery { gamesRemoteDataStore.searchGames(any(), any()) } returns Ok(DOMAIN_GAMES)

            SUT.execute(SEARCH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerify { gamesLocalDataStore.saveGames(DOMAIN_GAMES) }
        }
    }

    @Test
    fun `Does not save remote games into local data store when network is on & request fails`() {
        runTest {
            every { networkStateProvider.isNetworkAvailable } returns true
            coEvery { gamesRemoteDataStore.searchGames(any(), any()) } returns Err(DOMAIN_ERROR_UNKNOWN)

            SUT.execute(SEARCH_GAMES_USE_CASE_PARAMS).firstOrNull()

            coVerifyNotCalled { gamesLocalDataStore.saveGames(any()) }
        }
    }

    @Test
    fun `Emits from local data store when network is off`() {
        runTest {
            every { networkStateProvider.isNetworkAvailable } returns false
            coEvery { gamesLocalDataStore.searchGames(any(), any()) } returns DOMAIN_GAMES

            SUT.execute(SEARCH_GAMES_USE_CASE_PARAMS).test {
                assertThat(awaitItem().get()).isEqualTo(DOMAIN_GAMES)
                awaitComplete()
            }
        }
    }
}
