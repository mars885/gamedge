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

package com.paulrybitskyi.gamedge.core.urlopener

import android.content.Context
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import com.paulrybitskyi.commons.ktx.configuration
import com.paulrybitskyi.commons.ktx.getCompatColor
import com.paulrybitskyi.commons.window.anims.WindowAnimations
import com.paulrybitskyi.gamedge.core.R
import com.paulrybitskyi.gamedge.core.providers.CustomTabsProvider
import com.paulrybitskyi.gamedge.core.utils.attachNewTaskFlagIfNeeded
import com.paulrybitskyi.gamedge.core.utils.isDarkThemeEnabled
import com.paulrybitskyi.gamedge.core.utils.setAnimations
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@BindType(withQualifier = true)
@UrlOpenerKey(UrlOpenerKey.Type.CUSTOM_TAB)
internal class CustomTabUrlOpener @Inject constructor(
    private val customTabsProvider: CustomTabsProvider,
) : UrlOpener {

    override fun openUrl(url: String, context: Context): Boolean {
        // If the context is not activity based, then exit animations
        // won't work.

        return if (customTabsProvider.areCustomTabsSupported()) {
            createCustomTabsIntent(context).launchUrl(context, Uri.parse(url))
            true
        } else {
            false
        }
    }

    private fun createCustomTabsIntent(context: Context): CustomTabsIntent {
        return CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setDefaultColorSchemeParams(createColorSchemeParams(context))
            .setAnimations(context, WindowAnimations.HORIZONTAL_SLIDING_ANIMATIONS)
            .build()
            .apply {
                intent.`package` = customTabsProvider.getCustomTabsPackageName()
                intent.attachNewTaskFlagIfNeeded(context)
            }
    }

    private fun createColorSchemeParams(context: Context): CustomTabColorSchemeParams {
        val browserColors = BrowserColors.create(context)

        return CustomTabColorSchemeParams.Builder()
            .setToolbarColor(context.getCompatColor(browserColors.toolbar))
            .setSecondaryToolbarColor(context.getCompatColor(browserColors.secondaryToolbar))
            .setNavigationBarColor(context.getCompatColor(browserColors.navigationBar))
            .build()
    }

    private data class BrowserColors(
        @ColorRes val toolbar: Int,
        @ColorRes val secondaryToolbar: Int,
        @ColorRes val navigationBar: Int,
    ) {

        companion object {

            fun create(context: Context): BrowserColors {
                return if (context.configuration.isDarkThemeEnabled) {
                    BrowserColors(
                        toolbar = R.color.dark_colorPrimary,
                        secondaryToolbar = R.color.dark_colorSurface,
                        navigationBar = R.color.dark_colorNavigationBar,
                    )
                } else {
                    BrowserColors(
                        toolbar = R.color.light_colorPrimary,
                        secondaryToolbar = R.color.light_colorSurface,
                        navigationBar = R.color.light_colorNavigationBar,
                    )
                }
            }
        }
    }
}
