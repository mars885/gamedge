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

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // See https://github.com/gradle/gradle/issues/20084#issuecomment-1060822638
    // for why it has to be done this way
    id(libs.plugins.androidApplication.get().pluginId) apply false
    id(libs.plugins.androidLibrary.get().pluginId) apply false
    id(libs.plugins.kotlinJvm.get().pluginId) apply false
    id(libs.plugins.kotlinAndroid.get().pluginId) apply false
    id(libs.plugins.kotlinKapt.get().pluginId) apply false
    id(libs.plugins.protobuf.get().pluginId) apply false

    alias(libs.plugins.jetpackCompose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.daggerHilt) apply false
    alias(libs.plugins.kotlinxSerialization) apply false

    alias(libs.plugins.gradleVersions) apply true
    alias(libs.plugins.detekt) apply true
    alias(libs.plugins.ktlint) apply true
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
    config.setFrom("config/detekt/detekt.yml")
}

tasks.withType<Detekt>().configureEach {
    reports.html.required.set(true)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        listOf("alpha", "beta", "rc").any { keyword ->
            candidate.version.lowercase().contains(keyword)
        }
    }
}

allprojects {
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
    apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)

    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://jitpack.io") }
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set(rootProject.libs.versions.ktlint.get())
        android.set(true)
        outputToConsole.set(true)
        verbose.set(true)

        filter {
            // https://github.com/JLLeitschuh/ktlint-gradle/issues/266#issuecomment-529527697
            exclude { fileTreeElement -> fileTreeElement.file.path.contains("generated/") }
        }

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
    plugins.withId(rootProject.libs.plugins.kotlinJvm.get().pluginId) {
        extensions.findByType<KotlinProjectExtension>()?.run {
            jvmToolchain(rootProject.libs.versions.jvmToolchain.get().toInt())
        }
    }

    plugins.withId(rootProject.libs.plugins.kotlinKapt.get().pluginId) {
        extensions.findByType<KaptExtension>()?.run {
            correctErrorTypes = true
        }
    }

    tasks.withType<KotlinCompile>().all {
        compilerOptions {
            freeCompilerArgs.set(
                listOf(
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
                    "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                    "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                    "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                    "-opt-in=androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi",
                    "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                ),
            )
        }
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            // https://slack-chats.kotlinlang.org/t/18870077/running-in-to-strange-issue-with-combination-it-seems-of-jet#e9b98248-d6c8-427d-bff2-d0d82e4ab76c
            if (requested.module.toString() == rootProject.libs.composeFoundation.get().module.toString()) {
                useVersion(rootProject.libs.versions.composeFoundation.get())
            }

            // https://stackoverflow.com/a/70348822/7015881
            // https://issuetracker.google.com/issues/238425626
            if (requested.group == "androidx.lifecycle" && requested.name == "lifecycle-viewmodel-ktx") {
                useVersion(rootProject.libs.versions.viewModel.get())
            }
        }
    }
}

val clean by tasks.registering(Delete::class) {
    delete(layout.buildDirectory)
}
