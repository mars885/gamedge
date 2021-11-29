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
    detekt()
    ktlint()
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

detekt {
    parallel = true
    buildUponDefaultConfig = true
    config = files("config/detekt/detekt.yml")
    reports.html.enabled = true
}

allprojects {
    apply(plugin = PLUGIN_DETEKT)
    apply(plugin = PLUGIN_KTLINT)

    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://jitpack.io") }
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        outputToConsole.set(true)
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
        }
    }

    // Without the below block, a build failure was happening when
    // running ./gradlew connectedAndroidTest.
    // See: https://github.com/mockito/mockito/issues/2007#issuecomment-689365556
    configurations.all {
        resolutionStrategy.force("org.objenesis:objenesis:2.6")
    }
}

subprojects {

    tasks.withType<KotlinCompile>().all {
        sourceCompatibility = appConfig.javaCompatibilityVersion.toString()
        targetCompatibility = appConfig.javaCompatibilityVersion.toString()

        kotlinOptions {
            freeCompilerArgs += listOf(
                "-Xopt-in=kotlin.ExperimentalStdlibApi",
                "-Xopt-in=kotlin.time.ExperimentalTime",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-Xopt-in=coil.annotation.ExperimentalCoilApi",
                "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi",
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
