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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource

@Composable
fun Info(
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = dimensionResource(R.dimen.info_view_icon_size),
    iconColor: Color = colorResource(R.color.info_view_icon_color),
    titleTextSize: TextUnit = textSizeResource(R.dimen.info_view_title_text_size),
    titleTextColor: Color = colorResource(R.color.info_view_title_text_color)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            modifier = Modifier.size(iconSize),
            contentDescription = null,
            tint = iconColor
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.info_view_title_margin_top)))
        Text(
            text = title,
            color = titleTextColor,
            fontSize = titleTextSize,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(
    widthDp = 300,
    showBackground = true
)
@Composable
internal fun InfoPreview() {
    Info(
        icon = painterResource(R.drawable.heart),
        title = "Lorem ipsum dolor sit amet"
    )
}
