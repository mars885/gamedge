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
    implementation(project(deps.local.commonsUi))
    implementation(project(deps.local.igdbApi))
    implementation(project(deps.local.gamespotApi))
    implementation(project(deps.local.database))
    implementation(project(deps.local.featureCategory))
    implementation(project(deps.local.featureDashboard))
    implementation(project(deps.local.featureDiscovery))
    implementation(project(deps.local.featureInfo))
    implementation(project(deps.local.featureLikes))
    implementation(project(deps.local.featureNews))
    implementation(project(deps.local.featureSearch))
    implementation(project(deps.local.featureSplash))

    implementation(deps.androidX.navFragmentKtx)

    implementation(deps.google.daggerHilt)
    kapt(deps.google.daggerHiltCompiler)

    implementation(deps.misc.hiltBinder)
    kapt(deps.misc.hiltBinderCompiler)

    testImplementation(deps.testing.jUnit)
    androidTestImplementation(deps.testing.jUnitExt)
}