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

package com.paulrybitskyi.gamedge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.coroutineScope
import com.paulrybitskyi.commons.ktx.intentFor
import com.paulrybitskyi.gamedge.commons.ui.LocalNetworkStateProvider
import com.paulrybitskyi.gamedge.commons.ui.LocalTextSharer
import com.paulrybitskyi.gamedge.commons.ui.LocalUrlOpener
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.core.sharers.TextSharer
import com.paulrybitskyi.gamedge.core.urlopener.UrlOpener
import com.paulrybitskyi.gamedge.domain.commons.extensions.execute
import com.paulrybitskyi.gamedge.domain.settings.entities.Settings
import com.paulrybitskyi.gamedge.domain.settings.entities.Theme
import com.paulrybitskyi.gamedge.domain.settings.usecases.ObserveThemeUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {

        fun newIntent(context: Context): Intent {
            return context.intentFor<MainActivity>()
        }
    }

    @Inject lateinit var urlOpener: UrlOpener
    @Inject lateinit var textSharer: TextSharer
    @Inject lateinit var networkStateProvider: NetworkStateProvider

    @Inject lateinit var observeThemeUseCase: ObserveThemeUseCase

    private var appTheme: Theme? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        loadAppTheme()
        setupSystemBars()
        setupCompose()
    }

    private fun setupSplashScreen() {
        // Waiting until the app's theme is loaded first before
        // displaying the dashboard to prevent the user from seeing
        // the app blinking as a result of the theme change
        installSplashScreen().setKeepOnScreenCondition {
            appTheme == null
        }
    }

    private fun loadAppTheme() {
        lifecycle.coroutineScope.launch {
            appTheme = observeThemeUseCase.execute().first()
        }
    }

    private fun setupSystemBars() {
        // To be able to draw behind system bars & change their colors
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun setupCompose() {
        setContent {
            CompositionLocalProvider(LocalUrlOpener provides urlOpener) {
                CompositionLocalProvider(LocalTextSharer provides textSharer) {
                    CompositionLocalProvider(LocalNetworkStateProvider provides networkStateProvider) {
                        GamedgeTheme(useDarkTheme = shouldUseDarkTheme()) {
                            MainScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun shouldUseDarkTheme(): Boolean {
        val themeState = observeThemeUseCase.execute().collectAsState(
            initial = Settings.DEFAULT.theme,
        )

        return when (themeState.value) {
            Theme.LIGHT -> false
            Theme.DARK -> true
            Theme.SYSTEM -> isSystemInDarkTheme()
        }
    }
}
