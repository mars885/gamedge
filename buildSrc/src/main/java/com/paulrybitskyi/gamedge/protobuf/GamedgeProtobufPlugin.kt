/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.protobuf

import org.gradle.api.Plugin
import org.gradle.api.Project
import PLUGIN_PROTOBUF

@Suppress("DEPRECATION")
class GamedgeProtobufPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        setupPlugin()
        configurePlugin()
        addProtobufDependency()
    }

    private fun Project.setupPlugin() {
        plugins.apply(PLUGIN_PROTOBUF)
    }

    private fun Project.configurePlugin() {
/*        protobuf {
            protoc {
                artifact = deps.google.protobufCompiler
            }

            generateProtoTasks {
                all().forEach { task ->
                    task.builtins {
                        id("java") {
                            option("lite")
                        }
                    }
                }
            }
        }*/
    }

    private fun Project.addProtobufDependency() {
        dependencies.add("implementation", deps.google.protobuf)
    }
}
