package com.paulrybitskyi.gamedge.plugins

import com.paulrybitskyi.gamedge.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class GamedgeDaggerHiltPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        addDependencies()
    }

    private fun Project.setupPlugins(): Unit = with(plugins) {
        apply(libs.plugins.kotlinKapt.get().pluginId)
        apply(libs.plugins.ksp.get().pluginId)
        apply(libs.plugins.daggerHilt.get().pluginId)
    }

    private fun Project.addDependencies(): Unit = with(dependencies) {
        add("implementation", libs.daggerHiltAndroid.get())
        add("kapt", libs.daggerHiltAndroidCompiler.get())

        add("implementation", libs.hiltBinder.get())
        add("ksp", libs.hiltBinderCompiler.get())
    }
}
