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

package com.paulrybitskyi.gamedge.feature.settings.data.datastores

import androidx.datastore.core.Serializer
import com.paulrybitskyi.gamedge.feature.settings.domain.DomainSettings
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
internal object ProtoSettingsSerializer : Serializer<ProtoSettings> {

    override val defaultValue: ProtoSettings
        get() = ProtoSettings.newBuilder()
            .setThemeName(DomainSettings.DEFAULT.theme.name)
            .build()

    override suspend fun writeTo(t: ProtoSettings, output: OutputStream) = t.writeTo(output)
    override suspend fun readFrom(input: InputStream): ProtoSettings = ProtoSettings.parseFrom(input)
}
