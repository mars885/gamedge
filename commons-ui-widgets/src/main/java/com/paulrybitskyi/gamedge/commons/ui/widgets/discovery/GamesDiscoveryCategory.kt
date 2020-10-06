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

package com.paulrybitskyi.gamedge.commons.ui.widgets.discovery

import androidx.annotation.StringRes
import com.paulrybitskyi.gamedge.commons.ui.widgets.R


enum class GamesDiscoveryCategory(@StringRes val titleId: Int) {

    POPULAR(R.string.games_category_popular),
    RECENTLY_RELEASED(R.string.games_category_recently_released),
    COMING_SOON(R.string.games_category_coming_soon),
    MOST_ANTICIPATED(R.string.games_category_most_anticipated)

}