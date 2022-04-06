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

package com.paulrybitskyi.gamedge.feature.info.widgets.utils

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeCard
import com.paulrybitskyi.gamedge.feature.info.R

@Composable
internal fun GameInfoSection(
    title: String,
    modifier: Modifier = Modifier,
    titleBottomPadding: Dp = dimensionResource(R.dimen.game_info_section_title_padding_bottom),
    content: @Composable ColumnScope.() -> Unit,
) {
    GamedgeCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.game_info_section_padding_vertical)),
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.game_info_section_title_padding_horizontal))
                    .padding(bottom = titleBottomPadding),
                color = GamedgeTheme.colors.onPrimary,
                style = GamedgeTheme.typography.h6,
            )

            content()
        }
    }
}

@Composable
internal fun GameInfoSectionWithInnerList(
    title: String,
    content: LazyListScope.() -> Unit,
) {
    GameInfoSection(title = title) {
        LazyRow(
            contentPadding = PaddingValues(
                horizontal = dimensionResource(R.dimen.game_info_section_horizontal_content_padding),
            ),
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.game_info_section_horizontal_arrangement),
            ),
            content = content,
        )
    }
}
