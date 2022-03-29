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

package com.paulrybitskyi.gamedge.feature.info.widgets.screenshots

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource
import com.paulrybitskyi.gamedge.feature.info.R

@Composable
internal fun GameScreenshots(
    screenshots: List<GameInfoScreenshotModel>,
    onScreenshotClicked: (screenshotIndex: Int) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = colorResource(R.color.game_screenshots_card_background_color),
        elevation = dimensionResource(R.dimen.game_screenshots_card_elevation),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.game_screenshots_card_vertical_padding)),
        ) {
            Text(
                text = stringResource(R.string.game_screenshots_title),
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.game_screenshots_title_padding_bottom))
                    .padding(horizontal = dimensionResource(R.dimen.game_screenshots_title_padding)),
                color = colorResource(R.color.game_screenshots_title_text_color),
                fontSize = textSizeResource(R.dimen.game_screenshots_title_text_size),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
            )

            LazyRow(
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.game_screenshots_horizontal_content_padding),
                ),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.game_screenshots_horizontal_arrangement),
                )
            ) {
                itemsIndexed(
                    items = screenshots,
                    key = { _, screenshot -> screenshot.id },
                ) { index, screenshot ->
                    GameScreenshot(
                        screenshotUrl = screenshot.url,
                        modifier = Modifier.size(
                            width = dimensionResource(R.dimen.game_screenshots_item_width),
                            height = dimensionResource(R.dimen.game_screenshots_item_height),
                        ),
                        onScreenshotClicked = { onScreenshotClicked(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun GameScreenshot(
    screenshotUrl: String,
    modifier: Modifier,
    onScreenshotClicked: () -> Unit,
) {
    Card(
        onClick = onScreenshotClicked,
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.game_screenshot_card_corner_radius)),
        backgroundColor = colorResource(R.color.game_screenshot_card_background_color),
        elevation = dimensionResource(R.dimen.game_screenshot_card_elevation),
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(screenshotUrl)
                        .fallback(R.drawable.game_landscape_placeholder)
                        .placeholder(R.drawable.game_landscape_placeholder)
                        .error(R.drawable.game_landscape_placeholder)
                        .crossfade(CROSSFADE_ANIMATION_DURATION)
                        .build(),
                ),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Preview
@Composable
internal fun GameScreenshotsPreview() {
    GameScreenshots(
        screenshots = listOf(
            GameInfoScreenshotModel(
                id = "1",
                url = "",
            ),
            GameInfoScreenshotModel(
                id = "2",
                url = "",
            ),
            GameInfoScreenshotModel(
                id = "3",
                url = "",
            ),
        ),
        onScreenshotClicked = {},
    )
}
