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

package com.paulrybitskyi.gamedge.feature.discovery.mapping

import com.paulrybitskyi.gamedge.core.factories.IgdbImageSize
import com.paulrybitskyi.gamedge.core.factories.IgdbImageUrlFactory
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.feature.discovery.widgets.GamesDiscoveryItemGameModel
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


interface GamesDiscoveryItemGameModelMapper {

    fun mapToGameModel(game: Game): GamesDiscoveryItemGameModel

}


@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GamesDiscoveryItemGameModelMapperImpl @Inject constructor(
    private val igdbImageUrlFactory: IgdbImageUrlFactory
) : GamesDiscoveryItemGameModelMapper {


    override fun mapToGameModel(game: Game): GamesDiscoveryItemGameModel {
        return GamesDiscoveryItemGameModel(
            id = game.id,
            title = game.name,
            coverUrl = game.cover?.let { cover ->
                igdbImageUrlFactory.createUrl(cover, IgdbImageUrlFactory.Config(IgdbImageSize.BIG_COVER))
            }
        )
    }


}


internal fun GamesDiscoveryItemGameModelMapper.mapToGameModels(
    games: List<Game>
): List<GamesDiscoveryItemGameModel> {
    return games.map(::mapToGameModel)
}