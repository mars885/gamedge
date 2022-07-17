/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.common.data

import com.paulrybitskyi.gamedge.common.data.games.common.DiscoveryGamesReleaseDatesProvider

internal class FakeDiscoveryGamesReleaseDatesProvider : DiscoveryGamesReleaseDatesProvider {
    override fun getPopularGamesMinReleaseDate(): Long = 500L
    override fun getRecentlyReleasedGamesMinReleaseDate(): Long = 500L
    override fun getRecentlyReleasedGamesMaxReleaseDate(): Long = 500L
    override fun getComingSoonGamesMinReleaseDate(): Long = 500L
    override fun getMostAnticipatedGamesMinReleaseDate(): Long = 500L
}
