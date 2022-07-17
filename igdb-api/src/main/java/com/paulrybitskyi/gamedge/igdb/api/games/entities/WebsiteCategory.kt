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

import com.paulrybitskyi.gamedge.igdb.api.games.entities.WebsiteCategory.Companion.asWebsiteCategory
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = WebsiteCategorySerializer::class)
enum class WebsiteCategory(val rawValue: Int) {
    UNKNOWN(rawValue = -1),
    OFFICIAL(rawValue = 1),
    WIKIA(rawValue = 2),
    WIKIPEDIA(rawValue = 3),
    FACEBOOK(rawValue = 4),
    TWITTER(rawValue = 5),
    TWITCH(rawValue = 6),
    INSTAGRAM(rawValue = 8),
    YOUTUBE(rawValue = 9),
    APP_STORE(rawValue = 10),
    GOOGLE_PLAY(rawValue = 12),
    STEAM(rawValue = 13),
    SUBREDDIT(rawValue = 14),
    EPIC_GAMES(rawValue = 16),
    GOG(rawValue = 17),
    DISCORD(rawValue = 18);

    internal companion object {

        fun Int.asWebsiteCategory(): WebsiteCategory {
            return values().find { it.rawValue == this } ?: UNKNOWN
        }
    }
}

internal object WebsiteCategorySerializer : KSerializer<WebsiteCategory> {

    override val descriptor = PrimitiveSerialDescriptor(
        checkNotNull(WebsiteCategorySerializer::class.qualifiedName),
        PrimitiveKind.INT
    )

    override fun serialize(encoder: Encoder, value: WebsiteCategory) {
        encoder.encodeInt(value.rawValue)
    }

    override fun deserialize(decoder: Decoder): WebsiteCategory {
        return decoder.decodeInt().asWebsiteCategory()
    }
}
