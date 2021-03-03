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
import com.paulrybitskyi.gamedge.data.commons.Pagination
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.GetCompanyDevelopedGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.data.games.utils.DATA_GAMES
import com.paulrybitskyi.gamedge.data.games.utils.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.domain.commons.DomainPagination
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.gamedge.domain.commons.entities.Error
import com.paulrybitskyi.gamedge.domain.games.DomainCategory
import com.paulrybitskyi.gamedge.domain.games.DomainCompany
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.usecases.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.RefreshCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.RefreshCompanyDevelopedGamesUseCase.Params
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val DOMAIN_GAME = DomainGame(
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
    category = DomainCategory.UNKNOWN,
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
private val DOMAIN_GAMES = listOf(
    DOMAIN_GAME.copy(id = 1),
    DOMAIN_GAME.copy(id = 2),
    DOMAIN_GAME.copy(id = 3)
)

private val DOMAIN_COMPANY = DomainCompany(
    id = 1,
    name = "name",
    websiteUrl = "url",
    logo = null,
    developedGames = listOf(1, 2, 3)
)

private val USE_CASE_PARAMS = GetCompanyDevelopedGamesUseCase.Params(DOMAIN_COMPANY, DomainPagination())


internal class GetCompanyDevelopedGamesUseCaseTest {


    private lateinit var refreshCompanyDevelopedGamesUseCase: FakeRefreshCompanyDevelopedGamesUseCase
    private lateinit var gamesLocalDataStore: FakeGamesLocalDataStore
    private lateinit var gameMapper: GameMapper
    private lateinit var SUT: GetCompanyDevelopedGamesUseCaseImpl


    @Before
    fun setup() {
        refreshCompanyDevelopedGamesUseCase = FakeRefreshCompanyDevelopedGamesUseCase()
        gamesLocalDataStore = FakeGamesLocalDataStore()
        gameMapper = GameMapper()
        SUT = GetCompanyDevelopedGamesUseCaseImpl(
            refreshCompanyDevelopedGamesUseCase = refreshCompanyDevelopedGamesUseCase,
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = gameMapper
        )
    }


    @Test
    fun `Emits games that refresh use case successfully emits`() = runBlockingTest {
        refreshCompanyDevelopedGamesUseCase.shouldReturnGames = true

        assertEquals(
            DOMAIN_GAMES,
            SUT.execute(USE_CASE_PARAMS).first()
        )
    }


    @Test
    fun `Emits games from local data store if refresh use case does not emit`() = runBlockingTest {
        refreshCompanyDevelopedGamesUseCase.shouldReturnEmptyFlow = true

        assertEquals(
            gameMapper.mapToDomainGames(DATA_GAMES),
            SUT.execute(USE_CASE_PARAMS).first()
        )
    }


    private class FakeRefreshCompanyDevelopedGamesUseCase : RefreshCompanyDevelopedGamesUseCase {

        var shouldReturnGames = false
        var shouldReturnEmptyFlow = false
        var shouldReturnError = false

        override suspend fun execute(params: Params): Flow<DomainResult<List<Game>>> {
            return when {
                shouldReturnGames -> flowOf(Ok(DOMAIN_GAMES))
                shouldReturnEmptyFlow -> flowOf()
                shouldReturnError -> flowOf(Err(Error.Unknown("error")))

                else -> throw IllegalStateException()
            }
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
            pagination: Pagination
        ): List<DataGame> {
            return DATA_GAMES
        }

        override suspend fun getSimilarGames(
            game: DataGame,
            pagination: Pagination
        ): List<DataGame> {
            return emptyList() // no-op
        }

        override suspend fun searchGames(
            searchQuery: String,
            pagination: Pagination
        ): List<DataGame> {
            return emptyList() // no-op
        }

        override suspend fun observePopularGames(pagination: Pagination): Flow<List<DataGame>> {
            return flowOf() // no-op
        }

        override suspend fun observeRecentlyReleasedGames(pagination: Pagination): Flow<List<DataGame>> {
            return flowOf() // no-op
        }

        override suspend fun observeComingSoonGames(pagination: Pagination): Flow<List<DataGame>> {
            return flowOf() // no-op
        }

        override suspend fun observeMostAnticipatedGames(pagination: Pagination): Flow<List<DataGame>> {
            return flowOf() // no-op
        }

    }


}