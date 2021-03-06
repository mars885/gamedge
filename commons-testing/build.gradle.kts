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
    androidLibrary()
    gamedgeAndroid()
}

dependencies {
    implementation(project(deps.local.domain))
    implementation(project(deps.local.data))
    implementation(project(deps.local.core))
    implementation(project(deps.local.commonsData))
    implementation(project(deps.local.commonsApi))

    implementation(deps.misc.kotlinResult)

    implementation(deps.testing.jUnit)
    implementation(deps.testing.assertJ)
    implementation(deps.testing.mockk)
    implementation(deps.testing.coroutines)
}