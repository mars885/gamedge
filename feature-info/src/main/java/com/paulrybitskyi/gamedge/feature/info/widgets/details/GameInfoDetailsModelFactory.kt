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

package com.paulrybitskyi.gamedge.feature.info.widgets.details

import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.entities.Genre
import com.paulrybitskyi.gamedge.domain.games.entities.Mode
import com.paulrybitskyi.gamedge.domain.games.entities.Platform
import com.paulrybitskyi.gamedge.domain.games.entities.PlayerPerspective
import com.paulrybitskyi.gamedge.domain.games.entities.Theme
import com.paulrybitskyi.gamedge.feature.info.widgets.model.GameInfoDetailsModel
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GameInfoDetailsModelFactory {
    fun createDetailsModel(game: Game): GameInfoDetailsModel?
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameInfoDetailsModelFactoryImpl @Inject constructor() : GameInfoDetailsModelFactory {

    private companion object {
        private const val TEXT_SEPARATOR = " • "
    }

    override fun createDetailsModel(game: Game): GameInfoDetailsModel? {
        @Suppress("ComplexCondition")
        if (game.genres.isEmpty() &&
            game.platforms.isEmpty() &&
            game.modes.isEmpty() &&
            game.playerPerspectives.isEmpty() &&
            game.themes.isEmpty()) {
            return null
        }

        return GameInfoDetailsModel(
            genresText = game.genresToText(),
            platformsText = game.platformsToText(),
            modesText = game.modesToText(),
            playerPerspectivesText = game.playerPerspectivesToText(),
            themesText = game.themesToText()
        )
    }

    private fun Game.genresToText(): String? {
        return genres
            .takeIf(List<Genre>::isNotEmpty)
            ?.map(Genre::name)
            ?.joinToString()
    }

    private fun Game.platformsToText(): String? {
        return platforms
            .takeIf(List<Platform>::isNotEmpty)
            ?.map(Platform::name)
            ?.joinToString()
    }

    private fun Game.modesToText(): String? {
        return modes
            .takeIf(List<Mode>::isNotEmpty)
            ?.map(Mode::name)
            ?.joinToString()
    }

    private fun Game.playerPerspectivesToText(): String? {
        return playerPerspectives
            .takeIf(List<PlayerPerspective>::isNotEmpty)
            ?.map(PlayerPerspective::name)
            ?.joinToString()
    }

    private fun Game.themesToText(): String? {
        return themes
            .takeIf(List<Theme>::isNotEmpty)
            ?.map(Theme::name)
            ?.joinToString()
    }

    private fun List<String>.joinToString(): String {
        return joinToString(separator = TEXT_SEPARATOR)
    }
}
