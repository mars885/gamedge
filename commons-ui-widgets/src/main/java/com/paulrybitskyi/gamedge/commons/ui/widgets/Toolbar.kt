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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.paulrybitskyi.gamedge.commons.ui.extensions.textSizeResource


@Composable
fun Toolbar(
    title: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorResource(R.color.toolbar_background_color),
    titleTextColor: Color = colorResource(R.color.toolbar_title_text_color),
    iconColor: Color = colorResource(R.color.toolbar_button_icon_color),
    titleTextSize: TextUnit = textSizeResource(R.dimen.toolbar_title_text_size),
    leftButtonIcon: ImageVector? = null,
    rightButtonIcon: ImageVector? = null,
    onLeftButtonClick: (() -> Unit)? = null,
    onRightButtonClick: (() -> Unit)? = null
) {
    val titleLeftPadding = getTitleHorizontalPadding(leftButtonIcon)
    val titleRightPadding = getTitleHorizontalPadding(rightButtonIcon)

    Row(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .then(modifier)
            .height(dimensionResource(R.dimen.toolbar_height)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(leftButtonIcon != null) {
            ToolbarButton(
                icon = leftButtonIcon,
                iconDescription = "Left button of the toolbar",
                iconColor = iconColor,
                onClick = { onLeftButtonClick?.invoke() }
            )
        }

        Text(
            text = title,
            modifier = Modifier
                .weight(1F)
                .padding(start = titleLeftPadding, end = titleRightPadding),
            color = titleTextColor,
            fontSize = titleTextSize,
            fontFamily = FontFamily.SansSerif,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        if(rightButtonIcon != null) {
            ToolbarButton(
                icon = rightButtonIcon,
                iconDescription = "Right button of the toolbar",
                iconColor = iconColor,
                onClick = { onRightButtonClick?.invoke() }
            )
        }
    }
}


@Composable
private fun getTitleHorizontalPadding(icon: ImageVector?): Dp {
    return dimensionResource(
        if(icon != null) {
            R.dimen.toolbar_title_horizontal_padding_with_icon
        } else {
            R.dimen.toolbar_title_horizontal_padding_without_icon
        }
    )
}


@Composable
private fun ToolbarButton(
    icon: ImageVector,
    iconDescription: String? = null,
    iconColor: Color = colorResource(R.color.toolbar_button_icon_color),
    onClick: (() -> Unit)? = null
) {
    IconButton(
        modifier = Modifier.size(dimensionResource(R.dimen.toolbar_height)),
        onClick = { onClick?.invoke() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            tint = iconColor
        )
    }
}


@Preview
@Composable
private fun ToolbarPreviewWithTitle() {
    Toolbar(
        title = "Toolbar"
    )
}


@Preview
@Composable
private fun ToolbarPreviewWithLongTitle() {
    Toolbar(
        title = "Toolbar toolbar toolbar toolbar toolbar toolbar toolbar toolbar"
    )
}


@Preview
@Composable
private fun ToolbarPreviewWithBothIcons() {
    Toolbar(
        title = "Toolbar",
        leftButtonIcon = Icons.Filled.Menu,
        rightButtonIcon = Icons.Filled.Check
    )
}


@Preview
@Composable
private fun ToolbarPreviewWithLeftIcon() {
    Toolbar(
        title = "Toolbar",
        leftButtonIcon = Icons.Filled.Menu
    )
}


@Preview
@Composable
private fun ToolbarPreviewWithRightIcon() {
    Toolbar(
        title = "Toolbar",
        rightButtonIcon = Icons.Filled.Check
    )
}