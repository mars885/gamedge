package com.paulrybitskyi.gamedge.plugins

import com.android.build.api.dsl.LibraryExtension
import com.paulrybitskyi.gamedge.extensions.addBundle
import com.paulrybitskyi.gamedge.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class GamedgeRemoteApiPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        configurePlugins()
        addDependencies()
    }

    private fun Project.setupPlugins(): Unit = with(plugins) {
        apply(libs.plugins.androidLibrary.get().pluginId)
        apply(libs.plugins.gamedgeAndroid.get().pluginId)
        apply(libs.plugins.gamedgeDaggerHilt.get().pluginId)
        apply(libs.plugins.kotlinxSerialization.get().pluginId)
    }

    private fun Project.configurePlugins() {
        configure<LibraryExtension> {
            buildFeatures {
                buildConfig = true
            }
        }
    }

    private fun Project.addDependencies(): Unit = with(dependencies) {
        add("api", project(localModules.commonApi))
        add("implementation", project(localModules.core))

        add("implementation", libs.kotlinxSerialization.get())
        add("implementation", libs.retrofit.get())

        add("testImplementation", project(localModules.commonTesting))
        addBundle("testImplementation", libs.bundles.testing.get())

        addBundle("androidTestImplementation", libs.bundles.testingAndroid.get())
        add("androidTestImplementation", libs.mockWebServer.get())
    }
}
