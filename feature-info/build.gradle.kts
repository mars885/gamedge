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
    implementation(project(libs.versions.localCommonDomain.get()))
    implementation(project(libs.versions.localCommonData.get()))
    implementation(project(libs.versions.localCore.get()))
    implementation(project(libs.versions.localCommonUi.get()))
    implementation(project(libs.versions.localCommonUiWidgets.get()))

    implementation(libs.coroutines)

    implementation(libs.composeUi)
    implementation(libs.composeTooling)
    implementation(libs.composeAnimation)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeRuntime)
    implementation(libs.composeConstraintLayout)
    implementation(libs.composeHilt)
    implementation(libs.accompanistFlowLayout)
    implementation(libs.accompanistPager)

    implementation(libs.commonsCore)
    implementation(libs.commonsKtx)

    implementation(libs.kotlinResult)
    implementation(libs.coil)

    implementation(libs.daggerHiltAndroid)
    kapt(libs.daggerHiltAndroidCompiler)

    implementation(libs.hiltBinder)
    ksp(libs.hiltBinderCompiler)

    testImplementation(project(libs.versions.localCommonTesting.get()))
    testImplementation(libs.jUnit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutinesTesting)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.jUnitExt)
}
