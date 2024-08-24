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

package com.paulrybitskyi.gamedge.feature.info.presentation.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.presentation.widgets.utils.GameInfoSection

private const val AnimationDuration = 300
private const val ContentMaxLines = 4

@Composable
internal fun GameInfoSummary(summary: String) {
    var hasTextBeenLaidOut by rememberSaveable { mutableStateOf(false) }
    var collapsedHeight by rememberSaveable { mutableIntStateOf(0) }
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var isExpandable by rememberSaveable { mutableStateOf(true) }
    val cardClickableModifier by remember {
        derivedStateOf {
            if (isExpandable || isExpanded) {
                Modifier.clickable(
                    onClick = { isExpanded = !isExpanded },
                )
            } else {
                Modifier
            }
        }
    }

    GameInfoSection(
        title = stringResource(R.string.game_info_summary_title),
        modifier = cardClickableModifier,
    ) { paddingValues ->
        AnimatedContent(
            targetState = isExpanded,
            modifier = Modifier.padding(paddingValues),
            transitionSpec = {
                val isExpanding = !initialState && targetState

                if (isExpanding) {
                    expandVertically(
                        animationSpec = tween(AnimationDuration),
                        expandFrom = Alignment.Top,
                        initialHeight = { collapsedHeight },
                    ) togetherWith ExitTransition.None
                } else {
                    EnterTransition.None togetherWith shrinkVertically(
                        animationSpec = tween(AnimationDuration),
                        shrinkTowards = Alignment.Top,
                        targetHeight = { collapsedHeight },
                    )
                } using SizeTransform(
                    sizeAnimationSpec = { _, _ -> tween(AnimationDuration) },
                )
            },
            label = "GameInfoSummary",
        ) { isInExpandedState ->
            Text(
                text = summary,
                color = GamedgeTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (isInExpandedState) Int.MAX_VALUE else ContentMaxLines,
                onTextLayout = { textLayoutResult ->
                    if (!hasTextBeenLaidOut) {
                        hasTextBeenLaidOut = true
                        collapsedHeight = textLayoutResult.size.height
                        isExpandable = textLayoutResult.didOverflowHeight
                    }
                },
                style = GamedgeTheme.typography.body1,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun GameInfoSummaryCollapsedPreview() {
    GamedgeTheme {
        GameInfoSummary(
            summary = "Elden Ring is an action-RPG open world game with RPG " +
                    "elements such as stats, weapons and spells.",
        )
    }
}
