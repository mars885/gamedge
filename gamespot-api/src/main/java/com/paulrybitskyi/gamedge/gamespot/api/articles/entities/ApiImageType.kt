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

package com.paulrybitskyi.gamedge.gamespot.api.articles.entities

import com.paulrybitskyi.gamedge.gamespot.api.articles.entities.ApiImageType.Companion.asImageType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ImageTypeSerializer::class)
enum class ApiImageType(val rawType: String) {
    UNKNOWN("unknown"),
    SQUARE_TINY("square_tiny"),
    SQUARE_SMALL("square_small"),
    SCREEN_TINY("screen_tiny"),
    ORIGINAL("original"),
    ;

    internal companion object {

        fun String.asImageType(): ApiImageType {
            return values().find { it.rawType == this } ?: UNKNOWN
        }
    }
}

internal object ImageTypeSerializer : KSerializer<ApiImageType> {

    override val descriptor = PrimitiveSerialDescriptor(
        checkNotNull(ImageTypeSerializer::class.qualifiedName),
        PrimitiveKind.STRING,
    )

    override fun serialize(encoder: Encoder, value: ApiImageType) {
        encoder.encodeString(value.rawType)
    }

    override fun deserialize(decoder: Decoder): ApiImageType {
        return decoder.decodeString().asImageType()
    }
}
