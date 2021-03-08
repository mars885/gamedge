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
import com.paulrybitskyi.gamedge.core.utils.SdkInfo
import com.paulrybitskyi.gamedge.core.utils.attachNewTaskFlagIfNeeded
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@BindType(withQualifier = true)
@UrlOpenerKey(UrlOpenerKey.Type.NATIVE_APP)
internal class NativeAppUrlOpener @Inject constructor() : UrlOpener {


    override fun openUrl(url: String, context: Context): Boolean {
        return if(SdkInfo.IS_AT_LEAST_11) {
            openUrlInNewWay(url, context)
        } else {
            openUrlInLegacyWay(url, context)
        }
    }


    private fun openUrlInNewWay(url: String, context: Context): Boolean {
        val intent = createIntent(url, context).apply {
            addFlags(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER)
        }

        return try {
            context.startActivity(intent)
            true
        } catch(error: Throwable) {
            false
        }
    }


    private fun openUrlInLegacyWay(url: String, context: Context): Boolean {
        if(!context.packageManager.canUrlBeOpenedByNativeApp(url)) return false

        val nativeAppPackage = context.packageManager.getNativeAppPackageForUrl(url)
        val intent = createIntent(url, context).apply {
            `package` = nativeAppPackage
        }

        context.startActivity(intent)

        return true
    }


    private fun createIntent(url: String, context: Context): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            attachNewTaskFlagIfNeeded(context)
        }
    }


}