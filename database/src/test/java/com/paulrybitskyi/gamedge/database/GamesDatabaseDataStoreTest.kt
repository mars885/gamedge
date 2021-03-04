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

package com.paulrybitskyi.gamedge.database

import com.paulrybitskyi.gamedge.commons.data.QueryTimestampProvider
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.games.DataCategory
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.database.games.DatabaseGame
import com.paulrybitskyi.gamedge.database.games.datastores.GameMapper
import com.paulrybitskyi.gamedge.database.games.datastores.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.database.games.datastores.mapToDatabaseGames
import com.paulrybitskyi.gamedge.database.games.tables.GamesTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
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

private val DATA_PAGINATION = DataPagination(offset = 0, limit = 10)


internal class GamesDatabaseDataStoreTest {


    private lateinit var gamesTable: FakeGamesTable
    private lateinit var gameMapper: FakeGameMapper
    private lateinit var SUT: GamesDatabaseDataStore


    @Before
    fun setup() {
        gamesTable = FakeGamesTable()
        gameMapper = FakeGameMapper()
        SUT = GamesDatabaseDataStore(
            gamesTable = gamesTable,
            dispatcherProvider = FakeDispatcherProvider(),
            queryTimestampProvider = FakeQueryTimestampProvider(),
            gameMapper = gameMapper
        )
    }


    @Test
    fun `Saves games to table successfully`() {
        runBlockingTest {
            SUT.saveGames(DATA_GAMES)

            assertEquals(
                gameMapper.mapToDatabaseGames(DATA_GAMES),
                gamesTable.games
            )
        }
    }


    @Test
    fun `Retrieves game successfully`() {
        runBlockingTest {
            gamesTable.shouldReturnGame = true

            SUT.saveGames(DATA_GAMES)

            val game = DATA_GAMES.first()

            assertEquals(game, SUT.getGame(game.id))
        }
    }


    @Test
    fun `Retrieves null instead of game`() {
        runBlockingTest {
            gamesTable.shouldNotReturnGame = true

            assertNull(SUT.getGame(1))
        }
    }


    @Test
    fun `Retrieves company developed games successfully`() {
        runBlockingTest {
            SUT.saveGames(DATA_GAMES)

            val company = DataCompany(
                id = 1,
                name = "name",
                websiteUrl = "url",
                logo = null,
                developedGames = listOf()
            )

            assertEquals(DATA_GAMES, SUT.getCompanyDevelopedGames(company, DATA_PAGINATION))
        }
    }


    @Test
    fun `Retrieves similar games successfully`() {
        runBlockingTest {
            SUT.saveGames(DATA_GAMES)

            assertEquals(
                DATA_GAMES,
                SUT.getSimilarGames(DATA_GAME, DATA_PAGINATION)
            )
        }
    }


    @Test
    fun `Searches games successfully`() {
        runBlockingTest {
            SUT.saveGames(DATA_GAMES)

            assertEquals(
                DATA_GAMES,
                SUT.searchGames("", DATA_PAGINATION)
            )
        }
    }


    @Test
    fun `Observes popular games successfully`() {
        runBlockingTest {
            SUT.saveGames(DATA_GAMES)

            assertEquals(
                DATA_GAMES,
                SUT.observePopularGames(DATA_PAGINATION).first()
            )
        }
    }


    @Test
    fun `Observes recently released games successfully`() {
        runBlockingTest {
            SUT.saveGames(DATA_GAMES)

            assertEquals(
                DATA_GAMES,
                SUT.observeRecentlyReleasedGames(DATA_PAGINATION).first()
            )
        }
    }


    @Test
    fun `Observes coming soon games successfully`() {
        runBlockingTest {
            SUT.saveGames(DATA_GAMES)

            assertEquals(
                DATA_GAMES,
                SUT.observeComingSoonGames(DATA_PAGINATION).first()
            )
        }
    }


    @Test
    fun `Observes most anticipated games successfully`() {
        runBlockingTest {
            SUT.saveGames(DATA_GAMES)

            assertEquals(
                DATA_GAMES,
                SUT.observeMostAnticipatedGames(DATA_PAGINATION).first()
            )
        }
    }


    private class FakeGamesTable : GamesTable {

        var shouldReturnGame = false
        var shouldNotReturnGame = false

