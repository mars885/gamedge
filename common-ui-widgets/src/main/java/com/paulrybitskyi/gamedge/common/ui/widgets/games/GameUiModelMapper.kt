/*
 * Copyright 2021 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.common.ui.widgets.games

import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.core.factories.IgdbImageSize
import com.paulrybitskyi.gamedge.core.factories.IgdbImageUrlFactory
import com.paulrybitskyi.gamedge.core.formatters.GameReleaseDateFormatter
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface GameUiModelMapper {
    fun mapToUiModel(game: Game): GameUiModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameUiModelMapperImpl @Inject constructor(
    private val igdbImageUrlFactory: IgdbImageUrlFactory,
    private val gameReleaseDateFormatter: GameReleaseDateFormatter,
) : GameUiModelMapper {

    override fun mapToUiModel(game: Game): GameUiModel {
        return GameUiModel(
            id = game.id,
            coverImageUrl = game.buildCoverImageUrl(),
            name = game.name,
            releaseDate = gameReleaseDateFormatter.formatReleaseDate(game),
            developerName = game.developerCompany?.name,
            description = game.buildDescription(),
        )
    }

    private fun Game.buildCoverImageUrl(): String? {
        return cover?.let { cover ->
            igdbImageUrlFactory.createUrl(cover, IgdbImageUrlFactory.Config(IgdbImageSize.BIG_COVER))
        }
    }

    private fun Game.buildDescription(): String? {
        if (summary != null) return summary
        if (storyline != null) return storyline

        return null
    }
}

fun GameUiModelMapper.mapToUiModels(games: List<Game>): List<GameUiModel> {
    return games.map(::mapToUiModel)
}
