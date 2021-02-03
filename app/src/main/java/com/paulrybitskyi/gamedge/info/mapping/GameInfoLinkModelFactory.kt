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

package com.paulrybitskyi.gamedge.info.mapping

import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.GameInfoLinkModel
import com.paulrybitskyi.gamedge.core.WebsiteIconRetriever
import com.paulrybitskyi.gamedge.core.WebsiteNameRetriever
import com.paulrybitskyi.gamedge.domain.games.entities.Website
import com.paulrybitskyi.gamedge.domain.games.entities.WebsiteCategory
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface GameInfoLinkModelFactory {

    fun createLinkModels(websites: List<Website>): List<GameInfoLinkModel>

    fun createLinkModel(website: Website): GameInfoLinkModel?

}


@BindType(installIn = BindType.Component.ACTIVITY_RETAINED)
internal class GameInfoLinkModelFactoryImpl @Inject constructor(
    private val websiteNameRetriever: WebsiteNameRetriever,
    private val websiteIconRetriever: WebsiteIconRetriever
) : GameInfoLinkModelFactory {


    override fun createLinkModels(websites: List<Website>): List<GameInfoLinkModel> {
        if(websites.isEmpty()) return emptyList()

        return websites
            .sortedBy { it.category.orderPosition }
            .mapNotNull(::createLinkModel)
    }


    override fun createLinkModel(website: Website): GameInfoLinkModel? {
        if(website.category == WebsiteCategory.UNKNOWN) return null

        return GameInfoLinkModel(
            id = website.id,
            text = websiteNameRetriever.getWebsiteName(website),
            iconId = websiteIconRetriever.getIconIdForWebsite(website),
            payload = website.url
        )
    }


    private val WebsiteCategory.orderPosition: Int
        get() = when(this) {
            WebsiteCategory.UNKNOWN -> -1
            WebsiteCategory.STEAM -> -0
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