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

package com.paulrybitskyi.hiltbinder.processor.validator

import com.paulrybitskyi.hiltbinder.processor.detectors.QualifierAnnotationDetector
import com.paulrybitskyi.hiltbinder.processor.providers.MessageProvider
import com.paulrybitskyi.hiltbinder.processor.validator.concrete.ComponentDuplicationValidator
import com.paulrybitskyi.hiltbinder.processor.validator.concrete.MapKeyPresenceValidator
import com.paulrybitskyi.hiltbinder.processor.validator.concrete.QualifierPresenceValidator
import com.paulrybitskyi.hiltbinder.processor.validator.concrete.ReturnTypeValidator
import javax.annotation.processing.ProcessingEnvironment

internal object AnnotationsValidatorFactory {


    fun create(env: ProcessingEnvironment): AnnotationsValidator {
        return AnnotationsValidator(
            returnTypeValidator = createReturnTypeValidator(env),
            componentDuplicationValidator = createComponentDuplicationValidator(env),
            mapKeyPresenceValidator = createMapKeyPresenceValidator(env),
            qualifierPresenceValidator = createQualifierPresenceValidator(env)
        )
    }


    private fun createReturnTypeValidator(env: ProcessingEnvironment): ReturnTypeValidator {
        return ReturnTypeValidator(
            messageProvider = createMessageProvider(),
            elementUtils = env.elementUtils,
            typeUtils = env.typeUtils
        )
    }


    private fun createComponentDuplicationValidator(
        env: ProcessingEnvironment
    ): ComponentDuplicationValidator {
        return ComponentDuplicationValidator(
            messageProvider = createMessageProvider(),
            elementUtils = env.elementUtils,
            typeUtils = env.typeUtils
        )
    }


    private fun createMapKeyPresenceValidator(env: ProcessingEnvironment): MapKeyPresenceValidator {
        return MapKeyPresenceValidator(
            messageProvider = createMessageProvider(),
            elementUtils = env.elementUtils,
            typeUtils = env.typeUtils
        )
    }


    private fun createQualifierPresenceValidator(env: ProcessingEnvironment): QualifierPresenceValidator {
        return QualifierPresenceValidator(
            qualifierAnnotationDetector = createQualifierAnnotationDetector(env),
            messageProvider = createMessageProvider()
        )
    }


    private fun createQualifierAnnotationDetector(env: ProcessingEnvironment): QualifierAnnotationDetector {
        return QualifierAnnotationDetector(
            elementUtils = env.elementUtils,
            typeUtils = env.typeUtils
        )
    }


    private fun createMessageProvider(): MessageProvider {
        return MessageProvider()
    }


}