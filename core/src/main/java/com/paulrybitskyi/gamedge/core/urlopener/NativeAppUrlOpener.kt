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
import android.content.Intent
import android.net.Uri
import com.paulrybitskyi.commons.ktx.canUrlBeOpenedByNativeApp
import com.paulrybitskyi.commons.ktx.getNativeAppPackageForUrl
import com.paulrybitskyi.gamedge.core.utils.attachNewTaskFlagIfNeeded

internal class NativeAppUrlOpener : UrlOpener {


    override fun openUrl(url: String, context: Context) {
        val nativeAppPackage = context.packageManager.getNativeAppPackageForUrl(url)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            `package` = nativeAppPackage
            data = Uri.parse(url)
            attachNewTaskFlagIfNeeded(context)
        }

        context.startActivity(intent)
    }


    override fun canOpenUrl(url: String, context: Context): Boolean {
        return context.packageManager.canUrlBeOpenedByNativeApp(url)
    }


}