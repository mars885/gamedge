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
import com.paulrybitskyi.gamedge.domain.games.DomainCategory
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.SearchGamesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
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


internal class GamesSearchViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var searchGamesUseCase: FakeSearchGamesUseCase
    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamesSearchViewModel


    @Before
    fun setup() {
        searchGamesUseCase = FakeSearchGamesUseCase()
        logger = FakeLogger()
        SUT = GamesSearchViewModel(
            searchGamesUseCase = searchGamesUseCase,
            uiStateFactory = FakeGamesSearchUiStateFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
            savedStateHandle = setupSavedStateHandle()
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
            SUT.onToolbarBackButtonClicked()

            val route = SUT.routeFlow.first()

            assertThat(route is GamesSearchRoute.Back).isTrue
        }
    }


    @Test
    fun `Emits correct ui states when searching for games`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldReturnGames = true

            val uiStates = mutableListOf<GamesUiState>()
            val uiStateJob = launch { SUT.uiState.toList(uiStates) }

            SUT.onSearchActionRequested("god of war")

            assertThat(uiStates[0] is GamesUiState.Empty).isTrue
            assertThat(uiStates[1] is GamesUiState.Loading).isTrue
            assertThat(uiStates[2] is GamesUiState.Result).isTrue
            assertThat((uiStates[2] as GamesUiState.Result).items)
                .hasSize(DOMAIN_GAMES.size)

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Does not emit ui states when search query is empty`() {
        mainCoroutineRule.runBlockingTest {
            val uiStates = mutableListOf<GamesUiState>()
            val uiStateJob = launch { SUT.uiState.toList(uiStates) }

            SUT.onSearchActionRequested("")

            assertThat(uiStates).hasSize(1)
            assertThat(uiStates[0] is GamesUiState.Empty).isTrue

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Does not emit ui states when the current search query is provided`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldReturnGames = true

            SUT.onSearchActionRequested("god of war")

            val uiStates = mutableListOf<GamesUiState>()
            val uiStateJob = launch { SUT.uiState.toList(uiStates) }

            SUT.onSearchActionRequested("god of war")

            assertThat(uiStates).hasSize(1)

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Emits empty ui state when blank search query is provided`() {
        mainCoroutineRule.runBlockingTest {
            val uiStates = mutableListOf<GamesUiState>()
            val uiStateJob = launch { SUT.uiState.toList(uiStates) }

            SUT.onSearchActionRequested("   ")

            assertThat(uiStates).hasSize(1)
            assertThat(uiStates[0] is GamesUiState.Empty).isTrue

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Dispatches items clearing command when performing new search`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldReturnGames = true

            SUT.onSearchActionRequested("god of war")

            val command = SUT.commandFlow.first()

            assertThat(command is GamesSearchCommand.ClearItems).isTrue
        }
    }


    @Test
    fun `Logs error when searching games use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldThrowError = true

            SUT.onSearchActionRequested("god of war")

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Dispatches toast showing command when searching games use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            searchGamesUseCase.shouldThrowError = true

            SUT.onSearchActionRequested("god of war")

            val command = SUT.commandFlow.drop(1).first()

            assertThat(command is GeneralCommand.ShowLongToast).isTrue
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

            assertThat(route is GamesSearchRoute.Info).isTrue
            assertThat((route as GamesSearchRoute.Info).gameId)
                .isEqualTo(gameModel.id)
        }
    }


    private class FakeSearchGamesUseCase : SearchGamesUseCase {

        var shouldReturnGames = false
        var shouldThrowError = false

        override suspend fun execute(params: SearchGamesUseCase.Params): Flow<List<DomainGame>> {
            return when {
                shouldReturnGames -> flowOf(DOMAIN_GAMES)
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

        override fun createWithResultState(games: List<DomainGame>): GamesUiState {
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