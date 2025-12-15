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

package com.paulrybitskyi.gamedge.navigation

import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.paulrybitskyi.gamedge.common.ui.Fading
import com.paulrybitskyi.gamedge.common.ui.HorizontalSliding
import com.paulrybitskyi.gamedge.common.ui.OvershootScaling
import com.paulrybitskyi.gamedge.feature.category.GamesCategoryDirection
import com.paulrybitskyi.gamedge.feature.category.GamesCategoryRoute
import com.paulrybitskyi.gamedge.feature.category.GamesCategoryScreen
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryDirection
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryRoute
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryScreen
import com.paulrybitskyi.gamedge.feature.image.viewer.ImageViewerDirection
import com.paulrybitskyi.gamedge.feature.image.viewer.ImageViewerRoute
import com.paulrybitskyi.gamedge.feature.image.viewer.ImageViewerScreen
import com.paulrybitskyi.gamedge.feature.info.presentation.GameInfoDirection
import com.paulrybitskyi.gamedge.feature.info.presentation.GameInfoRoute
import com.paulrybitskyi.gamedge.feature.info.presentation.GameInfoScreen
import com.paulrybitskyi.gamedge.feature.likes.presentation.LikedGamesDirection
import com.paulrybitskyi.gamedge.feature.likes.presentation.LikedGamesRoute
import com.paulrybitskyi.gamedge.feature.likes.presentation.LikedGamesScreen
import com.paulrybitskyi.gamedge.feature.news.presentation.GamingNewsRoute
import com.paulrybitskyi.gamedge.feature.news.presentation.GamingNewsScreen
import com.paulrybitskyi.gamedge.feature.search.presentation.GamesSearchDirection
import com.paulrybitskyi.gamedge.feature.search.presentation.GamesSearchRoute
import com.paulrybitskyi.gamedge.feature.search.presentation.GamesSearchScreen
import com.paulrybitskyi.gamedge.feature.settings.presentation.SettingsRoute
import com.paulrybitskyi.gamedge.feature.settings.presentation.SettingsScreen

@Composable
internal fun AppNavigation(
    backStack: NavBackStack<NavKey>,
    entryDecorators: List<NavEntryDecorator<NavKey>>,
    modifier: Modifier,
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = entryDecorators,
        transitionSpec = { HorizontalSliding.enter() togetherWith HorizontalSliding.exit() },
        popTransitionSpec = { HorizontalSliding.popEnter() togetherWith HorizontalSliding.popExit() },
        predictivePopTransitionSpec = { HorizontalSliding.popEnter() togetherWith HorizontalSliding.popExit() },
        entryProvider = entryProvider {
            gamesDiscoveryScreen(
                backStack = backStack,
                modifier = modifier,
            )
            likedGamesScreen(
                backStack = backStack,
                modifier = modifier,
            )
            gamingNewsScreen(modifier = modifier)
            settingsScreen(modifier = modifier)
            gamesSearchScreen(backStack = backStack)
            gamesCategoryScreen(backStack = backStack)
            gameInfoScreen(backStack = backStack)
            imageViewerScreen(backStack = backStack)
        },
    )
}

private fun EntryProviderScope<NavKey>.gamesDiscoveryScreen(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier,
) = entry<GamesDiscoveryRoute>(metadata = METADATA_BOTTOM_BAR_SCREEN_ANIMATIONS) {
    GamesDiscoveryScreen(modifier) { direction ->
        when (direction) {
            is GamesDiscoveryDirection.Search -> {
                backStack.add(GamesSearchRoute)
            }
            is GamesDiscoveryDirection.Category -> {
                backStack.add(GamesCategoryRoute(category = direction.category))
            }
            is GamesDiscoveryDirection.Info -> {
                backStack.add(GameInfoRoute(gameId = direction.gameId))
            }
        }
    }
}

