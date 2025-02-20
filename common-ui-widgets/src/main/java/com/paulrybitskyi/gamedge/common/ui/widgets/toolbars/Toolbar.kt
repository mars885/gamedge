/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.common.ui.widgets.toolbars

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.core.R

@Composable
fun Toolbar(
    title: String,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = ToolbarDefaults.windowInsets,
    backgroundColor: Color = GamedgeTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = ToolbarElevation,
    titleTextStyle: TextStyle = GamedgeTheme.typography.h5,
    leftButtonIcon: Painter? = null,
    rightButtonIcon: Painter? = null,
    onLeftButtonClick: (() -> Unit)? = null,
    onRightButtonClick: (() -> Unit)? = null,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .height(ToolbarHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val titleLeftPadding = getTitleHorizontalPadding(leftButtonIcon)
            val titleRightPadding = getTitleHorizontalPadding(rightButtonIcon)

            if (leftButtonIcon != null) {
                Button(
                    icon = leftButtonIcon,
                    onClick = { onLeftButtonClick?.invoke() },
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
                Button(
                    icon = rightButtonIcon,
                    onClick = { onRightButtonClick?.invoke() },
                )
            }
        }
    }
}

@Composable
private fun getTitleHorizontalPadding(icon: Painter?): Dp {
    return if (icon != null) {
        GamedgeTheme.spaces.spacing_4_0
    } else {
        GamedgeTheme.spaces.spacing_5_0
    }
}

@Composable
private fun Button(
    icon: Painter,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier.size(ToolbarHeight),
        onClick = onClick,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
    }
}

@PreviewLightDark
@Composable
private fun ToolbarPreviewWithTitle() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar",
        )
    }
}

@PreviewLightDark
@Composable
private fun ToolbarPreviewWithLongTitle() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar toolbar toolbar toolbar toolbar toolbar toolbar toolbar",
        )
    }
}

@PreviewLightDark
@Composable
private fun ToolbarPreviewWithBothIcons() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar",
            leftButtonIcon = painterResource(R.drawable.arrow_left),
            rightButtonIcon = painterResource(R.drawable.magnify),
        )
    }
}

@PreviewLightDark
@Composable
private fun ToolbarPreviewWithLeftIcon() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar",
            leftButtonIcon = painterResource(R.drawable.arrow_left),
        )
    }
}

@PreviewLightDark
@Composable
private fun ToolbarPreviewWithRightIcon() {
    GamedgeTheme {
        Toolbar(
            title = "Toolbar",
            rightButtonIcon = painterResource(R.drawable.magnify),
        )
    }
}
