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
    navSafeArgsKotlin()
    daggerHiltAndroid()
}

android {
    buildFeatures {
        viewBinding = true
    }

    // https://dagger.dev/hilt/gradle-setup#classpath-aggregation
    lintOptions {
        isCheckReleaseBuilds = false
    }
}

hilt {
    enableExperimentalClasspathAggregation = true
}

dependencies {
    implementation(project(deps.local.domain))
    implementation(project(deps.local.data))
    implementation(project(deps.local.igdbApi))
    implementation(project(deps.local.gamespotApi))
    implementation(project(deps.local.database))
    implementation(project(deps.local.core))
    implementation(project(deps.local.commonsUi))
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
    implementation(deps.commons.commonsNavigation)
    implementation(deps.commons.commonsMaterial)

    implementation(deps.misc.kotlinResult)

    implementation(deps.google.daggerHilt)
    kapt(deps.google.daggerHiltCompiler)

    implementation(deps.misc.hiltBinder)
    kapt(deps.misc.hiltBinderCompiler)

    implementation(deps.androidX.daggerHiltAssistedInjection)
    kapt(deps.androidX.daggerHiltAssistedInjectionCompiler)

    testImplementation(deps.testing.jUnit)
    androidTestImplementation(deps.testing.jUnitExt)
}