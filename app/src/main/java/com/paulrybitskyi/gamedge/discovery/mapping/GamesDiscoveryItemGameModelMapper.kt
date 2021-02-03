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

package com.paulrybitskyi.gamedge.discovery.mapping

import com.paulrybitskyi.gamedge.commons.ui.widgets.discovery.GamesDiscoveryItemChildModel
import com.paulrybitskyi.gamedge.core.IgdbImageSize
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface GamesDiscoveryItemGameModelMapper {

    fun mapToItemModel(game: Game): GamesDiscoveryItemChildModel

}


@BindType(installIn = BindType.Component.ACTIVITY_RETAINED)
internal class GamesDiscoveryItemGameModelMapperImpl @Inject constructor(
    private val igdbImageUrlBuilder: IgdbImageUrlBuilder
) : GamesDiscoveryItemGameModelMapper {


    override fun mapToItemModel(game: Game): GamesDiscoveryItemChildModel {
        return GamesDiscoveryItemChildModel(
            id = game.id,
            title = game.name,
            coverUrl = game.cover?.let { cover ->
                igdbImageUrlBuilder.buildUrl(cover, IgdbImageUrlBuilder.Config(IgdbImageSize.BIG_COVER))
            }
        )
    }


}


internal fun GamesDiscoveryItemGameModelMapper.mapToItemModels(
    games: List<Game>
): List<GamesDiscoveryItemChildModel> {
    return games.map(::mapToItemModel)
}