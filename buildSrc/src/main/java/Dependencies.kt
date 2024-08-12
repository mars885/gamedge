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

@file:Suppress("ClassName")

import org.gradle.api.JavaVersion

object appConfig {

    const val compileSdkVersion = 34
    const val targetSdkVersion = 34
    const val minSdkVersion = 21
    const val applicationId = "com.paulrybitskyi.gamedge"
    const val versionCode = 1
    const val versionName = "1.0.0"

    const val jvmToolchainVersion = 17

    val javaCompatibilityVersion = JavaVersion.VERSION_17
}

object versions {

    const val kotlin = "2.0.0" // also in buildSrc build.gradle.kts file
    const val gradleVersionsPlugin = "0.51.0"
    const val kspPlugin = "2.0.0-1.0.24"
    const val daggerHilt = "2.52"
    const val detektPlugin = "1.23.6"
    const val ktlintPlugin = "12.1.1"
    const val ktlint = "1.3.1"
    const val coroutines = "1.8.1"
    const val room = "2.6.1"
}

object deps {

    object plugins {

        private const val androidPluginVersion = "8.3.1" // also in buildSrc build.gradle.kts file
        private const val protobufPluginVersion = "0.9.4" // also in buildSrc build.gradle.kts file

        const val androidGradle = "com.android.tools.build:gradle:${androidPluginVersion}"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
        const val protobuf = "com.google.protobuf:protobuf-gradle-plugin:$protobufPluginVersion"
        const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${versions.gradleVersionsPlugin}"
    }

    object local {

        const val core = ":core"
        const val database = ":database"
        const val igdbApi = ":igdb-api"
        const val igdbApicalypse = ":igdb-apicalypse"
        const val gamespotApi = ":gamespot-api"
        const val commonDomain = ":common-domain"
        const val commonData = ":common-data"
        const val commonUi = ":common-ui"
        const val commonUiWidgets = ":common-ui-widgets"
        const val commonApi = ":common-api"
        const val commonTestingDomain = ":common-testing-domain"
        const val commonTesting = ":common-testing"

        const val featureCategory = ":feature-category"
        const val featureDiscovery = ":feature-discovery"
        const val featureInfo = ":feature-info"
        const val featureImageViewer = ":feature-image-viewer"
        const val featureLikes = ":feature-likes"
        const val featureNews = ":feature-news"
        const val featureSearch = ":feature-search"
        const val featureSettings = ":feature-settings"
    }

    object kotlin {

