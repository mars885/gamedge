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
import androidx.browser.customtabs.CustomTabsIntent
import com.paulrybitskyi.commons.ktx.getCompatColor
import com.paulrybitskyi.commons.window.anims.WindowAnimations
import com.paulrybitskyi.gamedge.core.R
import com.paulrybitskyi.gamedge.core.providers.CustomTabsProvider
import com.paulrybitskyi.gamedge.core.utils.attachNewTaskFlagIfNeeded
import com.paulrybitskyi.gamedge.core.utils.setAnimations

internal class CustomTabUrlOpener(
    private val customTabsProvider: CustomTabsProvider
) : UrlOpener {


    override fun openUrl(url: String, context: Context) {
        // If the context is not activity based, then exit animations
        // won't work.

        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setToolbarColor(context.getCompatColor(R.color.colorPrimary))
            .setSecondaryToolbarColor(context.getCompatColor(R.color.colorPrimaryDark))
            .setNavigationBarColor(context.getCompatColor(R.color.colorNavigationBar))
            .setAnimations(context, WindowAnimations.HORIZONTAL_SLIDING_ANIMATIONS)
            .build()
            .apply {
                intent.`package` = customTabsProvider.getCustomTabsPackageName()
                intent.attachNewTaskFlagIfNeeded(context)

                launchUrl(context, Uri.parse(url))
            }
    }


    override fun canOpenUrl(url: String, context: Context): Boolean {
        return customTabsProvider.areCustomTabsSupported()
    }


}