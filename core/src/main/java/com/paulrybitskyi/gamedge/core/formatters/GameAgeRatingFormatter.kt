/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

import com.paulrybitskyi.gamedge.common.domain.games.entities.AgeRatingCategory
import com.paulrybitskyi.gamedge.common.domain.games.entities.AgeRatingType
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.core.R
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface GameAgeRatingFormatter {
    fun formatAgeRating(game: Game): String
}

@BindType
internal class GameAgeRatingFormatterImpl @Inject constructor(
    private val stringProvider: StringProvider,
) : GameAgeRatingFormatter {

    override fun formatAgeRating(game: Game): String {
        val ageRatings = game.ageRatings.filterNot {
            it.category == AgeRatingCategory.UNKNOWN ||
            it.type == AgeRatingType.UNKNOWN
        }

        val ageRating = ageRatings.firstOrNull { it.category == AgeRatingCategory.PEGI }
            ?: ageRatings.firstOrNull { it.category == AgeRatingCategory.ESRB }
            ?: return stringProvider.getString(R.string.not_available_abbr)

        return stringProvider.getString(
            R.string.age_rating_template,
            ageRating.category.title,
            ageRating.type.title,
        )
    }
}
