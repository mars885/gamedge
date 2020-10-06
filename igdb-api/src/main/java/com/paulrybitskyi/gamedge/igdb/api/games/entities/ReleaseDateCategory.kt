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

internal enum class ReleaseDateCategory(val value: Int) {


    UNKNOWN(value = -1),

    YYYY_MMMM_DD(value = 0),
    YYYY_MMMM(value = 1),
    YYYY(value = 2),
    YYYYQ1(value = 3),
    YYYYQ2(value = 4),
    YYYYQ3(value = 5),
    YYYYQ4(value = 6),

    TBD(value = 7);


    internal companion object {

        fun Int.asReleaseDateCategory(): ReleaseDateCategory {
            return values().find { it.value == this } ?: UNKNOWN
        }

    }


}