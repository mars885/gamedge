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

import com.google.protobuf.gradle.id

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.gamedgeAndroid.get().pluginId)
    id(libs.plugins.protobuf.get().pluginId)

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.paulrybitskyi.gamedge.common.data"
}

protobuf {
    protoc {
        artifact = libs.protobufCompiler.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(project(localModules.commonDomain))
    implementation(project(localModules.core))
    implementation(project(localModules.igdbApi))
    implementation(project(localModules.database))

    implementation(libs.coroutines)

    implementation(libs.prefsDataStore)
    implementation(libs.protoDataStore)
    implementation(libs.protobuf)

    implementation(libs.kotlinResult)

    implementation(libs.daggerHiltAndroid)
    ksp(libs.daggerHiltAndroidCompiler)

    implementation(libs.hiltBinder)
    ksp(libs.hiltBinderCompiler)

    testImplementation(project(localModules.commonTesting))
    testImplementation(libs.jUnit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutinesTesting)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.jUnitExt)
}
