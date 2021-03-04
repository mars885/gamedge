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

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.games.DataCategory
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.database.games.DatabaseGame
import com.paulrybitskyi.gamedge.database.games.datastores.GameMapper
import com.paulrybitskyi.gamedge.database.games.datastores.LikedGameFactory
import com.paulrybitskyi.gamedge.database.games.datastores.LikedGamesDatabaseDataStore
import com.paulrybitskyi.gamedge.database.games.entities.LikedGame
import com.paulrybitskyi.gamedge.database.games.tables.LikedGamesTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test


private val DATABASE_GAME = DatabaseGame(
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
private val DATABASE_GAMES = listOf(
    DATABASE_GAME.copy(id = 1),
    DATABASE_GAME.copy(id = 2),
    DATABASE_GAME.copy(id = 3)
)


internal class LikedGamesDatabaseDataStoreTest {


    private lateinit var likedGamesTable: FakeLikedGamesTable
    private lateinit var gameMapper: FakeGameMapper
    private lateinit var SUT: LikedGamesDatabaseDataStore


    @Before
    fun setup() {
        likedGamesTable = FakeLikedGamesTable()
        SUT = LikedGamesDatabaseDataStore(
            likedGamesTable = likedGamesTable,
            likedGameFactory = FakeLikedGameFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            gameMapper = FakeGameMapper()
        )
    }


    @Test
    fun `Likes game successfully`() {
        runBlockingTest {
            val gameId = 100

            SUT.likeGame(gameId)

            assertThat(likedGamesTable.isGameLiked(gameId)).isTrue
        }
    }


    @Test
    fun `Unlikes game successfully`() {
        runBlockingTest {
            val gameId = 100

            SUT.likeGame(gameId)
            SUT.unlikeGame(gameId)

            assertThat(likedGamesTable.isGameLiked(gameId)).isFalse
        }
    }


    @Test
    fun `Performs whether game is liked successfully`() {
        runBlockingTest {
            SUT.likeGame(gameId = 100)

            assertThat(likedGamesTable.isGameLiked(gameId = 100)).isTrue
            assertThat(likedGamesTable.isGameLiked(gameId = 110)).isFalse
        }
    }


    @Test
    fun `Observes game like state successfully`() {
        runBlockingTest {
            SUT.likeGame(gameId = 100)

            assertThat(likedGamesTable.observeGameLikeState(gameId = 100).first()).isTrue
            assertThat(likedGamesTable.observeGameLikeState(gameId = 110).first()).isFalse
        }
    }


    @Test
    fun `Observes liked games successfully`() {
        runBlockingTest {
            assertThat(likedGamesTable.observeLikedGames(offset = 0, limit = 20).first())
                .isEqualTo(DATABASE_GAMES)
        }
    }


    private class FakeLikedGamesTable : LikedGamesTable {

        var likedGames = mutableListOf<LikedGame>()

        override suspend fun saveLikedGame(likedGame: LikedGame) {
            likedGames.add(likedGame)
        }

        override suspend fun deleteLikedGame(gameId: Int) {
            likedGames.removeAll { it.gameId == gameId }
        }

        override suspend fun isGameLiked(gameId: Int): Boolean {
            return likedGames.any { it.gameId == gameId }
        }

        override fun observeGameLikeState(gameId: Int): Flow<Boolean> {
            return flowOf(
                likedGames.any { it.gameId == gameId }
            )
        }

        override fun observeLikedGames(offset: Int, limit: Int): Flow<List<DatabaseGame>> {
            return flowOf(DATABASE_GAMES)
        }

    }


    private class FakeLikedGameFactory : LikedGameFactory {

        override fun createLikedGame(gameId: Int): LikedGame {
            return LikedGame(
                id = 1,
                gameId = gameId,
                likeTimestamp = 500L
            )
        }

    }


    private class FakeDispatcherProvider(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
        override val main: CoroutineDispatcher = testDispatcher,
        override val io: CoroutineDispatcher = testDispatcher,
        override val computation: CoroutineDispatcher = testDispatcher
    ) : DispatcherProvider


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