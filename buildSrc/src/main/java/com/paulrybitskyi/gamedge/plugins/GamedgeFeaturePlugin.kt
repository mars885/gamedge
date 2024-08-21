package com.paulrybitskyi.gamedge.plugins

import com.paulrybitskyi.gamedge.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class GamedgeFeaturePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        addDependencies()
    }

    private fun Project.setupPlugins(): Unit = with(plugins) {
        apply(libs.plugins.androidLibrary.get().pluginId)
        apply(libs.plugins.gamedgeAndroid.get().pluginId)
        apply(libs.plugins.gamedgeJetpackCompose.get().pluginId)
        apply(libs.plugins.gamedgeDaggerHilt.get().pluginId)
    }

    private fun Project.addDependencies(): Unit = with(dependencies) {
        add("implementation", project(localModules.commonDomain))
        add("implementation", project(localModules.commonData))
        add("implementation", project(localModules.core))
        add("implementation", project(localModules.commonUi))
        add("implementation", project(localModules.commonUiWidgets))
        add("implementation", libs.coroutines.get())
        add("implementation", libs.composeHilt.get())
        add("implementation", libs.commonsCore.get())
        add("implementation", libs.commonsKtx.get())
        add("implementation", libs.kotlinResult.get())
        add("implementation", libs.coil.get())
        add("testImplementation", project(localModules.commonTesting))
        add("testImplementation", libs.jUnit.get())
        add("testImplementation", libs.truth.get())
        add("testImplementation", libs.mockk.get())
        add("testImplementation", libs.coroutinesTesting.get())
        add("testImplementation", libs.turbine.get())
        add("androidTestImplementation", libs.testRunner.get())
        add("androidTestImplementation", libs.jUnitExt.get())
    }
}
