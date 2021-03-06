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

package com.paulrybitskyi.gamedge.feature.dashboard.fragment

import com.paulrybitskyi.gamedge.feature.dashboard.R

internal enum class DashboardPage(
    val position: Int,
    val menuItemId: Int
) {


    NEWS(
        position = 0,
        menuItemId = R.id.dashboard_bottom_navigation_item_news
    ),
    DISCOVER(
        position = 1,
        menuItemId = R.id.dashboard_bottom_navigation_item_discover
    ),
    LIKES(
        position = 2,
        menuItemId = R.id.dashboard_bottom_navigation_item_likes
    );


    internal companion object {

        fun Int.toDashboardPageFromPosition(): DashboardPage {
            return values().find { it.position == this }
                ?: throw IllegalArgumentException("Could not find the dashboard page for the specified position: $this.")
        }

        fun Int.toDashboardPageFromMenuItemId(): DashboardPage {
            return values().find { it.menuItemId == this }
                ?: throw IllegalArgumentException("Could not find the dashboard page for the specified menu item ID: $this.")
        }

    }


}