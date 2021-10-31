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

package com.paulrybitskyi.gamedge.domain.games.entities

enum class AgeRatingType(val title: String) {

    UNKNOWN(title = ""),
    THREE(title = "3"),
    SEVEN(title = "7"),
    TWELVE(title = "12"),
    SIXTEEN(title = "16"),
    EIGHTEEN(title = "18"),
    RP(title = "RP"),
    EC(title = "EC"),
    E(title = "E"),
    E10(title = "E10"),
    T(title = "T"),
    M(title = "M"),
    AO(title = "AO"),

}
