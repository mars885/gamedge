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

internal enum class WebsiteCategory(val value: Int) {


    UNKNOWN(value = -1),
    OFFICIAL(value = 1),
    WIKIA(value = 2),
    WIKIPEDIA(value = 3),
    FACEBOOK(value = 4),
    TWITTER(value = 5),
    TWITCH(value = 6),
    INSTAGRAM(value = 8),
    YOUTUBE(value = 9),
    APP_STORE(value = 10),
    GOOGLE_PLAY(value = 12),
    STEAM(value = 13),
    SUBREDDIT(value = 14),
    EPIC_GAMES(value = 16),
    GOG(value = 17),
    DISCORD(value = 18);


    internal companion object {

        fun Int.asWebsiteCategory(): WebsiteCategory {
            return values().find { it.value == this } ?: UNKNOWN
        }

    }


}