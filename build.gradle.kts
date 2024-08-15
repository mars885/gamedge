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
    androidApplication() apply false
    androidLibrary() apply false
    kotlinAndroid() apply false
    protobuf() apply false

    jetpackCompose() version versions.kotlin apply false
    ksp() version versions.kspPlugin apply false
    daggerHilt() version versions.daggerHilt apply false
    kotlinxSerialization() version versions.kotlin apply false

    gradleVersions() version versions.gradleVersionsPlugin apply true
    detekt() version versions.detektPlugin apply true
    ktlint() version versions.ktlintPlugin apply true
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
    apply(plugin = PLUGIN_DETEKT)
    apply(plugin = PLUGIN_KTLINT)

    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://jitpack.io") }
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set(versions.ktlint)
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
    plugins.withId(PLUGIN_KOTLIN) {
        extensions.findByType<KotlinProjectExtension>()?.run {
            jvmToolchain(appConfig.jvmToolchainVersion)
        }
    }

    plugins.withId(PLUGIN_KOTLIN_KAPT) {
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
                    "-opt-in=com.google.accompanist.pager.ExperimentalPagerApi",
                ),
            )
        }
    }

    // https://stackoverflow.com/a/70348822/7015881
    // https://issuetracker.google.com/issues/238425626
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "androidx.lifecycle" && requested.name == "lifecycle-viewmodel-ktx") {
                useVersion(deps.androidX.viewModel)
            }
        }
    }
}

val clean by tasks.registering(Delete::class) {
    delete(layout.buildDirectory)
}
