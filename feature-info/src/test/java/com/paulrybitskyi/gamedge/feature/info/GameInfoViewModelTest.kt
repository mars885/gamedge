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
import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_ERROR_API
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_ERROR_NOT_FOUND
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_GAME
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.FakeErrorMapper
import com.paulrybitskyi.gamedge.commons.testing.FakeLogger
import com.paulrybitskyi.gamedge.commons.testing.FakeStringProvider
import com.paulrybitskyi.gamedge.commons.testing.MainCoroutineRule
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.core.factories.ImageViewerGameUrlFactory
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameInfoCompanyUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.header.GameInfoHeaderUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameInfoLinkUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.GameInfoUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.GameInfoUiModelMapper
import com.paulrybitskyi.gamedge.feature.info.widgets.main.finiteUiState
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameInfoVideoUiModel
import com.paulrybitskyi.gamedge.feature.info.widgets.relatedgames.GameInfoRelatedGameUiModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class GameInfoViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private lateinit var useCases: GameInfoUseCases
    private lateinit var logger: FakeLogger
    private lateinit var SUT: GameInfoViewModel

    @Before
    fun setup() {
        useCases = setupUseCases()
        logger = FakeLogger()
        SUT = GameInfoViewModel(
            savedStateHandle = setupSavedStateHandle(),
            transitionAnimationDuration = 0L,
            useCases = useCases,
            modelFactory = FakeGameInfoUiModelMapper(),
            gameUrlFactory = FakeGameUrlFactory(),
            dispatcherProvider = FakeDispatcherProvider(),
            stringProvider = FakeStringProvider(),
            errorMapper = FakeErrorMapper(),
            logger = logger,
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
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))

            SUT.uiState.test {
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Empty)
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Loading)
                assertThat(awaitItem().finiteUiState).isEqualTo(FiniteUiState.Success)
            }
        }
    }

    @Test
    fun `Logs error when game fetching use case throws error`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_API))

            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when game fetching use case throws error`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_NOT_FOUND))

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Routes to image viewer screen when artwork is clicked`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))

            val artworkIndex = 10

            SUT.routeFlow.test {
                SUT.onArtworkClicked(artworkIndex = artworkIndex)

                val route = awaitItem()

                assertThat(route).isInstanceOf(GameInfoRoute.ImageViewer::class.java)
                assertThat((route as GameInfoRoute.ImageViewer).initialPosition).isEqualTo(artworkIndex)
            }
        }
    }

    @Test
    fun `Logs error when artwork is clicked and game fetching use case throws error when`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_API))

            SUT.onArtworkClicked(artworkIndex = 0)
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when artwork is clicked and game fetching use case throws error`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_NOT_FOUND))

            advanceUntilIdle()

            SUT.commandFlow.test {
                SUT.onArtworkClicked(artworkIndex = 0)

                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Routes to previous screen when back button is clicked`() {
        runTest {
            SUT.routeFlow.test {
                SUT.onBackButtonClicked()

                assertThat(awaitItem()).isInstanceOf(GameInfoRoute.Back::class.java)
            }
        }
    }

    @Test
    fun `Routes to image viewer screen when cover is clicked`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))

            SUT.routeFlow.test {
                SUT.onCoverClicked()

                assertThat(awaitItem()).isInstanceOf(GameInfoRoute.ImageViewer::class.java)
            }
        }
    }

    @Test
    fun `Logs error when cover is clicked and game fetching use case throws error when`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_API))

            SUT.onCoverClicked()
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when cover is clicked and game fetching use case throws error`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_NOT_FOUND))

            advanceUntilIdle()

            SUT.commandFlow.test {
                SUT.onCoverClicked()

                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Dispatches url opening command when video is clicked`() {
        runTest {
            val video = GameInfoVideoUiModel(
                id = "1",
                thumbnailUrl = "thumbnail_url",
                videoUrl = "video_url",
                title = "title",
            )

            SUT.commandFlow.test {
                SUT.onVideoClicked(video)

                val command = awaitItem()

                assertThat(command).isInstanceOf(GameInfoCommand.OpenUrl::class.java)
                assertThat((command as GameInfoCommand.OpenUrl).url).isEqualTo(video.videoUrl)
            }
        }
    }

    @Test
    fun `Routes to image viewer screen when screenshot is clicked`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))

            val screenshotIndex = 10

            SUT.routeFlow.test {
                SUT.onScreenshotClicked(screenshotIndex = screenshotIndex)

                val route = awaitItem()

                assertThat(route).isInstanceOf(GameInfoRoute.ImageViewer::class.java)
                assertThat((route as GameInfoRoute.ImageViewer).initialPosition).isEqualTo(screenshotIndex)
            }
        }
    }

    @Test
    fun `Logs error when screenshot is clicked and game fetching use case throws error when`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_API))

            SUT.onScreenshotClicked(screenshotIndex = 0)
            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when screenshot is clicked and game fetching use case throws error`() {
        runTest {
            coEvery { useCases.getGameUseCase.execute(any()) } returns flowOf(Err(DOMAIN_ERROR_NOT_FOUND))

            advanceUntilIdle()

            SUT.commandFlow.test {
                SUT.onScreenshotClicked(screenshotIndex = 0)

                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Dispatches url opening command when game link is clicked`() {
        runTest {
            val link = GameInfoLinkUiModel(
                id = 1,
                text = "text",
                iconId = 0,
                url = "url",
            )

            SUT.commandFlow.test {
                SUT.onLinkClicked(link)

                val command = awaitItem()

                assertThat(command).isInstanceOf(GameInfoCommand.OpenUrl::class.java)
                assertThat((command as GameInfoCommand.OpenUrl).url).isEqualTo(link.url)
            }
        }
    }

    @Test
    fun `Dispatches url opening command when company is clicked`() {
        runTest {
            val company = GameInfoCompanyUiModel(
                id = 1,
                logoWidth = null,
                logoHeight = null,
                logoUrl = null,
                websiteUrl = "url",
                name = "",
                roles = "",
            )

            SUT.commandFlow.test {
                SUT.onCompanyClicked(company)

                val command = awaitItem()

                assertThat(command).isInstanceOf(GameInfoCommand.OpenUrl::class.java)
                assertThat((command as GameInfoCommand.OpenUrl).url).isEqualTo(company.websiteUrl)
            }
        }
    }

    @Test
    fun `Routes to game info when related game is clicked`() {
        runTest {
            val relatedGame = GameInfoRelatedGameUiModel(
                id = 1,
                title = "title",
                coverUrl = "url",
            )

            SUT.routeFlow.test {
                SUT.onRelatedGameClicked(relatedGame)

                val route = awaitItem()

                assertThat(route).isInstanceOf(GameInfoRoute.Info::class.java)
                assertThat((route as GameInfoRoute.Info).gameId).isEqualTo(relatedGame.id)
            }
        }
    }

    private class FakeGameInfoUiModelMapper : GameInfoUiModelMapper {

        override fun mapToUiModel(
            game: Game,
            isLiked: Boolean,
            companyGames: List<Game>,
            similarGames: List<Game>
        ): GameInfoUiModel {
            return GameInfoUiModel(
                id = 1,
                headerModel = GameInfoHeaderUiModel(
                    artworks = emptyList(),
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
                screenshotModels = emptyList(),
                summary = null,
                detailsModel = null,
                linkModels = emptyList(),
                companyModels = emptyList(),
                otherCompanyGames = null,
                similarGames = null
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
