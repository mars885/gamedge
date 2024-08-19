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

import com.paulrybitskyi.gamedge.extensions.property
import com.paulrybitskyi.gamedge.extensions.stringField

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.gamedgeAndroid.get().pluginId)

    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "com.paulrybitskyi.gamedge.igdb.api"

    defaultConfig {
        stringField("TWITCH_APP_CLIENT_ID", property("TWITCH_APP_CLIENT_ID", ""))
        stringField("TWITCH_APP_CLIENT_SECRET", property("TWITCH_APP_CLIENT_SECRET", ""))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(project(localModules.commonApi))
    implementation(project(localModules.igdbApicalypse))
    implementation(project(localModules.core))

    implementation(libs.coroutines)
    implementation(libs.kotlinxSerialization)

    implementation(libs.retrofit)
    implementation(libs.retrofitScalarsConverter)

    implementation(libs.kotlinResult)

    implementation(libs.daggerHiltAndroid)
    ksp(libs.daggerHiltAndroidCompiler)

    implementation(libs.hiltBinder)
    ksp(libs.hiltBinderCompiler)

    testImplementation(project(localModules.commonTesting))
    testImplementation(libs.jUnit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutinesTesting)

    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.jUnitExt)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.mockWebServer)

    androidTestImplementation(libs.daggerHiltTesting)
    kspAndroidTest(libs.daggerHiltAndroidCompiler)
}
