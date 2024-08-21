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

package com.paulrybitskyi.gamedge.feature.info.presentation.widgets.screenshots

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.paulrybitskyi.gamedge.common.ui.images.defaultImageRequest
import com.paulrybitskyi.gamedge.common.ui.images.secondaryImage
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.widgets.GamedgeCard
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.utils.GameInfoSectionWithInnerList
import com.paulrybitskyi.gamedge.common.ui.widgets.R as CommonUiWidgetsR

@Composable
internal fun GameInfoScreenshots(
    screenshots: List<GameInfoScreenshotUiModel>,
    onScreenshotClicked: (screenshotIndex: Int) -> Unit,
) {
    GameInfoSectionWithInnerList(title = stringResource(R.string.game_info_screenshots_title)) {
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
    screenshot: GameInfoScreenshotUiModel,
    modifier: Modifier,
    onScreenshotClicked: () -> Unit,
) {
    GamedgeCard(
        onClick = onScreenshotClicked,
        modifier = modifier,
        shape = GamedgeTheme.shapes.medium,
        backgroundColor = Color.Transparent,
    ) {
        AsyncImage(
            model = defaultImageRequest(screenshot.url) {
                secondaryImage(CommonUiWidgetsR.drawable.game_landscape_placeholder)
            },
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@PreviewLightDark
@Composable
private fun GameInfoScreenshotsPreview() {
    GamedgeTheme {
        GameInfoScreenshots(
            screenshots = listOf(
                GameInfoScreenshotUiModel(
                    id = "1",
                    url = "",
                ),
                GameInfoScreenshotUiModel(
                    id = "2",
                    url = "",
                ),
                GameInfoScreenshotUiModel(
                    id = "3",
                    url = "",
                ),
            ),
            onScreenshotClicked = {},
        )
    }
}
