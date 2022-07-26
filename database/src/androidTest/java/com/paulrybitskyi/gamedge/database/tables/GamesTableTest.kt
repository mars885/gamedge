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
import com.paulrybitskyi.gamedge.database.DB_GAMES
import com.paulrybitskyi.gamedge.database.games.entities.DbGame
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
internal class GamesTableTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject lateinit var SUT: GamesTable

    @Module(includes = [TestDatabaseModule::class])
    @InstallIn(SingletonComponent::class)
    class TestModule

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun saves_games_and_gets_game_by_ID() {
        runTest {
            SUT.saveGames(DB_GAMES)

            val expectedGame = DB_GAMES.first()

            assertThat(SUT.getGame(expectedGame.id)).isEqualTo(expectedGame)
        }
    }

    @Test
    fun saves_games_and_gets_null_for_non_existent_game_ID() {
        runTest {
            SUT.saveGames(DB_GAMES)

            assertThat(SUT.getGame(id = 500)).isNull()
        }
    }

    @Test
    fun saves_games_and_gets_games_by_IDs() {
        runTest {
            SUT.saveGames(DB_GAMES)

            assertThat(
                SUT.getGames(
                    ids = DB_GAMES.map(DbGame::id),
                    offset = 0,
                    limit = DB_GAMES.size
                )
            ).isEqualTo(DB_GAMES)
        }
    }

    @Test
    fun saves_games_and_gets_empty_game_list_for_non_existent_game_IDs() {
        runTest {
            SUT.saveGames(DB_GAMES)

            assertThat(
                SUT.getGames(
                    ids = listOf(100, 200, 300),
                    offset = 0,
                    limit = DB_GAMES.size
                )
            ).isEqualTo(emptyList<DbGame>())
        }
    }

    @Test
    fun saves_games_and_gets_some_games_for_some_game_IDs() {
        runTest {
            SUT.saveGames(DB_GAMES)

            val expectedGames = listOf(DB_GAMES.first(), DB_GAMES.last())

            assertThat(
                SUT.getGames(
                    ids = expectedGames.map(DbGame::id),
                    offset = 0,
                    limit = expectedGames.size
                )
            ).isEqualTo(expectedGames)
        }
    }

    @Test
    fun saves_games_and_gets_sorted_games_by_searching_with_upper_case_game_name() {
        runTest {
            SUT.saveGames(DB_GAMES)

            val expectedGames = DB_GAMES
                .sortedByDescending(DbGame::totalRating)

            assertThat(
                SUT.searchGames(
                    searchQuery = "Game",
                    offset = 0,
                    limit = DB_GAMES.size
                )
            ).isEqualTo(expectedGames)
        }
    }

    @Test
    fun saves_games_and_gets_sorted_games_by_searching_with_lower_case_game_name() {
        runTest {
            SUT.saveGames(DB_GAMES)

            val expectedGames = DB_GAMES
                .sortedByDescending(DbGame::totalRating)

            assertThat(
                SUT.searchGames(
                    searchQuery = "game",
                    offset = 0,
                    limit = DB_GAMES.size
                )
            ).isEqualTo(expectedGames)
        }
    }

    @Test
    fun saves_games_and_gets_empty_game_list_by_searching_with_not_available_game_name() {
        runTest {
            SUT.saveGames(DB_GAMES)

            assertThat(
                SUT.searchGames(
                    searchQuery = "shadow of the colossus",
                    offset = 0,
                    limit = DB_GAMES.size
                )
            ).isEqualTo(emptyList<DbGame>())
        }
    }

    @Test
    fun saves_games_and_gets_empty_game_list_by_searching_with_word_that_ends_with_target_game_name() {
        runTest {
            SUT.saveGames(DB_GAMES)

            assertThat(
                SUT.searchGames(
                    searchQuery = "endgame",
                    offset = 0,
                    limit = DB_GAMES.size
                )
            ).isEqualTo(emptyList<DbGame>())
        }
    }

    @Test
    fun saves_specific_games_and_gets_properly_sorted_games_by_searching_with_existing_game_name() {
        runTest {
            val gamesToSave = listOf(
                DB_GAMES[0].copy(totalRating = null),
                DB_GAMES[1].copy(totalRating = 20.0),
                DB_GAMES[2].copy(totalRating = 20.0),
                DB_GAMES[3],
                DB_GAMES[4],
            )

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave.sortedWith(
                compareByDescending(DbGame::totalRating)
                    .thenBy(DbGame::id)
            )

            assertThat(
                SUT.searchGames(
                    searchQuery = "Game",
                    offset = 0,
                    limit = DB_GAMES.size
                )
            ).isEqualTo(expectedGames)
        }
    }

    @Test
    fun saves_popular_games_and_observes_popular_games() {
        runTest {
            SUT.saveGames(DB_GAMES)

            val expectedGames = DB_GAMES
                .sortedByDescending(DbGame::totalRating)

            SUT.observePopularGames(
                minReleaseDateTimestamp = 50L,
                offset = 0,
                limit = DB_GAMES.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_popular_games_and_observes_only_games_that_have_users_rating() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(usersRating = null))
                addAll(DB_GAMES.drop(1))
            }

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.usersRating != null }
                .sortedByDescending(DbGame::totalRating)

            SUT.observePopularGames(
                minReleaseDateTimestamp = 50L,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_popular_games_and_observes_only_games_that_have_release_date() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(usersRating = null))
                add(DB_GAMES[1].copy(releaseDate = null))
                addAll(DB_GAMES.drop(2))
            }

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.usersRating != null }
                .filter { it.releaseDate != null }
                .sortedByDescending(DbGame::totalRating)

            SUT.observePopularGames(
                minReleaseDateTimestamp = 50L,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_popular_games_and_observes_only_games_that_have_min_release_date() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(usersRating = null))
                add(DB_GAMES[1].copy(releaseDate = null))
                addAll(DB_GAMES.drop(2))
            }
            val minReleaseDateTimestamp = 300L

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.usersRating != null }
                .filter { it.releaseDate != null }
                .filter { it.releaseDate != null && it.releaseDate!! > minReleaseDateTimestamp }
                .sortedByDescending(DbGame::totalRating)

            SUT.observePopularGames(
                minReleaseDateTimestamp = minReleaseDateTimestamp,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_recently_released_games_and_observes_recently_released_games() {
        runTest {
            SUT.saveGames(DB_GAMES)

            val expectedGames = DB_GAMES
                .sortedByDescending(DbGame::releaseDate)

            SUT.observeRecentlyReleasedGames(
                minReleaseDateTimestamp = 50L,
                maxReleaseDateTimestamp = 1000L,
                offset = 0,
                limit = DB_GAMES.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_recently_released_games_and_observes_recently_released_games_that_have_release_date() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(releaseDate = null))
                addAll(DB_GAMES.drop(1))
            }

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.releaseDate != null }
                .sortedByDescending(DbGame::releaseDate)

            SUT.observeRecentlyReleasedGames(
                minReleaseDateTimestamp = 50L,
                maxReleaseDateTimestamp = 1000L,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_recently_released_games_and_observes_recently_released_games_that_have_min_release_date() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(releaseDate = null))
                addAll(DB_GAMES.drop(1))
            }
            val minReleaseDateTimestamp = 300L

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.releaseDate != null }
                .filter { it.releaseDate != null && it.releaseDate!! > minReleaseDateTimestamp }
                .sortedByDescending(DbGame::releaseDate)

            SUT.observeRecentlyReleasedGames(
                minReleaseDateTimestamp = minReleaseDateTimestamp,
                maxReleaseDateTimestamp = 1000L,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_recently_released_games_and_observes_recently_released_games_that_have_max_release_date() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(releaseDate = null))
                addAll(DB_GAMES.drop(1))
            }
            val minReleaseDateTimestamp = 200L
            val maxReleaseDateTimestamp = 400L

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.releaseDate != null }
                .filter { it.releaseDate != null && it.releaseDate!! > minReleaseDateTimestamp }
                .filter { it.releaseDate != null && it.releaseDate!! < maxReleaseDateTimestamp }
                .sortedByDescending(DbGame::releaseDate)

            SUT.observeRecentlyReleasedGames(
                minReleaseDateTimestamp = minReleaseDateTimestamp,
                maxReleaseDateTimestamp = maxReleaseDateTimestamp,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_coming_soon_games_and_observes_coming_soon_games() {
        runTest {
            SUT.saveGames(DB_GAMES)

            val expectedGames = DB_GAMES.sortedBy(DbGame::releaseDate)

            SUT.observeComingSoonGames(
                minReleaseDateTimestamp = 50L,
                offset = 0,
                limit = DB_GAMES.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_coming_soon_games_and_observes_coming_soon_games_that_have_release_date() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(releaseDate = null))
                addAll(DB_GAMES.drop(1))
            }

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.releaseDate != null }
                .sortedBy(DbGame::releaseDate)

            SUT.observeComingSoonGames(
                minReleaseDateTimestamp = 50L,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_coming_soon_games_and_observes_coming_soon_games_that_have_min_release_date() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(releaseDate = null))
                addAll(DB_GAMES.drop(1))
            }
            val minReleaseDateTimestamp = 300L

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.releaseDate != null }
                .filter { it.releaseDate != null && it.releaseDate!! > minReleaseDateTimestamp }
                .sortedBy(DbGame::releaseDate)

            SUT.observeComingSoonGames(
                minReleaseDateTimestamp = minReleaseDateTimestamp,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_most_anticipated_games_and_observes_most_anticipated_games() {
        runTest {
            SUT.saveGames(DB_GAMES)

            val expectedGames = DB_GAMES
                .sortedByDescending(DbGame::hypeCount)

            SUT.observeMostAnticipatedGames(
                minReleaseDateTimestamp = 50L,
                offset = 0,
                limit = DB_GAMES.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_most_anticipated_games_and_observes_most_anticipated_games_that_have_release_date() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(releaseDate = null))
                addAll(DB_GAMES.drop(1))
            }

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.releaseDate != null }
                .sortedByDescending(DbGame::hypeCount)

            SUT.observeMostAnticipatedGames(
                minReleaseDateTimestamp = 50L,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_most_anticipated_games_and_observes_most_anticipated_games_that_have_min_release_date() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(releaseDate = null))
                addAll(DB_GAMES.drop(1))
            }
            val minReleaseDateTimestamp = 300L

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.releaseDate != null }
                .filter { it.releaseDate != null && it.releaseDate!! > minReleaseDateTimestamp }
                .sortedByDescending(DbGame::hypeCount)

            SUT.observeMostAnticipatedGames(
                minReleaseDateTimestamp = minReleaseDateTimestamp,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }

    @Test
    fun saves_most_anticipated_games_and_observes_most_anticipated_games_that_have_hype_count() {
        runTest {
            val gamesToSave = buildList {
                add(DB_GAMES[0].copy(releaseDate = null))
                add(DB_GAMES[1].copy(hypeCount = null))
                addAll(DB_GAMES.drop(2))
            }
            val minReleaseDateTimestamp = 300L

            SUT.saveGames(gamesToSave)

            val expectedGames = gamesToSave
                .filter { it.releaseDate != null }
                .filter { it.releaseDate != null && it.releaseDate!! > minReleaseDateTimestamp }
                .filter { it.hypeCount != null }
                .sortedByDescending(DbGame::hypeCount)

            SUT.observeMostAnticipatedGames(
                minReleaseDateTimestamp = minReleaseDateTimestamp,
                offset = 0,
                limit = gamesToSave.size
            ).test {
                assertThat(awaitItem()).isEqualTo(expectedGames)
            }
        }
    }
}
