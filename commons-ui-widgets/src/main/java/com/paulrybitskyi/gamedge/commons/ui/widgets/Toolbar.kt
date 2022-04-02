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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme

@Composable
fun Toolbar(
    title: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = GamedgeTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    titleTextStyle: TextStyle = GamedgeTheme.typography.h5,
    leftButtonIcon: Painter? = null,
    rightButtonIcon: Painter? = null,
    onLeftButtonClick: (() -> Unit)? = null,
    onRightButtonClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
            .height(dimensionResource(R.dimen.toolbar_height)),
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val titleLeftPadding = getTitleHorizontalPadding(leftButtonIcon)
            val titleRightPadding = getTitleHorizontalPadding(rightButtonIcon)

            if (leftButtonIcon != null) {
                ToolbarButton(
                    icon = leftButtonIcon,
                    onClick = { onLeftButtonClick?.invoke() }
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = titleLeftPadding, end = titleRightPadding),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = titleTextStyle,
            )

            if (rightButtonIcon != null) {
                ToolbarButton(
                    icon = rightButtonIcon,
                    onClick = { onRightButtonClick?.invoke() }
                )
            }
        }
    }
}

@Composable
private fun getTitleHorizontalPadding(icon: Painter?): Dp {
    return dimensionResource(
        if (icon != null) {
            R.dimen.toolbar_title_horizontal_padding_with_icon
        } else {
            R.dimen.toolbar_title_horizontal_padding_without_icon
        }
    )
}

@Composable
private fun ToolbarButton(
    icon: Painter,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier.size(dimensionResource(R.dimen.toolbar_height)),
        onClick = onClick,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
internal fun ToolbarPreviewWithTitle() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar"
        )
    }
}

@Preview
@Composable
internal fun ToolbarPreviewWithLongTitle() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar toolbar toolbar toolbar toolbar toolbar toolbar toolbar"
        )
    }
}

@Preview
@Composable
internal fun ToolbarPreviewWithBothIcons() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar",
            leftButtonIcon = painterResource(R.drawable.arrow_left),
            rightButtonIcon = painterResource(R.drawable.magnify)
        )
    }
}

@Preview
@Composable
internal fun ToolbarPreviewWithLeftIcon() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar",
            leftButtonIcon = painterResource(R.drawable.arrow_left),
        )
    }
}

@Preview
@Composable
internal fun ToolbarPreviewWithRightIcon() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar",
            rightButtonIcon = painterResource(R.drawable.magnify)
        )
    }
}
