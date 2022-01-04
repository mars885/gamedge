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

package com.paulrybitskyi.gamedge.commons.ui.widgets.games

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource
import com.paulrybitskyi.gamedge.commons.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import kotlin.math.roundToInt

@Composable
fun Game(
    coverImageUrl: String?,
    name: String,
    releaseDate: String,
    developerName: String?,
    description: String?,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = colorResource(R.color.game_card_background_color),
        elevation = dimensionResource(R.dimen.game_card_elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.game_padding))
        ) {
            val gameCoverHeight = dimensionResource(R.dimen.game_cover_height)

            GameCover(
                title = null,
                imageUrl = coverImageUrl,
                modifier = Modifier.size(
                    width = dimensionResource(R.dimen.game_cover_width),
                    height = gameCoverHeight,
                ),
                onCoverClicked = onClick,
            )

            GameDetailsContent(
                name = name,
                releaseDate = releaseDate,
                developerName = developerName,
                description = description,
                modifier = Modifier.height(gameCoverHeight),
            )
        }
    }
}

@Composable
private fun GameDetailsContent(
    name: String,
    releaseDate: String,
    developerName: String?,
    description: String?,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.game_name_padding_start),
            ),
            color = colorResource(R.color.game_name_text_color),
            fontSize = textSizeResource(R.dimen.game_name_text_size),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
        )

        Text(
            text = releaseDate,
            modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.game_details_padding_top),
                    start = dimensionResource(R.dimen.game_details_padding_start),
                ),
            color = colorResource(R.color.game_details_text_color),
            fontSize = textSizeResource(R.dimen.game_details_text_size),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
        )

        if (developerName != null) {
            Text(
                text = developerName,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.game_details_padding_start)
                ),
                color = colorResource(R.color.game_details_text_color),
                fontSize = textSizeResource(R.dimen.game_details_text_size),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
            )
        }

        if (description != null) {
            GameDetailsDescription(description = description)
        }
    }
}

@Composable
private fun GameDetailsDescription(description: String) {
    var maxLines by remember { mutableStateOf(Int.MAX_VALUE) }

    Text(
        text = description,
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                top = dimensionResource(R.dimen.game_details_padding_top),
                start = dimensionResource(R.dimen.game_details_padding_start),
            ),
        color = colorResource(R.color.game_details_text_color),
        fontSize = textSizeResource(R.dimen.game_details_text_size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.multiParagraph.lineCount > 0) {
                val textHeight = textLayoutResult.size.height
                val firstLineHeight = textLayoutResult.multiParagraph.getLineHeight(0)

                maxLines = (textHeight / firstLineHeight).roundToInt()
            }
        }
    )
}

@Preview
@Composable
internal fun GameFullPreview() {
    Game(
        coverImageUrl = null,
        name = "Forza Horizon 5",
        releaseDate = "Nov 09, 2021 (7 days ago)",
        developerName = "Playground Games",
        description = "Your Ultimate Horizon Adventure awaits! Explore the vibrant " +
            "and ever-evolving open-world landscapes of Mexico.",
        onClick = {},
    )
}

@Preview
@Composable
internal fun GameWithoutDeveloperPreview() {
    Game(
        coverImageUrl = null,
        name = "Forza Horizon 5",
        releaseDate = "Nov 09, 2021 (7 days ago)",
        developerName = null,
        description = "Your Ultimate Horizon Adventure awaits! Explore the vibrant " +
                "and ever-evolving open-world landscapes of Mexico.",
        onClick = {},
    )
}

@Preview
@Composable
internal fun GameWithoutDescriptionPreview() {
    Game(
        coverImageUrl = null,
        name = "Forza Horizon 5",
        releaseDate = "Nov 09, 2021 (7 days ago)",
        developerName = "Playground Games",
        description = null,
        onClick = {},
    )
}

@Preview
@Composable
internal fun GameMinimalPreview() {
    Game(
        coverImageUrl = null,
        name = "Forza Horizon 5",
        releaseDate = "Nov 09, 2021 (7 days ago)",
        developerName = null,
        description = null,
        onClick = {},
    )
}
