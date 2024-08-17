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

@file:Suppress("ClassName")

import org.gradle.api.JavaVersion

object appConfig {

    const val compileSdkVersion = 34
    const val targetSdkVersion = 34
    const val minSdkVersion = 21
    const val applicationId = "com.paulrybitskyi.gamedge"
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val instrumentationRunner = "com.paulrybitskyi.gamedge.common.testing.GamedgeTestRunner"

    const val jvmToolchainVersion = 17

    val javaCompatibilityVersion = JavaVersion.VERSION_17
}
