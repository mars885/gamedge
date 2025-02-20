/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.feature.settings.presentation

import androidx.compose.runtime.Immutable
import com.paulrybitskyi.gamedge.common.ui.widgets.FiniteUiState

@Immutable
internal data class SettingsUiState(
    val isLoading: Boolean,
    val sections: List<SettingsSectionUiModel>,
    val selectedThemeName: String?,
    val isThemePickerVisible: Boolean,
)

internal val SettingsUiState.finiteUiState: FiniteUiState
    get() = when {
        isInLoadingState -> FiniteUiState.Loading
        isInSuccessState -> FiniteUiState.Success
        else -> error("Unknown settings UI state.")
    }

private val SettingsUiState.isInLoadingState: Boolean
    get() = (isLoading && sections.isEmpty())

private val SettingsUiState.isInSuccessState: Boolean
    get() = sections.isNotEmpty()

internal fun SettingsUiState.toLoadingState(): SettingsUiState {
    return copy(isLoading = true)
}

internal fun SettingsUiState.toSuccessState(
    sections: List<SettingsSectionUiModel>,
    selectedThemeName: String,
): SettingsUiState {
    return copy(
        isLoading = false,
        sections = sections,
        selectedThemeName = selectedThemeName,
    )
}

@Immutable
internal data class SettingsSectionUiModel(
    val id: Int,
    val title: String,
    val items: List<SettingsSectionItemUiModel>,
)

@Immutable
internal data class SettingsSectionItemUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val isClickable: Boolean = true,
)

internal enum class SettingSection(val id: Int) {
    APPEARANCE(id = 1),
    ABOUT(id = 2),
}

internal enum class SettingItem(val id: Int) {
    THEME(id = 1),
    SOURCE_CODE(id = 2),
    VERSION(id = 3),
}
