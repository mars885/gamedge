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

package com.paulrybitskyi.gamedge.feature.dashboard.fragment.adapter

import androidx.fragment.app.Fragment
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface DashboardViewPagerAdapterFactory {
    fun createAdapter(fragment: Fragment): DashboardViewPagerAdapter
}

@BindType(installIn = BindType.Component.FRAGMENT)
internal class DashboardViewPagerAdapterFactoryImpl @Inject constructor(
    private val fragmentFactory: DashboardAdapterFragmentFactory
) : DashboardViewPagerAdapterFactory {

    override fun createAdapter(fragment: Fragment): DashboardViewPagerAdapter {
        return DashboardViewPagerAdapter(
            fragment = fragment,
            fragmentFactory = fragmentFactory
        )
    }
}
