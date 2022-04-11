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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.utils.GameInfoSection

private const val ANIMATION_DURATION = 300
private const val CONTENT_MAX_LINES = 4

@Composable
internal fun GameInfoSummary(summary: String) {
    var isExpanded by remember { mutableStateOf(false) }
    var isExpandable by remember { mutableStateOf(true) }
    val cardClickableModifier by remember {
        derivedStateOf {
            if (isExpandable || isExpanded) {
                Modifier.clickable(onClick = { isExpanded = !isExpanded })
            } else {
                Modifier
            }
        }
    }

    GameInfoSection(
        title = stringResource(R.string.game_summary_title),
        modifier = cardClickableModifier,
    ) { paddingValues ->
        Text(
            text = summary,
            modifier = Modifier
                .animateContentSize(animationSpec = tween(durationMillis = ANIMATION_DURATION))
                .padding(paddingValues),
            color = GamedgeTheme.colors.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = if (isExpanded) Integer.MAX_VALUE else CONTENT_MAX_LINES,
            onTextLayout = { textLayoutResult ->
                isExpandable = textLayoutResult.didOverflowHeight
            },
            style = GamedgeTheme.typography.body1,
        )
    }
}

@Preview
@Composable
internal fun GameInfoSummaryCollapsedPreview() {
    GamedgeTheme {
        GameInfoSummary(
            summary = "Elden Ring is an action-RPG open world game with RPG " +
                    "elements such as stats, weapons and spells.",
        )
    }
}
