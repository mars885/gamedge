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
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.database.common.di.DatabaseModule
import com.paulrybitskyi.gamedge.database.games.tables.GamesTable
import com.paulrybitskyi.gamedge.database.games.tables.LikedGamesTable
import com.paulrybitskyi.gamedge.database.DB_GAMES
import com.paulrybitskyi.gamedge.database.DB_LIKED_GAME
import com.paulrybitskyi.gamedge.database.DB_LIKED_GAMES
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
internal class LikedGamesTableTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject lateinit var gamesTable: GamesTable
    @Inject lateinit var SUT: LikedGamesTable

    @Module(includes = [TestDatabaseModule::class])
    @InstallIn(SingletonComponent::class)
    class TestModule

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun likes_game_and_verifies_that_it_is_liked() {
        runTest {
            SUT.saveLikedGame(DB_LIKED_GAME)

            assertThat(SUT.isGameLiked(DB_LIKED_GAME.gameId)).isTrue()
        }
    }

    @Test
    fun likes_game_unlikes_it_and_verifies_that_it_is_unliked_by_checking() {
        runTest {
            SUT.saveLikedGame(DB_LIKED_GAME)
            SUT.deleteLikedGame(DB_LIKED_GAME.gameId)

            assertThat(SUT.isGameLiked(DB_LIKED_GAME.gameId)).isFalse()
        }
    }

    @Test
    fun verifies_that_unliked_game_is_unliked() {
        runTest {
            assertThat(SUT.isGameLiked(100)).isFalse()
        }
    }

    @Test
    fun likes_game_and_observes_that_it_is_liked() {
        runTest {
            SUT.saveLikedGame(DB_LIKED_GAME)

            SUT.observeGameLikeState(DB_LIKED_GAME.gameId).test {
                assertThat(awaitItem()).isTrue()
            }
        }
    }

    @Test
    fun likes_game_unlikes_it_and_verifies_that_it_is_unliked_by_observing() {
        runTest {
            SUT.saveLikedGame(DB_LIKED_GAME)
            SUT.deleteLikedGame(DB_LIKED_GAME.gameId)

            SUT.observeGameLikeState(DB_LIKED_GAME.gameId).test {
                assertThat(awaitItem()).isFalse()
            }
        }
    }

    @Test
    fun likes_games_and_observes_liked_games() {
        runTest {
            DB_LIKED_GAMES.forEach { SUT.saveLikedGame(it) }
            gamesTable.saveGames(DB_GAMES)

            val expectedGames = DB_GAMES
                .sortedByDescending { game ->
                    DB_LIKED_GAMES
                        .first { likedGame -> likedGame.gameId == game.id }
                        .likeTimestamp
                }

            SUT.observeLikedGames(offset = 0, limit = expectedGames.size)
                .test {
                    assertThat(awaitItem()).isEqualTo(expectedGames)
                }
        }
    }
}
