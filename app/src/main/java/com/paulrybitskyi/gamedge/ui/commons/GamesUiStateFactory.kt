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
import com.paulrybitskyi.gamedge.commons.ui.widgets.videos.GamesUiState
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import javax.inject.Inject


internal interface GamesUiStateFactory {

    fun createWithEmptyState(): GamesUiState

    fun createWithLoadingState(): GamesUiState

    fun createWithResultState(games: List<Game>): GamesUiState

}


@BindType(installIn = BindType.Component.ACTIVITY_RETAINED)
internal class GamesUiStateFactoryImpl @Inject constructor(
    private val gameModelMapper: GameModelMapper
) : GamesUiStateFactory {


    override fun createWithEmptyState(): GamesUiState {
        return GamesUiState.Empty
    }


    override fun createWithLoadingState(): GamesUiState {
        return GamesUiState.Loading
    }


    override fun createWithResultState(games: List<Game>): GamesUiState {
        if(games.isEmpty()) return createWithEmptyState()

        return GamesUiState.Result(games.map(gameModelMapper::mapToGameModel))
    }


}