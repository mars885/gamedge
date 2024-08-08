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

package com.paulrybitskyi.gamedge.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import PLUGIN_ANDROID_APPLICATION
import PLUGIN_KOTLIN_ANDROID
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import java.util.Properties

class GamedgeAndroidPlugin : Plugin<Project> {

    private companion object {
        const val BUILD_TYPE_DEBUG = "debug"
        const val BUILD_TYPE_RELEASE = "release"
        const val SIGNING_CONFIG_RELEASE = "release"
        const val KEYSTORE_FILE_NAME = "keystore.properties"
    }

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        configurePlugins()
    }

    private fun Project.setupPlugins() {
        plugins.apply(PLUGIN_KOTLIN_ANDROID)
    }

    private fun Project.configurePlugins() {
        configureAndroidCommonInfo()
        configureAndroidApplicationId()
    }

    private fun Project.configureAndroidCommonInfo() {
        extensions.findByType<BaseExtension>()?.run {
            compileSdkVersion(appConfig.compileSdkVersion)

            defaultConfig {
                minSdk = appConfig.minSdkVersion
                targetSdk = appConfig.targetSdkVersion
                versionCode = appConfig.versionCode
                versionName = appConfig.versionName

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                getByName(BUILD_TYPE_DEBUG) {
                    sourceSets {
                        getByName(BUILD_TYPE_DEBUG) {
                            java.srcDir(file("build/generated/ksp/$BUILD_TYPE_DEBUG/java"))
                            java.srcDir(file("build/generated/ksp/$BUILD_TYPE_DEBUG/kotlin"))
                        }
                    }

                    // Enabling accessing sites with http schemas for testing (especially
                    // instrumented tests using MockWebServer) and disabling it in the
                    // production to avoid security issues
                    manifestPlaceholders["usesCleartextTraffic"] = true
                }

                getByName(BUILD_TYPE_RELEASE) {
                    sourceSets {
                        getByName(BUILD_TYPE_RELEASE) {
                            java.srcDir(file("build/generated/ksp/$BUILD_TYPE_RELEASE/java"))
                            java.srcDir(file("build/generated/ksp/$BUILD_TYPE_RELEASE/kotlin"))
                        }
                    }

                    debuggable(true)
                    manifestPlaceholders["usesCleartextTraffic"] = false

                    isMinifyEnabled = true
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
                namespace = appConfig.applicationId

                defaultConfig {
                    applicationId = appConfig.applicationId

                }

                signingConfigs {
                    create(SIGNING_CONFIG_RELEASE) {
                        if (rootProject.file(KEYSTORE_FILE_NAME).canRead()) {
                            val properties = readProperties(KEYSTORE_FILE_NAME)

                            storeFile = file(properties.getValue("storeFile"))
                            storePassword = properties.getValue("storePassword")
                            keyAlias = properties.getValue("keyAlias")
                            keyPassword = properties.getValue("keyPassword")
                        } else {
                            println("""
                                Cannot create a release signing config. The file,
                                $KEYSTORE_FILE_NAME, either does not exist or
                                cannot be read from.
                            """.trimIndent())
                        }
                    }
                }

                buildTypes {
                    getByName(BUILD_TYPE_RELEASE) {
                        signingConfig = signingConfigs.getByName(SIGNING_CONFIG_RELEASE)
                    }
                }
            }
        }
    }

    private fun Project.readProperties(fileName: String): Properties {
        return Properties().apply {
            load(rootProject.file(fileName).inputStream())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> Properties.getValue(key: String): T {
        return (get(key) as T)
    }
}
