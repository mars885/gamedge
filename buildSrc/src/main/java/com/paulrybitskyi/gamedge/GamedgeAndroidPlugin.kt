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

package com.paulrybitskyi.gamedge

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import PLUGIN_ANDROID_APPLICATION
import PLUGIN_KOTLIN_ANDROID
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.kotlin.dsl.findByType

class GamedgeAndroidPlugin : Plugin<Project> {


    override fun apply(project: Project) = with(project) {
        configurePlugins()
        configureAndroid()
    }


    private fun Project.configurePlugins() {
        plugins.apply(PLUGIN_KOTLIN_ANDROID)
    }


    private fun Project.configureAndroid() {
        configureAndroidCommonInfo()
        configureAndroidApplicationId()
    }


    private fun Project.configureAndroidCommonInfo() {
        extensions.findByType<BaseExtension>()?.run {
            compileSdkVersion(appConfig.compileSdkVersion)
            buildToolsVersion(appConfig.buildToolsVersion)

            defaultConfig {
                minSdk = appConfig.minSdkVersion
                targetSdk = appConfig.targetSdkVersion
                versionCode = appConfig.versionCode
                versionName = appConfig.versionName

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                // Enabling accessing sites with http schemas for testing (especially
                // instrumented tests using MockWebServer) and disabling it in the
                // production to avoid security issues
                getByName("debug") {
                    manifestPlaceholders["usesCleartextTraffic"] = true
                }

                getByName("release") {
                    manifestPlaceholders["usesCleartextTraffic"] = false

                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
            }

            compileOptions {
                sourceCompatibility = appConfig.javaCompatibilityVersion
                targetCompatibility = appConfig.javaCompatibilityVersion
            }

            // Without the below block, a build failure was happening when running ./gradlew connectedAndroidTest
            // See: https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-debug#debug-agent-and-android
            packagingOptions {
                // for JNA and JNA-platform
                resources.excludes.add("META-INF/AL2.0")
                resources.excludes.add("META-INF/LGPL2.1")
                // for byte-buddy
                resources.excludes.add("META-INF/licenses/ASM")
                resources.pickFirsts.add("win32-x86-64/attach_hotspot_windows.dll")
                resources.pickFirsts.add("win32-x86/attach_hotspot_windows.dll")
            }
        }
    }


    private fun Project.configureAndroidApplicationId() {
        plugins.withId(PLUGIN_ANDROID_APPLICATION) {
            extensions.findByType<BaseAppModuleExtension>()?.run {
                defaultConfig {
                    applicationId = appConfig.applicationId
                }
            }
        }
    }


}