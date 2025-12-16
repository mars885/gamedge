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

package com.paulrybitskyi.gamedge

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryRoute
import com.paulrybitskyi.gamedge.navigation.AppNavigation
import com.paulrybitskyi.gamedge.navigation.decorators.rememberConfigurableSaveableStateHolderNavEntryDecorator
import com.paulrybitskyi.gamedge.navigation.decorators.rememberConfigurableViewModelStoreNavEntryDecorator

@Composable
internal fun MainScreen() {
    val backStack = rememberNavBackStack(GamesDiscoveryRoute)

    Scaffold(
        bottomBar = {
            BottomBar(backStack = backStack)
        },
        content = { paddingValues ->
            AppNavigation(
                backStack = backStack,
                entryDecorators = listOf(
                    rememberConfigurableSaveableStateHolderNavEntryDecorator(
                        removeSavedStateOnPop = Any::isNotBottomNavScreen,
                    ),
                    rememberConfigurableViewModelStoreNavEntryDecorator(
                        removeViewModelStoreOnPop = Any::isNotBottomNavScreen,
                    ),
                ),
                modifier = Modifier.padding(paddingValues),
            )
        },
    )
}
