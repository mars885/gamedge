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

package com.paulrybitskyi.gamedge.common.ui.widgets.games

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.widgets.DefaultCoverHeight
import com.paulrybitskyi.gamedge.common.ui.widgets.GameCover
import com.paulrybitskyi.gamedge.common.ui.widgets.GamedgeCard
import kotlin.math.roundToInt

@Composable
fun Game(
    game: GameUiModel,
    onClick: () -> Unit,
) {
    GamedgeCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GamedgeTheme.spaces.spacing_3_5),
        ) {
            GameCover(
                title = null,
                imageUrl = game.coverImageUrl,
            )

            Details(
                name = game.name,
                releaseDate = game.releaseDate,
                developerName = game.developerName,
                description = game.description,
                modifier = Modifier.height(DefaultCoverHeight),
            )
        }
    }
}

@Composable
private fun Details(
    name: String,
    releaseDate: String,
    developerName: String?,
    description: String?,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            modifier = Modifier.padding(start = GamedgeTheme.spaces.spacing_3_0),
            color = GamedgeTheme.colors.onPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            style = GamedgeTheme.typography.subtitle2,
        )

        Text(
            text = releaseDate,
            modifier = Modifier
                .padding(
                    top = GamedgeTheme.spaces.spacing_2_5,
                    start = GamedgeTheme.spaces.spacing_3_0,
                ),
            style = GamedgeTheme.typography.caption,
        )

        if (developerName != null) {
            Text(
                text = developerName,
                modifier = Modifier.padding(start = GamedgeTheme.spaces.spacing_3_0),
                style = GamedgeTheme.typography.caption,
            )
        }

        if (description != null) {
            DetailsDescription(description = description)
        }
    }
}

@Composable
private fun DetailsDescription(description: String) {
    var maxLines by rememberSaveable { mutableIntStateOf(Int.MAX_VALUE) }

    Text(
        text = description,
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                top = GamedgeTheme.spaces.spacing_2_5,
                start = GamedgeTheme.spaces.spacing_3_0,
            ),
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.multiParagraph.lineCount > 0) {
                val textHeight = textLayoutResult.size.height
                val firstLineHeight = textLayoutResult.multiParagraph.getLineHeight(0)

                maxLines = (textHeight / firstLineHeight).roundToInt()
            }
        },
        style = GamedgeTheme.typography.body2.copy(
            lineHeight = TextUnit.Unspecified,
        ),
    )
}

@PreviewLightDark
@Composable
private fun GameFullPreview() {
    GamedgeTheme {
        Game(
            game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "Forza Horizon 5",
                releaseDate = "Nov 09, 2021 (7 days ago)",
                developerName = "Playground Games",
                description = "Your Ultimate Horizon Adventure awaits! Explore the vibrant " +
                    "and ever-evolving open-world landscapes of Mexico.",
            ),
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun GameWithoutDeveloperPreview() {
    GamedgeTheme {
        Game(
            game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "Forza Horizon 5",
                releaseDate = "Nov 09, 2021 (7 days ago)",
                developerName = null,
                description = "Your Ultimate Horizon Adventure awaits! Explore the vibrant " +
                    "and ever-evolving open-world landscapes of Mexico.",
            ),
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun GameWithoutDescriptionPreview() {
    GamedgeTheme {
        Game(
            game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "Forza Horizon 5",
                releaseDate = "Nov 09, 2021 (7 days ago)",
                developerName = "Playground Games",
                description = null,
            ),
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun GameMinimalPreview() {
    GamedgeTheme {
        Game(
            game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "Forza Horizon 5",
                releaseDate = "Nov 09, 2021 (7 days ago)",
                developerName = null,
                description = null,
            ),
            onClick = {},
        )
    }
}
