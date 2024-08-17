/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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
}

android {
    namespace = "com.paulrybitskyi.gamedge.common.api"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(deps.local.core))

    implementation(deps.kotlin.serialization)

    implementation(deps.square.okHttpLoggingInterceptor)
    implementation(deps.square.retrofit)
    implementation(deps.square.retrofitKotlinxSerializationConverter)

    implementation(deps.misc.kotlinResult)

    implementation(deps.google.daggerHiltAndroid)
    kapt(deps.google.daggerHiltAndroidCompiler)

    implementation(deps.misc.hiltBinder)
    ksp(deps.misc.hiltBinderCompiler)

    testImplementation(deps.testing.jUnit)
    androidTestImplementation(deps.testing.jUnitExt)
}
