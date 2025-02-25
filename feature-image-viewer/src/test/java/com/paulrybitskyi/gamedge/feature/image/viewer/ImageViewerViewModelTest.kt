/*
 * Copyright 2021 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.feature.image.viewer

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.FakeStringProvider
import com.paulrybitskyi.gamedge.common.testing.domain.MainCoroutineRule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

private const val INITIAL_POSITION = 0

internal class ImageViewerViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val SUT by lazy {
        ImageViewerViewModel(
            route = ImageViewerRoute(
                imageUrls = listOf("url1", "url2", "url3"),
                title = "",
                initialPosition = INITIAL_POSITION,
            ),
            stringProvider = FakeStringProvider(),
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(KEY_SELECTED_POSITION to INITIAL_POSITION),
            ),
        )
    }

    @Test
    fun `Dispatches text sharing command when toolbar right button is clicked`() {
        runTest {
            SUT.commandFlow.test {
                SUT.onToolbarRightButtonClicked()

                assertThat(awaitItem()).isInstanceOf(ImageViewerCommand.ShareText::class.java)
            }
        }
    }

    @Test
    fun `Emits selected position when page is changed`() {
        runTest {
            val selectedPosition = 10

            SUT.uiState.test {
                SUT.onImageChanged(selectedPosition)

                assertThat(awaitItem().selectedImageUrlIndex).isEqualTo(INITIAL_POSITION)
                assertThat(awaitItem().selectedImageUrlIndex).isEqualTo(selectedPosition)
            }
        }
    }

    @Test
    fun `Emits toolbar title when page is changed`() {
        runTest {
            SUT.onImageChanged(10)
            advanceUntilIdle()

            SUT.uiState.test {
                assertThat(awaitItem().toolbarTitle).isNotEmpty()
            }
        }
    }

    @Test
    fun `Navigates to previous screen when back button is clicked`() {
        runTest {
            SUT.directionFlow.test {
                SUT.onBackPressed()

                assertThat(awaitItem()).isInstanceOf(ImageViewerDirection.Back::class.java)
            }
        }
    }
}
