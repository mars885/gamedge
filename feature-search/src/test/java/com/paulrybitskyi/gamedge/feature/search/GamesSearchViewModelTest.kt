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

package com.paulrybitskyi.gamedge.feature.search

import androidx.lifecycle.SavedStateHandle
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GamesUiState
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.domain.games.entities.Category
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.usecases.SearchGamesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private val GAME = Game(
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
    category = Category.UNKNOWN,
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
private val GAMES = listOf(
    GAME.copy(id = 1),
    GAME.copy(id = 2),
    GAME.copy(id = 3)
)


internal class GamesSearchViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var searchGamesUseCase: FakeSearchGamesUseCase
    private lateinit var logger: FakeLogger
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: GamesSearchViewModel


    @Before
    fun setup() {
        searchGamesUseCase = FakeSearchGamesUseCase()
        logger = FakeLogger()
        savedStateHandle = setupSavedStateHandle()
        viewModel = GamesSearchViewModel(
            searchGamesUseCase = searchGamesUseCase,
            uiStateFactory = FakeGamesSearchUiStateFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
            savedStateHandle = savedStateHandle
        )
    }


    private fun setupSavedStateHandle(): SavedStateHandle {
        return mockk<SavedStateHandle>(relaxed = true).also {
            every { it.get<String>(any()) } returns ""
        }
    }


    @Test
    fun `Routes to previous screen when toolbar back button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            viewModel.onToolbarBackButtonClicked()

            val route = viewModel.routeFlow.first()

            assertTrue(route is GamesSearchRoute.Back)
        }
    }


    @Test
    fun `Emits correct ui states when searching for games`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldReturnGames = true

            val uiStates = mutableListOf<GamesUiState>()
            val uiStateJob = launch { viewModel.uiState.toList(uiStates) }

            viewModel.onSearchActionRequested("god of war")

            assertTrue(uiStates[0] is GamesUiState.Empty)
            assertTrue(uiStates[1] is GamesUiState.Loading)
            assertTrue(uiStates[2] is GamesUiState.Result)
            assertEquals(
                GAMES.size,
                (uiStates[2] as GamesUiState.Result).items.size
            )

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Does not emit ui states when search query is empty`() {
        mainCoroutineRule.runBlockingTest {
            val uiStates = mutableListOf<GamesUiState>()
            val uiStateJob = launch { viewModel.uiState.toList(uiStates) }

            viewModel.onSearchActionRequested("")

            assertEquals(1, uiStates.size)
            assertTrue(uiStates[0] is GamesUiState.Empty)

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Does not emit ui states when the current search query is provided`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldReturnGames = true

            viewModel.onSearchActionRequested("god of war")

            val uiStates = mutableListOf<GamesUiState>()
            val uiStateJob = launch { viewModel.uiState.toList(uiStates) }

            viewModel.onSearchActionRequested("god of war")

            assertEquals(1, uiStates.size)

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Emits empty ui state when blank search query is provided`() {
        mainCoroutineRule.runBlockingTest {
            val uiStates = mutableListOf<GamesUiState>()
            val uiStateJob = launch { viewModel.uiState.toList(uiStates) }

            viewModel.onSearchActionRequested("   ")

            assertEquals(1, uiStates.size)
            assertTrue(uiStates[0] is GamesUiState.Empty)

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Dispatches items clearing command when performing new search`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldReturnGames = true

            viewModel.onSearchActionRequested("god of war")

            val command = viewModel.commandFlow.first()

            assertTrue(command is GamesSearchCommand.ClearItems)
        }
    }


    @Test
    fun `Logs error when searching games use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldThrowError = true

            viewModel.onSearchActionRequested("god of war")

            assertNotEquals("", logger.errorMessage)
        }
    }


    @Test
    fun `Dispatches toast showing command when searching games use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldThrowError = true

            viewModel.onSearchActionRequested("god of war")

            val command = viewModel.commandFlow.drop(1).first()

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

            viewModel.onGameClicked(gameModel)

            val route = viewModel.routeFlow.first()

            assertTrue(route is GamesSearchRoute.Info)
            assertEquals(gameModel.id, (route as GamesSearchRoute.Info).gameId)
        }
    }


    private class FakeSearchGamesUseCase : SearchGamesUseCase {

        var shouldReturnGames = false
        var shouldReturnEmptyList = false
        var shouldThrowError = false

        override suspend fun execute(params: SearchGamesUseCase.Params): Flow<List<Game>> {
            return when {
                shouldReturnGames -> flowOf(GAMES)
                shouldReturnEmptyList -> flowOf(emptyList())
                shouldThrowError -> flow { throw Exception("error") }

                else -> throw IllegalStateException()
            }
        }

    }


    private class FakeGamesSearchUiStateFactory : GamesSearchUiStateFactory {

        override fun createWithEmptyState(searchQuery: String): GamesUiState {
            return GamesUiState.Empty(iconId = -1, title = "title")
        }

        override fun createWithLoadingState(): GamesUiState {
            return GamesUiState.Loading
        }

        override fun createWithResultState(games: List<Game>): GamesUiState {
            return GamesUiState.Result(
                games.map {
                    GameModel(
                        id = it.id,
                        coverImageUrl = null,
                        name = it.name,
                        releaseDate = "Sometime",
                        developerName = "dev",
                        description = "description"
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