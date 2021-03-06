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

package com.paulrybitskyi.gamedge.feature.image.viewer

import androidx.lifecycle.SavedStateHandle
import com.paulrybitskyi.gamedge.commons.testing.FakeStringProvider
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.commons.testing.MainCoroutineRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val INITIAL_POSITION = 0


internal class ImageViewerViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var SUT: ImageViewerViewModel


    @Before
    fun setup() {
        SUT = ImageViewerViewModel(
            stringProvider = FakeStringProvider(),
            savedStateHandle = setupSavedStateHandle()
        )
    }


    private fun setupSavedStateHandle(): SavedStateHandle {
        return mockk(relaxed = true) {
            every { get<String>(PARAM_TITLE) } returns ""
            every { get<Int>(PARAM_INITIAL_POSITION) } returns INITIAL_POSITION
            every { get<Array<String>>(PARAM_IMAGE_URLS) } returns arrayOf("", "", "")
            every { get<Int>(KEY_SELECTED_POSITION) } returns INITIAL_POSITION
        }
    }


    @Test
    fun `Dispatches text sharing command when toolbar right button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            SUT.onToolbarRightButtonClicked()

            val command = SUT.commandFlow.first()

            assertThat(command is ImageViewerCommand.ShareText).isTrue
        }
    }


    @Test
    fun `Emits selected position when page is changed`() {
        mainCoroutineRule.runBlockingTest {
            val selectedPositions = mutableListOf<Int>()
            val selectedPositionsJob = launch { SUT.selectedPosition.toList(selectedPositions) }

            SUT.onPageChanged(10)

            assertThat(selectedPositions[0] == INITIAL_POSITION).isTrue
            assertThat(selectedPositions[1] == 10).isTrue

            selectedPositionsJob.cancel()
        }
    }


    @Test
    fun `Emits toolbar title when page is changed`() {
        mainCoroutineRule.runBlockingTest {
            val toolbarTitles = mutableListOf<String>()
            val toolbarTitlesJob = launch { SUT.toolbarTitle.toList(toolbarTitles) }

            SUT.onPageChanged(10)

            assertThat(toolbarTitles[0]).isNotEmpty

            toolbarTitlesJob.cancel()
        }
    }


    @Test
    fun `Dispatches system windows reset command when back button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            SUT.onBackPressed()

            val command = SUT.commandFlow.first()

            assertThat(command is ImageViewerCommand.ResetSystemWindows).isTrue
        }
    }


    @Test
    fun `Routes to previous screen when back button is clicked`() {
        mainCoroutineRule.runBlockingTest {
            SUT.onBackPressed()

            val route = SUT.routeFlow.first()

            assertThat(route is ImageViewerRoute.Back).isTrue
        }
    }


}