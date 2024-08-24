package com.paulrybitskyi.gamedge.plugins

import com.paulrybitskyi.gamedge.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class GamedgeJetpackComposePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        addDependencies()
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
}