        private const val serializationVersion = "1.7.1"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions.coroutines}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion"
    }

    object androidX {

        private const val splashVersion = "1.0.1"
        const val viewModel = "2.5.1" // to be deleted when the linked issue at use site is fixed
        private const val browserVersion = "1.8.0"
        private const val dataStoreVersion = "1.1.1"

        const val splash = "androidx.core:core-splashscreen:$splashVersion"
        const val browser = "androidx.browser:browser:${browserVersion}"
        const val room = "androidx.room:room-runtime:${versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${versions.room}"
        const val prefsDataStore = "androidx.datastore:datastore-preferences:${dataStoreVersion}"
        const val protoDataStore = "androidx.datastore:datastore:${dataStoreVersion}"
    }

    object compose {

        private const val composeVersion = "1.6.8"
        private const val activityVersion = "1.9.1"
        private const val navigationVersion = "2.7.7"
        private const val constraintLayoutVersion = "1.1.0-alpha14"
        private const val hiltVersion = "1.0.0"

        const val ui = "androidx.compose.ui:ui:$composeVersion"
        const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
        const val animation = "androidx.compose.animation:animation-graphics:$composeVersion"
        const val foundation = "androidx.compose.foundation:foundation:$composeVersion"
        const val material = "androidx.compose.material:material:$composeVersion"
        const val runtime = "androidx.compose.runtime:runtime:$composeVersion"

        const val activity = "androidx.activity:activity-compose:$activityVersion"
        const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:$constraintLayoutVersion"
        const val hilt = "androidx.hilt:hilt-navigation-compose:$hiltVersion"

        object accompanist {

            const val version = "0.25.0"

            const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
            const val flowLayout = "com.google.accompanist:accompanist-flowlayout:$version"
            const val pager = "com.google.accompanist:accompanist-pager:$version"
            const val systemUi = "com.google.accompanist:accompanist-systemuicontroller:$version"
            const val navigationAnimations = "com.google.accompanist:accompanist-navigation-animation:$version"
        }
    }

    object google {

        private const val protobufVersion = "3.21.4"

        const val daggerHiltCore = "com.google.dagger:hilt-core:${versions.daggerHilt}"
        const val daggerHiltCoreCompiler = "com.google.dagger:hilt-compiler:${versions.daggerHilt}"
        const val daggerHiltAndroid = "com.google.dagger:hilt-android:${versions.daggerHilt}"
        const val daggerHiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${versions.daggerHilt}"
        const val protobuf = "com.google.protobuf:protobuf-javalite:${protobufVersion}"
        const val protobufCompiler = "com.google.protobuf:protoc:${protobufVersion}"
    }

    object square {

        private const val okHttpVersion = "4.10.0"
        private const val retrofitVersion = "2.9.0"
        private const val retrofitKotlinxSerializationConverterVersion = "0.8.0"

        const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${okHttpVersion}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${retrofitVersion}"
        const val retrofitScalarsConverter = "com.squareup.retrofit2:converter-scalars:${retrofitVersion}"
        const val retrofitKotlinxSerializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:$retrofitKotlinxSerializationConverterVersion"
    }

    object commons {

        private const val coreVersion = "1.0.4"
        private const val ktxVersion = "1.0.4"
        private const val networkVersion = "1.0.3"
        private const val windowAnimsVersion = "1.0.2"

        const val core = "com.paulrybitskyi.commons:commons-core:${coreVersion}"
        const val ktx = "com.paulrybitskyi.commons:commons-ktx:${ktxVersion}"
        const val network = "com.paulrybitskyi.commons:commons-network:${networkVersion}"
        const val windowAnims = "com.paulrybitskyi.commons:commons-window-anims:${windowAnimsVersion}"
    }

    object misc {

        private const val desugaredLibsVersion = "1.1.5"
        private const val kotlinResultVersion = "1.1.16"
        private const val hiltBinderVersion = "1.1.2"
        private const val coilVersion = "2.1.0"
        private const val zoomableVersion = "1.5.1"

        const val desugaredLibs = "com.android.tools:desugar_jdk_libs:${desugaredLibsVersion}"
        const val kotlinResult = "com.michael-bull.kotlin-result:kotlin-result:${kotlinResultVersion}"
        const val hiltBinder = "com.paulrybitskyi:hilt-binder:$hiltBinderVersion"
        const val hiltBinderCompiler = "com.paulrybitskyi:hilt-binder-compiler:$hiltBinderVersion"
        const val coil = "io.coil-kt:coil-compose:$coilVersion"
        const val zoomable = "com.mxalbert.zoomable:zoomable:$zoomableVersion"
    }

    object testing {

        private const val jUnitVersion = "4.13.2"
        private const val jUnitExtVersion = "1.1.3"
        private const val truthVersion = "1.1.3"
        private const val mockkVersion = "1.13.12"
        private const val turbineVersion = "0.9.0"
        private const val testRunnerVersion = "1.4.0"
        private const val archCoreVersion = "2.1.0"
        private const val mockWebServerVersion = "4.10.0"

        const val jUnit = "junit:junit:$jUnitVersion"
        const val jUnitExt = "androidx.test.ext:junit:$jUnitExtVersion"
        const val truth = "com.google.truth:truth:$truthVersion"
        const val mockk = "io.mockk:mockk:$mockkVersion"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.coroutines}"
        const val turbine = "app.cash.turbine:turbine:$turbineVersion"
        const val testRunner = "androidx.test:runner:$testRunnerVersion"
        const val archCore = "androidx.arch.core:core-testing:$archCoreVersion"
        const val daggerHilt = "com.google.dagger:hilt-android-testing:${versions.daggerHilt}"
        const val room = "androidx.room:room-testing:${versions.room}"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$mockWebServerVersion"
    }
}