        var games = listOf<DatabaseGame>()

        override suspend fun saveGames(games: List<DatabaseGame>) {
            this.games = games
        }

        override suspend fun getGame(id: Int): DatabaseGame? {
            return (if(shouldReturnGame) this.games.first() else null)
        }

        override suspend fun getGames(ids: List<Int>, offset: Int, limit: Int): List<DatabaseGame> {
            return this.games
        }

        override suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): List<DatabaseGame> {
            return this.games
        }

        override fun observePopularGames(
            minReleaseDateTimestamp: Long,
            offset: Int,
            limit: Int
        ): Flow<List<DatabaseGame>> {
            return flowOf(this.games)
        }

        override fun observeRecentlyReleasedGames(
            minReleaseDateTimestamp: Long,
            maxReleaseDateTimestamp: Long,
            offset: Int,
            limit: Int
        ): Flow<List<DatabaseGame>> {
            return flowOf(this.games)
        }

        override fun observeComingSoonGames(
            minReleaseDateTimestamp: Long,
            offset: Int,
            limit: Int
        ): Flow<List<DatabaseGame>> {
            return flowOf(this.games)
        }

        override fun observeMostAnticipatedGames(
            minReleaseDateTimestamp: Long,
            offset: Int,
            limit: Int
        ): Flow<List<DatabaseGame>> {
            return flowOf(this.games)
        }

    }


    private class FakeDispatcherProvider(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
        override val main: CoroutineDispatcher = testDispatcher,
        override val io: CoroutineDispatcher = testDispatcher,
        override val computation: CoroutineDispatcher = testDispatcher
    ) : DispatcherProvider


    private class FakeQueryTimestampProvider : QueryTimestampProvider {

        override fun getPopularGamesMinReleaseDate(): Long = 500L
        override fun getRecentlyReleasedGamesMinReleaseDate(): Long = 500L
        override fun getRecentlyReleasedGamesMaxReleaseDate(): Long = 500L
        override fun getComingSoonGamesMinReleaseDate(): Long = 500L
        override fun getMostAnticipatedGamesMinReleaseDate(): Long = 500L

    }


    private class FakeGameMapper : GameMapper {

        override fun mapToDatabaseGame(dataGame: DataGame): DatabaseGame {
            return DatabaseGame(
                id = dataGame.id,
                followerCount = dataGame.followerCount,
                hypeCount = dataGame.hypeCount,
                releaseDate = dataGame.releaseDate,
                criticsRating = dataGame.criticsRating,
                usersRating = dataGame.usersRating,
                totalRating = dataGame.totalRating,
                name = dataGame.name,
                summary = dataGame.summary,
                storyline = dataGame.storyline,
                category = "category",
                cover = "cover",
                releaseDates = "release_dates",
                ageRatings = "age_ratings",
                videos = "videos",
                artworks = "artworks",
                screenshots = "screenshots",
                genres = "genres",
                platforms = "platforms",
                playerPerspectives = "player_perspectives",
                themes = "themes",
                modes = "modes",
                keywords = "keywords",
                involvedCompanies = "involved_companies",
                websites = "websites",
                similarGames = "similar_games"
            )
        }

        override fun mapToDataGame(databaseGame: DatabaseGame): DataGame {
            return DataGame(
                id = databaseGame.id,
                followerCount = databaseGame.followerCount,
                hypeCount = databaseGame.hypeCount,
                releaseDate = databaseGame.releaseDate,
                criticsRating = databaseGame.criticsRating,
                usersRating = databaseGame.usersRating,
                totalRating = databaseGame.totalRating,
                name = databaseGame.name,
                summary = databaseGame.summary,
                storyline = databaseGame.storyline,
                category = DataCategory.UNKNOWN,
                cover = null,
                releaseDates = emptyList(),
                ageRatings = emptyList(),
                videos = emptyList(),
                artworks = emptyList(),
                screenshots = emptyList(),
                genres = emptyList(),
                platforms = emptyList(),
                playerPerspectives = emptyList(),
                themes = emptyList(),
                modes = emptyList(),
                keywords = emptyList(),
                involvedCompanies = emptyList(),
                websites = emptyList(),
                similarGames = emptyList()
            )
        }

    }


}