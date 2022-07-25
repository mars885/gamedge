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
    androidApplication()
    gamedgeAndroid()
    kotlinKapt()
    ksp()
    daggerHiltAndroid()
}

android {
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = versions.compose
    }
}

dependencies {
    implementation(project(deps.local.commonDomain))
    implementation(project(deps.local.commonData))
    implementation(project(deps.local.core))
    implementation(project(deps.local.commonUi))
    implementation(project(deps.local.commonUiWidgets))
    implementation(project(deps.local.igdbApi))
    implementation(project(deps.local.gamespotApi))
    implementation(project(deps.local.database))
    implementation(project(deps.local.featureCategory))
    implementation(project(deps.local.featureDiscovery))
    implementation(project(deps.local.featureInfo))
    implementation(project(deps.local.featureImageViewer))
    implementation(project(deps.local.featureLikes))
    implementation(project(deps.local.featureNews))
    implementation(project(deps.local.featureSearch))
    implementation(project(deps.local.featureSettings))

    implementation(deps.androidX.splash)

    implementation(deps.compose.ui)
    implementation(deps.compose.tooling)
    implementation(deps.compose.foundation)
    implementation(deps.compose.material)
    implementation(deps.compose.runtime)
    implementation(deps.compose.navigation)
    implementation(deps.compose.accompanist.insets)
    implementation(deps.compose.accompanist.navigationAnimations)

    implementation(deps.commons.core)
    implementation(deps.commons.ktx)

    implementation(deps.google.daggerHiltAndroid)
    kapt(deps.google.daggerHiltAndroidCompiler)

    implementation(deps.misc.hiltBinder)
    ksp(deps.misc.hiltBinderCompiler)

    coreLibraryDesugaring(deps.misc.desugaredLibs)

    testImplementation(deps.testing.jUnit)
    androidTestImplementation(deps.testing.jUnitExt)
}

val installGitHook by tasks.registering(Copy::class) {
    from(File(rootProject.rootDir, "hooks/pre-push"))
    into(File(rootProject.rootDir, ".git/hooks/"))
    // https://github.com/gradle/kotlin-dsl-samples/issues/1412
    fileMode = 0b111101101 // -rwxr-xr-x
}

tasks.getByPath(":app:preBuild").dependsOn(installGitHook)
