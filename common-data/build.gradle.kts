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
    androidLibrary()
    gamedgeAndroid()
    kotlinKapt()
    ksp()
    protobuf()
}

android {
    namespace = "com.paulrybitskyi.gamedge.common.data"
}

protobuf {
    protoc {
        artifact = deps.google.protobufCompiler
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
    implementation(project(deps.local.commonDomain))
    implementation(project(deps.local.core))
    implementation(project(deps.local.igdbApi))
    implementation(project(deps.local.database))

    implementation(deps.kotlin.coroutines)

    implementation(deps.androidX.prefsDataStore)
    implementation(deps.androidX.protoDataStore)
    implementation(deps.google.protobuf)

    implementation(deps.misc.kotlinResult)

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
}
