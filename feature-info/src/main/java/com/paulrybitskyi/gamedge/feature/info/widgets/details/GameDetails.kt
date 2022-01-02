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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource
import com.paulrybitskyi.gamedge.feature.info.R

@Composable
internal fun GameDetails(
    genres: String?,
    platforms: String?,
    modes: String?,
    playerPerspectives: String?,
    themes: String?,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = colorResource(R.color.game_details_card_background_color),
        elevation = dimensionResource(R.dimen.game_details_card_elevation),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.game_details_container_padding)),
        ) {
            GameDetailsTitle(
                modifier = Modifier.padding(
                    bottom = dimensionResource(R.dimen.game_details_title_padding_bottom)
                ),
            )

            if (genres != null) {
                GameDetailsGenresSection(genres)
            }

            if (platforms != null) {
                GameDetailsPlatformsSection(platforms)
            }

            if (modes != null) {
                GameDetailsModesSection(modes)
            }

            if (playerPerspectives != null) {
                GameDetailsPlayerPerspectivesSection(playerPerspectives)
            }

            if (themes != null) {
                GameDetailsThemesSection(themes)
            }
        }
    }
}

@Composable
private fun GameDetailsTitle(modifier: Modifier) {
    Text(
        text = stringResource(R.string.game_details_title),
        modifier = modifier,
        color = colorResource(R.color.game_details_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_title_text_size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
    )
}

@Composable
private fun GameDetailsGenresSection(genres: String) {
    Text(
        text = stringResource(R.string.game_details_genres_title),
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_title_padding_top)),
        color = colorResource(R.color.game_details_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_title_text_size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = genres,
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_value_padding_top)),
        color = colorResource(R.color.game_details_category_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_value_text_size),
        lineHeight = textSizeResource(R.dimen.game_details_category_value_line_height),
    )
}

@Composable
private fun GameDetailsPlatformsSection(platforms: String) {
    Text(
        text = stringResource(R.string.game_details_platforms_title),
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_title_padding_top)),
        color = colorResource(R.color.game_details_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_title_text_size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = platforms,
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_value_padding_top)),
        color = colorResource(R.color.game_details_category_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_value_text_size),
        lineHeight = textSizeResource(R.dimen.game_details_category_value_line_height),
    )
}

@Composable
private fun GameDetailsModesSection(modes: String) {
    Text(
        text = stringResource(R.string.game_details_modes_title),
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_title_padding_top)),
        color = colorResource(R.color.game_details_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_title_text_size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = modes,
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_value_padding_top)),
        color = colorResource(R.color.game_details_category_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_value_text_size),
        lineHeight = textSizeResource(R.dimen.game_details_category_value_line_height),
    )
}

@Composable
private fun GameDetailsPlayerPerspectivesSection(playerPerspectives: String) {
    Text(
        text = stringResource(R.string.game_details_player_perspectives_title),
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_title_padding_top)),
        color = colorResource(R.color.game_details_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_title_text_size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = playerPerspectives,
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_value_padding_top)),
        color = colorResource(R.color.game_details_category_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_value_text_size),
        lineHeight = textSizeResource(R.dimen.game_details_category_value_line_height),
    )
}

@Composable
private fun GameDetailsThemesSection(themes: String) {
    Text(
        text = stringResource(R.string.game_details_themes_title),
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_title_padding_top)),
        color = colorResource(R.color.game_details_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_title_text_size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = themes,
        modifier = Modifier.padding(top = dimensionResource(R.dimen.game_details_category_value_padding_top)),
        color = colorResource(R.color.game_details_category_title_text_color),
        fontSize = textSizeResource(R.dimen.game_details_category_value_text_size),
        lineHeight = textSizeResource(R.dimen.game_details_category_value_line_height),
    )
}

@Composable
@Preview
internal fun GameDetailsPreview() {
    GameDetails(
        genres = "Adventure • Shooter • Role-playing (RPG)",
        platforms = "PC • PS4 • XONE • PS5 • Series X • Stadia",
        modes = "Single Player • Multiplayer",
        playerPerspectives = "First person • Third person",
        themes = "Action • Science Fiction • Horror • Survival",
    )
}
