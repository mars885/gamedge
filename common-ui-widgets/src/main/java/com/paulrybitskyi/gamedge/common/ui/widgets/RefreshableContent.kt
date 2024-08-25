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

package com.paulrybitskyi.gamedge.common.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme

@Composable
fun RefreshableContent(
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    isSwipeEnabled: Boolean = true,
    onRefreshRequested: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefreshRequested ?: {},
        refreshingOffset = PullRefreshDefaults.RefreshingOffset + GamedgeTheme.spaces.spacing_1_5,
    )

    Box(
        modifier = modifier.pullRefresh(
            state = refreshState,
            enabled = isSwipeEnabled,
        ),
    ) {
        content()

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = GamedgeTheme.colors.secondary,
        )
    }
}
