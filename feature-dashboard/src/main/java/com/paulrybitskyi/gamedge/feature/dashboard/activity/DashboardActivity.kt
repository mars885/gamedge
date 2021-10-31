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

package com.paulrybitskyi.gamedge.feature.dashboard.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import com.paulrybitskyi.commons.ktx.intentFor
import com.paulrybitskyi.commons.window.anims.WindowAnimations
import com.paulrybitskyi.commons.window.anims.overrideEnterTransition
import com.paulrybitskyi.gamedge.feature.dashboard.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(R.layout.activity_dashboard) {


    companion object {

        fun newIntent(context: Context): Intent {
            return context.intentFor<DashboardActivity>()
        }

    }


    @Inject lateinit var navGraphProvider: DashboardNavGraphProvider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overrideEnterTransition(WindowAnimations.FADING_ANIMATIONS)
        initSystemUiVisibility()
        initNavGraph()
    }


    private fun initSystemUiVisibility() {
        // To be able to draw behind system bars & change their colors
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }


    private fun initNavGraph() {
        val navHostFragment = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        val navController = navHostFragment.navController
        val graph = navController.navInflater.inflate(navGraphProvider.getNavGraphId())

        navController.graph = graph
    }


}
