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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.theme.subtitle3
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.utils.GameInfoSection

@Composable
internal fun GameInfoDetails(details: GameInfoDetailsModel) {
    GameInfoSection(
        title = stringResource(R.string.game_details_title),
        titleBottomPadding = GamedgeTheme.spaces.spacing_1_0,
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (details.hasGenresText) {
                CategorySection(
                    title = stringResource(R.string.game_details_genres_title),
                    value = checkNotNull(details.genresText),
                )
            }

            if (details.hasPlatformsText) {
                CategorySection(
                    title = stringResource(R.string.game_details_platforms_title),
                    value = checkNotNull(details.platformsText),
                )
            }

            if (details.hasModesText) {
                CategorySection(
                    title = stringResource(R.string.game_details_modes_title),
                    value = checkNotNull(details.modesText),
                )
            }

            if (details.hasPlayerPerspectivesText) {
                CategorySection(
                    title = stringResource(R.string.game_details_player_perspectives_title),
                    value = checkNotNull(details.playerPerspectivesText),
                )
            }

            if (details.hasThemesText) {
                CategorySection(
                    title = stringResource(R.string.game_details_themes_title),
                    value = checkNotNull(details.themesText),
                )
            }
        }
    }
}

@Composable
private fun CategorySection(title: String, value: String) {
    Text(
        text = title,
        modifier = Modifier.padding(top = GamedgeTheme.spaces.spacing_2_5),
        color = GamedgeTheme.colors.onPrimary,
        style = GamedgeTheme.typography.subtitle3,
    )
    Text(
        text = value,
        modifier = Modifier.padding(top = GamedgeTheme.spaces.spacing_1_0),
        style = GamedgeTheme.typography.body2,
    )
}

@Preview
@Composable
private fun GameInfoDetailsPreview() {
    GamedgeTheme {
        GameInfoDetails(
            details = GameInfoDetailsModel(
                genresText = "Adventure • Shooter • Role-playing (RPG)",
                platformsText = "PC • PS4 • XONE • PS5 • Series X • Stadia",
                modesText = "Single Player • Multiplayer",
                playerPerspectivesText = "First person • Third person",
                themesText = "Action • Science Fiction • Horror • Survival",
            ),
        )
    }
}
