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
    kotlinxSerialization()
    daggerHiltAndroid() // does not compile instrumented tests without the plugin
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.paulrybitskyi.gamedge.commons.testing.GamedgeTestRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
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
    implementation(project(deps.local.data))
    implementation(project(deps.local.commonsData))
    implementation(project(deps.local.core))

    implementation(deps.kotlin.coroutines)
    implementation(deps.kotlin.serialization)

    implementation(deps.misc.kotlinResult)

    implementation(deps.androidX.room)
    implementation(deps.androidX.roomKtx)
    kapt(deps.androidX.roomCompiler)

    implementation(deps.google.daggerHilt)
    kapt(deps.google.daggerHiltCompiler)

    implementation(deps.misc.hiltBinder)
    kapt(deps.misc.hiltBinderCompiler)

    testImplementation(project(deps.local.commonsTesting))
    testImplementation(deps.testing.jUnit)
    testImplementation(deps.testing.assertJ)
    testImplementation(deps.testing.mockk)
    testImplementation(deps.testing.coroutines)
    testImplementation(deps.testing.turbine)

    androidTestImplementation(project(deps.local.commonsTesting))
    androidTestImplementation(deps.testing.testRunner)
    androidTestImplementation(deps.testing.jUnitExt)
    androidTestImplementation(deps.testing.assertJ)
    androidTestImplementation(deps.testing.archCore)
    androidTestImplementation(deps.testing.coroutines)
    androidTestImplementation(deps.testing.turbine)
    androidTestImplementation(deps.testing.room)

    androidTestImplementation(deps.testing.daggerHilt)
    kaptAndroidTest(deps.google.daggerHiltCompiler)
}