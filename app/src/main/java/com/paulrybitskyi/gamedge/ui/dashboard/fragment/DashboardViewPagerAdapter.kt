/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.ui.dashboard.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.paulrybitskyi.gamedge.ui.dashboard.fragment.DashboardPage.Companion.toDashboardPageFromPosition
import com.paulrybitskyi.gamedge.ui.discovery.GamesDiscoveryFragment
import com.paulrybitskyi.gamedge.ui.favorites.FavoriteGamesFragment

internal class DashboardViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


    override fun createFragment(position: Int): Fragment {
        return when(position.toDashboardPageFromPosition()) {
            DashboardPage.DISCOVER -> GamesDiscoveryFragment()
            DashboardPage.FAVORITES -> FavoriteGamesFragment()
        }
    }


    override fun getItemCount(): Int {
        return DashboardPage.values().size
    }


}