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

package com.paulrybitskyi.gamedge.feature.settings.presentation

import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.providers.VersionNameProvider
import com.paulrybitskyi.gamedge.feature.settings.R
import com.paulrybitskyi.gamedge.feature.settings.domain.entities.Settings
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface SettingsUiModelMapper {
    fun mapToUiModels(settings: Settings): List<SettingsSectionUiModel>
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class SettingsUiModelMapperImpl @Inject constructor(
    private val stringProvider: StringProvider,
    private val versionNameProvider: VersionNameProvider,
) : SettingsUiModelMapper {

    override fun mapToUiModels(settings: Settings): List<SettingsSectionUiModel> {
        return listOf(
            createAppearanceSection(settings),
            createAboutSection(),
        )
    }

    private fun createAppearanceSection(settings: Settings): SettingsSectionUiModel {
        return SettingsSectionUiModel(
            id = SettingSection.APPEARANCE.id,
            title = stringProvider.getString(R.string.settings_section_appearance_title),
            items = listOf(
                SettingsSectionItemUiModel(
                    id = SettingItem.THEME.id,
                    title = stringProvider.getString(R.string.settings_item_theme_title),
                    description = stringProvider.getString(settings.theme.uiTextRes),
                )
            )
        )
    }

    private fun createAboutSection(): SettingsSectionUiModel {
        return SettingsSectionUiModel(
            id = SettingSection.ABOUT.id,
            title = stringProvider.getString(R.string.settings_section_about_title),
            items = listOf(
                SettingsSectionItemUiModel(
                    id = SettingItem.SOURCE_CODE.id,
                    title = stringProvider.getString(R.string.settings_item_source_code_title),
                    description = stringProvider.getString(R.string.settings_item_source_code_description),
                ),
                SettingsSectionItemUiModel(
                    id = SettingItem.VERSION.id,
                    title = stringProvider.getString(R.string.settings_item_version_title),
                    description = versionNameProvider.getVersionName(),
                    isClickable = false,
                )
            )
        )
    }
}
