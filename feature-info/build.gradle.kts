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

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.gamedgeAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)

    alias(libs.plugins.jetpackCompose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "com.paulrybitskyi.gamedge.feature.info"
}

dependencies {
    implementation(project(deps.local.commonDomain))
    implementation(project(deps.local.commonData))
    implementation(project(deps.local.core))
    implementation(project(deps.local.commonUi))
    implementation(project(deps.local.commonUiWidgets))

    implementation(deps.kotlin.coroutines)

    implementation(deps.compose.ui)
    implementation(deps.compose.tooling)
    implementation(deps.compose.animation)
    implementation(deps.compose.foundation)
    implementation(deps.compose.material)
    implementation(deps.compose.runtime)
    implementation(deps.compose.constraintLayout)
    implementation(deps.compose.hilt)
    implementation(deps.compose.accompanist.flowLayout)
    implementation(deps.compose.accompanist.pager)

    implementation(deps.commons.core)
    implementation(deps.commons.ktx)

    implementation(deps.misc.kotlinResult)
    implementation(deps.misc.coil)

    implementation(deps.google.daggerHiltAndroid)
    kapt(deps.google.daggerHiltAndroidCompiler)

    implementation(deps.misc.hiltBinder)
    ksp(deps.misc.hiltBinderCompiler)

    testImplementation(project(deps.local.commonTesting))
    testImplementation(deps.testing.jUnit)
    testImplementation(deps.testing.truth)
    testImplementation(deps.testing.mockk)
    testImplementation(deps.testing.coroutines)
    testImplementation(deps.testing.turbine)

    androidTestImplementation(deps.testing.testRunner)
    androidTestImplementation(deps.testing.jUnitExt)
}
