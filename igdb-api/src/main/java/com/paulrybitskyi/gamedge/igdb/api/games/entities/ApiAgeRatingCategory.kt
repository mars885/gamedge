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

import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiAgeRatingCategory.Companion.asAgeRatingCategory
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = AgeRatingCategorySerializer::class)
enum class ApiAgeRatingCategory(val rawValue: Int) {
    UNKNOWN(rawValue = -1),
    ESRB(rawValue = 1),
    PEGI(rawValue = 2);

    internal companion object {

        fun Int.asAgeRatingCategory(): ApiAgeRatingCategory {
            return values().find { it.rawValue == this } ?: UNKNOWN
        }
    }
}

internal object AgeRatingCategorySerializer : KSerializer<ApiAgeRatingCategory> {

    override val descriptor = PrimitiveSerialDescriptor(
        checkNotNull(AgeRatingCategorySerializer::class.qualifiedName),
        PrimitiveKind.INT
    )

    override fun serialize(encoder: Encoder, value: ApiAgeRatingCategory) {
        encoder.encodeInt(value.rawValue)
    }

    override fun deserialize(decoder: Decoder): ApiAgeRatingCategory {
        return decoder.decodeInt().asAgeRatingCategory()
    }
}
