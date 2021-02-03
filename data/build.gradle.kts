import com.google.protobuf.gradle.*

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
    protobuf()
    kotlinKapt()
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
    implementation(project(deps.local.domain))
    implementation(project(deps.local.core))

    implementation(deps.kotlin.coroutines)

    implementation(deps.androidX.prefsDataStore)
    implementation(deps.androidX.protoDataStore)

    // Protobuf-generated classes extend Protobuf's public
    // class, which is needed to be present in the classpath
    // of the dependant modules
    api(deps.google.protobuf)

    implementation(deps.misc.kotlinResult)

    implementation(deps.google.daggerHilt)
    kapt(deps.google.daggerHiltCompiler)

    implementation(deps.misc.hiltBinder)
    kapt(deps.misc.hiltBinderCompiler)

    testImplementation(deps.testing.jUnit)
    androidTestImplementation(deps.testing.jUnitExt)
}