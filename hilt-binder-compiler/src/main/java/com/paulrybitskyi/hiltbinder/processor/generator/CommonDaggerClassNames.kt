/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.hiltbinder.processor.generator

import com.squareup.javapoet.ClassName


internal val DAGGER_TYPE_MODULE = ClassName.get("dagger", "Module")
internal val DAGGER_TYPE_INSTALL_IN = ClassName.get("dagger.hilt", "InstallIn")
internal val DAGGER_TYPE_BINDS = ClassName.get("dagger", "Binds")
internal val DAGGER_TYPE_INTO_SET = ClassName.get("dagger.multibindings", "IntoSet")
internal val DAGGER_TYPE_INTO_MAP = ClassName.get("dagger.multibindings", "IntoMap")
internal val DAGGER_TYPE_MAP_INT_KEY = ClassName.get("dagger.multibindings", "IntKey")
internal val DAGGER_TYPE_MAP_LONG_KEY = ClassName.get("dagger.multibindings", "LongKey")
internal val DAGGER_TYPE_MAP_STRING_KEY = ClassName.get("dagger.multibindings", "StringKey")
internal val DAGGER_TYPE_MAP_CLASS_KEY = ClassName.get("dagger.multibindings", "ClassKey")