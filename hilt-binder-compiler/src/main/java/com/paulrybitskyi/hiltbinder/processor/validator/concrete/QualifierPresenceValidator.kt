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

package com.paulrybitskyi.hiltbinder.processor.validator.concrete

import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.hiltbinder.processor.detectors.QualifierAnnotationDetector
import com.paulrybitskyi.hiltbinder.processor.providers.MessageProvider
import com.paulrybitskyi.hiltbinder.processor.utils.HiltBinderException
import javax.lang.model.element.TypeElement

internal class QualifierPresenceValidator(
    private val qualifierAnnotationDetector: QualifierAnnotationDetector,
    private val messageProvider: MessageProvider
) {


    fun validate(typeElement: TypeElement) {
        if(!typeElement.getAnnotation(BindType::class.java).withQualifier) return

        if(qualifierAnnotationDetector.detectAnnotation(typeElement) == null) {
            throw HiltBinderException(
                messageProvider.qualifierAbsentError(),
                typeElement
            )
        }
    }


}