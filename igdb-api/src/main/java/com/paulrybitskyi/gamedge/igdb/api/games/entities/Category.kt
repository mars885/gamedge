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

import com.paulrybitskyi.gamedge.igdb.api.games.entities.Category.Companion.asCategory
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = CategorySerializer::class)
internal enum class Category(val rawValue: Int) {
    UNKNOWN(rawValue = -1),
    MAIN_GAME(rawValue = 0),
    DLC(rawValue = 1),
    EXPANSION(rawValue = 2),
    BUNDLE(rawValue = 3),
    STANDALONE_EXPANSION(rawValue = 4),
    MOD(rawValue = 5),
    EPISODE(rawValue = 6),
    SEASON(rawValue = 7);

    internal companion object {

        fun Int.asCategory(): Category {
            return values().find { it.rawValue == this } ?: UNKNOWN
        }
    }
}

internal object CategorySerializer : KSerializer<Category> {

    override val descriptor = PrimitiveSerialDescriptor(
        checkNotNull(CategorySerializer::class.qualifiedName),
        PrimitiveKind.INT
    )

    override fun serialize(encoder: Encoder, value: Category) {
        encoder.encodeInt(value.rawValue)
    }

    override fun deserialize(decoder: Decoder): Category {
        return decoder.decodeInt().asCategory()
    }
}
