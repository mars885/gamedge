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

import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAME
import org.junit.Before
import org.junit.Test

internal class GameLikeCountCalculatorImplTest {

    private lateinit var SUT: GameLikeCountCalculatorImpl

    @Before
    fun setup() {
        SUT = GameLikeCountCalculatorImpl()
    }

    @Test
    fun `Calculates like count properly when both follower count and hype count fields are not null`() {
        val game = DOMAIN_GAME.copy(followerCount = 20, hypeCount = 10)

        assertThat(SUT.calculateLikeCount(game)).isEqualTo(30)
    }

    @Test
    fun `Calculates like count properly when follower count field is null`() {
        val game = DOMAIN_GAME.copy(followerCount = null, hypeCount = 50)

        assertThat(SUT.calculateLikeCount(game)).isEqualTo(50)
    }

    @Test
    fun `Calculates like count properly when hype count field is null`() {
        val game = DOMAIN_GAME.copy(followerCount = 70, hypeCount = null)

        assertThat(SUT.calculateLikeCount(game)).isEqualTo(70)
    }

    @Test
    fun `Calculates like count properly when both follower count and hype count fields are null`() {
        val game = DOMAIN_GAME.copy(followerCount = null, hypeCount = null)

        assertThat(SUT.calculateLikeCount(game)).isEqualTo(0)
    }
}
