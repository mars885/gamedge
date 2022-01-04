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

package com.paulrybitskyi.gamedge.feature.info.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource
import com.paulrybitskyi.gamedge.feature.info.R

private const val ANIMATION_DURATION = 300

@Composable
internal fun GameSummary(summary: String) {
    var isExpanded by remember { mutableStateOf(false) }
    var isExpandable by remember { mutableStateOf(true) }
    val cardClickableModifier = if (isExpandable || isExpanded) {
        Modifier.clickable(onClick = { isExpanded = !isExpanded })
    } else {
        Modifier
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(cardClickableModifier),
        shape = RectangleShape,
        backgroundColor = colorResource(R.color.game_summary_card_background_color),
        elevation = dimensionResource(R.dimen.game_summary_card_elevation),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.game_summary_title),
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.game_summary_title_padding),
                        bottom = dimensionResource(R.dimen.game_summary_title_padding_bottom)
                    )
                    .padding(horizontal = dimensionResource(R.dimen.game_summary_title_padding)),
                color = colorResource(R.color.game_summary_title_text_color),
                fontSize = textSizeResource(R.dimen.game_summary_title_text_size),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = summary,
                modifier = Modifier
                    .animateContentSize(animationSpec = tween(durationMillis = ANIMATION_DURATION))
                    .padding(horizontal = dimensionResource(R.dimen.game_summary_content_padding))
                    .padding(bottom = dimensionResource(R.dimen.game_summary_content_padding)),
                color = colorResource(R.color.game_summary_content_text_color),
                fontSize = textSizeResource(R.dimen.game_summary_content_text_size),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (isExpanded) {
                    Integer.MAX_VALUE
                } else {
                    integerResource(R.integer.game_summary_content_max_lines)
                },
                onTextLayout = { textLayoutResult ->
                    isExpandable = textLayoutResult.didOverflowHeight
                }
            )
        }
    }
}

@Preview
@Composable
internal fun GameSummaryCollapsedPreview() {
    GameSummary(
        summary = "Elden Ring is an action-RPG open world game with RPG " +
            "elements such as stats, weapons and spells.",
    )
}
