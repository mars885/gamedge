/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.igdb.api.games.entities

import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiAgeRatingType.Companion.asAgeRatingType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = AgeRatingTypeSerializer::class)
enum class ApiAgeRatingType(val rawValue: Int) {
    UNKNOWN(rawValue = -1),
    THREE(rawValue = 1),
    SEVEN(rawValue = 2),
    TWELVE(rawValue = 3),
    SIXTEEN(rawValue = 4),
    EIGHTEEN(rawValue = 5),
    RP(rawValue = 6),
    EC(rawValue = 7),
    E(rawValue = 8),
    E10(rawValue = 9),
    T(rawValue = 10),
    M(rawValue = 11),
    AO(rawValue = 12);

    internal companion object {

        fun Int.asAgeRatingType(): ApiAgeRatingType {
            return values().find { it.rawValue == this } ?: UNKNOWN
        }
    }
}

internal object AgeRatingTypeSerializer : KSerializer<ApiAgeRatingType> {

    override val descriptor = PrimitiveSerialDescriptor(
        checkNotNull(AgeRatingTypeSerializer::class.qualifiedName),
        PrimitiveKind.INT
    )

    override fun serialize(encoder: Encoder, value: ApiAgeRatingType) {
        encoder.encodeInt(value.rawValue)
    }

    override fun deserialize(decoder: Decoder): ApiAgeRatingType {
        return decoder.decodeInt().asAgeRatingType()
    }
}
