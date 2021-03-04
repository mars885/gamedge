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

import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.GetGameUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.utils.DATA_GAME
import com.paulrybitskyi.gamedge.data.games.utils.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.domain.commons.entities.Error
import com.paulrybitskyi.gamedge.domain.games.usecases.GetGameUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val USE_CASE_PARAMS = GetGameUseCase.Params(gameId = 100)


internal class GetGameUseCaseImplTest {


    private lateinit var gamesLocalDataStore: FakeGamesLocalDataStore
    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: GetGameUseCaseImpl


    @Before
    fun setup() {
        gamesLocalDataStore = FakeGamesLocalDataStore()
        gameMapper = GameMapper()
        SUT = GetGameUseCaseImpl(
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = gameMapper
        )
    }


    @Test
    fun `Emits game successfully`() {
        runBlockingTest {
            gamesLocalDataStore.shouldReturnGame = true

            assertEquals(
                gameMapper.mapToDomainGame(DATA_GAME),
                SUT.execute(USE_CASE_PARAMS).first().get()
            )
        }
    }


    @Test
    fun `Emits not found error if game ID does not reference existing game`() {
        runBlockingTest {
            gamesLocalDataStore.shouldReturnGame = false

            val error = SUT.execute(USE_CASE_PARAMS).first().getError()

            assertTrue(error is Error.NotFound)
        }
    }


    private class FakeGamesLocalDataStore : GamesLocalDataStore {

        var shouldReturnGame = false

        override suspend fun saveGames(games: List<DataGame>) {
            // no-op
        }

        override suspend fun getGame(id: Int): DataGame? {
            return (if(shouldReturnGame) DATA_GAME else null)
        }

        override suspend fun getCompanyDevelopedGames(
            company: DataCompany,
            pagination: DataPagination
        ): List<DataGame> {
            return emptyList() // no-op
        }

        override suspend fun getSimilarGames(
            game: DataGame,
            pagination: DataPagination
        ): List<DataGame> {
            return emptyList() // no-op
        }

        override suspend fun searchGames(
            searchQuery: String,
            pagination: DataPagination
        ): List<DataGame> {
            return emptyList() // no-op
        }

        override suspend fun observePopularGames(pagination: DataPagination): Flow<List<DataGame>> {
            return flowOf() // no-op
        }

        override suspend fun observeRecentlyReleasedGames(pagination: DataPagination): Flow<List<DataGame>> {
            return flowOf() // no-op
        }

        override suspend fun observeComingSoonGames(pagination: DataPagination): Flow<List<DataGame>> {
            return flowOf() // no-op
        }

        override suspend fun observeMostAnticipatedGames(pagination: DataPagination): Flow<List<DataGame>> {
            return flowOf() // no-op
        }

    }


}