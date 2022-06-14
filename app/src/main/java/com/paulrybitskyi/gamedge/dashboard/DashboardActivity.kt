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

package com.paulrybitskyi.gamedge.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.paulrybitskyi.commons.ktx.intentFor
import com.paulrybitskyi.commons.window.anims.WindowAnimations
import com.paulrybitskyi.commons.window.anims.overrideEnterTransition
import com.paulrybitskyi.gamedge.commons.ui.LocalNetworkStateProvider
import com.paulrybitskyi.gamedge.commons.ui.LocalTextSharer
import com.paulrybitskyi.gamedge.commons.ui.LocalUrlOpener
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.core.sharers.TextSharer
import com.paulrybitskyi.gamedge.core.urlopener.UrlOpener
import com.paulrybitskyi.gamedge.dashboard.compose.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    companion object {

        fun newIntent(context: Context): Intent {
            return context.intentFor<DashboardActivity>()
        }
    }

    @Inject lateinit var urlOpener: UrlOpener
    @Inject lateinit var textSharer: TextSharer
    @Inject lateinit var networkStateProvider: NetworkStateProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overrideEnterTransition(WindowAnimations.FADING_ANIMATIONS)
        initSystemUiVisibility()

        setContent {
            CompositionLocalProvider(LocalUrlOpener provides urlOpener) {
                CompositionLocalProvider(LocalTextSharer provides textSharer) {
                    CompositionLocalProvider(LocalNetworkStateProvider provides networkStateProvider) {
                        GamedgeTheme {
                            MainScreen()
                        }
                    }
                }
            }
        }
    }

    private fun initSystemUiVisibility() {
        // To be able to draw behind system bars & change their colors
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
