/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.feature.info.domain

import app.cash.turbine.test
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAME
import com.paulrybitskyi.gamedge.common.testing.domain.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.entities.Error
import com.paulrybitskyi.gamedge.core.factories.ImageViewerGameUrlFactory
import com.paulrybitskyi.gamedge.feature.info.domain.entities.GameImageType
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameImageUrlsUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameImageUrlsUseCaseImpl
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val USE_CASE_PARAMS = GetGameImageUrlsUseCase.Params(
    gameId = 0,
    imageType = GameImageType.COVER,
)

internal class GetGameImageUrlsUseCaseImplTest {

    @MockK private lateinit var getGameUseCase: GetGameUseCase
    @MockK private lateinit var gameUrlFactory: ImageViewerGameUrlFactory

    private lateinit var SUT: GetGameImageUrlsUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = GetGameImageUrlsUseCaseImpl(
            getGameUseCase = getGameUseCase,
            gameUrlFactory = gameUrlFactory,
            dispatcherProvider = FakeDispatcherProvider(),
        )
    }

    @Test
    fun `Emits game cover image url successfully`() {
        runTest {
            val expectedImageUrl = "cover_image_url"

            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))
            coEvery { gameUrlFactory.createCoverImageUrl(DOMAIN_GAME) } returns expectedImageUrl

            SUT.execute(USE_CASE_PARAMS.copy(imageType = GameImageType.COVER)).test {
                val actualImageUrl = awaitItem()

                assertThat(actualImageUrl.get()).isNotNull()
                assertThat(actualImageUrl.get()!!.first()).isEqualTo(expectedImageUrl)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Emits error when game cover image url cannot be created`() {
        runTest {
            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))
            coEvery { gameUrlFactory.createCoverImageUrl(DOMAIN_GAME) } returns null

            SUT.execute(USE_CASE_PARAMS.copy(imageType = GameImageType.COVER)).test {
                val errorResult = awaitItem()

                assertThat(errorResult.getError()).isNotNull()
                assertThat(errorResult.getError()).isInstanceOf(Error.Unknown::class.java)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Emits game artwork image urls successfully`() {
        runTest {
            val expectedArtworkImageUrls = listOf(
                "artwork_image_url_1",
                "artwork_image_url_2",
                "artwork_image_url_3",
            )

            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))
            coEvery { gameUrlFactory.createArtworkImageUrls(DOMAIN_GAME) } returns expectedArtworkImageUrls

            SUT.execute(USE_CASE_PARAMS.copy(imageType = GameImageType.ARTWORK)).test {
                val actualArtworkImageUrls = awaitItem()

                assertThat(actualArtworkImageUrls.get()).isNotNull()
                assertThat(actualArtworkImageUrls.get()).isEqualTo(expectedArtworkImageUrls)
                awaitComplete()
            }
        }
    }

    @Test
    fun `Emits game screenshot image urls successfully`() {
        runTest {
            val expectedScreenshotImageUrls = listOf(
                "screenshot_image_url_1",
                "screenshot_image_url_2",
                "screenshot_image_url_3",
            )

            coEvery { getGameUseCase.execute(any()) } returns flowOf(Ok(DOMAIN_GAME))
            coEvery { gameUrlFactory.createScreenshotImageUrls(DOMAIN_GAME) } returns expectedScreenshotImageUrls

            SUT.execute(USE_CASE_PARAMS.copy(imageType = GameImageType.SCREENSHOT)).test {
                val actualScreenshotImageUrls = awaitItem()

                assertThat(actualScreenshotImageUrls.get()).isNotNull()
                assertThat(actualScreenshotImageUrls.get()).isEqualTo(expectedScreenshotImageUrls)
                awaitComplete()
            }
        }
    }
}
