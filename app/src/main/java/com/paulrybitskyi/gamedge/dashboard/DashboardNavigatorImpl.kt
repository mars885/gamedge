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

package com.paulrybitskyi.gamedge.dashboard

import androidx.navigation.NavController
import com.paulrybitskyi.gamedge.feature.dashboard.fragment.DashboardFragmentDirections
import com.paulrybitskyi.gamedge.feature.dashboard.fragment.DashboardNavigator
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@BindType(installIn = BindType.Component.FRAGMENT)
internal class DashboardNavigatorImpl @Inject constructor(
    private val navController: NavController
) : DashboardNavigator {


    override fun goToSearch() {
        navController.navigate(DashboardFragmentDirections.actionSearchFragment())
    }


}