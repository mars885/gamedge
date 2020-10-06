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
    daggerHiltAndroid()
}

android {
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(deps.local.domain))
    implementation(project(deps.local.data))
    implementation(project(deps.local.igdbApi))
    implementation(project(deps.local.database))
    implementation(project(deps.local.core))
    implementation(project(deps.local.commonsUiWidgets))

    implementation(deps.androidX.appCompat)
    implementation(deps.androidX.navFragmentKtx)
    implementation(deps.androidX.navUiKtx)
    implementation(deps.androidX.constraintLayout)
    implementation(deps.androidX.recyclerView)
    implementation(deps.androidX.viewPager2)
    implementation(deps.androidX.lifecycleCommonJava8)
    implementation(deps.androidX.lifecycleViewModel)
    implementation(deps.androidX.coreKtx)
    implementation(deps.androidX.fragmentKtx)

    implementation(deps.commons.commonsCore)
    implementation(deps.commons.commonsKtx)
    implementation(deps.commons.commonsWidgets)
    implementation(deps.commons.commonsWindowAnims)

    implementation(deps.misc.kotlinResult)

    implementation(deps.google.daggerHilt)
    kapt(deps.google.daggerHiltCompiler)

    implementation(deps.androidX.daggerHiltAssistedInjection)
    kapt(deps.androidX.daggerHiltAssistedInjectionCompiler)

    // Have to be added due to how Dagger Hilt (alpha) works currently.
    // Should be removed when the Dagger Hilt is stable.
    // Issue: https://github.com/google/dagger/issues/1991
    // Solution: https://github.com/google/dagger/issues/1991#issuecomment-667810346
    implementation(project(deps.local.igdbApicalypse))
    implementation(project(deps.local.commonsData))
    implementation(project(deps.local.imageLoading))
    implementation(deps.square.retrofit)
    implementation(deps.square.moshi)
    implementation(deps.square.okHttpLoggingInterceptor)
    implementation(deps.square.picasso)
    implementation(deps.androidX.room)
    implementation(deps.androidX.prefsDataStore)

    testImplementation(deps.testing.jUnit)
    androidTestImplementation(deps.testing.jUnitExt)
}