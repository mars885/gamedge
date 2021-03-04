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

package com.paulrybitskyi.gamedge.data.games.utils

import com.paulrybitskyi.gamedge.data.games.usecases.commons.throttling.GamesRefreshingThrottlerKeyProvider
import com.paulrybitskyi.gamedge.domain.commons.DomainPagination
import com.paulrybitskyi.gamedge.domain.games.DomainCompany
import com.paulrybitskyi.gamedge.domain.games.DomainGame

internal class FakeGamesRefreshingThrottlerKeyProvider : GamesRefreshingThrottlerKeyProvider {


    override fun providePopularGamesKey(pagination: DomainPagination): String {
        return "" // no-op
    }


    override fun provideRecentlyReleasedGamesKey(pagination: DomainPagination): String {
        return "" // no-op
    }


    override fun provideComingSoonGamesKey(pagination: DomainPagination): String {
        return "" // no-op
    }


    override fun provideMostAnticipatedGamesKey(pagination: DomainPagination): String {
        return "" // no-op
    }


    override fun provideCompanyDevelopedGamesKey(
        company: DomainCompany,
        pagination: DomainPagination
    ): String {
        return "" // no-op
    }


    override fun provideSimilarGamesKey(game: DomainGame, pagination: DomainPagination): String {
        return "" // no-op
    }


}