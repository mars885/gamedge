/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.feature.info.widgets.main

internal enum class GameInfoSection(val id: Int) {
    HEADER(id = 1),
    VIDEOS(id = 2),
    SCREENSHOTS(id = 3),
    SUMMARY(id = 4),
    DETAILS(id = 5),
    LINKS(id = 6),
    COMPANIES(id = 7),
    OTHER_COMPANY_GAMES(id = 8),
    SIMILAR_GAMES(id = 9),
}
