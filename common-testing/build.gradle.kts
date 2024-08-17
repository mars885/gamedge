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

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.gamedgeAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)
}

android {
    namespace = "com.paulrybitskyi.gamedge.common.testing"
}

dependencies {
    api(project(libs.versions.localCommonTestingDomain.get()))
    implementation(project(libs.versions.localCore.get()))
    implementation(project(libs.versions.localCommonApi.get()))

    // Unit tests
    implementation(libs.jUnit)
    implementation(libs.mockk)
    implementation(libs.coroutinesTesting)

    // Instrumentation tests
    implementation(libs.testRunner)
    implementation(libs.mockWebServer)

    implementation(libs.daggerHiltTesting)
    kapt(libs.daggerHiltAndroidCompiler)
}
