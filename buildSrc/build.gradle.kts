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
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.2")
    // https://github.com/google/dagger/issues/3068#issuecomment-999118496
    // Needs to be checked whether JavaPoet is still needed after AGP is updated
    // because currently it forces 1.10 JavaPoet version, for some odd reason.
    implementation("com.squareup:javapoet:1.13.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation(gradleApi()) // for custom plugins
}
