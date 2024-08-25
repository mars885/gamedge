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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import com.paulrybitskyi.gamedge.feature.category.GamesCategoryDestination
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryDestination
import com.paulrybitskyi.gamedge.feature.image.viewer.ImageViewerDestination
import com.paulrybitskyi.gamedge.feature.info.presentation.GameInfoDestination
import com.paulrybitskyi.gamedge.feature.likes.presentation.LikedGamesDestination
import com.paulrybitskyi.gamedge.feature.news.presentation.GamingNewsDestination
import com.paulrybitskyi.gamedge.feature.search.presentation.GamesSearchDestination
import com.paulrybitskyi.gamedge.feature.settings.presentation.SettingsDestination
import kotlin.reflect.KClass

internal val START_SCREEN = Screen.GamesDiscovery

internal sealed class Screen(val routeClass: KClass<*>) {

    data object GamesDiscovery : Screen(GamesDiscoveryDestination::class)
    data object LikedGames : Screen(LikedGamesDestination::class)
    data object GamingNews : Screen(GamingNewsDestination::class)
    data object Settings : Screen(SettingsDestination::class)
    data object GamesSearch : Screen(GamesSearchDestination::class)
    data object GamesCategory : Screen(GamesCategoryDestination::class) {

        fun createRoute(category: String): GamesCategoryDestination {
            return GamesCategoryDestination(category = category)
        }
    }

    data object GameInfo : Screen(GameInfoDestination::class) {

        fun createRoute(gameId: Int): GameInfoDestination {
            return GameInfoDestination(gameId = gameId)
        }
    }

    data object ImageViewer : Screen(ImageViewerDestination::class) {

        fun createRoute(
            imageUrls: List<String>,
            title: String? = null,
            initialPosition: Int = 0,
        ): ImageViewerDestination {
            return ImageViewerDestination(
                imageUrls = imageUrls,
                title = title,
                initialPosition = initialPosition,
            )
        }
    }

    val route: String
        get() = routeClass.qualifiedName!!

    internal companion object {

        val Saver = Saver(
            save = { it.route },
            restore = ::forRoute,
        )

        fun forRoute(route: String): Screen {
            return when {
                route.contains(GamesDiscovery.route) -> GamesDiscovery
                route.contains(LikedGames.route) -> LikedGames
                route.contains(GamingNews.route) -> GamingNews
                route.contains(Settings.route) -> Settings
                route.contains(GamesSearch.route) -> GamesSearch
                route.contains(GamesCategory.route) -> GamesCategory
                route.contains(GameInfo.route) -> GameInfo
                route.contains(ImageViewer.route) -> ImageViewer
                else -> error("Cannot find screen for the route: $route.")
            }
        }
    }
}

@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<Screen> {
    val selectedScreen = rememberSaveable(stateSaver = Screen.Saver) {
        mutableStateOf(START_SCREEN)
    }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedScreen.value = Screen.forRoute(checkNotNull(destination.requireRoute()))
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedScreen
}
