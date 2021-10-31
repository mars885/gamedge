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

package com.paulrybitskyi.gamedge.core

import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_GAME
import com.paulrybitskyi.gamedge.core.formatters.GameAgeRatingFormatterImpl
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.games.DomainAgeRating
import com.paulrybitskyi.gamedge.domain.games.DomainAgeRatingCategory
import com.paulrybitskyi.gamedge.domain.games.DomainAgeRatingType
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class GameAgeRatingFormatterImplTest {

    private lateinit var stringProvider: FakeStringProvider
    private lateinit var SUT: GameAgeRatingFormatterImpl

    @Before
    fun setup() {
        stringProvider = FakeStringProvider()
        SUT = GameAgeRatingFormatterImpl(stringProvider)
    }

    @Test
    fun `Returns properly formatted string with age rating`() {
        val game = DOMAIN_GAME.copy(
            ageRatings = listOf(
                DomainAgeRating(DomainAgeRatingCategory.PEGI, DomainAgeRatingType.AO)
            )
        )

        SUT.formatAgeRating(game)

        assertThat(stringProvider.isRatingAvailable).isTrue
    }

    @Test
    fun `Returns not available string when game does not contain any ratings`() {
        SUT.formatAgeRating(DOMAIN_GAME)

        assertThat(stringProvider.isRatingNotAvailable).isTrue
    }

    @Test
    fun `Returns not available string when game does not contain valid ratings`() {
        val game = DOMAIN_GAME.copy(
            ageRatings = listOf(
                DomainAgeRating(DomainAgeRatingCategory.UNKNOWN, DomainAgeRatingType.AO),
                DomainAgeRating(DomainAgeRatingCategory.PEGI, DomainAgeRatingType.UNKNOWN)
            )
        )

        SUT.formatAgeRating(game)

        assertThat(stringProvider.isRatingNotAvailable).isTrue
    }

    private class FakeStringProvider : StringProvider {

        var isRatingAvailable = false
        var isRatingNotAvailable = false

        override fun getString(id: Int, vararg args: Any): String {
            isRatingAvailable = (id == R.string.age_rating_template)
            isRatingNotAvailable = (id == R.string.not_available_abbr)

            return ""
        }

        override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String {
            return ""
        }
    }
}
