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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.unit.Dp
import com.paulrybitskyi.commons.device.info.screenMetrics
import com.paulrybitskyi.commons.ktx.getFloat
import com.paulrybitskyi.gamedge.feature.category.R

@Immutable
internal data class GamesGridConfig(
    val spanCount: Int,
    val itemSpacingInPx: Float,
    val itemWidthInDp: Dp,
    val itemHeightInDp: Dp,
)

@Composable
internal fun rememberGamesGridConfig(): GamesGridConfig {
    val context = LocalContext.current
    val density = LocalDensity.current
    val gridSpanCount = integerResource(R.integer.games_category_grid_span_count)
    val gridItemSpacingInDp = dimensionResource(R.dimen.games_category_grid_item_spacing)

    return remember(context, density) {
        val gridItemSpacingInPx = with(density) { gridItemSpacingInDp.toPx() }

        val horizontalTotalSpacing = (gridItemSpacingInPx * (gridSpanCount + 1))
        val screenWidth = (context.screenMetrics.width.sizeInPixels - horizontalTotalSpacing)
        val itemWidth = (screenWidth / gridSpanCount.toFloat())
        val itemWidthInDp = with(density) { itemWidth.toDp() }

        val itemHeightToWidthRatio = context.getFloat(R.integer.games_category_item_height_to_width_ratio)
        val itemHeight = (itemWidth * itemHeightToWidthRatio)
        val itemHeightInDp = with(density) { itemHeight.toDp() }

        GamesGridConfig(
            spanCount = gridSpanCount,
            itemSpacingInPx = gridItemSpacingInPx,
            itemWidthInDp = itemWidthInDp,
            itemHeightInDp = itemHeightInDp,
        )
    }
}
