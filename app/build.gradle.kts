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
    id(libs.plugins.gamedgeJetpackCompose.get().pluginId)
    id(libs.plugins.gamedgeDaggerHilt.get().pluginId)
}

dependencies {
    implementation(project(localModules.commonDomain))
    implementation(project(localModules.commonData))
    implementation(project(localModules.core))
    implementation(project(localModules.commonUi))
    implementation(project(localModules.commonUiWidgets))
    implementation(project(localModules.igdbApi))
    implementation(project(localModules.gamespotApi))
    implementation(project(localModules.database))
    implementation(project(localModules.featureCategory))
    implementation(project(localModules.featureDiscovery))
    implementation(project(localModules.featureInfo))
    implementation(project(localModules.featureImageViewer))
    implementation(project(localModules.featureLikes))
    implementation(project(localModules.featureNews))
    implementation(project(localModules.featureSearch))
    implementation(project(localModules.featureSettings))

    implementation(libs.activity)
    implementation(libs.splash)

    implementation(libs.composeNavigation)

    implementation(libs.commonsCore)
    implementation(libs.commonsKtx)
}

val installGitHook by tasks.registering(Copy::class) {
    from(File(rootProject.rootDir, "hooks/pre-push"))
    into(File(rootProject.rootDir, ".git/hooks/"))
    // https://github.com/gradle/kotlin-dsl-samples/issues/1412
    fileMode = 0b111101101 // -rwxr-xr-x
}

tasks.getByPath(":app:preBuild").dependsOn(installGitHook)
