/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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
    id(libs.plugins.gamedgeDaggerHilt.get().pluginId)

    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "com.paulrybitskyi.gamedge.gamespot.api"

    defaultConfig {
        stringField("GAMESPOT_API_KEY", property("GAMESPOT_API_KEY", ""))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(project(localModules.commonApi))
    implementation(project(localModules.core))

    implementation(libs.coroutines)
    implementation(libs.kotlinxSerialization)

    implementation(libs.retrofit)
    implementation(libs.retrofitKotlinxSerializationConverter)

    implementation(libs.kotlinResult)

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
    kaptAndroidTest(libs.daggerHiltAndroidCompiler)
}
