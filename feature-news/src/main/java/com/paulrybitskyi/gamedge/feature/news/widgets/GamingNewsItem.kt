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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.feature.news.R

@Composable
internal fun GamingNewsItem(
    imageUrl: String?,
    title: String,
    lede: String,
    publicationDate: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        elevation = dimensionResource(R.dimen.gaming_news_item_card_elevation),
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.gaming_news_item_padding))) {
            if (imageUrl != null) {
                Image(
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.gaming_news_item_image_height))
                        .padding(bottom = dimensionResource(R.dimen.gaming_news_item_image_padding_bottom))
                )
            }

            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = GamedgeTheme.colors.onPrimary,
                style = GamedgeTheme.typography.subtitle2,
            )
            Text(
                text = lede,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.gaming_news_item_lede_padding_top)),
                style = GamedgeTheme.typography.body2.copy(
                    lineHeight = TextUnit.Unspecified,
                ),
            )
            Timestamp(publicationDate = publicationDate)
        }
    }
}

@Composable
private fun Image(
    imageUrl: String,
    modifier: Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        backgroundColor = Color.Transparent,
        elevation = dimensionResource(R.dimen.gaming_news_item_image_card_elevation)
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
        modifier = Modifier.padding(
            top = dimensionResource(R.dimen.gaming_news_item_publication_date_padding_top)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.clock_outline_16dp),
            contentDescription = null,
            modifier = Modifier.padding(
                end = dimensionResource(R.dimen.gaming_news_item_publication_date_icon_padding)
            ),
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
            imageUrl = "url",
            title = "Steam Concurrent Player Count Breaks Record Again, Tops 26 Million",
            lede = "However, the record for those actively in a game has not been broken yet.",
            publicationDate = "3 mins ago",
            onClick = {}
        )
    }
}

@Preview
@Composable
internal fun GamingNewsItemWithoutImagePreview() {
    GamedgeTheme {
        GamingNewsItem(
            imageUrl = null,
            title = "Steam Concurrent Player Count Breaks Record Again, Tops 26 Million",
            lede = "However, the record for those actively in a game has not been broken yet.",
            publicationDate = "3 mins ago",
            onClick = {}
        )
    }
}
