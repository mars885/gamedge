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

package com.paulrybitskyi.gamedge.feature.info

import androidx.lifecycle.SavedStateHandle
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.factories.ImageViewerGameUrlFactory
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.gamedge.domain.commons.entities.Error
import com.paulrybitskyi.gamedge.domain.games.entities.Category
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.usecases.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.GetGameUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ToggleGameLikeStateUseCase
import com.paulrybitskyi.gamedge.feature.info.mapping.GameInfoUiStateFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.main.GameInfoUiState
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.*
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.games.GameInfoRelatedGameModel
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

private val API_ERROR = Error.ApiError.ClientError("message")
private val NOT_FOUND_ERROR = Error.NotFound("message")
private val UNKNOWN_ERROR = Error.Unknown("unknown")


internal class GameInfoViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var useCases: FakeGameInfoUseCases
    private lateinit var logger: FakeLogger
    private lateinit var viewModel: GameInfoViewModel


    @Before
    fun setup() {
        useCases = FakeGameInfoUseCases()
        logger = FakeLogger()
        viewModel = GameInfoViewModel(
            useCases = useCases,
            uiStateFactory = FakeGameInfoUiStateFactory(),
            gameUrlFactory = FakeGameUrlFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            stringProvider = FakeStringProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
            savedStateHandle = setupSavedStateHandle()
        )
    }


    private fun setupSavedStateHandle(): SavedStateHandle {
        return mockk<SavedStateHandle>(relaxed = true).also {
            every { it.get<Int>(any()) } returns 1
        }
    }


    @Test
    fun `Emits correct ui states when loading data`() {
        mainCoroutineRule.runBlockingTest {
            useCases.getGameUseCase.shouldReturnGame = true

            val uiStates = mutableListOf<GameInfoUiState>()
            val uiStateJob = launch { viewModel.uiState.toList(uiStates) }

            viewModel.loadData(resultEmissionDelay = 0L)

            assertTrue(uiStates[0] is GameInfoUiState.Empty)
            assertTrue(uiStates[1] is GameInfoUiState.Loading)
            assertTrue(uiStates[2] is GameInfoUiState.Result)

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Logs error when game fetching use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            useCases.getGameUseCase.shouldReturnNotFoundError = true

            viewModel.loadData(resultEmissionDelay = 0L)

            assertNotEquals("", logger.errorMessage)
        }
    }


    @Test
    fun `Dispatches toast showing command when game fetching use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            useCases.getGameUseCase.shouldReturnUnknownError = true

            viewModel.loadData(resultEmissionDelay = 0L)

            val command = viewModel.commandFlow.first()

            assertTrue(command is GeneralCommand.ShowLongToast)
        }
    }


    @Test
    fun `Routes to image viewer screen when artwork is clicked`() {
        mainCoroutineRule.runBlockingTest {
            useCases.getGameUseCase.shouldReturnGame = true

            viewModel.onArtworkClicked(position = 0)

            val route = viewModel.routeFlow.first()

            assertTrue(route is GameInfoRoute.ImageViewer)
        }
    }


    @Test
    fun `Routes to previous screen when back button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            viewModel.onBackButtonClicked()

            val route = viewModel.routeFlow.first()

            assertTrue(route is GameInfoRoute.Back)
        }
    }


    @Test
    fun `Routes to image viewer screen when cover is clicked`() {
        mainCoroutineRule.runBlockingTest {
            useCases.getGameUseCase.shouldReturnGame = true

            viewModel.onCoverClicked()

            val route = viewModel.routeFlow.first()

            assertTrue(route is GameInfoRoute.ImageViewer)
        }
    }


    @Test
    fun `Dispatches url opening command when video is clicked`() {
        mainCoroutineRule.runBlockingTest {
            val video = GameInfoVideoModel(
                thumbnailUrl = "thumbnail_url",
                videoUrl = "video_url",
                title = "title"
            )

            viewModel.onVideoClicked(video)

            val command = viewModel.commandFlow.first()

            assertTrue(command is GameInfoCommand.OpenUrl)
            assertEquals(
                video.videoUrl,
                (command as GameInfoCommand.OpenUrl).url
            )
        }
    }


    @Test
    fun `Routes to image viewer screen when screenshot is clicked`() {
        mainCoroutineRule.runBlockingTest {
            useCases.getGameUseCase.shouldReturnGame = true

            viewModel.onScreenshotClicked(position = 0)

            val route = viewModel.routeFlow.first()

            assertTrue(route is GameInfoRoute.ImageViewer)
        }
    }


    @Test
    fun `Dispatches url opening command when game link is clicked`() {
        mainCoroutineRule.runBlockingTest {
            val link = GameInfoLinkModel(
                id = 1,
                text = "text",
                iconId = null,
                payload = "url"
            )

            viewModel.onLinkClicked(link)

            val command = viewModel.commandFlow.first()

            assertTrue(command is GameInfoCommand.OpenUrl)
            assertEquals(
                link.payload,
                (command as GameInfoCommand.OpenUrl).url
            )
        }
    }


    @Test
    fun `Dispatches url opening command when company is clicked`() {
        mainCoroutineRule.runBlockingTest {
            val company = GameInfoCompanyModel(
                logoViewSize = (0 to 0),
                logoImageSize = (0 to 0),
                logoUrl = null,
                websiteUrl = "url",
                name = "",
                roles = ""
            )

            viewModel.onCompanyClicked(company)

            val command = viewModel.commandFlow.first()

            assertTrue(command is GameInfoCommand.OpenUrl)
            assertEquals(
                company.websiteUrl,
                (command as GameInfoCommand.OpenUrl).url
            )
        }
    }


    @Test
    fun `Routes to game info when related game is clicked`() {
        mainCoroutineRule.runBlockingTest {
            val relatedGame = GameInfoRelatedGameModel(
                id = 1,
                title = "title",
                coverUrl = "url"
            )

            viewModel.onRelatedGameClicked(relatedGame)

            val route = viewModel.routeFlow.first()

            assertTrue(route is GameInfoRoute.Info)
            assertEquals(
                relatedGame.id,
                (route as GameInfoRoute.Info).gameId
            )
        }
    }


    private class FakeGameInfoUseCases(
        override val getGameUseCase: FakeGetGameUseCase = FakeGetGameUseCase(),
        override val observeGameLikeStateUseCase: FakeObserveGameLikeStateUseCase = FakeObserveGameLikeStateUseCase(),
        override val toggleGameLikeStateUseCase: FakeToggleGameLikeStateUseCase = FakeToggleGameLikeStateUseCase(),
        override val getCompanyDevelopedGamesUseCase: FakeGetCompanyDevelopedGamesUseCase = FakeGetCompanyDevelopedGamesUseCase(),
        override val getSimilarGamesUseCase: FakeGetSimilarGamesUseCase = FakeGetSimilarGamesUseCase()
    ) : GameInfoUseCases


    private class FakeGetGameUseCase : GetGameUseCase {

        var shouldReturnGame = false
        var shouldReturnApiError = false
        var shouldReturnNotFoundError = false
        var shouldReturnUnknownError = false

        override suspend fun execute(params: GetGameUseCase.Params): Flow<DomainResult<Game>> {
            return when {
                shouldReturnGame -> flowOf(Ok(GAME))
                shouldReturnApiError -> flowOf(Err(API_ERROR))
                shouldReturnNotFoundError -> flowOf(Err(NOT_FOUND_ERROR))
                shouldReturnUnknownError -> flowOf(Err(UNKNOWN_ERROR))

                else -> throw IllegalStateException()
            }
        }

    }


    private class FakeObserveGameLikeStateUseCase : ObserveGameLikeStateUseCase {

        override suspend fun execute(params: ObserveGameLikeStateUseCase.Params): Flow<Boolean> {
            return flowOf(true)
        }

    }


    private class FakeToggleGameLikeStateUseCase : ToggleGameLikeStateUseCase {

        override suspend fun execute(params: ToggleGameLikeStateUseCase.Params) {
            // no-op
        }

    }


    private class FakeGetCompanyDevelopedGamesUseCase : GetCompanyDevelopedGamesUseCase {

        override suspend fun execute(params: GetCompanyDevelopedGamesUseCase.Params): Flow<List<Game>> {
            return flowOf(emptyList())
        }

    }


    private class FakeGetSimilarGamesUseCase : GetSimilarGamesUseCase {

        override suspend fun execute(params: GetSimilarGamesUseCase.Params): Flow<List<Game>> {
            return flowOf(emptyList())
        }

    }


    private class FakeGameInfoUiStateFactory : GameInfoUiStateFactory {

        override fun createWithEmptyState(): GameInfoUiState {
            return GameInfoUiState.Empty
        }

        override fun createWithLoadingState(): GameInfoUiState {
            return GameInfoUiState.Loading
        }

        override fun createWithResultState(
            game: Game,
            isLiked: Boolean,
            companyGames: List<Game>,
            similarGames: List<Game>
        ): GameInfoUiState {
            return GameInfoUiState.Result(
                GameInfoModel(
                    id = 1,
                    headerModel = GameInfoHeaderModel(
                        backgroundImageModels = emptyList(),
                        isLiked = true,
                        coverImageUrl = null,
                        title = "title",
                        releaseDate = "release_date",
                        developerName = null,
                        rating = "",
                        likeCount = "10",
                        ageRating = "",
                        gameCategory = ""
                    ),
                    videoModels = emptyList(),
                    screenshotUrls = emptyList(),
                    summary = null,
                    detailsModel = null,
                    linkModels = emptyList(),
                    companyModels = emptyList(),
                    otherCompanyGames = null,
                    similarGames = null
                )
            )
        }

    }


    private class FakeGameUrlFactory : ImageViewerGameUrlFactory {

        override fun createCoverImageUrl(game: Game): String? {
            return "url"
        }

        override fun createArtworkImageUrls(game: Game): List<String> {
            return listOf("url")
        }

        override fun createScreenshotImageUrls(game: Game): List<String> {
            return listOf("url")
        }

    }


    private class FakeDispatcherProvider(
        private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
        override val main: CoroutineDispatcher = testDispatcher,
        override val io: CoroutineDispatcher = testDispatcher,
        override val computation: CoroutineDispatcher = testDispatcher
    ) : DispatcherProvider


    private class FakeStringProvider : StringProvider {

        override fun getString(id: Int, vararg args: Any): String {
            return "string"
        }

        override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String {
            return "quantity_string"
        }

    }


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