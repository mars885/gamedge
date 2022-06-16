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

package com.paulrybitskyi.gamedge.feature.info.widgets.links

import com.paulrybitskyi.gamedge.core.providers.WebsiteIconProvider
import com.paulrybitskyi.gamedge.core.providers.WebsiteNameProvider
import com.paulrybitskyi.gamedge.domain.games.entities.Website
import com.paulrybitskyi.gamedge.domain.games.entities.WebsiteCategory
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GameInfoLinkUiModelMapper {
    fun mapToUiModel(website: Website): GameInfoLinkUiModel?
    fun mapToUiModels(websites: List<Website>): List<GameInfoLinkUiModel>
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameInfoLinkUiModelMapperImpl @Inject constructor(
    private val websiteNameProvider: WebsiteNameProvider,
    private val websiteIconProvider: WebsiteIconProvider,
) : GameInfoLinkUiModelMapper {

    override fun mapToUiModel(website: Website): GameInfoLinkUiModel? {
        if (website.category == WebsiteCategory.UNKNOWN) return null

        return GameInfoLinkUiModel(
            id = website.id,
            text = websiteNameProvider.provideWebsiteName(website),
            iconId = websiteIconProvider.provideIconIdForWebsite(website),
            url = website.url,
        )
    }

    override fun mapToUiModels(websites: List<Website>): List<GameInfoLinkUiModel> {
        if (websites.isEmpty()) return emptyList()

        return websites
            .sortedBy { it.category.orderPosition }
            .mapNotNull(::mapToUiModel)
    }

    @Suppress("MagicNumber")
    private val WebsiteCategory.orderPosition: Int
        get() = when (this) {
            WebsiteCategory.UNKNOWN -> -1
            WebsiteCategory.STEAM -> 0
            WebsiteCategory.GOG -> 1
            WebsiteCategory.EPIC_GAMES -> 2
            WebsiteCategory.GOOGLE_PLAY -> 3
            WebsiteCategory.APP_STORE -> 4
            WebsiteCategory.OFFICIAL -> 5
            WebsiteCategory.TWITTER -> 6
            WebsiteCategory.SUBREDDIT -> 7
            WebsiteCategory.YOUTUBE -> 8
            WebsiteCategory.TWITCH -> 9
            WebsiteCategory.INSTAGRAM -> 10
            WebsiteCategory.FACEBOOK -> 11
            WebsiteCategory.WIKIPEDIA -> 12
            WebsiteCategory.WIKIA -> 13
            WebsiteCategory.DISCORD -> 14
        }
}
