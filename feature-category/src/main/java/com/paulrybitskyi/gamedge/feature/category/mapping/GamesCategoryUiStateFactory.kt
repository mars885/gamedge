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

package com.paulrybitskyi.gamedge.feature.category.mapping

import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.feature.category.widgets.GamesCategoryUiState
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface GamesCategoryUiStateFactory {

    fun createWithEmptyState(): GamesCategoryUiState

    fun createWithLoadingState(): GamesCategoryUiState

    fun createWithResultState(games: List<Game>): GamesCategoryUiState

}


@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GamesCategoryUiStateFactoryImpl @Inject constructor(
    private val gameCategoryModelMapper: GameCategoryModelMapper
) : GamesCategoryUiStateFactory {


    override fun createWithEmptyState(): GamesCategoryUiState {
        return GamesCategoryUiState.Empty
    }


    override fun createWithLoadingState(): GamesCategoryUiState {
        return GamesCategoryUiState.Loading
    }


    override fun createWithResultState(games: List<Game>): GamesCategoryUiState {
        if(games.isEmpty()) return createWithEmptyState()

        return GamesCategoryUiState.Result(
            games.map(gameCategoryModelMapper::mapToGameCategoryModel)
        )
    }


}