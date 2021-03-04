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

package com.paulrybitskyi.gamedge.feature.likes

import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.domain.games.DomainCategory
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveLikedGamesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
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


internal class LikedGamesViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var observeLikedGamesUseCase: FakeObserveLikedGamesUseCase
    private lateinit var logger: FakeLogger
    private lateinit var SUT: LikedGamesViewModel


    @Before
    fun setup() {
        observeLikedGamesUseCase = FakeObserveLikedGamesUseCase()
        logger = FakeLogger()
        SUT = LikedGamesViewModel(
            observeLikedGamesUseCase = observeLikedGamesUseCase,
            uiStateFactory = FakeUiStateFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger
        )
    }


    @Test
    fun `Emits correct ui states when loading data`() {
        mainCoroutineRule.runBlockingTest {
            observeLikedGamesUseCase.shouldReturnGames = true

            val uiStates = mutableListOf<GamesUiState>()
            val uiStateJob = launch { SUT.uiState.toList(uiStates) }

            SUT.loadData()

            assertTrue(uiStates[0] is GamesUiState.Empty)
            assertTrue(uiStates[1] is GamesUiState.Loading)
            assertTrue(uiStates[2] is GamesUiState.Result)
            assertEquals(
                DOMAIN_GAMES.size,
                (uiStates[2] as GamesUiState.Result).items.size
            )

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Logs error when liked games loading fails`() {
        mainCoroutineRule.runBlockingTest {
            observeLikedGamesUseCase.shouldThrowError = true

            SUT.loadData()

            assertNotEquals("", logger.errorMessage)
        }
    }


    @Test
    fun `Dispatches toast showing command when liked games loading fails`() {
        mainCoroutineRule.runBlockingTest {
            observeLikedGamesUseCase.shouldThrowError = true

            SUT.loadData()

            val command = SUT.commandFlow.first()

            assertTrue(command is GeneralCommand.ShowLongToast)
        }
    }


    @Test
    fun `Routes to info screen when game is clicked`() {
        mainCoroutineRule.runBlockingTest {
            val gameModel = GameModel(
                id = 1,
                coverImageUrl = null,
                name = "",
                releaseDate = "",
                developerName = null,
                description = null
            )

            SUT.onGameClicked(gameModel)

            val route = SUT.routeFlow.first()

            assertTrue(route is LikedGamesRoute.Info)
            assertEquals(gameModel.id, (route as LikedGamesRoute.Info).gameId)
        }
    }


    private class FakeObserveLikedGamesUseCase : ObserveLikedGamesUseCase {

        var shouldReturnGames = false
        var shouldThrowError = false

        override suspend fun execute(params: ObserveGamesUseCaseParams): Flow<List<DomainGame>> {
            return when {
                shouldReturnGames -> flowOf(DOMAIN_GAMES)
                shouldThrowError -> flow { throw Exception("error") }

                else -> throw IllegalStateException()
            }
        }

    }


    private class FakeUiStateFactory : LikedGamesUiStateFactory {

        override fun createWithEmptyState(): GamesUiState {
            return GamesUiState.Empty(iconId = -1, title = "title")
        }

        override fun createWithLoadingState(): GamesUiState {
            return GamesUiState.Loading
        }

        override fun createWithResultState(games: List<DomainGame>): GamesUiState {
            return GamesUiState.Result(
                games.map {
                    GameModel(
                        id = it.id,
                        coverImageUrl = null,
                        name = it.name,
                        releaseDate = "",
                        developerName = null,
                        description = null
                    )
                }
            )
        }

    }


    private class FakeDispatcherProvider(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
        override val main: CoroutineDispatcher = testDispatcher,
        override val io: CoroutineDispatcher = testDispatcher,
        override val computation: CoroutineDispatcher = testDispatcher
    ) : DispatcherProvider


    private class FakeErrorMapper : ErrorMapper {

        override fun mapToMessage(error: Throwable): String {
            return "error"
        }

    }


    private class FakeLogger : Logger {

        var errorMessage = ""

        override fun info(tag: String, message: String, throwable: Throwable?) {}
        override fun debug(tag: String, message: String, throwable: Throwable?) {}
        override fun warning(tag: String, message: String, throwable: Throwable?) {}

        override fun error(tag: String, message: String, throwable: Throwable?) {
            errorMessage = message
        }

    }


}