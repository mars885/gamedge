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

package com.paulrybitskyi.gamedge.feature.discovery

import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.gamedge.domain.games.DomainCategory
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.commons.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.*
import com.paulrybitskyi.gamedge.feature.discovery.di.GamesDiscoveryKey
import com.paulrybitskyi.gamedge.feature.discovery.mapping.GamesDiscoveryItemGameModelMapper
import com.paulrybitskyi.gamedge.feature.discovery.mapping.GamesDiscoveryItemModelFactoryImpl
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemGameModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
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


internal class GamesDiscoveryViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var observePopularGamesUseCase: FakeObservePopularGamesUseCase
    private lateinit var refreshPopularGamesUseCase: FakeRefreshPopularGamesUseCase

    private lateinit var useCases: GamesDiscoveryUseCases
    private lateinit var logger: FakeLogger
    private lateinit var SUT: GamesDiscoveryViewModel


    @Before
    fun setup() {
        useCases = setupUseCases()
        logger = FakeLogger()
        SUT = GamesDiscoveryViewModel(
            useCases = useCases,
            itemModelFactory = GamesDiscoveryItemModelFactoryImpl(FakeStringProvider()),
            itemGameModelMapper = FakeGamesDiscoveryItemGameModelMapper(),
            dispatcherProvider = FakeDispatcherProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger
        )
    }


    private fun setupUseCases(): GamesDiscoveryUseCases {
        observePopularGamesUseCase = FakeObservePopularGamesUseCase()
        refreshPopularGamesUseCase = FakeRefreshPopularGamesUseCase()

        return GamesDiscoveryUseCases(
            observeGamesUseCasesMap = mapOf(
                GamesDiscoveryKey.Type.POPULAR to observePopularGamesUseCase,
                GamesDiscoveryKey.Type.RECENTLY_RELEASED to FakeObserveRecentlyReleasedGamesUseCase(),
                GamesDiscoveryKey.Type.COMING_SOON to FakeObserveComingSoonGamesUseCase(),
                GamesDiscoveryKey.Type.MOST_ANTICIPATED to FakeObserveMostAnticipatedGamesUseCase(),
            ),
            refreshGamesUseCasesMap = mapOf(
                GamesDiscoveryKey.Type.POPULAR to refreshPopularGamesUseCase,
                GamesDiscoveryKey.Type.RECENTLY_RELEASED to FakeRefreshRecentlyReleasedGamesUseCase(),
                GamesDiscoveryKey.Type.COMING_SOON to FakeRefreshComingSoonGamesUseCase(),
                GamesDiscoveryKey.Type.MOST_ANTICIPATED to FakeRefreshMostAnticipatedGamesUseCase(),
            )
        )
    }


    @Test
    fun `Logs error when games observing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            observePopularGamesUseCase.shouldThrowError = true
            refreshPopularGamesUseCase.shouldReturnGames = true

            SUT.loadData()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Logs error when games refreshing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            observePopularGamesUseCase.shouldReturnGames = true
            refreshPopularGamesUseCase.shouldThrowError = true

            SUT.loadData()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Dispatches toast showing command when games refreshing use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            observePopularGamesUseCase.shouldReturnGames = true
            refreshPopularGamesUseCase.shouldThrowError = true

            SUT.loadData()

            val command = SUT.commandFlow.first()

            assertThat(command is GeneralCommand.ShowLongToast).isTrue
        }
    }


    @Test
    fun `Routes to games category screen when more button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            SUT.onCategoryMoreButtonClicked("popular")

            val route = SUT.routeFlow.first()

            assertThat(route is GamesDiscoveryRoute.Category).isTrue
            assertThat((route as GamesDiscoveryRoute.Category).category)
                .isEqualTo("popular")
        }
    }


    @Test
    fun `Routes to game info screen when game is clicked`() {
        mainCoroutineRule.runBlockingTest {
            val item = GamesDiscoveryItemGameModel(
                id = 1,
                title = "title",
                coverUrl = null
            )

            SUT.onCategoryGameClicked(item)

            val route = SUT.routeFlow.first()

            assertThat(route is GamesDiscoveryRoute.Info).isTrue
            assertThat((route as GamesDiscoveryRoute.Info).gameId)
                .isEqualTo(item.id)
        }
    }


    private class FakeObservePopularGamesUseCase : ObservePopularGamesUseCase {

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


    private class FakeRefreshPopularGamesUseCase : RefreshPopularGamesUseCase {

        var shouldReturnGames = false
        var shouldThrowError = false

        override suspend fun execute(params: RefreshGamesUseCaseParams): Flow<DomainResult<List<DomainGame>>> {
            return when {
                shouldReturnGames -> flowOf(Ok(DOMAIN_GAMES))
                shouldThrowError -> flow { throw Exception("error") }

                else -> throw IllegalStateException()
            }
        }

    }


    private class FakeObserveRecentlyReleasedGamesUseCase : ObserveRecentlyReleasedGamesUseCase {

        override suspend fun execute(params: ObserveGamesUseCaseParams): Flow<List<DomainGame>> {
            return flowOf(DOMAIN_GAMES)
        }

    }


    private class FakeRefreshRecentlyReleasedGamesUseCase : RefreshRecentlyReleasedGamesUseCase {

        override suspend fun execute(params: RefreshGamesUseCaseParams): Flow<DomainResult<List<DomainGame>>> {
            return flowOf(Ok(DOMAIN_GAMES))
        }

    }


    private class FakeObserveComingSoonGamesUseCase : ObserveComingSoonGamesUseCase {

        override suspend fun execute(params: ObserveGamesUseCaseParams): Flow<List<DomainGame>> {
            return flowOf(DOMAIN_GAMES)
        }

    }


    private class FakeRefreshComingSoonGamesUseCase : RefreshComingSoonGamesUseCase {

        override suspend fun execute(params: RefreshGamesUseCaseParams): Flow<DomainResult<List<DomainGame>>> {
            return flowOf(Ok(DOMAIN_GAMES))
        }

    }


    private class FakeObserveMostAnticipatedGamesUseCase : ObserveMostAnticipatedGamesUseCase {

        override suspend fun execute(params: ObserveGamesUseCaseParams): Flow<List<DomainGame>> {
            return flowOf(DOMAIN_GAMES)
        }

    }


    private class FakeRefreshMostAnticipatedGamesUseCase : RefreshMostAnticipatedGamesUseCase {

        override suspend fun execute(params: RefreshGamesUseCaseParams): Flow<DomainResult<List<DomainGame>>> {
            return flowOf(Ok(DOMAIN_GAMES))
        }

    }


    private class FakeStringProvider : StringProvider {

        override fun getString(id: Int, vararg args: Any): String {
            return "string"
        }

        override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String {
            return "quantity_string"
        }

    }


    private class FakeGamesDiscoveryItemGameModelMapper : GamesDiscoveryItemGameModelMapper {

        override fun mapToGameModel(game: DomainGame): GamesDiscoveryItemGameModel {
            return GamesDiscoveryItemGameModel(
                id = game.id,
                title = game.name,
                coverUrl = null
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