package com.paulrybitskyi.gamedge.plugins

import com.paulrybitskyi.gamedge.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class GamedgeKotlinKaptPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        configurePlugin()
        configureTasks()
    }

    private fun Project.setupPlugins(): Unit = with(plugins) {
        apply(libs.plugins.kotlinKapt.get().pluginId)
    }

    private fun Project.configurePlugin() {
        configure<KaptExtension> {
            correctErrorTypes = true
        }
    }

    private fun Project.configureTasks() {
        tasks.withType<KotlinCompile>().all {
            compilerOptions {
                // w: Kapt currently doesn't support language version 2.0+. Falling back to 1.9.
                freeCompilerArgs.add("-Xsuppress-version-warnings")
            }
        }
    }
}
