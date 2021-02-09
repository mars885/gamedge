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
    androidLibrary()
    gamedgeAndroid()
    kotlinKapt()
    daggerHiltAndroid()
}

android {
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(deps.local.domain))
    implementation(project(deps.local.core))
    implementation(project(deps.local.commonsUi))
    implementation(project(deps.local.commonsUiWidgets))
    implementation(project(deps.local.imageLoading))

    implementation(deps.kotlin.coroutines)

    implementation(deps.androidX.recyclerView)
    implementation(deps.androidX.constraintLayout)
    implementation(deps.androidX.fragmentKtx)

    implementation(deps.google.materialComponents)

    implementation(deps.commons.commonsCore)
    implementation(deps.commons.commonsKtx)
    implementation(deps.commons.commonsWidgets)
    implementation(deps.commons.commonsRecyclerView)

    implementation(deps.misc.expandableTextView)

    implementation(deps.google.daggerHilt)
    kapt(deps.google.daggerHiltCompiler)

    implementation(deps.androidX.daggerHiltAssistedInjection)
    kapt(deps.androidX.daggerHiltAssistedInjectionCompiler)

    implementation(deps.misc.hiltBinder)
    kapt(deps.misc.hiltBinderCompiler)

    testImplementation(deps.testing.jUnit)
    androidTestImplementation(deps.testing.jUnitExt)
}