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

import com.paulrybitskyi.gamedge.extensions.property
import com.paulrybitskyi.gamedge.extensions.stringField

plugins {
    androidLibrary()
    gamedgeAndroid()
    kotlinKapt()
}

android {
    defaultConfig {
        stringField("TWITCH_APP_CLIENT_ID", property("TWITCH_APP_CLIENT_ID", ""))
        stringField("TWITCH_APP_CLIENT_SECRET", property("TWITCH_APP_CLIENT_SECRET", ""))
    }
}

dependencies {
    implementation(project(deps.local.data))
    implementation(project(deps.local.commonsApi))
    implementation(project(deps.local.commonsData))
    implementation(project(deps.local.igdbApicalypse))
    implementation(project(deps.local.core))

    implementation(deps.kotlin.coroutines)

    implementation(deps.square.okHttpLoggingInterceptor)
    implementation(deps.square.retrofit)
    implementation(deps.square.retrofitMoshiConverter)
    implementation(deps.square.retrofitScalarsConverter)

    implementation(deps.misc.kotlinResult)

    implementation(deps.square.moshi)
    kapt(deps.square.moshiCodeGenerator)

    implementation(deps.google.daggerHilt)
    kapt(deps.google.daggerHiltCompiler)

    implementation(deps.misc.hiltBinder)
    kapt(deps.misc.hiltBinderCompiler)

    testImplementation(deps.testing.jUnit)
    androidTestImplementation(deps.testing.jUnitExt)
}