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

package com.paulrybitskyi.gamedge.feature.splash

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.paulrybitskyi.gamedge.commons.ui.base.BaseComposeActivity
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SplashActivity : BaseComposeActivity<SplashViewModel, SplashNavigator>() {

    override val viewModel by viewModels<SplashViewModel>()

    override fun getContent() = @Composable {
        SplashScreen()
    }

    override fun onLoadData() {
        super.onLoadData()

        viewModel.init()
    }

    override fun onRoute(route: Route) {
        super.onRoute(route)

        when (route) {
            is SplashRoute.Dashboard -> navigator.goToDashboard(this)
            is SplashRoute.Exit -> navigator.exitApp()
        }
    }
}
