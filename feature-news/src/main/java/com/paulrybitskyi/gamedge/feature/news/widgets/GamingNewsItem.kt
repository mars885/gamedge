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

package com.paulrybitskyi.gamedge.feature.news.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.paulrybitskyi.gamedge.commons.ui.images.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeCard
import com.paulrybitskyi.gamedge.feature.news.R

@Composable
internal fun GamingNewsItem(
    model: GamingNewsItemModel,
    onClick: () -> Unit
) {
    GamedgeCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(GamedgeTheme.spaces.spacing_4_0)) {
            if (model.hasImageUrl) {
                Image(
                    imageUrl = checkNotNull(model.imageUrl),
                    modifier = Modifier
                        .height(168.dp)
                        .padding(bottom = GamedgeTheme.spaces.spacing_3_5)
                )
            }

            Text(
                text = model.title,
                modifier = Modifier.fillMaxWidth(),
                color = GamedgeTheme.colors.onPrimary,
                style = GamedgeTheme.typography.subtitle2,
            )
            Text(
                text = model.lede,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = GamedgeTheme.spaces.spacing_1_5),
                style = GamedgeTheme.typography.body2.copy(
                    lineHeight = TextUnit.Unspecified,
                ),
            )
            Timestamp(publicationDate = model.publicationDate)
        }
    }
}

@Composable
private fun Image(
    imageUrl: String,
    modifier: Modifier
) {
    GamedgeCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        shape = GamedgeTheme.shapes.medium,
        backgroundColor = Color.Transparent,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .fallback(R.drawable.game_landscape_placeholder)
                .placeholder(R.drawable.game_landscape_placeholder)
                .error(R.drawable.game_landscape_placeholder)
                .crossfade(CROSSFADE_ANIMATION_DURATION)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun Timestamp(publicationDate: String) {
    Row(
        modifier = Modifier.padding(top = GamedgeTheme.spaces.spacing_2_5),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.clock_outline_16dp),
            contentDescription = null,
            modifier = Modifier.padding(end = GamedgeTheme.spaces.spacing_1_0),
        )
        Text(
            text = publicationDate,
            style = GamedgeTheme.typography.caption,
        )
    }
}

@Preview
@Composable
internal fun GamingNewsItemWithImagePreview() {
    GamedgeTheme {
        GamingNewsItem(
            model = GamingNewsItemModel(
                id = 1,
                imageUrl = "url",
                title = "Steam Concurrent Player Count Breaks Record Again, Tops 26 Million",
                lede = "However, the record for those actively in a game has not been broken yet.",
                publicationDate = "3 mins ago",
                siteDetailUrl = "url",
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
internal fun GamingNewsItemWithoutImagePreview() {
    GamedgeTheme {
        GamingNewsItem(
            model = GamingNewsItemModel(
                id = 1,
                imageUrl = null,
                title = "Steam Concurrent Player Count Breaks Record Again, Tops 26 Million",
                lede = "However, the record for those actively in a game has not been broken yet.",
                publicationDate = "3 mins ago",
                siteDetailUrl = "url",
            ),
            onClick = {}
        )
    }
}
