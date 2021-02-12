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

package com.paulrybitskyi.gamedge.feature.dashboard.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.paulrybitskyi.gamedge.feature.dashboard.DashboardPage
import com.paulrybitskyi.gamedge.feature.dashboard.DashboardPage.Companion.toDashboardPageFromPosition

internal class DashboardViewPagerAdapter(
    fragment: Fragment,
    private val fragmentFactory: DashboardAdapterFragmentFactory
) : FragmentStateAdapter(fragment) {


    override fun createFragment(position: Int): Fragment {
        return when(position.toDashboardPageFromPosition()) {
            DashboardPage.NEWS -> fragmentFactory.createNewsFragment()
            DashboardPage.DISCOVER -> fragmentFactory.createDiscoveryFragment()
            DashboardPage.LIKES -> fragmentFactory.createLikesFragment()
        }
    }


    override fun getItemCount(): Int {
        return DashboardPage.values().size
    }


}