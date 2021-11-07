/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.commons.ui.widgets.chipflow

import android.content.Context
import com.paulrybitskyi.commons.ktx.getCompatColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.gamedge.commons.ui.widgets.R

internal data class DefaultValues(
    val backgroundColor: Int,
    val iconColor: Int,
    val backgroundCornerRadius: Float,
    val iconSize: Float,
    val textAppearanceResource: Int,
    val horizontalSpacing: Int,
    val verticalSpacing: Int
)

internal fun initDefaultValues(context: Context): DefaultValues {
    return DefaultValues(
        backgroundColor = context.getCompatColor(R.color.chip_flow_default_item_background_color),
        iconColor = context.getCompatColor(R.color.chip_flow_default_item_icon_color),
        backgroundCornerRadius = context.getDimension(R.dimen.chip_flow_default_item_background_corner_radius),
        iconSize = context.getDimension(R.dimen.chip_flow_default_item_icon_size),
        textAppearanceResource = R.style.Gamedge_ChipFlow_TextAppearance,
        horizontalSpacing = context.getDimensionPixelSize(R.dimen.chip_flow_default_item_horizontal_spacing),
        verticalSpacing = context.getDimensionPixelSize(R.dimen.chip_flow_default_item_vertical_spacing)
    )
}
