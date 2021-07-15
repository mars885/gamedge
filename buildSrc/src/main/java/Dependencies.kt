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

    const val compileSdkVersion = 30
    const val targetSdkVersion = 30
    const val minSdkVersion = 21
    const val buildToolsVersion = "30.0.2"
    const val applicationId = "com.paulrybitskyi.gamedge"
    const val versionCode = 1
    const val versionName = "1.0.0"

    val javaCompatibilityVersion = JavaVersion.VERSION_1_8
    val kotlinCompatibilityVersion = JavaVersion.VERSION_1_8

}


object versions {

    const val kotlin = "1.5.10" // also in buildSrc build.gradle.kts file
    const val compose = "1.0.0-rc01"
    const val gradleVersionsPlugin = "0.39.0"
    const val kspPlugin = "1.5.10-1.0.0-beta02"
    const val navigation = "2.3.5"
    const val daggerHilt = "2.37"
    const val coroutines = "1.5.1"
    const val room = "2.3.0"

}


object deps {

    object plugins {

        private const val gradlePluginVersion = "7.0.0-beta05" // also in buildSrc build.gradle.kts file
        private const val protobufPluginVersion = "0.8.16"

        const val androidGradle = "com.android.tools.build:gradle:${gradlePluginVersion}"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
        const val navSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${versions.navigation}"
        const val daggerHiltGradle = "com.google.dagger:hilt-android-gradle-plugin:${versions.daggerHilt}"
        const val protobuf = "com.google.protobuf:protobuf-gradle-plugin:$protobufPluginVersion"
        const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${versions.gradleVersionsPlugin}"

    }

    object local {

        const val domain = ":domain"
        const val data = ":data"
        const val database = ":database"
        const val igdbApi = ":igdb-api"
        const val igdbApicalypse = ":igdb-apicalypse"
        const val imageLoading = ":image-loading"
        const val core = ":core"
        const val commonsData = ":commons-data"
        const val commonsUi = ":commons-ui"
        const val commonsUiWidgets = ":commons-ui-widgets"
        const val commonsTesting = ":commons-testing"
        const val commonsApi = ":commons-api"
        const val gamespotApi = ":gamespot-api"

        const val featureCategory = ":feature-category"
        const val featureDashboard = ":feature-dashboard"
        const val featureDiscovery = ":feature-discovery"
        const val featureInfo = ":feature-info"
        const val featureImageViewer = ":feature-image-viewer"
        const val featureLikes = ":feature-likes"
        const val featureNews = ":feature-news"
        const val featureSearch = ":feature-search"
        const val featureSplash = ":feature-splash"

    }

    object kotlin {

