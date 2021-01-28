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

package com.paulrybitskyi.gamedge.ui.commons

import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.gamedge.commons.ui.widgets.games.GameModel
import com.paulrybitskyi.gamedge.core.IgdbImageSize
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.core.formatters.GameReleaseDateFormatter
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.entities.extensions.developerCompany
import javax.inject.Inject


internal interface GameModelMapper {

    fun mapToGameModel(game: Game): GameModel

}


@BindType(installIn = BindType.Component.ACTIVITY_RETAINED)
internal class GameModelMapperImpl @Inject constructor(
    private val igdbImageUrlBuilder: IgdbImageUrlBuilder,
    private val gameReleaseDateFormatter: GameReleaseDateFormatter
) : GameModelMapper {


    override fun mapToGameModel(game: Game): GameModel {
        return GameModel(
            id = game.id,
            coverImageUrl = game.buildCoverImageUrl(),
            name = game.name,
            releaseDate = gameReleaseDateFormatter.formatReleaseDate(game),
            developerName = game.developerCompany?.name,
            description = game.buildDescription()
        )
    }


    private fun Game.buildCoverImageUrl(): String? {
        return cover?.let { cover ->
            igdbImageUrlBuilder.buildUrl(cover, IgdbImageUrlBuilder.Config(IgdbImageSize.BIG_COVER))
        }
    }


    private fun Game.buildDescription(): String? {
        if(summary != null) return summary
        if(storyline != null) return storyline

        return null
    }


}