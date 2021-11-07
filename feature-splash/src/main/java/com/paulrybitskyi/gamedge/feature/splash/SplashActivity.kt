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

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.paulrybitskyi.gamedge.commons.ui.base.BaseComposeActivity
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SplashActivity : BaseComposeActivity<SplashViewModel, SplashNavigator>() {

    override val viewModel by viewModels<SplashViewModel>()

    override fun onInitUi() {
        setContent {
            SplashScreen()
        }
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


@Preview
@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = BiasAlignment(
            horizontalBias = 0f,
            verticalBias = 0.5f
        )
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(dimensionResource(R.dimen.splash_progress_bar_size)),
            color = colorResource(R.color.colorAccent),
            strokeWidth = dimensionResource(R.dimen.progress_bar_stroke_width)
        )
    }
}
