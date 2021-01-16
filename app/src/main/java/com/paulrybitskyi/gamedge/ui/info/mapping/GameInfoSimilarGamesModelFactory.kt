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

package com.paulrybitskyi.gamedge.ui.info.mapping

import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.games.GameInfoRelatedGameModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.games.GameInfoRelatedGamesModel
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.games.GameInfoRelatedGamesType
import com.paulrybitskyi.gamedge.core.IgdbImageSize
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import javax.inject.Inject


internal interface GameInfoSimilarGamesModelFactory {

    fun createSimilarGamesModel(similarGames: List<Game>): GameInfoRelatedGamesModel?

}


internal class GameInfoSimilarGamesModelFactoryImpl @Inject constructor(
    private val stringProvider: StringProvider,
    private val igdbImageUrlBuilder: IgdbImageUrlBuilder
) : GameInfoSimilarGamesModelFactory {


    override fun createSimilarGamesModel(similarGames: List<Game>): GameInfoRelatedGamesModel? {
        if(similarGames.isEmpty()) return null

        return GameInfoRelatedGamesModel(
            type = GameInfoRelatedGamesType.SIMILAR_GAMES,
            title = stringProvider.getString(R.string.game_info_similar_games_title),
            items = similarGames.toRelatedGameModels()
        )
    }


    private fun List<Game>.toRelatedGameModels(): List<GameInfoRelatedGameModel> {
        return map {
            GameInfoRelatedGameModel(
                id = it.id,
                title = it.name,
                coverUrl = it.cover?.let { cover ->
                    igdbImageUrlBuilder.buildUrl(cover, IgdbImageUrlBuilder.Config(IgdbImageSize.BIG_COVER))
                }
            )
        }
    }


}