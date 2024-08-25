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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.core.R as CoreR
import com.paulrybitskyi.gamedge.feature.discovery.R as FeatureDiscoveryR
import com.paulrybitskyi.gamedge.feature.likes.R as FeatureLikesR
import com.paulrybitskyi.gamedge.feature.news.R as FeatureNewsR
import com.paulrybitskyi.gamedge.feature.settings.R as FeatureSettingsR

private const val BOTTOM_BAR_ANIMATION_DURATION = 300

@Composable
internal fun BottomBar(
    navController: NavHostController,
    currentScreen: Screen,
) {
    val isBottomBarScreenDisplayed = remember(currentScreen) {
        BottomNavigationItemModel
            .entries
            .any { itemModel -> itemModel.screen == currentScreen }
    }

    LaunchedEffect(isBottomBarScreenDisplayed) {
        navController.enableOnBackPressed(!isBottomBarScreenDisplayed)
    }

    AnimatedVisibility(
        visible = isBottomBarScreenDisplayed,
        enter = slideInVertically(
            animationSpec = tween(BOTTOM_BAR_ANIMATION_DURATION),
            initialOffsetY = { it },
        ),
        exit = slideOutVertically(
            animationSpec = tween(BOTTOM_BAR_ANIMATION_DURATION),
            targetOffsetY = { it },
        ),
    ) {
        BottomBarNavigation(
            navController = navController,
            currentScreen = currentScreen,
        )
    }
}

@Composable
private fun BottomBarNavigation(
    navController: NavHostController,
    currentScreen: Screen,
) {
    BottomNavigation(
        windowInsets = WindowInsets.navigationBars,
        backgroundColor = GamedgeTheme.colors.primary,
    ) {
        for (bottomNavigationItemModel in BottomNavigationItemModel.entries) {
            val itemScreen = bottomNavigationItemModel.screen

            BottomNavigationItem(
                selected = currentScreen == itemScreen,
                onClick = {
                    navController.navigate(itemScreen.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(bottomNavigationItemModel.iconId),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(text = stringResource(bottomNavigationItemModel.titleId))
                },
                selectedContentColor = GamedgeTheme.colors.secondary,
                unselectedContentColor = GamedgeTheme.colors.onBackground,
            )
        }
    }
}

private enum class BottomNavigationItemModel(
    @DrawableRes
    val iconId: Int,
    @StringRes
    val titleId: Int,
    val screen: Screen,
) {
    DISCOVER(
        iconId = CoreR.drawable.compass_rose,
        titleId = FeatureDiscoveryR.string.games_discovery_toolbar_title,
        screen = Screen.Discover,
    ),
    LIKES(
        iconId = CoreR.drawable.heart,
        titleId = FeatureLikesR.string.liked_games_toolbar_title,
        screen = Screen.Likes,
    ),
    NEWS(
        iconId = CoreR.drawable.newspaper,
        titleId = FeatureNewsR.string.gaming_news_toolbar_title,
        screen = Screen.News,
    ),
    SETTINGS(
        iconId = CoreR.drawable.cog_outline,
        titleId = FeatureSettingsR.string.settings_toolbar_title,
        screen = Screen.Settings,
    ),
}
