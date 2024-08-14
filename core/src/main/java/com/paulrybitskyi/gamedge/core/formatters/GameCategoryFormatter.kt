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

package com.paulrybitskyi.gamedge.core.formatters

import com.paulrybitskyi.gamedge.common.domain.games.entities.Category
import com.paulrybitskyi.gamedge.core.R
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface GameCategoryFormatter {
    fun formatCategory(category: Category): String
}

@BindType
internal class GameCategoryFormatterImpl @Inject constructor(
    private val stringProvider: StringProvider,
) : GameCategoryFormatter {

    override fun formatCategory(category: Category): String {
        return stringProvider.getString(
            when (category) {
                Category.UNKNOWN -> R.string.not_available_abbr
                Category.MAIN_GAME -> R.string.game_category_main
                Category.BUNDLE -> R.string.game_category_bundle
                Category.MOD -> R.string.game_category_mod
                Category.EPISODE -> R.string.game_category_episode
                Category.SEASON -> R.string.game_category_season

                Category.DLC,
                Category.EXPANSION,
                Category.STANDALONE_EXPANSION,
                -> R.string.game_category_dlc
            },
        )
    }
}
