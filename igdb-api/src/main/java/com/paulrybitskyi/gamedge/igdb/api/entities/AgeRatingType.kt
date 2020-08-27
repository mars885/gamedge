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

enum class AgeRatingType(val value: Int) {


    UNKNOWN(value = -1),
    THREE(value = 1),
    SEVEN(value = 2),
    TWELVE(value = 3),
    SIXTEEN(value = 4),
    EIGHTEEN(value = 5),
    RP(value = 6),
    EC(value = 7),
    E(value = 8),
    E10(value = 9),
    T(value = 10),
    M(value = 11),
    AO(value = 12);


    internal companion object {

        fun Int.asAgeRatingType(): AgeRatingType {
            return values().find { it.value == this } ?: UNKNOWN
        }

    }


}