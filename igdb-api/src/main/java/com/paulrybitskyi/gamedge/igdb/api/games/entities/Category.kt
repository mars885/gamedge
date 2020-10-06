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

internal enum class Category(val value: Int) {


    UNKNOWN(value = -1),
    MAIN_GAME(value = 0),
    DLC(value = 1),
    EXPANSION(value = 2),
    BUNDLE(value = 3),
    STANDALONE_EXPANSION(value = 4),
    MOD(value = 5),
    EPISODE(value = 6),
    SEASON(value = 7);


    internal companion object {

        fun Int.asCategory(): Category {
            return values().find { it.value == this } ?: UNKNOWN
        }

    }


}