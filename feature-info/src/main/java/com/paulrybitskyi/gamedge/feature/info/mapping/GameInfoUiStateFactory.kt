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

package com.paulrybitskyi.gamedge.feature.info.mapping

import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.feature.info.widgets.main.GameInfoUiState
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface GameInfoUiStateFactory {

    fun createWithEmptyState(): GameInfoUiState

    fun createWithLoadingState(): GameInfoUiState

    fun createWithResultState(
        game: Game,
        isLiked: Boolean,
        companyGames: List<Game>,
        similarGames: List<Game>
    ): GameInfoUiState

}


@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameInfoUiStateFactoryImpl @Inject constructor(
    private val modelFactory: GameInfoModelFactory
) : GameInfoUiStateFactory {


    override fun createWithEmptyState(): GameInfoUiState {
        return GameInfoUiState.Empty
    }


    override fun createWithLoadingState(): GameInfoUiState {
        return GameInfoUiState.Loading
    }


    override fun createWithResultState(
        game: Game,
        isLiked: Boolean,
        companyGames: List<Game>,
        similarGames: List<Game>
    ): GameInfoUiState {
        return GameInfoUiState.Result(
            modelFactory.createInfoModel(game, isLiked, companyGames, similarGames)
        )
    }


}