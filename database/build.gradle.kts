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
    id(libs.plugins.gamedgeDaggerHilt.get().pluginId)

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
    implementation(project(localModules.core))

    implementation(libs.coroutines)
    implementation(libs.kotlinxSerialization)

    implementation(libs.room)
    implementation(libs.roomKtx)
    ksp(libs.roomCompiler)

    testImplementation(project(localModules.commonTesting))
    testImplementation(libs.jUnit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutinesTesting)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.jUnitExt)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.archCore)
    androidTestImplementation(libs.coroutinesTesting)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.roomTesting)

    androidTestImplementation(libs.daggerHiltTesting)
    kaptAndroidTest(libs.daggerHiltAndroidCompiler)
}
