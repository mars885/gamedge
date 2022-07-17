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

package com.paulrybitskyi.gamedge.feature.discovery

import com.paulrybitskyi.gamedge.feature.discovery.di.GamesDiscoveryKey

internal val GamesDiscoveryCategory.titleId: Int
    get() = when (this) {
        GamesDiscoveryCategory.POPULAR -> R.string.games_category_popular
        GamesDiscoveryCategory.RECENTLY_RELEASED -> R.string.games_category_recently_released
        GamesDiscoveryCategory.COMING_SOON -> R.string.games_category_coming_soon
        GamesDiscoveryCategory.MOST_ANTICIPATED -> R.string.games_category_most_anticipated
    }

internal fun GamesDiscoveryCategory.toKeyType(): GamesDiscoveryKey.Type {
    return when (this) {
        GamesDiscoveryCategory.POPULAR -> GamesDiscoveryKey.Type.POPULAR
        GamesDiscoveryCategory.RECENTLY_RELEASED -> GamesDiscoveryKey.Type.RECENTLY_RELEASED
        GamesDiscoveryCategory.COMING_SOON -> GamesDiscoveryKey.Type.COMING_SOON
        GamesDiscoveryCategory.MOST_ANTICIPATED -> GamesDiscoveryKey.Type.MOST_ANTICIPATED
    }
}
