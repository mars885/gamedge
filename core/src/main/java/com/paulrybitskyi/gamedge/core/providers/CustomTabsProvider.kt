/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.core.providers

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import com.paulrybitskyi.hiltbinder.BindType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface CustomTabsProvider {
    fun getCustomTabsPackageName(): String?
    fun areCustomTabsSupported(): Boolean
}

@Singleton
@BindType
internal class CustomTabsProviderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : CustomTabsProvider {

    private companion object {
        private const val STABLE_PACKAGE = "com.android.chrome"
        private const val BETA_PACKAGE = "com.chrome.beta"
        private const val DEV_PACKAGE = "com.chrome.dev"
        private const val LOCAL_PACKAGE = "com.google.android.apps.chrome"
    }

    private var customTabsPackageName: String? = null

    override fun getCustomTabsPackageName(): String? {
        if (customTabsPackageName != null) return checkNotNull(customTabsPackageName)

        val packageManager = context.packageManager
        val viewIntentResolveInfoList = getViewIntentResolveInfoList()
        val packagesSupportingCustomTabs = buildList {
            for (resolveInfo in viewIntentResolveInfoList) {
                val serviceIntent = composeServiceIntent(resolveInfo)

                if (packageManager.resolveService(serviceIntent, 0) != null) {
                    add(resolveInfo.activityInfo.packageName)
                }
            }
        }

        return findPackageNameToUse(packagesSupportingCustomTabs)
            .also { customTabsPackageName = it }
    }

    private fun getViewIntentResolveInfoList(): List<ResolveInfo> {
        val packageManager = context.packageManager
        val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))

        return packageManager.queryIntentActivities(viewIntent, 0)
    }

    private fun composeServiceIntent(resolveInfo: ResolveInfo): Intent {
        return Intent().apply {
            `package` = resolveInfo.activityInfo.packageName
        }
    }

    private fun findPackageNameToUse(packagesSupportingCustomTabs: List<String>): String? {
        return when {
            (packagesSupportingCustomTabs.size == 1) -> packagesSupportingCustomTabs[0]
            packagesSupportingCustomTabs.contains(STABLE_PACKAGE) -> STABLE_PACKAGE
            packagesSupportingCustomTabs.contains(BETA_PACKAGE) -> BETA_PACKAGE
            packagesSupportingCustomTabs.contains(DEV_PACKAGE) -> DEV_PACKAGE
            packagesSupportingCustomTabs.contains(LOCAL_PACKAGE) -> LOCAL_PACKAGE

            else -> null
        }
    }

    override fun areCustomTabsSupported(): Boolean {
        return (getCustomTabsPackageName() != null)
    }
}
