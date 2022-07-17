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

import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

const val PLUGIN_GAMEDGE_ANDROID = "com.paulrybitskyi.gamedge.android"
const val PLUGIN_GAMEDGE_PROTOBUF = "com.paulrybitskyi.gamedge.protobuf"
const val PLUGIN_GRADLE_VERSIONS = "com.github.ben-manes.versions"
const val PLUGIN_ANDROID_APPLICATION = "com.android.application"
const val PLUGIN_ANDROID_LIBRARY = "com.android.library"
const val PLUGIN_KOTLIN = "kotlin"
const val PLUGIN_KOTLIN_ANDROID = "kotlin-android"
const val PLUGIN_KOTLIN_KAPT = "kotlin-kapt"
const val PLUGIN_KSP = "com.google.devtools.ksp"
const val PLUGIN_KOTLINX_SERIALIZATION = "org.jetbrains.kotlin.plugin.serialization"
const val PLUGIN_DAGGER_HILT_ANDROID = "dagger.hilt.android.plugin"
const val PLUGIN_DETEKT = "io.gitlab.arturbosch.detekt"
const val PLUGIN_KTLINT = "org.jlleitschuh.gradle.ktlint"
const val PLUGIN_PROTOBUF = "com.google.protobuf"

fun PluginDependenciesSpec.gamedgeAndroid(): PluginDependencySpec {
    return id(PLUGIN_GAMEDGE_ANDROID)
}

fun PluginDependenciesSpec.gamedgeProtobuf(): PluginDependencySpec {
    return id(PLUGIN_GAMEDGE_PROTOBUF)
}

fun PluginDependenciesSpec.gradleVersions(): PluginDependencySpec {
    return (id(PLUGIN_GRADLE_VERSIONS) version versions.gradleVersionsPlugin)
}

fun PluginDependenciesSpec.androidApplication(): PluginDependencySpec {
    return id(PLUGIN_ANDROID_APPLICATION)
}

fun PluginDependenciesSpec.androidLibrary(): PluginDependencySpec {
    return id(PLUGIN_ANDROID_LIBRARY)
}

fun PluginDependenciesSpec.kotlin(): PluginDependencySpec {
    return id(PLUGIN_KOTLIN)
}

fun PluginDependenciesSpec.kotlinAndroid(): PluginDependencySpec {
    return id(PLUGIN_KOTLIN_ANDROID)
}

fun PluginDependenciesSpec.kotlinKapt(): PluginDependencySpec {
    return id(PLUGIN_KOTLIN_KAPT)
}

fun PluginDependenciesSpec.ksp(): PluginDependencySpec {
    return (id(PLUGIN_KSP) version versions.kspPlugin)
}

fun PluginDependenciesSpec.kotlinxSerialization(): PluginDependencySpec {
    return (id(PLUGIN_KOTLINX_SERIALIZATION) version versions.kotlin)
}

fun PluginDependenciesSpec.daggerHiltAndroid(): PluginDependencySpec {
    return id(PLUGIN_DAGGER_HILT_ANDROID)
}

fun PluginDependenciesSpec.detekt(): PluginDependencySpec {
    return (id(PLUGIN_DETEKT) version versions.detektPlugin)
}

fun PluginDependenciesSpec.ktlint(): PluginDependencySpec {
    return (id(PLUGIN_KTLINT) version versions.ktlintPlugin)
}

fun PluginDependenciesSpec.protobuf(): PluginDependencySpec {
    return id(PLUGIN_PROTOBUF)
}
