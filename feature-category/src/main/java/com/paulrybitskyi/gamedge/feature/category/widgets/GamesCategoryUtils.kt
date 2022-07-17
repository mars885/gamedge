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

package com.paulrybitskyi.gamedge.feature.category.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme

private const val GRID_SPAN_COUNT = 3
private const val ITEM_HEIGHT_TO_WIDTH_RATIO = 1.366f

@Immutable
internal data class GamesGridConfig(
    val spanCount: Int,
    val itemSpacingInPx: Float,
    val itemWidthInDp: Dp,
    val itemHeightInDp: Dp,
)

@Composable
internal fun rememberGamesGridConfig(): GamesGridConfig {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val gridItemSpacingInDp = GamedgeTheme.spaces.spacing_0_5

    return remember(density, configuration) {
        val gridItemSpacingInPx = with(density) { gridItemSpacingInDp.toPx() }
        val screenWidthInPx = with(density) { configuration.screenWidthDp.dp.roundToPx() }

        val horizontalTotalSpacing = (gridItemSpacingInPx * (GRID_SPAN_COUNT + 1))
        val availableScreenWidth = (screenWidthInPx - horizontalTotalSpacing)
        val itemWidth = (availableScreenWidth / GRID_SPAN_COUNT.toFloat())
        val itemWidthInDp = with(density) { itemWidth.toDp() }

        val itemHeight = (itemWidth * ITEM_HEIGHT_TO_WIDTH_RATIO)
        val itemHeightInDp = with(density) { itemHeight.toDp() }

        GamesGridConfig(
            spanCount = GRID_SPAN_COUNT,
            itemSpacingInPx = gridItemSpacingInPx,
            itemWidthInDp = itemWidthInDp,
            itemHeightInDp = itemHeightInDp,
        )
    }
}
