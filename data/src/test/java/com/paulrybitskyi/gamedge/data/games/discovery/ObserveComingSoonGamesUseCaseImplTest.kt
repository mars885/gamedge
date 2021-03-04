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
import com.paulrybitskyi.gamedge.data.games.DataCategory
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.data.games.usecases.discovery.ObserveComingSoonGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.utils.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test


private val DATA_GAME = DataGame(
    id = 1,
    followerCount = null,
    hypeCount = null,
    releaseDate = null,
    criticsRating = null,
    usersRating = null,
    totalRating = null,
    name = "name",
    summary = null,
    storyline = null,
    category = DataCategory.UNKNOWN,
    cover = null,
    releaseDates = listOf(),
    ageRatings = listOf(),
    videos = listOf(),
    artworks = listOf(),
    screenshots = listOf(),
    genres = listOf(),
    platforms = listOf(),
    playerPerspectives = listOf(),
    themes = listOf(),
    modes = listOf(),
    keywords = listOf(),
    involvedCompanies = listOf(),
    websites = listOf(),
    similarGames = listOf()
)
private val DATA_GAMES = listOf(
    DATA_GAME.copy(id = 1),
    DATA_GAME.copy(id = 2),
    DATA_GAME.copy(id = 3)
)


internal class ObserveComingSoonGamesUseCaseImplTest {


    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: ObserveComingSoonGamesUseCaseImpl


    @Before
    fun setup() {
        gameMapper = GameMapper()
        SUT = ObserveComingSoonGamesUseCaseImpl(
            gamesLocalDataStore = FakeGamesLocalDataStore(),
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = gameMapper
        )
    }


    @Test
    fun `Emits games successfully`() {
        runBlockingTest {
            assertThat(SUT.execute(ObserveGamesUseCaseParams()).first())
                .isEqualTo(gameMapper.mapToDomainGames(DATA_GAMES))
        }
    }


    private class FakeGamesLocalDataStore : GamesLocalDataStore {

        override suspend fun saveGames(games: List<DataGame>) {
            // no-op
        }

        override suspend fun getGame(id: Int): DataGame? {
            return null // no-op
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
            return flowOf(DATA_GAMES)
        }

        override suspend fun observeMostAnticipatedGames(pagination: DataPagination): Flow<List<DataGame>> {
            return flowOf() // no-op
        }

    }


}