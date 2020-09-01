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

package com.paulrybitskyi.gamedge

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

    const val kotlinVersion = "1.4.0"
    const val gradlePluginVersion = "4.0.1"
    const val gradleVersionsPluginVersion = "0.29.0"
    const val appCompatVersion = "1.1.0"
    const val navFragmentKtxVersion = "2.3.0"
    const val navUiKtxVersion = "2.3.0"
    const val constraintLayoutVersion = "1.1.3"
    const val recyclerViewVersion = "1.1.0"
    const val viewPager2Version = "1.0.0"
    const val lifecycleCommonJava8Version = "2.2.0"
    const val daggerHiltVersion = "2.28.3-alpha"
    const val kotlinResultVersion = "1.1.8"
    const val okHttpVersion = "4.8.0"
    const val retrofitVersion = "2.9.0"
    const val moshiVersion = "1.9.3"
    const val roomVersion = "2.2.5"
    const val coreKtxVersion = "1.3.1"
    const val commonsCoreVersion = "1.0.0"
    const val commonsKtxVersion = "1.0.0"
    const val commonsWidgets = "1.0.0"
    const val jUnitVersion = "4.13"
    const val jUnitExtVersion = "1.1.1"

}


object deps {

    object plugins {

        const val androidGradlePlugin = "com.android.tools.build:gradle:${versions.gradlePluginVersion}"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlinVersion}"
        const val daggerHiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${versions.daggerHiltVersion}"
        const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${versions.gradleVersionsPluginVersion}"

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

        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions.kotlinVersion}"

    }

    object androidX {

        const val appCompat = "androidx.appcompat:appcompat:${versions.appCompatVersion}"
        const val navFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${versions.navFragmentKtxVersion}"
        const val navUiKtx = "androidx.navigation:navigation-ui-ktx:${versions.navUiKtxVersion}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${versions.constraintLayoutVersion}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${versions.recyclerViewVersion}"
        const val viewPager2 = "androidx.viewpager2:viewpager2:${versions.viewPager2Version}"
        const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:${versions.lifecycleCommonJava8Version}"
        const val room = "androidx.room:room-runtime:${versions.roomVersion}"
        const val roomKtx = "androidx.room:room-ktx:${versions.roomVersion}"
        const val roomCompiler = "androidx.room:room-compiler:${versions.roomVersion}"
        const val coreKtx = "androidx.core:core-ktx:${versions.coreKtxVersion}"

    }

    object google {

        const val daggerHilt = "com.google.dagger:hilt-android:${versions.daggerHiltVersion}"
        const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:${versions.daggerHiltVersion}"

    }

    object square {

        const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${versions.okHttpVersion}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${versions.retrofitVersion}"
        const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${versions.retrofitVersion}"
        const val retrofitScalarsConverter = "com.squareup.retrofit2:converter-scalars:${versions.retrofitVersion}"
        const val moshi = "com.squareup.moshi:moshi:${versions.moshiVersion}"
        const val moshiCodeGenerator = "com.squareup.moshi:moshi-kotlin-codegen:${versions.moshiVersion}"
        const val moshiReflection = "com.squareup.moshi:moshi-kotlin:${versions.moshiVersion}"

    }

    object commons {

        const val commonsCore = "com.paulrybitskyi.commons:commons-core:${versions.commonsCoreVersion}"
        const val commonsKtx = "com.paulrybitskyi.commons:commons-ktx:${versions.commonsKtxVersion}"
        const val commonsWidgets = "com.paulrybitskyi.commons:commons-widgets:${versions.commonsWidgets}"

    }

    object misc {

        const val kotlinResult = "com.michael-bull.kotlin-result:kotlin-result:${versions.kotlinResultVersion}"

    }

    object testing {

        const val jUnit = "junit:junit:${versions.jUnitVersion}"
        const val jUnitExt = "androidx.test.ext:junit:${versions.jUnitExtVersion}"

    }

}