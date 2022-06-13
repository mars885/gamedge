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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import com.paulrybitskyi.gamedge.core.utils.toCsv
import java.net.URLEncoder

internal val START_SCREEN = Screen.Discover

internal sealed class Screen(val route: String) {
    object News : Screen("news")
    object Discover : Screen("discover")
    object Likes : Screen("likes")
    object GamesSearch : Screen("games-search")

    object GamesCategory : Screen("games-category/{${Parameters.CATEGORY}}") {
        object Parameters {
            const val CATEGORY = "category"
        }

        fun createLink(category: String): String {
            return "games-category/$category"
        }
    }

    object GameInfo : Screen("game-info/{${Parameters.GAME_ID}}") {
        object Parameters {
            const val GAME_ID = "gameId"
        }

        fun createLink(gameId: Int): String {
            return "game-info/$gameId"
        }
    }

    object ImageViewer : Screen(
        "image-viewer?" +
        "${Parameters.TITLE}={${Parameters.TITLE}}&" +
        "${Parameters.INITIAL_POSITION}={${Parameters.INITIAL_POSITION}}&" +
        "${Parameters.IMAGE_URLS}={${Parameters.IMAGE_URLS}}"
    ) {
        object Parameters {
            const val TITLE = "title"
            const val INITIAL_POSITION = "initial-position"
            const val IMAGE_URLS = "image-urls"
        }

        fun createLink(
            title: String?,
            initialPosition: Int,
            imageUrls: List<String>,
        ): String {
            val modifiedImageUrls = imageUrls
                .map { imageUrl -> URLEncoder.encode(imageUrl, "UTF-8") }
                .toCsv()

            return buildString {
                append("image-viewer?")

                if (title != null) {
                    append("${Parameters.TITLE}=$title&")
                }

                append("${Parameters.INITIAL_POSITION}=$initialPosition&")
                append("${Parameters.IMAGE_URLS}=$modifiedImageUrls")
            }
        }
    }

    internal companion object {

        val Saver = Saver(
            save = { it.route },
            restore = ::forRoute,
        )

        fun forRoute(route: String): Screen {
            return when (route) {
                News.route -> News
                Discover.route -> Discover
                Likes.route -> Likes
                GamesSearch.route -> GamesSearch
                GamesCategory.route -> GamesCategory
                GameInfo.route -> GameInfo
                ImageViewer.route -> ImageViewer
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
