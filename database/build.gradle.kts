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

    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "com.paulrybitskyi.gamedge.database"

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }

    lint {
        // Fix an error "Error: EntityInsertionAdapter can only be accessed from within
        // the same library group prefix (referenced groupId=androidx.room with prefix
        // androidx from groupId=Gamedge) [RestrictedApi]
        disable.add("RestrictedApi")
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(project(deps.local.core))

    implementation(deps.kotlin.coroutines)
    implementation(deps.kotlin.serialization)

    implementation(deps.androidX.room)
    implementation(deps.androidX.roomKtx)
    ksp(deps.androidX.roomCompiler)

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
    androidTestImplementation(deps.testing.truth)
    androidTestImplementation(deps.testing.archCore)
    androidTestImplementation(deps.testing.coroutines)
    androidTestImplementation(deps.testing.turbine)
    androidTestImplementation(deps.testing.room)

    androidTestImplementation(deps.testing.daggerHilt)
    kaptAndroidTest(deps.google.daggerHiltAndroidCompiler)
}
