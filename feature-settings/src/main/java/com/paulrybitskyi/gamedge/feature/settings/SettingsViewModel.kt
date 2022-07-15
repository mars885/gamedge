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

import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.core.Constants
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.domain.settings.entities.Settings
import com.paulrybitskyi.gamedge.domain.settings.entities.Theme
import com.paulrybitskyi.gamedge.domain.settings.usecases.ObserveSettingsUseCase
import com.paulrybitskyi.gamedge.domain.settings.usecases.SaveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val saveSettingsUseCase: SaveSettingsUseCase,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val settingsModelUiMapper: SettingsModelUiMapper,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(createLoadingUiState())

    private val currentUiState: SettingsUiState
        get() = _uiState.value

    val uiState: StateFlow<SettingsUiState>
        get() = _uiState

    init {
        observeSettings()
    }

    private fun createLoadingUiState(): SettingsUiState {
        return SettingsUiState(
            isLoading = false,
            sections = emptyList(),
            selectedThemeName = null,
            isThemePickerVisible = false,
        )
        .toLoadingState()
    }

    private fun observeSettings() {
        observeSettingsUseCase.execute()
            .map { settings ->
                val sections = settingsModelUiMapper.mapToUiModels(settings)
                val selectedThemeName = settings.theme.name

                sections to selectedThemeName
            }
            .flowOn(dispatcherProvider.computation)
            .map { (sections, selectedThemeName) ->
                currentUiState.toSuccessState(sections, selectedThemeName)
            }
            .onStart { emit(currentUiState.toLoadingState()) }
            .onEach { emittedUiState -> _uiState.update { emittedUiState } }
            .launchIn(viewModelScope)
    }

    fun onSettingClicked(item: SettingsSectionItemUiModel) {
        when (item.id) {
            SettingItem.THEME.id -> onThemeSettingClicked()
            SettingItem.SOURCE_CODE.id -> onSourceCodeSettingClicked()
        }
    }

    private fun onThemeSettingClicked() {
        _uiState.update { it.copy(isThemePickerVisible = true) }
    }

    fun onThemePicked(theme: Theme) {
        onThemePickerDismissed()

        updateSettings { oldSettings ->
            oldSettings.copy(theme = theme)
        }
    }

    private fun updateSettings(newSettingsProvider: (old: Settings) -> Settings) {
        viewModelScope.launch {
            val oldSettings = observeSettingsUseCase.execute().first()
            val newSettings = newSettingsProvider(oldSettings)

            saveSettingsUseCase.execute(newSettings)
        }
    }

    fun onThemePickerDismissed() {
        _uiState.update { it.copy(isThemePickerVisible = false) }
    }

    private fun onSourceCodeSettingClicked() {
        dispatchCommand(SettingsCommand.OpenUrl(Constants.SOURCE_CODE_LINK))
    }
}
