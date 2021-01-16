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

package com.paulrybitskyi.hiltbinder.processor.parser

import com.paulrybitskyi.hiltbinder.processor.ComponentMapper
import com.paulrybitskyi.hiltbinder.processor.detectors.*
import com.paulrybitskyi.hiltbinder.processor.factories.BindingMethodNameFactory
import com.paulrybitskyi.hiltbinder.processor.factories.BindingSchemaFactory
import com.paulrybitskyi.hiltbinder.processor.factories.FileInterfaceNameFactory
import com.paulrybitskyi.hiltbinder.processor.factories.ModuleSchemaFactory
import com.paulrybitskyi.hiltbinder.processor.providers.PackageNameProvider
import javax.annotation.processing.ProcessingEnvironment

internal object AnnotationsParserFactory {


    fun create(env: ProcessingEnvironment): AnnotationsParser {
        return AnnotationsParser(
            bindingSchemaFactory = createBindingSchemaFactory(env),
            moduleSchemaFactory = createModuleSchemaFactory(env),
            packageNameProvider = createPackageNameProvider()
        )
    }


    private fun createBindingSchemaFactory(env: ProcessingEnvironment): BindingSchemaFactory {
        return BindingSchemaFactory(
            bindingPackageNameDetector = createBindingPackageNameDetector(env),
            hiltComponentDetector = createHiltComponentDetector(env),
            contributionTypeDetector = createContributionTypeDetector(env),
            qualifierAnnotationDetector = createQualifierAnnotationDetector(env),
            bindingReturnTypeDetector = createBindingReturnTypeDetector(env),
            bindingMethodNameFactory = createBindingMethodNameFactory()
        )
    }


    private fun createBindingPackageNameDetector(env: ProcessingEnvironment): BindingPackageNameDetector {
        return BindingPackageNameDetector(env.elementUtils)
    }


    private fun createHiltComponentDetector(env: ProcessingEnvironment): HiltComponentDetector {
        return HiltComponentDetector(
            componentMapper = createComponentMapper(),
            elementUtils = env.elementUtils,
            typeUtils = env.typeUtils
        )
    }


    private fun createComponentMapper(): ComponentMapper {
        return ComponentMapper()
    }


    private fun createContributionTypeDetector(env: ProcessingEnvironment): ContributionTypeDetector {
        return ContributionTypeDetector(
            elementUtils = env.elementUtils,
            typeUtils = env.typeUtils
        )
    }


    private fun createQualifierAnnotationDetector(env: ProcessingEnvironment): QualifierAnnotationDetector {
        return QualifierAnnotationDetector(
            elementUtils = env.elementUtils,
            typeUtils = env.typeUtils
        )
    }


    private fun createBindingReturnTypeDetector(env: ProcessingEnvironment): BindingReturnTypeDetector {
        return BindingReturnTypeDetector(
            elementUtils = env.elementUtils,
            typeUtils = env.typeUtils
        )
    }


    private fun createBindingMethodNameFactory(): BindingMethodNameFactory {
        return BindingMethodNameFactory()
    }


    private fun createModuleSchemaFactory(env: ProcessingEnvironment): ModuleSchemaFactory {
        return ModuleSchemaFactory(
            fileInterfaceNameFactory = createFileInterfaceNameFactory(),
            elementUtils = env.elementUtils
        )
    }


    private fun createFileInterfaceNameFactory(): FileInterfaceNameFactory {
        return FileInterfaceNameFactory()
    }


    private fun createPackageNameProvider(): PackageNameProvider {
        return PackageNameProvider()
    }


}