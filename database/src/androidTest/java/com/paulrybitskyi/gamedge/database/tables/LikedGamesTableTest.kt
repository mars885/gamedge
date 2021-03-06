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

package com.paulrybitskyi.gamedge.database.tables

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.paulrybitskyi.gamedge.database.GamedgeDatabase
import com.paulrybitskyi.gamedge.database.games.tables.GamesTable
import com.paulrybitskyi.gamedge.database.games.tables.LikedGamesTable
import com.paulrybitskyi.gamedge.database.utils.DATABASE_GAMES
import com.paulrybitskyi.gamedge.database.utils.LIKED_GAME
import com.paulrybitskyi.gamedge.database.utils.LIKED_GAMES
import com.paulrybitskyi.gamedge.database.utils.createMemoryDb
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class LikedGamesTableTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: GamedgeDatabase
    private lateinit var gamesTable: GamesTable
    private lateinit var SUT: LikedGamesTable


    @Before
    fun setup() {
        db = createMemoryDb()
        gamesTable = db.gamesTable
        SUT = db.likedGamesTable
    }


    @Test
    fun likes_game_and_verifies_that_it_is_liked() {
        runBlockingTest {
            SUT.saveLikedGame(LIKED_GAME)

            assertThat(SUT.isGameLiked(LIKED_GAME.gameId)).isTrue
        }
    }


    @Test
    fun likes_game_unlikes_it_and_verifies_that_it_is_unliked_by_checking() {
        runBlockingTest {
            SUT.saveLikedGame(LIKED_GAME)
            SUT.deleteLikedGame(LIKED_GAME.gameId)

            assertThat(SUT.isGameLiked(LIKED_GAME.gameId)).isFalse
        }
    }


    @Test
    fun verifies_that_unliked_game_is_unliked() {
        runBlockingTest {
            assertThat(SUT.isGameLiked(100)).isFalse
        }
    }


    @Test
    fun likes_game_and_observes_that_it_is_liked() {
        runBlockingTest {
            SUT.saveLikedGame(LIKED_GAME)

            SUT.observeGameLikeState(LIKED_GAME.gameId).test {
                assertThat(expectItem()).isTrue
            }
        }
    }


    @Test
    fun likes_game_unlikes_it_and_verifies_that_it_is_unliked_by_observing() {
        runBlockingTest {
            SUT.saveLikedGame(LIKED_GAME)
            SUT.deleteLikedGame(LIKED_GAME.gameId)

            SUT.observeGameLikeState(LIKED_GAME.gameId).test {
                assertThat(expectItem()).isFalse
            }
        }
    }


    @Test
    fun likes_games_and_observes_liked_games() {
        runBlockingTest {
            LIKED_GAMES.forEach { SUT.saveLikedGame(it) }
            gamesTable.saveGames(DATABASE_GAMES)

            val expectedGames = DATABASE_GAMES
                .sortedByDescending { game ->
                    LIKED_GAMES
                        .first { likedGame -> likedGame.gameId == game.id }
                        .likeTimestamp
                }

            SUT.observeLikedGames(offset = 0, limit = expectedGames.size)
                .test {
                    assertThat(expectItem()).isEqualTo(expectedGames)
                }
        }
    }


    @After
    fun cleanup() {
        db.close()
    }


}