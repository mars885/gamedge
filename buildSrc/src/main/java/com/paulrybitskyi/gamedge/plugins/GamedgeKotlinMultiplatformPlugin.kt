package com.paulrybitskyi.gamedge.plugins

import com.paulrybitskyi.gamedge.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class GamedgeKotlinMultiplatformPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        configurePlugins()
        configureTasks()
    }

    private fun Project.setupPlugins(): Unit = with(plugins) {
        apply(libs.plugins.kotlinMultiplatform.get().pluginId)
    }

    private fun Project.configurePlugins() {
        configure<KotlinMultiplatformExtension> {
            jvm()
            jvmToolchain(libs.versions.jvmToolchain.get().toInt())
        }
    }

    private fun Project.configureTasks() {
        tasks.withType<KotlinCompile>().all {
            compilerOptions {
                freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }
}
