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

package com.paulrybitskyi.gamedge

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.ComposeNavigatorDestinationBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.get
import androidx.navigation.toRoute
import com.paulrybitskyi.gamedge.common.ui.HorizontalSliding
import com.paulrybitskyi.gamedge.common.ui.OvershootScaling
import com.paulrybitskyi.gamedge.feature.category.GamesCategoryDirection
import com.paulrybitskyi.gamedge.feature.category.GamesCategoryScreen
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryDirection
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryScreen
import com.paulrybitskyi.gamedge.feature.image.viewer.ImageViewerDirection
import com.paulrybitskyi.gamedge.feature.image.viewer.ImageViewerScreen
import com.paulrybitskyi.gamedge.feature.info.presentation.GameInfoDirection
import com.paulrybitskyi.gamedge.feature.info.presentation.GameInfoScreen
import com.paulrybitskyi.gamedge.feature.likes.presentation.LikedGamesDirection
import com.paulrybitskyi.gamedge.feature.likes.presentation.LikedGamesScreen
import com.paulrybitskyi.gamedge.feature.news.presentation.GamingNewsScreen
import com.paulrybitskyi.gamedge.feature.search.presentation.GamesSearchDirection
import com.paulrybitskyi.gamedge.feature.search.presentation.GamesSearchScreen
import com.paulrybitskyi.gamedge.feature.settings.presentation.SettingsScreen
import kotlin.reflect.KClass
import kotlin.reflect.KType

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = START_SCREEN.routeClass,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        gamesDiscoveryScreen(
            navController = navController,
            modifier = modifier,
        )
        likedGamesScreen(
            navController = navController,
            modifier = modifier,
        )
        gamingNewsScreen(modifier = modifier)
        settingsScreen(modifier = modifier)
        gamesSearchScreen(navController = navController)
        gamesCategoryScreen(navController = navController)
        gameInfoScreen(navController = navController)
        imageViewerScreen(navController = navController)
    }
}

private fun NavGraphBuilder.gamesDiscoveryScreen(
    navController: NavHostController,
    modifier: Modifier,
) {
    composable(
        route = Screen.GamesDiscovery.routeClass,
        enterTransition = { null },
        exitTransition = {
            when (Screen.forRoute(targetState.destination.requireRoute())) {
                Screen.GamesSearch -> OvershootScaling.exit()
                Screen.GamesCategory,
                Screen.GameInfo,
                -> HorizontalSliding.exit()
                else -> null
            }
        },
        popEnterTransition = {
            when (Screen.forRoute(initialState.destination.requireRoute())) {
                Screen.GamesSearch -> OvershootScaling.popEnter()
                Screen.GamesCategory,
                Screen.GameInfo,
                -> HorizontalSliding.popEnter()
                else -> null
            }
        },
        popExitTransition = { null },
    ) {
        GamesDiscoveryScreen(modifier) { direction ->
            when (direction) {
                is GamesDiscoveryDirection.Search -> {
                    navController.navigate(Screen.GamesSearch.route)
                }
                is GamesDiscoveryDirection.Category -> {
                    navController.navigate(Screen.GamesCategory.createRoute(category = direction.category))
                }
                is GamesDiscoveryDirection.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = direction.gameId))
                }
            }
        }
    }
}

private fun NavGraphBuilder.likedGamesScreen(
    navController: NavHostController,
    modifier: Modifier,
) {
    composable(
        route = Screen.LikedGames.routeClass,
        enterTransition = { null },
        exitTransition = {
            when (Screen.forRoute(targetState.destination.requireRoute())) {
                Screen.GamesSearch -> OvershootScaling.exit()
                Screen.GameInfo -> HorizontalSliding.exit()
                else -> null
            }
        },
        popEnterTransition = {
            when (Screen.forRoute(initialState.destination.requireRoute())) {
                Screen.GamesSearch -> OvershootScaling.popEnter()
                Screen.GameInfo -> HorizontalSliding.popEnter()
                else -> null
            }
        },
        popExitTransition = { null },
    ) {
        LikedGamesScreen(modifier) { direction ->
            when (direction) {
                is LikedGamesDirection.Search -> {
                    navController.navigate(Screen.GamesSearch.route)
                }
                is LikedGamesDirection.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = direction.gameId))
                }
            }
        }
    }
}

private fun NavGraphBuilder.gamingNewsScreen(modifier: Modifier) {
    composable(route = Screen.GamingNews.routeClass) {
        GamingNewsScreen(modifier)
    }
}

private fun NavGraphBuilder.settingsScreen(modifier: Modifier) {
    composable(route = Screen.Settings.routeClass) {
        SettingsScreen(modifier)
    }
}