        private const val serializationVersion = "1.2.2"

        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:${versions.kotlin}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions.coroutines}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion"

    }

    object androidX {

        private const val appCompatVersion = "1.3.0"
        private const val constraintLayoutVersion = "2.0.4"
        private const val recyclerViewVersion = "1.2.1"
        private const val viewPager2Version = "1.0.0"
        private const val swipeRefreshLayoutVersion = "1.1.0"
        private const val lifecycleVersion = "2.4.0-alpha02"
        private const val browserVersion = "1.3.0"
        private const val coreKtxVersion = "1.6.0"
        private const val fragmentKtxVersion = "1.3.5"
        private const val dataStoreVersion = "1.0.0-rc01"

        const val appCompat = "androidx.appcompat:appcompat:${appCompatVersion}"
        const val navFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${versions.navigation}"
        const val navUiKtx = "androidx.navigation:navigation-ui-ktx:${versions.navigation}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${constraintLayoutVersion}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${recyclerViewVersion}"
        const val viewPager2 = "androidx.viewpager2:viewpager2:${viewPager2Version}"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${swipeRefreshLayoutVersion}"
        const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:${lifecycleVersion}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${lifecycleVersion}"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel:${lifecycleVersion}"
        const val browser = "androidx.browser:browser:${browserVersion}"
        const val room = "androidx.room:room-runtime:${versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${versions.room}"
        const val coreKtx = "androidx.core:core-ktx:${coreKtxVersion}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${fragmentKtxVersion}"
        const val prefsDataStore = "androidx.datastore:datastore-preferences:${dataStoreVersion}"
        const val protoDataStore = "androidx.datastore:datastore-core:${dataStoreVersion}"

    }

    object compose {

        private const val coilCompose = "1.3.0"

        const val ui = "androidx.compose.ui:ui:${versions.compose}"
        const val tooling = "androidx.compose.ui:ui-tooling:1.0.0-beta09" // Should be changed to rc02 when it is released
        const val foundation = "androidx.compose.foundation:foundation:${versions.compose}"
        const val activity = "androidx.activity:activity-compose:${versions.compose}"
        const val material = "androidx.compose.material:material:${versions.compose}"
        const val iconsCore = "androidx.compose.material:material-icons-core:${versions.compose}"
        const val extendedIcons = "androidx.compose.material:material-icons-extended:${versions.compose}"
        const val runtime = "androidx.compose.runtime:runtime:${versions.compose}"

        const val coil = "io.coil-kt:coil-compose:$coilCompose"

        object accompanist {

            const val version = "0.14.0"

            const val insets = "com.google.accompanist:accompanist-insets:$version"

        }

    }

    object google {

        private const val materialComponentsVersion = "1.4.0"
        private const val protobufVersion = "3.15.8"

        const val daggerHilt = "com.google.dagger:hilt-android:${versions.daggerHilt}"
        const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:${versions.daggerHilt}"
        const val materialComponents = "com.google.android.material:material:${materialComponentsVersion}"
        const val protobuf = "com.google.protobuf:protobuf-javalite:${protobufVersion}"
        const val protobufCompiler = "com.google.protobuf:protoc:${protobufVersion}"

    }

    object square {

        private const val okHttpVersion = "4.9.1"
        private const val retrofitVersion = "2.9.0"
        private const val retrofitKotlinxSerializationConverterVersion = "0.8.0"
        private const val picassoVersion = "2.8"

        const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${okHttpVersion}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${retrofitVersion}"
        const val retrofitScalarsConverter = "com.squareup.retrofit2:converter-scalars:${retrofitVersion}"
        const val retrofitKotlinxSerializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:$retrofitKotlinxSerializationConverterVersion"
        const val picasso = "com.squareup.picasso:picasso:${picassoVersion}"

    }

    object commons {

        private const val coreVersion = "1.0.2"
        private const val ktxVersion = "1.0.2"
        private const val widgetsVersion = "1.0.1"
        private const val navigationVersion = "1.0.1"
        private const val materialVersion = "1.0.1"
        private const val networkVersion = "1.0.1"
        private const val recyclerViewVersion = "1.0.0"
        private const val windowAnimsVersion = "1.0.0"
        private const val deviceInfoVersion = "1.0.0"

        const val core = "com.paulrybitskyi.commons:commons-core:${coreVersion}"
        const val ktx = "com.paulrybitskyi.commons:commons-ktx:${ktxVersion}"
        const val widgets = "com.paulrybitskyi.commons:commons-widgets:${widgetsVersion}"
        const val navigation = "com.paulrybitskyi.commons:commons-navigation:${navigationVersion}"
        const val material = "com.paulrybitskyi.commons:commons-material:${materialVersion}"
        const val network = "com.paulrybitskyi.commons:commons-network:${networkVersion}"
        const val recyclerView = "com.paulrybitskyi.commons:commons-recyclerview:${recyclerViewVersion}"
        const val windowAnims = "com.paulrybitskyi.commons:commons-window-anims:${windowAnimsVersion}"
        const val deviceInfo = "com.paulrybitskyi.commons:commons-device-info:${deviceInfoVersion}"

    }

    object misc {

        private const val desugaredLibsVersion = "1.1.5"
        private const val kotlinResultVersion = "1.1.12"
        private const val expandableTextViewVersion = "1.0.5"
        private const val hiltBinderVersion = "1.1.0"
        private const val photoViewVersion = "2.3.0"

        const val desugaredLibs = "com.android.tools:desugar_jdk_libs:${desugaredLibsVersion}"
        const val kotlinResult = "com.michael-bull.kotlin-result:kotlin-result:${kotlinResultVersion}"
        const val expandableTextView = "at.blogc:expandabletextview:${expandableTextViewVersion}"
        const val hiltBinder = "com.paulrybitskyi:hilt-binder:$hiltBinderVersion"
        const val hiltBinderCompiler = "com.paulrybitskyi:hilt-binder-compiler:$hiltBinderVersion"
        const val photoView = "com.github.chrisbanes:PhotoView:$photoViewVersion"

    }

    object testing {

        private const val jUnitVersion = "4.13.2"
        private const val jUnitExtVersion = "1.1.3"
        private const val assertJVersion = "3.20.2"
        private const val mockkVersion = "1.12.0"
        private const val turbineVersion = "0.5.2"
        private const val testRunnerVersion = "1.4.0"
        private const val archCoreVersion = "2.1.0"
        private const val mockWebServerVersion = "4.9.1"

        const val jUnit = "junit:junit:$jUnitVersion"
        const val jUnitExt = "androidx.test.ext:junit:$jUnitExtVersion"
        const val assertJ = "org.assertj:assertj-core:$assertJVersion"
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