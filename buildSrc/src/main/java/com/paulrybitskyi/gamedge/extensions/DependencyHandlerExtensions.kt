package com.paulrybitskyi.gamedge.extensions

import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.addBundle(configurationName: String, bundle: ExternalModuleDependencyBundle) {
    bundle.forEach { dependency ->
        add(configurationName, dependency)
    }
}
