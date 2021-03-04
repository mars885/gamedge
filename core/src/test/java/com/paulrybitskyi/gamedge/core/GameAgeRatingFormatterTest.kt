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

import com.paulrybitskyi.gamedge.core.formatters.GameAgeRatingFormatterImpl
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.games.DomainCategory
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.entities.AgeRating
import com.paulrybitskyi.gamedge.domain.games.entities.AgeRatingCategory
import com.paulrybitskyi.gamedge.domain.games.entities.AgeRatingType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val DOMAIN_GAME = DomainGame(
    id = 1,
    followerCount = null,
    hypeCount = null,
    releaseDate = null,
    criticsRating = null,
    usersRating = null,
    totalRating = null,
    name = "name",
    summary = null,
    storyline = null,
    category = DomainCategory.UNKNOWN,
    cover = null,
    releaseDates = listOf(),
    ageRatings = listOf(),
    videos = listOf(),
    artworks = listOf(),
    screenshots = listOf(),
    genres = listOf(),
    platforms = listOf(),
    playerPerspectives = listOf(),
    themes = listOf(),
    modes = listOf(),
    keywords = listOf(),
    involvedCompanies = listOf(),
    websites = listOf(),
    similarGames = listOf()
)


internal class GameAgeRatingFormatterTest {


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
                AgeRating(AgeRatingCategory.PEGI, AgeRatingType.AO)
            )
        )

        SUT.formatAgeRating(game)

        assertTrue(stringProvider.isRatingAvailable)
    }


    @Test
    fun `Returns not available string when game does not contain any ratings`() {
        SUT.formatAgeRating(DOMAIN_GAME)

        assertTrue(stringProvider.isRatingNotAvailable)
    }


    @Test
    fun `Returns not available string when game does not contain valid ratings`() {
        val game = DOMAIN_GAME.copy(
            ageRatings = listOf(
                AgeRating(AgeRatingCategory.UNKNOWN, AgeRatingType.AO),
                AgeRating(AgeRatingCategory.PEGI, AgeRatingType.UNKNOWN)
            )
        )

        SUT.formatAgeRating(game)

        assertTrue(stringProvider.isRatingNotAvailable)
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