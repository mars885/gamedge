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

package com.paulrybitskyi.gamedge.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.paulrybitskyi.gamedge.commons.ui.HandleCommands
import com.paulrybitskyi.gamedge.commons.ui.HandleRoutes
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeProgressIndicator

@Composable
internal fun Splash(onRoute: (Route) -> Unit) {
    Splash(
        viewModel = hiltViewModel(),
        onRoute = onRoute,
    )
}

@Composable
private fun Splash(
    viewModel: SplashViewModel,
    onRoute: (Route) -> Unit,
) {
    HandleCommands(viewModel)
    HandleRoutes(viewModel = viewModel, onRoute = onRoute)
    Splash(
        uiState = viewModel.uiState.collectAsState().value,
    )
}

@Composable
private fun Splash(uiState: SplashUiState) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = BiasAlignment(
            horizontalBias = 0f,
            verticalBias = 0.5f,
        )
    ) {
        if (uiState.isProgressVisible) {
            GamedgeProgressIndicator(
                modifier = Modifier.size(30.dp),
                strokeWidth = 3.dp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    GamedgeTheme {
        Splash(
            uiState = SplashUiState(),
        )
    }
}
