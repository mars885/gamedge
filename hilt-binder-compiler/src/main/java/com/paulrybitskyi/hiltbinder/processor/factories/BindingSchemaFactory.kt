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

package com.paulrybitskyi.hiltbinder.processor.factories

import com.paulrybitskyi.hiltbinder.processor.detectors.*
import com.paulrybitskyi.hiltbinder.processor.model.BindingSchema
import javax.lang.model.element.TypeElement

internal class BindingSchemaFactory(
    private val bindingPackageNameDetector: BindingPackageNameDetector,
    private val hiltComponentDetector: HiltComponentDetector,
    private val contributionTypeDetector: ContributionTypeDetector,
    private val qualifierAnnotationDetector: QualifierAnnotationDetector,
    private val bindingReturnTypeDetector: BindingReturnTypeDetector,
    private val bindingMethodNameFactory: BindingMethodNameFactory
) {


    private companion object {

        private const val BINDING_PARAM_NAME = "binding"

    }


    fun createBindingSchema(typeElement: TypeElement): BindingSchema {
        val packageName = bindingPackageNameDetector.detectPackageName(typeElement)
        val component = hiltComponentDetector.detectComponent(typeElement)
        val contributionType = contributionTypeDetector.detectType(typeElement)
        val qualifierAnnotation = qualifierAnnotationDetector.detectAnnotation(typeElement)
        val returnType = bindingReturnTypeDetector.detectReturnType(typeElement)
        val methodName = bindingMethodNameFactory.createMethodName(typeElement)

        return BindingSchema(
            packageName = packageName,
            component = component,
            contributionType = contributionType,
            qualifierAnnotation = qualifierAnnotation,
            methodName = methodName,
            paramType = typeElement,
            paramName = BINDING_PARAM_NAME,
            returnType = returnType
        )
    }


}