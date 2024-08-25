package com.paulrybitskyi.gamedge.plugins

import com.paulrybitskyi.gamedge.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class GamedgeJetpackComposePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        addDependencies()
        configureTasks()
    }

    private fun Project.setupPlugins(): Unit = with(plugins) {
        apply(libs.plugins.jetpackCompose.get().pluginId)
    }

    private fun Project.addDependencies(): Unit = with(dependencies) {
        add("implementation", platform(libs.composeBom.get()))
        add("implementation", libs.composeUi.get())
        add("debugImplementation", libs.composeTooling.get())
        add("implementation", libs.composeToolingPreview.get())
        add("implementation", libs.composeFoundation.get())
        add("implementation", libs.composeMaterial.get())
        add("implementation", libs.composeRuntime.get())
        add("implementation", libs.composeAnimation.get())
        add("implementation", libs.composeConstraintLayout.get())
    }

    private fun Project.configureTasks() {
        tasks.withType<KotlinCompile>().all {
            compilerOptions {
                freeCompilerArgs.addAll(
                    listOf(
                        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                        "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                        "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                        "-opt-in=androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi",
                    ),
                )
            }
        }
    }
}
