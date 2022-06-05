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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State
import coil.request.ImageRequest
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme

val DEFAULT_COVER_WIDTH = 112.dp
val DEFAULT_COVER_HEIGHT = 153.dp

@Composable
fun GameCover(
    title: String?,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    hasRoundedShape: Boolean = true,
    onCoverClicked: () -> Unit,
) {
    GamedgeCard(
        onClick = onCoverClicked,
        modifier = Modifier
            .size(width = DEFAULT_COVER_WIDTH, height = DEFAULT_COVER_HEIGHT)
            .then(modifier),
        shape = if (hasRoundedShape) GamedgeTheme.shapes.medium else RectangleShape,
        backgroundColor = Color.Transparent,
    ) {
        Box {
            var imageState by remember { mutableStateOf<State>(State.Empty) }
            val shouldDisplayTitle by remember(title) {
                derivedStateOf {
                    (title != null) &&
                    (imageState !is State.Success)
                }
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .fallback(R.drawable.game_cover_placeholder)
                    .placeholder(R.drawable.game_cover_placeholder)
                    .error(R.drawable.game_cover_placeholder)
                    .crossfade(CROSSFADE_ANIMATION_DURATION)
                    .build(),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                onState = { state ->
                    imageState = state
                },
                contentScale = ContentScale.Crop,
            )

            if (shouldDisplayTitle) {
                Text(
                    text = checkNotNull(title),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = GamedgeTheme.spaces.spacing_4_0),
                    color = GamedgeTheme.colors.onPrimary,
                    textAlign = TextAlign.Center,
                    style = GamedgeTheme.typography.caption,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun GameCoverWithTitlePreview() {
    GamedgeTheme {
        GameCover(
            title = "Ghost of Tsushima: Director's Cut",
            imageUrl = null,
            onCoverClicked = {},
        )
    }
}

@Preview
@Composable
internal fun GameCoverWithoutTitlePreview() {
    GamedgeTheme {
        GameCover(
            title = null,
            imageUrl = null,
            onCoverClicked = {},
        )
    }
}
