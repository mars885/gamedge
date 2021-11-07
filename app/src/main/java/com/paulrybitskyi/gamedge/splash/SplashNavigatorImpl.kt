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

package com.paulrybitskyi.gamedge.splash

import android.app.Activity
import android.content.Context
import com.paulrybitskyi.gamedge.feature.dashboard.activity.DashboardActivity
import com.paulrybitskyi.gamedge.feature.splash.SplashNavigator
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@BindType(installIn = BindType.Component.ACTIVITY)
internal class SplashNavigatorImpl @Inject constructor(
    private val activity: Activity
) : SplashNavigator {

    override fun goToDashboard(context: Context) {
        activity.startActivity(DashboardActivity.newIntent(context))
        activity.finish()
    }

    override fun exitApp() {
        activity.finish()
    }
}
