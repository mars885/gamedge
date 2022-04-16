/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.dashboard.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
internal fun MainScreen() {
    val navController = rememberAnimatedNavController()
    val currentScreen by navController.currentScreenAsState()

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentScreen = currentScreen,
            )
        },
        content = { paddingValues ->
            AppNavigation(
                navController = navController,
                modifier = Modifier.padding(paddingValues),
            )
        },
    )
}
