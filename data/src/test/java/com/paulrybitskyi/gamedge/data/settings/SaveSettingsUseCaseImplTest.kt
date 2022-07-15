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

package com.paulrybitskyi.gamedge.data.settings

import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_SETTINGS
import com.paulrybitskyi.gamedge.data.settings.datastores.SettingsLocalDataStore
import com.paulrybitskyi.gamedge.data.settings.usecases.SaveSettingsUseCaseImpl
import com.paulrybitskyi.gamedge.data.settings.usecases.commons.SettingsMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class SaveSettingsUseCaseImplTest {

    private lateinit var settingsLocalDataStore: SettingsLocalDataStore
    private lateinit var settingsMapper: SettingsMapper
    private lateinit var SUT: SaveSettingsUseCaseImpl

    @Before
    fun setup() {
        settingsLocalDataStore = FakeSettingsLocalDataStore()
        settingsMapper = SettingsMapper()
        SUT = SaveSettingsUseCaseImpl(
            localDataStore = settingsLocalDataStore,
            settingsMapper = settingsMapper,
        )
    }

    @Test
    fun `Saves settings into local data store`() {
        runTest {
            val settings = DOMAIN_SETTINGS

            SUT.execute(settings)

            assertThat(settingsLocalDataStore.observeSettings().first())
                .isEqualTo(settingsMapper.mapToDataSettings(settings))
        }
    }

    private class FakeSettingsLocalDataStore : SettingsLocalDataStore {

        private var settings: DataSettings? = null

        override suspend fun saveSettings(settings: DataSettings) {
            this.settings = settings
        }

        override fun observeSettings(): Flow<DataSettings> {
            return if (settings == null) emptyFlow() else flowOf(checkNotNull(settings))
        }
    }
}
