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

    const val compileSdkVersion = 29
    const val targetSdkVersion = 29
    const val minSdkVersion = 21
    const val buildToolsVersion = "29.0.2"
    const val applicationId = "com.paulrybitskyi.gamedge"
    const val versionCode = 1
    const val versionName = "1.0.0"

    val javaCompatibilityVersion = JavaVersion.VERSION_1_8
    val kotlinCompatibilityVersion = JavaVersion.VERSION_1_8

}


object versions {

    const val kotlin = "1.4.21" // also in buildSrc build.gradle.kts file
    const val gradleVersionsPlugin = "0.36.0"
    const val protobufPlugin = "0.8.14"
    const val navigation = "2.3.2"
    const val daggerHilt = "2.31-alpha"

}


object deps {

    object plugins {

        private const val gradlePluginVersion = "4.1.1" // also in buildSrc build.gradle.kts file

        const val androidGradle = "com.android.tools.build:gradle:${gradlePluginVersion}"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
        const val navSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${versions.navigation}"
        const val daggerHiltGradle = "com.google.dagger:hilt-android-gradle-plugin:${versions.daggerHilt}"
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
        const val commonsUiWidgets = ":commons-ui-widgets"

    }

    object kotlin {

        private const val kotlinCoroutinesCoreVersion = "1.4.2"

        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinCoroutinesCoreVersion}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${versions.kotlin}"

    }

    object androidX {

        private const val appCompatVersion = "1.1.0"
        private const val constraintLayoutVersion = "2.0.2"
        private const val recyclerViewVersion = "1.1.0"
        private const val viewPager2Version = "1.0.0"
        private const val swipeRefreshLayoutVersion = "1.1.0"
        private const val lifecycleVersion = "2.2.0"
        private const val browserVersion = "1.3.0"
        private const val roomVersion = "2.2.5"
        private const val coreKtxVersion = "1.3.1"
        private const val fragmentKtxVersion = "1.2.5"
        private const val dataStoreVersion = "1.0.0-alpha01"
        private const val daggerHiltAssistedInjectionVersion = "1.0.0-alpha02"

        const val appCompat = "androidx.appcompat:appcompat:${appCompatVersion}"
        const val navFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${versions.navigation}"
        const val navUiKtx = "androidx.navigation:navigation-ui-ktx:${versions.navigation}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${constraintLayoutVersion}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${recyclerViewVersion}"
        const val viewPager2 = "androidx.viewpager2:viewpager2:${viewPager2Version}"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${swipeRefreshLayoutVersion}"
        const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:${lifecycleVersion}"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel:${lifecycleVersion}"
        const val browser = "androidx.browser:browser:${browserVersion}"
        const val room = "androidx.room:room-runtime:${roomVersion}"
        const val roomKtx = "androidx.room:room-ktx:${roomVersion}"
        const val roomCompiler = "androidx.room:room-compiler:${roomVersion}"
        const val coreKtx = "androidx.core:core-ktx:${coreKtxVersion}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${fragmentKtxVersion}"
        const val prefsDataStore = "androidx.datastore:datastore-preferences:${dataStoreVersion}"
        const val protoDataStore = "androidx.datastore:datastore-core:${dataStoreVersion}"
        const val daggerHiltAssistedInjection = "androidx.hilt:hilt-lifecycle-viewmodel:${daggerHiltAssistedInjectionVersion}"
        const val daggerHiltAssistedInjectionCompiler = "androidx.hilt:hilt-compiler:${daggerHiltAssistedInjectionVersion}"

    }

    object google {

        private const val materialComponentsVersion = "1.3.0-alpha02"
        private const val protobufVersion = "3.10.0"

        const val daggerHilt = "com.google.dagger:hilt-android:${versions.daggerHilt}"
        const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:${versions.daggerHilt}"
        const val materialComponents = "com.google.android.material:material:${materialComponentsVersion}"
        const val protobuf = "com.google.protobuf:protobuf-javalite:${protobufVersion}"
        const val protobufCompiler = "com.google.protobuf:protoc:${protobufVersion}"

    }

    object square {

        private const val okHttpVersion = "4.8.0"
        private const val retrofitVersion = "2.9.0"
        private const val moshiVersion = "1.11.0"
        private const val picassoVersion = "2.71828"

        const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${okHttpVersion}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${retrofitVersion}"
        const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${retrofitVersion}"
        const val retrofitScalarsConverter = "com.squareup.retrofit2:converter-scalars:${retrofitVersion}"
        const val moshi = "com.squareup.moshi:moshi:${moshiVersion}"
        const val moshiCodeGenerator = "com.squareup.moshi:moshi-kotlin-codegen:${moshiVersion}"
        const val moshiReflection = "com.squareup.moshi:moshi-kotlin:${moshiVersion}"
        const val picasso = "com.squareup.picasso:picasso:${picassoVersion}"

    }

    object commons {

        private const val commonsCoreVersion = "1.0.1"
        private const val commonsKtxVersion = "1.0.1"
        private const val commonsWidgetsVersion = "1.0.1"
        private const val commonsNavigationVersion = "1.0.1"
        private const val commonsMaterialVersion = "1.0.1"
        private const val commonsNetworkVersion = "1.0.0"
        private const val commonsRecyclerViewVersion = "1.0.0"
        private const val commonsWindowAnimsVersion = "1.0.0"

        const val commonsCore = "com.paulrybitskyi.commons:commons-core:${commonsCoreVersion}"
        const val commonsKtx = "com.paulrybitskyi.commons:commons-ktx:${commonsKtxVersion}"
        const val commonsWidgets = "com.paulrybitskyi.commons:commons-widgets:${commonsWidgetsVersion}"
        const val commonsNavigation = "com.paulrybitskyi.commons:commons-navigation:${commonsNavigationVersion}"
        const val commonsMaterial = "com.paulrybitskyi.commons:commons-material:${commonsMaterialVersion}"
        const val commonsNetwork = "com.paulrybitskyi.commons:commons-network:${commonsNetworkVersion}"
        const val commonsRecyclerView = "com.paulrybitskyi.commons:commons-recyclerview:${commonsRecyclerViewVersion}"
        const val commonsWindowAnims = "com.paulrybitskyi.commons:commons-window-anims:${commonsWindowAnimsVersion}"

    }

    object misc {

        private const val desugaredLibsVersion = "1.0.10"
        private const val kotlinResultVersion = "1.1.9"
        private const val expandableTextViewVersion = "1.0.5"

        const val desugaredLibs = "com.android.tools:desugar_jdk_libs:${desugaredLibsVersion}"
        const val kotlinResult = "com.michael-bull.kotlin-result:kotlin-result:${kotlinResultVersion}"
        const val expandableTextView = "at.blogc:expandabletextview:${expandableTextViewVersion}"

    }

    object testing {

        private const val jUnitVersion = "4.13.1"
        private const val jUnitExtVersion = "1.1.1"

        const val jUnit = "junit:junit:${jUnitVersion}"
        const val jUnitExt = "androidx.test.ext:junit:${jUnitExtVersion}"

    }

}