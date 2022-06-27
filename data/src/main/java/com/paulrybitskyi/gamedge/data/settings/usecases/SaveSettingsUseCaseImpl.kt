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

package com.paulrybitskyi.gamedge.data.settings.usecases

import com.paulrybitskyi.gamedge.data.settings.datastores.SettingsLocalDataStore
import com.paulrybitskyi.gamedge.data.settings.usecases.commons.SettingsMapper
import com.paulrybitskyi.gamedge.domain.settings.entities.Settings
import com.paulrybitskyi.gamedge.domain.settings.usecases.SaveSettingsUseCase
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class SaveSettingsUseCaseImpl @Inject constructor(
    private val localDataStore: SettingsLocalDataStore,
    private val settingsMapper: SettingsMapper,
) : SaveSettingsUseCase {

    override suspend fun execute(params: Settings) {
        localDataStore.saveSettings(
            settings = settingsMapper.mapToDataSettings(params),
        )
    }
}
