/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.common.testing.domain

import com.paulrybitskyi.gamedge.common.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.common.domain.games.common.throttling.GamesRefreshingThrottlerKeyProvider
import com.paulrybitskyi.gamedge.common.domain.games.entities.Company
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game

class FakeGamesRefreshingThrottlerKeyProvider : GamesRefreshingThrottlerKeyProvider {

    override fun providePopularGamesKey(pagination: Pagination): String {
        return "providePopularGamesKey"
    }

    override fun provideRecentlyReleasedGamesKey(pagination: Pagination): String {
        return "provideRecentlyReleasedGamesKey"
    }

    override fun provideComingSoonGamesKey(pagination: Pagination): String {
        return "provideComingSoonGamesKey"
    }

    override fun provideMostAnticipatedGamesKey(pagination: Pagination): String {
        return "provideMostAnticipatedGamesKey"
    }

    override fun provideCompanyDevelopedGamesKey(
        company: Company,
        pagination: Pagination,
    ): String {
        return "provideCompanyDevelopedGamesKey"
    }

    override fun provideSimilarGamesKey(game: Game, pagination: Pagination): String {
        return "provideSimilarGamesKey"
    }
}
