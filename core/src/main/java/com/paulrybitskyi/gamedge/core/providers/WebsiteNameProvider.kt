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

interface WebsiteNameProvider {
    fun provideWebsiteName(website: Website): String
}

@BindType
internal class WebsiteNameProviderImpl @Inject constructor(
    private val stringProvider: StringProvider,
) : WebsiteNameProvider {

    @Suppress("ComplexMethod")
    override fun provideWebsiteName(website: Website): String {
        return stringProvider.getString(
            when (website.category) {
                WebsiteCategory.UNKNOWN -> R.string.website_unknown
                WebsiteCategory.OFFICIAL -> R.string.website_official
                WebsiteCategory.WIKIA -> R.string.website_wikia
                WebsiteCategory.WIKIPEDIA -> R.string.website_wikipedia
                WebsiteCategory.FACEBOOK -> R.string.website_facebook
                WebsiteCategory.TWITTER -> R.string.website_twitter
                WebsiteCategory.TWITCH -> R.string.website_twitch
                WebsiteCategory.INSTAGRAM -> R.string.website_instagram
                WebsiteCategory.YOUTUBE -> R.string.website_youtube
                WebsiteCategory.APP_STORE -> R.string.website_app_store
                WebsiteCategory.GOOGLE_PLAY -> R.string.website_google_play
                WebsiteCategory.STEAM -> R.string.website_steam
                WebsiteCategory.SUBREDDIT -> R.string.website_subreddit
                WebsiteCategory.EPIC_GAMES -> R.string.website_epic_games
                WebsiteCategory.GOG -> R.string.website_gog
                WebsiteCategory.DISCORD -> R.string.website_discord
            },
        )
    }
}
