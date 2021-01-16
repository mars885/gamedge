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

import org.gradle.internal.jvm.Jvm

plugins {
    kotlin()
    kotlinKapt()
}

dependencies {
    implementation(project(":hilt-binder"))

    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("com.squareup:javapoet:1.13.0")

    compileOnly("net.ltgt.gradle.incap:incap:0.3")
    kapt("net.ltgt.gradle.incap:incap-processor:0.3")

    compileOnly("com.google.auto.service:auto-service:1.0-rc7")
    kapt("com.google.auto.service:auto-service:1.0-rc7")

    testImplementation("junit:junit:4.13.1")
    testImplementation("com.google.truth:truth:1.1")
    testImplementation("com.google.testing.compile:compile-testing:0.19")

    // The following dep excludes Android related classes for some reason
    // (Android components and their according scopes), so they are
    // provided in a separate local jar file.
    testImplementation("com.google.dagger:hilt-android:2.31-alpha")
    testImplementation(files("libs/hilt-android-2.31-alpha.jar"))

    // https://github.com/google/compile-testing/issues/28
    if(Jvm.current().javaVersion?.isJava9Compatible == false) {
        testImplementation(files(Jvm.current().toolsJar))
    }
}