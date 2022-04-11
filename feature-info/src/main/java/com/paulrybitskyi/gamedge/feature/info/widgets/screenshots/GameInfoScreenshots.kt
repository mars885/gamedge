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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeCard
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.utils.GameInfoSectionWithInnerList

@Composable
internal fun GameInfoScreenshots(
    screenshots: List<GameInfoScreenshotModel>,
    onScreenshotClicked: (screenshotIndex: Int) -> Unit,
) {
    GameInfoSectionWithInnerList(title = stringResource(R.string.game_screenshots_title)) {
        itemsIndexed(
            items = screenshots,
            key = { _, screenshot -> screenshot.id },
        ) { index, screenshot ->
            Screenshot(
                screenshot = screenshot,
                modifier = Modifier.size(width = 268.dp, height = 150.dp),
                onScreenshotClicked = { onScreenshotClicked(index) },
            )
        }
    }
}

@Composable
private fun Screenshot(
    screenshot: GameInfoScreenshotModel,
    modifier: Modifier,
    onScreenshotClicked: () -> Unit,
) {
    GamedgeCard(
        onClick = onScreenshotClicked,
        modifier = modifier,
        shape = GamedgeTheme.shapes.medium,
        backgroundColor = Color.Transparent,
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(screenshot.url)
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
internal fun GameInfoScreenshotsPreview() {
    GamedgeTheme {
        GameInfoScreenshots(
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
}
