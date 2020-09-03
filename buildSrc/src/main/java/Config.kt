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

    const val kotlin = "1.4.0" // also in buildSrc build.gradle.kts file
    const val gradlePlugin = "4.0.1" // also in buildSrc build.gradle.kts file
    const val gradleVersionsPlugin = "0.29.0"
    const val appCompat = "1.1.0"
    const val navFragmentKtx = "2.3.0"
    const val navUiKtx = "2.3.0"
    const val constraintLayout = "1.1.3"
    const val recyclerView = "1.1.0"
    const val viewPager2 = "1.0.0"
    const val lifecycleCommonJava8 = "2.2.0"
    const val daggerHilt = "2.28.3-alpha"
    const val kotlinResult = "1.1.8"
    const val okHttp = "4.8.0"
    const val retrofit = "2.9.0"
    const val moshi = "1.9.3"
    const val room = "2.2.5"
    const val coreKtx = "1.3.1"
    const val commonsCore = "1.0.0"
    const val commonsKtx = "1.0.0"
    const val commonsWidgets = "1.0.0"
    const val jUnit = "4.13"
    const val jUnitExt = "1.1.1"

}


object deps {

    object plugins {

        const val androidGradle = "com.android.tools.build:gradle:${versions.gradlePlugin}"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
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
        const val commonsData = ":commons-data"

    }

    object kotlin {

        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions.kotlin}"

    }

    object androidX {

        const val appCompat = "androidx.appcompat:appcompat:${versions.appCompat}"
        const val navFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${versions.navFragmentKtx}"
        const val navUiKtx = "androidx.navigation:navigation-ui-ktx:${versions.navUiKtx}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${versions.constraintLayout}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${versions.recyclerView}"
        const val viewPager2 = "androidx.viewpager2:viewpager2:${versions.viewPager2}"
        const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:${versions.lifecycleCommonJava8}"
        const val room = "androidx.room:room-runtime:${versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${versions.room}"
        const val coreKtx = "androidx.core:core-ktx:${versions.coreKtx}"

    }

    object google {

        const val daggerHilt = "com.google.dagger:hilt-android:${versions.daggerHilt}"
        const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:${versions.daggerHilt}"

    }

    object square {

        const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${versions.okHttp}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${versions.retrofit}"
        const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${versions.retrofit}"
        const val retrofitScalarsConverter = "com.squareup.retrofit2:converter-scalars:${versions.retrofit}"
        const val moshi = "com.squareup.moshi:moshi:${versions.moshi}"
        const val moshiCodeGenerator = "com.squareup.moshi:moshi-kotlin-codegen:${versions.moshi}"
        const val moshiReflection = "com.squareup.moshi:moshi-kotlin:${versions.moshi}"

    }

    object commons {

        const val commonsCore = "com.paulrybitskyi.commons:commons-core:${versions.commonsCore}"
        const val commonsKtx = "com.paulrybitskyi.commons:commons-ktx:${versions.commonsKtx}"
        const val commonsWidgets = "com.paulrybitskyi.commons:commons-widgets:${versions.commonsWidgets}"

    }

    object misc {

        const val kotlinResult = "com.michael-bull.kotlin-result:kotlin-result:${versions.kotlinResult}"

    }

    object testing {

        const val jUnit = "junit:junit:${versions.jUnit}"
        const val jUnitExt = "androidx.test.ext:junit:${versions.jUnitExt}"

    }

}