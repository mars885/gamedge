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

package com.paulrybitskyi.gamedge.core.providers

import com.paulrybitskyi.gamedge.common.domain.games.entities.Website
import com.paulrybitskyi.gamedge.common.domain.games.entities.WebsiteCategory
import com.paulrybitskyi.gamedge.core.R
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface WebsiteIconProvider {

    fun provideIconIdForWebsite(website: Website): Int
}

@BindType
internal class WebsiteIconProviderImpl @Inject constructor() : WebsiteIconProvider {

    override fun provideIconIdForWebsite(website: Website): Int {
        return when (website.category) {
            WebsiteCategory.WIKIPEDIA -> R.drawable.wikipedia
            WebsiteCategory.FACEBOOK -> R.drawable.facebook
            WebsiteCategory.TWITTER -> R.drawable.twitter
            WebsiteCategory.TWITCH -> R.drawable.twitch
            WebsiteCategory.INSTAGRAM -> R.drawable.instagram
            WebsiteCategory.YOUTUBE -> R.drawable.youtube
            WebsiteCategory.APP_STORE -> R.drawable.apple
            WebsiteCategory.GOOGLE_PLAY -> R.drawable.google_play
            WebsiteCategory.STEAM -> R.drawable.steam
            WebsiteCategory.SUBREDDIT -> R.drawable.reddit
            WebsiteCategory.GOG -> R.drawable.gog
            WebsiteCategory.DISCORD -> R.drawable.discord

            WebsiteCategory.UNKNOWN,
            WebsiteCategory.OFFICIAL,
            WebsiteCategory.WIKIA,
            WebsiteCategory.EPIC_GAMES,
            -> R.drawable.web
        }
    }
}
