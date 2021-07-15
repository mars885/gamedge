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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import com.paulrybitskyi.gamedge.commons.ui.extensions.textSizeResource
import com.paulrybitskyi.gamedge.feature.news.R


@Composable
internal fun GamingNewsItem(
    imageUrl: String,
    title: String,
    lede: String,
    publicationDate: String,
    onClick: (() -> Unit)? = null
) {
    Card(
        onClick = { onClick?.invoke() },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = colorResource(R.color.gaming_news_item_card_background_color),
        elevation = dimensionResource(R.dimen.gaming_news_item_card_elevation)
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.gaming_news_item_padding))) {
            GamingNewsItemImage(
                imageUrl = imageUrl,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.gaming_news_item_image_height))
                    .padding(bottom = dimensionResource(R.dimen.gaming_news_item_image_margin_bottom))
            )
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.gaming_news_item_title_text_color),
                fontSize = textSizeResource(R.dimen.gaming_news_item_title_text_size),
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = lede,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.gaming_news_item_lede_margin_top)),
                color = colorResource(R.color.gaming_news_item_lede_text_color),
                fontSize = textSizeResource(R.dimen.gaming_news_item_lede_text_size),
                fontFamily = FontFamily.SansSerif
            )
            Row(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.gaming_news_item_publication_date_margin_top)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.clock_outline_16dp),
                    contentDescription = null,
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.gaming_news_item_publication_date_icon_padding)),
                    tint = colorResource(R.color.gaming_news_item_publication_date_text_color)
                )
                Text(
                    text = publicationDate,
                    color = colorResource(R.color.gaming_news_item_publication_date_text_color),
                    fontSize = textSizeResource(R.dimen.gaming_news_item_publication_date_text_size),
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}


@Preview
@Composable
internal fun GamingNewsItemPreview() {
    GamingNewsItem(
        imageUrl = "url",
        title = "Steam Concurrent Player Count Breaks Record Again, Tops 26 Million",
        lede = "However, the record for those actively in a game has not been broken yet.",
        publicationDate = "3 mins ago"
    )
}


@Composable
internal fun GamingNewsItemImage(
    imageUrl: String,
    modifier: Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        shape = RoundedCornerShape(dimensionResource(R.dimen.gaming_news_item_image_card_radius)),
        backgroundColor = colorResource(R.color.gaming_news_item_image_card_background_color),
        elevation = dimensionResource(R.dimen.gaming_news_item_image_card_elevation)
    ) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    //todo centerCrop?
                    placeholder(R.drawable.game_landscape_placeholder)
                    error(R.drawable.game_landscape_placeholder)
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}