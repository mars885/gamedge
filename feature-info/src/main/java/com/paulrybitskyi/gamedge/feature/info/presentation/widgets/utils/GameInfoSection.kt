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

package com.paulrybitskyi.gamedge.feature.info.presentation.widgets.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.widgets.GamedgeCard

@Composable
internal fun GameInfoSection(
    title: String,
    modifier: Modifier = Modifier,
    titleBottomPadding: Dp = GamedgeTheme.spaces.spacing_2_5,
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
) {
    GamedgeCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
    ) {
        val contentPadding = GamedgeTheme.spaces.spacing_3_5

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = contentPadding),
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(horizontal = contentPadding)
                    .padding(bottom = titleBottomPadding),
                color = GamedgeTheme.colors.onPrimary,
                style = GamedgeTheme.typography.h6,
            )

            content(PaddingValues(horizontal = contentPadding))
        }
    }
}

@Composable
internal fun GameInfoSectionWithInnerList(
    title: String,
    content: LazyListScope.() -> Unit,
) {
    GameInfoSection(title = title) { paddingValues ->
        LazyRow(
            contentPadding = paddingValues,
            horizontalArrangement = Arrangement.spacedBy(GamedgeTheme.spaces.spacing_1_5),
            content = content,
        )
    }
}