private fun EntryProviderScope<NavKey>.likedGamesScreen(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier,
) = entry<LikedGamesRoute>(metadata = METADATA_BOTTOM_BAR_SCREEN_ANIMATIONS) {
    LikedGamesScreen(modifier) { direction ->
        when (direction) {
            is LikedGamesDirection.Search -> {
                backStack.add(GamesSearchRoute)
            }
            is LikedGamesDirection.Info -> {
                backStack.add(GameInfoRoute(gameId = direction.gameId))
            }
        }
    }
}

private fun EntryProviderScope<NavKey>.gamingNewsScreen(modifier: Modifier) {
    entry<GamingNewsRoute>(metadata = METADATA_BOTTOM_BAR_SCREEN_ANIMATIONS) {
        GamingNewsScreen(modifier)
    }
}

private fun EntryProviderScope<NavKey>.settingsScreen(modifier: Modifier) {
    entry<SettingsRoute>(metadata = METADATA_BOTTOM_BAR_SCREEN_ANIMATIONS) {
        SettingsScreen(modifier)
    }
}

private fun EntryProviderScope<NavKey>.gamesSearchScreen(backStack: NavBackStack<NavKey>) {
    entry<GamesSearchRoute>(
        metadata = buildMap {
            putAll(
                NavDisplay.transitionSpec {
                    OvershootScaling.enter() togetherWith OvershootScaling.exit()
                },
            )
            putAll(
                NavDisplay.popTransitionSpec {
                    OvershootScaling.popEnter() togetherWith OvershootScaling.popExit()
                },
            )
            putAll(
                NavDisplay.predictivePopTransitionSpec {
                    OvershootScaling.popEnter() togetherWith OvershootScaling.popExit()
                },
            )
        },
    ) {
        GamesSearchScreen { direction ->
            when (direction) {
                is GamesSearchDirection.Info -> {
                    backStack.add(GameInfoRoute(gameId = direction.gameId))
                }
                is GamesSearchDirection.Back -> {
                    backStack.removeLastOrNull()
                }
            }
        }
    }
}

private fun EntryProviderScope<NavKey>.gamesCategoryScreen(backStack: NavBackStack<NavKey>) {
    entry<GamesCategoryRoute> { route ->
        GamesCategoryScreen(route = route) { direction ->
            when (direction) {
                is GamesCategoryDirection.Info -> {
                    backStack.add(GameInfoRoute(gameId = direction.gameId))
                }
                is GamesCategoryDirection.Back -> {
                    backStack.removeLastOrNull()
                }
            }
        }
    }
}

private fun EntryProviderScope<NavKey>.gameInfoScreen(backStack: NavBackStack<NavKey>) {
    entry<GameInfoRoute> { route ->
        GameInfoScreen(route = route) { direction ->
            when (direction) {
                is GameInfoDirection.ImageViewer -> {
                    backStack.add(
                        ImageViewerRoute(
                            imageUrls = direction.imageUrls,
                            title = direction.title,
                            initialPosition = direction.initialPosition,
                        ),
                    )
                }
                is GameInfoDirection.Info -> {
                    backStack.add(GameInfoRoute(gameId = direction.gameId))
                }
                is GameInfoDirection.Back -> {
                    backStack.removeLastOrNull()
                }
            }
        }
    }
}

private fun EntryProviderScope<NavKey>.imageViewerScreen(backStack: NavBackStack<NavKey>) {
    entry<ImageViewerRoute> { route ->
        ImageViewerScreen(route = route) { direction ->
            when (direction) {
                is ImageViewerDirection.Back -> backStack.removeLastOrNull()
            }
        }
    }
}

private val METADATA_BOTTOM_BAR_SCREEN_ANIMATIONS = buildMap {
    putAll(
        NavDisplay.transitionSpec {
            Fading.enter() togetherWith Fading.exit()
        },
    )
    putAll(
        NavDisplay.popTransitionSpec {
            Fading.popEnter() togetherWith Fading.popExit()
        },
    )
}
