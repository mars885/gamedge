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
                minSdkVersion(appConfig.minSdkVersion)
                targetSdkVersion(appConfig.targetSdkVersion)
                versionCode = appConfig.versionCode
                versionName = appConfig.versionName

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
            }

            compileOptions {
                sourceCompatibility = appConfig.javaCompatibilityVersion
                targetCompatibility = appConfig.javaCompatibilityVersion
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