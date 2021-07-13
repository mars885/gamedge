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

import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    gradleVersions()
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(deps.plugins.androidGradle)
        classpath(deps.plugins.kotlinGradle)
        classpath(deps.plugins.navSafeArgs)
        classpath(deps.plugins.daggerHiltGradle)
        classpath(deps.plugins.protobuf)
        classpath(deps.plugins.gradleVersions)
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://jitpack.io") }
    }

    // Without the below block, a build failure was happening when
    // running ./gradlew connectedAndroidTest.
    // See: https://github.com/mockito/mockito/issues/2007#issuecomment-689365556
    configurations.all {
        resolutionStrategy.force("org.objenesis:objenesis:2.6")
    }
}

subprojects {

    tasks.withType(KotlinCompile::class.java).all {
        sourceCompatibility = appConfig.javaCompatibilityVersion.toString()
        targetCompatibility = appConfig.javaCompatibilityVersion.toString()

        kotlinOptions {
            freeCompilerArgs += listOf(
                "-Xuse-experimental=kotlin.ExperimentalStdlibApi",
                "-Xuse-experimental=kotlin.time.ExperimentalTime",
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xuse-experimental=kotlinx.coroutines.FlowPreview",
                "-Xuse-experimental=kotlinx.serialization.ExperimentalSerializationApi"
            )

            jvmTarget = appConfig.kotlinCompatibilityVersion.toString()
        }
    }

    plugins.withId(PLUGIN_KOTLIN_KAPT) {
        extensions.findByType<KaptExtension>()?.run {
            correctErrorTypes = true
        }
    }

}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}