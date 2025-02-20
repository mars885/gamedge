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

package com.paulrybitskyi.gamedge.feature.settings.data

import androidx.datastore.core.DataStore
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.feature.settings.DOMAIN_SETTINGS
import com.paulrybitskyi.gamedge.feature.settings.data.datastores.ProtoSettings
import com.paulrybitskyi.gamedge.feature.settings.data.datastores.ProtoSettingsMapper
import com.paulrybitskyi.gamedge.feature.settings.data.datastores.SettingsFileDataStore
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val PROTO_SETTINGS = ProtoSettings.newBuilder()
    .setThemeName(DOMAIN_SETTINGS.theme.name)
    .build()

internal class SettingsFileDataStoreTest {

    @MockK private lateinit var protoDataStore: DataStore<ProtoSettings>

    private lateinit var SUT: SettingsFileDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = SettingsFileDataStore(
            protoDataStore = protoDataStore,
            protoMapper = ProtoSettingsMapper(),
        )
    }

    @Test
    fun `Saves settings successfully`() {
        runTest {
            coEvery { protoDataStore.updateData(any()) } returns PROTO_SETTINGS

            SUT.saveSettings(DOMAIN_SETTINGS)

            coVerify { protoDataStore.updateData(any()) }
        }
    }

    @Test
    fun `Observes settings successfully`() {
        runTest {
            coEvery { protoDataStore.data } returns flowOf(PROTO_SETTINGS)

            assertThat(SUT.observeSettings().first()).isEqualTo(DOMAIN_SETTINGS)
        }
    }
}
