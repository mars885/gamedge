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

package com.paulrybitskyi.gamedge.igdb.api.entities

internal enum class WebsiteCategory(val value: Int) {


    UNKNOWN(value = -1),
    OFFICIAL(value = -1),
    WIKIA(value = -1),
    WIKIPEDIA(value = -1),
    FACEBOOK(value = -1),
    TWITTER(value = -1),
    TWITCH(value = -1),
    INSTAGRAM(value = -1),
    YOUTUBE(value = -1),
    STEAM(value = -1),
    REDDIT(value = -1),
    DISCORD(value = -1);


    internal companion object {

        fun Int.asWebsiteCategory(): WebsiteCategory {
            return values().find { it.value == this } ?: UNKNOWN
        }

    }


}