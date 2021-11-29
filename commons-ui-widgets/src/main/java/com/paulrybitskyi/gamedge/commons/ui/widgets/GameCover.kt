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

package com.paulrybitskyi.gamedge.commons.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter.State
import coil.compose.rememberImagePainter
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource

@Composable
fun GameCover(
    title: String?,
    imageUrl: String?,
    modifier: Modifier,
    hasRoundedShape: Boolean = true,
    onCoverClicked: () -> Unit,
) {
    Card(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onCoverClicked,
            ),
        shape = RoundedCornerShape(
            if (hasRoundedShape) dimensionResource(R.dimen.game_cover_card_corner_radius) else 0.dp,
        ),
        backgroundColor = colorResource(R.color.game_cover_card_background_color),
        elevation = dimensionResource(R.dimen.game_cover_card_elevation),
    ) {
        Box {
            val imagePainter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    fallback(R.drawable.game_cover_placeholder)
                    placeholder(R.drawable.game_cover_placeholder)
                    error(R.drawable.game_cover_placeholder)
                    crossfade(CROSSFADE_ANIMATION_DURATION)
                }
            )
            val shouldDisplayTitle = (
                (title != null) &&
                (imagePainter.state !is State.Success)
            )

            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
            )

            if (shouldDisplayTitle) {
                Text(
                    text = checkNotNull(title),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(
                            horizontal = dimensionResource(R.dimen.game_cover_title_horizontal_padding),
                        ),
                    color = colorResource(R.color.game_cover_title_text_color),
                    fontSize = textSizeResource(R.dimen.game_cover_title_text_size),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun GameCoverWithTitlePreview() {
    GameCover(
        title = "Ghost of Tsushima: Director's Cut",
        imageUrl = null,
        modifier = Modifier.size(
            width = dimensionResource(R.dimen.games_category_preview_cover_item_width),
            height = dimensionResource(R.dimen.games_category_preview_cover_item_height)
        ),
        onCoverClicked = {},
    )
}

@Preview
@Composable
internal fun GameCoverWithoutTitlePreview() {
    GameCover(
        title = null,
        imageUrl = null,
        modifier = Modifier.size(
            width = dimensionResource(R.dimen.games_category_preview_cover_item_width),
            height = dimensionResource(R.dimen.games_category_preview_cover_item_height)
        ),
        onCoverClicked = {},
    )
}
