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

package com.paulrybitskyi.gamedge.core

import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface GameLikeCountCalculator {
    fun calculateLikeCount(game: Game): Int
}

@BindType
internal class GameLikeCountCalculatorImpl @Inject constructor() : GameLikeCountCalculator {

    override fun calculateLikeCount(game: Game): Int {
        val followerCount = (game.followerCount ?: 0)
        val hypeCount = (game.hypeCount ?: 0)
        val likeCount = (followerCount + hypeCount)

        return likeCount
    }
}
