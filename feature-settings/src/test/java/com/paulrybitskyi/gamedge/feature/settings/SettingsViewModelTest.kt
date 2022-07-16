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

package com.paulrybitskyi.gamedge.feature.settings

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_SETTINGS
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.commons.testing.MainCoroutineRule
import com.paulrybitskyi.gamedge.commons.ui.widgets.FiniteUiState
import com.paulrybitskyi.gamedge.core.Constants
import com.paulrybitskyi.gamedge.domain.commons.extensions.execute
import com.paulrybitskyi.gamedge.domain.settings.entities.Settings
import com.paulrybitskyi.gamedge.domain.settings.entities.Theme
import com.paulrybitskyi.gamedge.domain.settings.usecases.ObserveSettingsUseCase
import com.paulrybitskyi.gamedge.domain.settings.usecases.SaveSettingsUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class SettingsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val saveSettingsUseCase = mockk<SaveSettingsUseCase>(relaxed = true)
    private val observeSettingsUseCase = mockk<ObserveSettingsUseCase>(relaxed = true)

    private val SUT by lazy {
        SettingsViewModel(
            saveSettingsUseCase = saveSettingsUseCase,
            observeSettingsUseCase = observeSettingsUseCase,
            settingsModelUiMapper = FakeSettingsModelUiMapper(),
            dispatcherProvider = FakeDispatcherProvider(),
        )
    }

    @Test
    fun `Emits correct ui states when initialization starts`() {
        runTest {
            every { observeSettingsUseCase.execute() } returns flowOf(DOMAIN_SETTINGS)

            SUT.uiState.test {
                val loadingState = awaitItem()
                val resultState = awaitItem()

                assertThat(loadingState.finiteUiState).isEqualTo(FiniteUiState.Loading)
                assertThat(resultState.finiteUiState).isEqualTo(FiniteUiState.Success)
                assertThat(resultState.sections).hasSize(FakeSettingsModelUiMapper.SECTION_ITEM_COUNT)
                assertThat(resultState.selectedThemeName).isEqualTo(DOMAIN_SETTINGS.theme.name)
            }
        }
    }

    @Test
    fun `Shows theme picker if theme setting is clicked`() {
        runTest {
            SUT.uiState.test {
                SUT.onSettingClicked(createSettingUiModel(SettingItem.THEME))

                val stateWithInvisiblePicker = awaitItem()
                val stateWithVisiblePicker = awaitItem()

                assertThat(stateWithInvisiblePicker.isThemePickerVisible).isFalse()
                assertThat(stateWithVisiblePicker.isThemePickerVisible).isTrue()
            }
        }
    }

    @Test
    fun `Hides theme picker when theme gets picked`() {
        runTest {
            SUT.onSettingClicked(createSettingUiModel(SettingItem.THEME))

            SUT.uiState.test {
                SUT.onThemePicked(Theme.LIGHT)

                val stateWithVisiblePicker = awaitItem()
                val stateWithInvisiblePicker = awaitItem()

                assertThat(stateWithVisiblePicker.isThemePickerVisible).isTrue()
                assertThat(stateWithInvisiblePicker.isThemePickerVisible).isFalse()
            }
        }
    }

    @Test
    fun `Updates setting with new theme when theme gets picked`() {
        runTest {
            val defaultSettings = DOMAIN_SETTINGS.copy(theme = Theme.LIGHT)
            val newSettings = defaultSettings.copy(theme = Theme.DARK)

            every { observeSettingsUseCase.execute() } returns flowOf(defaultSettings)

            SUT.onThemePicked(newSettings.theme)

            advanceUntilIdle()

            coVerify { saveSettingsUseCase.execute(newSettings) }
        }
    }

    @Test
    fun `Hides theme picker when picker gets dismissed`() {
        runTest {
            SUT.onSettingClicked(createSettingUiModel(SettingItem.THEME))

            SUT.uiState.test {
                SUT.onThemePickerDismissed()

                val stateWithVisiblePicker = awaitItem()
                val stateWithInvisiblePicker = awaitItem()

                assertThat(stateWithVisiblePicker.isThemePickerVisible).isTrue()
                assertThat(stateWithInvisiblePicker.isThemePickerVisible).isFalse()
            }
        }
    }

    @Test
    fun `Opens source code link if source code setting is clicked`() {
        runTest {
            SUT.commandFlow.test {
                SUT.onSettingClicked(createSettingUiModel(SettingItem.SOURCE_CODE))

                val command = awaitItem()

                assertThat(command).isInstanceOf(SettingsCommand.OpenUrl::class.java)
                assertThat((command as SettingsCommand.OpenUrl).url).isEqualTo(Constants.SOURCE_CODE_LINK)
            }
        }
    }

    private fun createSettingUiModel(setting: SettingItem): SettingsSectionItemUiModel {
        return SettingsSectionItemUiModel(
            id = setting.id,
            title = "title",
            description = "description",
        )
    }

    private class FakeSettingsModelUiMapper : SettingsModelUiMapper {

        companion object {
            const val SECTION_ITEM_COUNT = 3
        }

        override fun mapToUiModels(settings: Settings): List<SettingsSectionUiModel> {
            return buildList {
                repeat(SECTION_ITEM_COUNT) { index ->
                    add(
                        SettingsSectionUiModel(
                            id = index,
                            title = "title$index",
                            items = emptyList(),
                        )
                    )
                }
            }
        }
    }
}
