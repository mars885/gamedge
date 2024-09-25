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
import com.paulrybitskyi.gamedge.feature.category.GamesCategoryRoute
import com.paulrybitskyi.gamedge.feature.category.GamesCategoryScreen
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryRoute
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryScreen
import com.paulrybitskyi.gamedge.feature.image.viewer.ImageViewerRoute
import com.paulrybitskyi.gamedge.feature.image.viewer.ImageViewerScreen
import com.paulrybitskyi.gamedge.feature.info.presentation.GameInfoRoute
import com.paulrybitskyi.gamedge.feature.info.presentation.GameInfoScreen
import com.paulrybitskyi.gamedge.feature.likes.presentation.LikedGamesRoute
import com.paulrybitskyi.gamedge.feature.likes.presentation.LikedGamesScreen
import com.paulrybitskyi.gamedge.feature.news.presentation.GamingNewsScreen
import com.paulrybitskyi.gamedge.feature.search.presentation.GamesSearchRoute
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
        GamesDiscoveryScreen(modifier) { route ->
            when (route) {
                is GamesDiscoveryRoute.Search -> {
                    navController.navigate(Screen.GamesSearch.route)
                }
                is GamesDiscoveryRoute.Category -> {
                    navController.navigate(Screen.GamesCategory.createRoute(category = route.category))
                }
                is GamesDiscoveryRoute.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = route.gameId))
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
        LikedGamesScreen(modifier) { route ->
            when (route) {
                is LikedGamesRoute.Search -> {
                    navController.navigate(Screen.GamesSearch.route)
                }
                is LikedGamesRoute.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = route.gameId))
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
        GamesSearchScreen { route ->
            when (route) {
                is GamesSearchRoute.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = route.gameId))
                }
                is GamesSearchRoute.Back -> {
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
        GamesCategoryScreen(destination = entry.toRoute()) { route ->
            when (route) {
                is GamesCategoryRoute.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = route.gameId))
                }
                is GamesCategoryRoute.Back -> {
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
        GameInfoScreen(destination = entry.toRoute()) { route ->
            when (route) {
                is GameInfoRoute.ImageViewer -> {
                    navController.navigate(
                        Screen.ImageViewer.createRoute(
                            imageUrls = route.imageUrls,
                            title = route.title,
                            initialPosition = route.initialPosition,
                        ),
                    )
                }
                is GameInfoRoute.Info -> {
                    navController.navigate(Screen.GameInfo.createRoute(gameId = route.gameId))
                }
                is GameInfoRoute.Back -> {
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
        ImageViewerScreen(destination = entry.toRoute()) { route ->
            when (route) {
                is ImageViewerRoute.Back -> navController.popBackStack()
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
