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

package com.paulrybitskyi.gamedge.ui.category

import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.ui.category.di.GamesCategoryKey


internal val GamesCategory.titleId: Int
    get() = when(this) {
        GamesCategory.POPULAR -> R.string.games_category_popular
        GamesCategory.RECENTLY_RELEASED -> R.string.games_category_recently_released
        GamesCategory.COMING_SOON -> R.string.games_category_coming_soon
        GamesCategory.MOST_ANTICIPATED -> R.string.games_category_most_anticipated
    }


internal fun GamesCategory.toKeyType(): GamesCategoryKey.Type {
    return when(this) {
        GamesCategory.POPULAR -> GamesCategoryKey.Type.POPULAR
        GamesCategory.RECENTLY_RELEASED -> GamesCategoryKey.Type.RECENTLY_RELEASED
        GamesCategory.COMING_SOON -> GamesCategoryKey.Type.COMING_SOON
        GamesCategory.MOST_ANTICIPATED -> GamesCategoryKey.Type.MOST_ANTICIPATED
    }
}