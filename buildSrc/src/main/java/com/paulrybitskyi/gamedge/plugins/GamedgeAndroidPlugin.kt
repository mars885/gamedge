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

package com.paulrybitskyi.gamedge.plugins

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.paulrybitskyi.gamedge.extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

class GamedgeAndroidPlugin : Plugin<Project> {

    private companion object {
        const val APPLICATION_ID = "com.paulrybitskyi.gamedge"
        const val BUILD_TYPE_DEBUG = "debug"
        const val BUILD_TYPE_RELEASE = "release"
        const val SIGNING_CONFIG_RELEASE = "release"
        const val KEYSTORE_FILE_NAME = "keystore.properties"
    }

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        configurePlugins()
        addDependencies()
    }

    private fun Project.setupPlugins() {
        plugins.apply(libs.plugins.kotlinAndroid.get().pluginId)
    }

    private fun Project.configurePlugins() {
        configureAndroidCommonInfo()
        configureAndroidApplication()
    }

    private fun Project.configureAndroidCommonInfo() = configure<BaseExtension> {
        compileSdkVersion(libs.versions.compileSdk.get().toInt())

        defaultConfig {
            minSdk = libs.versions.minSdk.get().toInt()
            targetSdk = libs.versions.targetSdk.get().toInt()
            versionCode = libs.versions.appVersionCode.get().toInt()
            versionName = libs.versions.appVersionName.get()

            testInstrumentationRunner = "com.paulrybitskyi.gamedge.common.testing.GamedgeTestRunner"
        }

        buildTypes {
            getByName(BUILD_TYPE_DEBUG) {
                // Enabling accessing sites with http schemas for testing (especially
                // instrumented tests using MockWebServer) and disabling it in the
                // production to avoid security issues
                manifestPlaceholders["usesCleartextTraffic"] = true
            }

            getByName(BUILD_TYPE_RELEASE) {
                debuggable(true)
                manifestPlaceholders["usesCleartextTraffic"] = false

                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }

        compileOptions {
            val javaVersion = JavaVersion.toVersion(libs.versions.jvmToolchain.get().toInt())

            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion

            isCoreLibraryDesugaringEnabled = true
        }

        // Without the below block, a build failure was happening when running ./gradlew connectedAndroidTest
        // See: https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-debug#debug-agent-and-android
        packagingOptions {
            // for JNA and JNA-platform
            resources.excludes.add("META-INF/AL2.0")
            resources.excludes.add("META-INF/LGPL2.1")
            resources.excludes.add("META-INF/LICENSE.md")
            resources.excludes.add("META-INF/LICENSE-notice.md")
            // for byte-buddy
            resources.excludes.add("META-INF/licenses/ASM")
            resources.pickFirsts.add("win32-x86-64/attach_hotspot_windows.dll")
            resources.pickFirsts.add("win32-x86/attach_hotspot_windows.dll")
        }
    }

    private fun Project.configureAndroidApplication() {
        plugins.withId(libs.plugins.androidApplication.get().pluginId) {
            configure<BaseAppModuleExtension> {
                namespace = APPLICATION_ID

                defaultConfig {
                    applicationId = APPLICATION_ID
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

    private fun Project.addDependencies(): Unit = with(dependencies) {
        add("implementation", libs.coroutines.get())
        add("implementation", libs.kotlinResult.get())

        add("coreLibraryDesugaring", libs.desugaredJdk.get())
        add("androidTestImplementation", project(localModules.commonTesting))
    }
}