private fun NavGraphBuilder.gamesSearchScreen(navController: NavHostController) {
    composable(
        route = Screen.GamesSearch.routeClass,
        enterTransition = {
            when (Screen.forRoute(initialState.destination.requireRoute())) {
                Screen.GamingNews,
                Screen.GamesDiscovery,
                Screen.LikedGames,
                -> OvershootScaling.enter()
                else -> null
            }
        },
        exitTransition = {
            when (Screen.forRoute(targetState.destination.requireRoute())) {
                Screen.GameInfo -> HorizontalSliding.exit()
                else -> null
            }
        },
        popEnterTransition = {
            when (Screen.forRoute(initialState.destination.requireRoute())) {
                Screen.GameInfo -> HorizontalSliding.popEnter()
                else -> null
            }
        },
        popExitTransition = {
            when (Screen.forRoute(targetState.destination.requireRoute())) {
                Screen.GamingNews,
                Screen.GamesDiscovery,
                Screen.LikedGames,
                -> OvershootScaling.popExit()
                else -> null
            }
        },
    ) {
        GamesSearchScreen { direction ->
            when (direction) {
                is GamesSearchDirection.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = direction.gameId))
                }
                is GamesSearchDirection.Back -> {
                    navController.popBackStack()
                }
            }
        }
    }
}

private fun NavGraphBuilder.gamesCategoryScreen(navController: NavHostController) {
    composable(
        route = Screen.GamesCategory.routeClass,
        enterTransition = {
            when (Screen.forRoute(initialState.destination.requireRoute())) {
                Screen.GamesDiscovery -> HorizontalSliding.enter()
                else -> null
            }
        },
        exitTransition = {
            when (Screen.forRoute(targetState.destination.requireRoute())) {
                Screen.GameInfo -> HorizontalSliding.exit()
                else -> null
            }
        },
        popEnterTransition = {
            when (Screen.forRoute(initialState.destination.requireRoute())) {
                Screen.GameInfo -> HorizontalSliding.popEnter()
                else -> null
            }
        },
        popExitTransition = {
            when (Screen.forRoute(targetState.destination.requireRoute())) {
                Screen.GamesDiscovery -> HorizontalSliding.popExit()
                else -> null
            }
        },
    ) { entry ->
        GamesCategoryScreen(route = entry.toRoute()) { direction ->
            when (direction) {
                is GamesCategoryDirection.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = direction.gameId))
                }
                is GamesCategoryDirection.Back -> {
                    navController.popBackStack()
                }
            }
        }
    }
}

private fun NavGraphBuilder.gameInfoScreen(navController: NavHostController) {
    composable(
        route = Screen.GameInfo.routeClass,
        enterTransition = {
            when (Screen.forRoute(initialState.destination.requireRoute())) {
                Screen.GamesDiscovery,
                Screen.LikedGames,
                Screen.GamesSearch,
                Screen.GamesCategory,
                Screen.GameInfo,
                -> HorizontalSliding.enter()
                else -> null
            }
        },
        exitTransition = {
            when (Screen.forRoute(targetState.destination.requireRoute())) {
                Screen.ImageViewer,
                Screen.GameInfo,
                -> HorizontalSliding.exit()
                else -> null
            }
        },
        popEnterTransition = {
            when (Screen.forRoute(initialState.destination.requireRoute())) {
                Screen.ImageViewer,
                Screen.GameInfo,
                -> HorizontalSliding.popEnter()
                else -> null
            }
        },
        popExitTransition = {
            when (Screen.forRoute(targetState.destination.requireRoute())) {
                Screen.GamesDiscovery,
                Screen.LikedGames,
                Screen.GamesSearch,
                Screen.GamesCategory,
                Screen.GameInfo,
                -> HorizontalSliding.popExit()
                else -> null
            }
        },
    ) { entry ->
        GameInfoScreen(route = entry.toRoute()) { direction ->
            when (direction) {
                is GameInfoDirection.ImageViewer -> {
                    navController.navigate(
                        Screen.ImageViewer.createRoute(
                            imageUrls = direction.imageUrls,
                            title = direction.title,
                            initialPosition = direction.initialPosition,
                        ),
                    )
                }
                is GameInfoDirection.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = direction.gameId))
                }
                is GameInfoDirection.Back -> {
                    navController.popBackStack()
                }
            }
        }
    }
}

private fun NavGraphBuilder.imageViewerScreen(navController: NavHostController) {
    composable(
        route = Screen.ImageViewer.routeClass,
        enterTransition = {
            when (Screen.forRoute(initialState.destination.requireRoute())) {
                Screen.GameInfo -> HorizontalSliding.enter()
                else -> null
            }
        },
        exitTransition = { null },
        popEnterTransition = { null },
        popExitTransition = {
            when (Screen.forRoute(targetState.destination.requireRoute())) {
                Screen.GameInfo -> HorizontalSliding.popExit()
                else -> null
            }
        },
    ) { entry ->
        ImageViewerScreen(route = entry.toRoute()) { direction ->
            when (direction) {
                is ImageViewerDirection.Back -> navController.popBackStack()
            }
        }
    }
}

@Suppress("LongParameterList")
private fun NavGraphBuilder.composable(
    route: KClass<*>,
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
        enterTransition,
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
        exitTransition,
    sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)? = null,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    destination(
        ComposeNavigatorDestinationBuilder(
            provider[ComposeNavigator::class],
            route,
            typeMap,
            content,
        )
        .apply {
            deepLinks.forEach { deepLink -> deepLink(deepLink) }
            this.enterTransition = enterTransition
            this.exitTransition = exitTransition
            this.popEnterTransition = popEnterTransition
            this.popExitTransition = popExitTransition
            this.sizeTransform = sizeTransform
        },
    )
}
