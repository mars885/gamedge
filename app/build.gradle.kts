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
    id(libs.plugins.androidApplication.get().pluginId)
    id(libs.plugins.gamedgeAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)

    alias(libs.plugins.jetpackCompose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
}

dependencies {
    implementation(project(libs.versions.localCommonDomain.get()))
    implementation(project(libs.versions.localCommonData.get()))
    implementation(project(libs.versions.localCore.get()))
    implementation(project(libs.versions.localCommonUi.get()))
    implementation(project(libs.versions.localCommonUiWidgets.get()))
    implementation(project(libs.versions.localIgdbApi.get()))
    implementation(project(libs.versions.localGamespotApi.get()))
    implementation(project(libs.versions.localDatabase.get()))
    implementation(project(libs.versions.localFeatureCategory.get()))
    implementation(project(libs.versions.localFeatureDiscovery.get()))
    implementation(project(libs.versions.localFeatureInfo.get()))
    implementation(project(libs.versions.localFeatureImageViewer.get()))
    implementation(project(libs.versions.localFeatureLikes.get()))
    implementation(project(libs.versions.localFeatureNews.get()))
    implementation(project(libs.versions.localFeatureSearch.get()))
    implementation(project(libs.versions.localFeatureSettings.get()))

    implementation(libs.splash)

    implementation(libs.composeUi)
    implementation(libs.composeTooling)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeRuntime)
    implementation(libs.composeNavigation)
    implementation(libs.accompanistNavigationAnimations)

    implementation(libs.commonsCore)
    implementation(libs.commonsKtx)

    implementation(libs.daggerHiltAndroid)
    kapt(libs.daggerHiltAndroidCompiler)

    implementation(libs.hiltBinder)
    ksp(libs.hiltBinderCompiler)

    testImplementation(libs.jUnit)

    androidTestImplementation(libs.jUnitExt)
}

val installGitHook by tasks.registering(Copy::class) {
    from(File(rootProject.rootDir, "hooks/pre-push"))
    into(File(rootProject.rootDir, ".git/hooks/"))
    // https://github.com/gradle/kotlin-dsl-samples/issues/1412
    fileMode = 0b111101101 // -rwxr-xr-x
}

tasks.getByPath(":app:preBuild").dependsOn(installGitHook)
