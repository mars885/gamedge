package com.paulrybitskyi.gamedge.plugins

import com.paulrybitskyi.gamedge.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class GamedgeKotlinCoroutinesPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        addDependencies()
        configureTasks()
    }

    private fun Project.addDependencies(): Unit = with(dependencies) {
        add("implementation", libs.coroutines.get())
    }

    private fun Project.configureTasks() {
        tasks.withType<KotlinCompile>().all {
            compilerOptions {
                freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }
}
