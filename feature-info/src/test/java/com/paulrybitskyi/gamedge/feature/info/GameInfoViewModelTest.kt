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
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.usecases.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.GetGameUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ToggleGameLikeStateUseCase
import com.paulrybitskyi.gamedge.feature.info.mapping.GameInfoUiStateFactory
import com.paulrybitskyi.gamedge.feature.info.widgets.main.GameInfoUiState
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.*
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.games.GameInfoRelatedGameModel
import com.paulrybitskyi.gamedge.commons.testing.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


internal class GameInfoViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var useCases: GameInfoUseCases
    private lateinit var logger: FakeLogger
    private lateinit var SUT: GameInfoViewModel


    @Before
    fun setup() {
        useCases = setupUseCases()
        logger = FakeLogger()
        SUT = GameInfoViewModel(
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


    private fun setupUseCases(): GameInfoUseCases {
        return GameInfoUseCases(
            getGameUseCase = mockk(),
            observeGameLikeStateUseCase = mockk {
                coEvery { execute(any()) } returns flowOf(true)
            },
            toggleGameLikeStateUseCase = mockk(),
            getCompanyDevelopedGamesUseCase = mockk {
                coEvery { execute(any()) } returns flowOf(emptyList())
            },
            getSimilarGamesUseCase = mockk {
                coEvery { execute(any()) } returns flowOf(emptyList())
            }
        )
    }


    private fun setupSavedStateHandle(): SavedStateHandle {
        return mockk(relaxed = true) {
            every { get<Int>(any()) } returns 1
        }
    }


    @Test
    fun `Emits correct ui states when loading data`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))

            val uiStates = mutableListOf<GameInfoUiState>()
            val uiStateJob = launch { SUT.uiState.toList(uiStates) }

            SUT.loadData(resultEmissionDelay = 0L)

            assertThat(uiStates[0] is GameInfoUiState.Empty).isTrue
            assertThat(uiStates[1] is GameInfoUiState.Loading).isTrue
            assertThat(uiStates[2] is GameInfoUiState.Result).isTrue

            uiStateJob.cancel()
        }
    }


    @Test
    fun `Logs error when game fetching use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_API))

            SUT.loadData(resultEmissionDelay = 0L)

            assertThat(logger.errorMessage).isNotEmpty
        }
    }


    @Test
    fun `Dispatches toast showing command when game fetching use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_NOT_FOUND))

            SUT.loadData(resultEmissionDelay = 0L)

            val command = SUT.commandFlow.first()

            assertThat(command is GeneralCommand.ShowLongToast).isTrue
        }
    }


    @Test
    fun `Routes to image viewer screen when artwork is clicked`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))

            SUT.onArtworkClicked(position = 0)

            val route = SUT.routeFlow.first()

            assertThat(route is GameInfoRoute.ImageViewer).isTrue
        }
    }


    @Test
    fun `Routes to previous screen when back button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            SUT.onBackButtonClicked()

            val route = SUT.routeFlow.first()

            assertThat(route is GameInfoRoute.Back).isTrue
        }
    }


    @Test
    fun `Routes to image viewer screen when cover is clicked`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))

            SUT.onCoverClicked()

            val route = SUT.routeFlow.first()

            assertThat(route is GameInfoRoute.ImageViewer).isTrue
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

            SUT.onVideoClicked(video)

            val command = SUT.commandFlow.first()

            assertThat(command is GameInfoCommand.OpenUrl).isTrue
            assertThat((command as GameInfoCommand.OpenUrl).url)
                .isEqualTo(video.videoUrl)
        }
    }


    @Test
    fun `Routes to image viewer screen when screenshot is clicked`() {
        mainCoroutineRule.runBlockingTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))

            SUT.onScreenshotClicked(position = 0)

            val route = SUT.routeFlow.first()

            assertThat(route is GameInfoRoute.ImageViewer).isTrue
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

            SUT.onLinkClicked(link)

            val command = SUT.commandFlow.first()

            assertThat(command is GameInfoCommand.OpenUrl).isTrue
            assertThat((command as GameInfoCommand.OpenUrl).url)
                .isEqualTo(link.payload)
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

            SUT.onCompanyClicked(company)

            val command = SUT.commandFlow.first()

            assertThat(command is GameInfoCommand.OpenUrl).isTrue
            assertThat((command as GameInfoCommand.OpenUrl).url)
                .isEqualTo(company.websiteUrl)
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

            SUT.onRelatedGameClicked(relatedGame)

            val route = SUT.routeFlow.first()

            assertThat(route is GameInfoRoute.Info).isTrue
            assertThat((route as GameInfoRoute.Info).gameId)
                .isEqualTo(relatedGame.id)
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
            game: DomainGame,
            isLiked: Boolean,
            companyGames: List<DomainGame>,
            similarGames: List<DomainGame>
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
                        rating = "rating",
                        likeCount = "like_count",
                        ageRating = "age_rating",
                        gameCategory = "game_category"
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

        override fun createCoverImageUrl(game: DomainGame): String {
            return "cover_image_url"
        }

        override fun createArtworkImageUrls(game: DomainGame): List<String> {
            return listOf("url", "url", "url")
        }

        override fun createScreenshotImageUrls(game: DomainGame): List<String> {
            return listOf("url", "url", "url")
        }

    }


}