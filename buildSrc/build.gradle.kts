/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

gradlePlugin {
    plugins {
        create("com.paulrybitskyi.gamedge.android") {
            id = "com.paulrybitskyi.gamedge.android"
            implementationClass = "com.paulrybitskyi.gamedge.plugins.GamedgeAndroidPlugin"
        }
        create("com.paulrybitskyi.gamedge.kotlin.coroutines") {
            id = "com.paulrybitskyi.gamedge.kotlin.coroutines"
            implementationClass = "com.paulrybitskyi.gamedge.plugins.GamedgeKotlinCoroutinesPlugin"
        }
        create("com.paulrybitskyi.gamedge.kotlin.kapt") {
            id = "com.paulrybitskyi.gamedge.kotlin.kapt"
            implementationClass = "com.paulrybitskyi.gamedge.plugins.GamedgeKotlinKaptPlugin"
        }
        create("com.paulrybitskyi.gamedge.protobuf") {
            id = "com.paulrybitskyi.gamedge.protobuf"
            implementationClass = "com.paulrybitskyi.gamedge.plugins.GamedgeProtobufPlugin"
        }
        create("com.paulrybitskyi.gamedge.jetpack.compose") {
            id = "com.paulrybitskyi.gamedge.jetpack.compose"
            implementationClass = "com.paulrybitskyi.gamedge.plugins.GamedgeJetpackComposePlugin"
        }
        create("com.paulrybitskyi.gamedge.dagger.hilt") {
            id = "com.paulrybitskyi.gamedge.dagger.hilt"
            implementationClass = "com.paulrybitskyi.gamedge.plugins.GamedgeDaggerHiltPlugin"
        }
        create("com.paulrybitskyi.gamedge.kotlinx.serialization") {
            id = "com.paulrybitskyi.gamedge.kotlinx.serialization"
            implementationClass = "com.paulrybitskyi.gamedge.plugins.GamedgeKotlinxSerializationPlugin"
        }
        create("com.paulrybitskyi.gamedge.remote.api") {
            id = "com.paulrybitskyi.gamedge.remote.api"
            implementationClass = "com.paulrybitskyi.gamedge.plugins.GamedgeRemoteApiPlugin"
        }
        create("com.paulrybitskyi.gamedge.feature") {
            id = "com.paulrybitskyi.gamedge.feature"
            implementationClass = "com.paulrybitskyi.gamedge.plugins.GamedgeFeaturePlugin"
        }
    }
}

dependencies {
    // Enabling the usage of the version catalog to be used inside the buildSrc. Parenthesis
    // (libs) are used to avoid a strange "Unresolved reference to version catalog" IDE error.
    // For more info, see https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files((libs).javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.androidPlugin)
    implementation(libs.kotlinPlugin)
    implementation(libs.protobufPlugin)
    implementation(gradleApi()) // for custom plugins

    // https://github.com/google/dagger/issues/3068#issuecomment-999118496
    // Needs to be checked whether JavaPoet is still needed after AGP is updated
    // because currently it forces 1.10 JavaPoet version, for some odd reason.
    implementation(libs.javaPoet)
}
