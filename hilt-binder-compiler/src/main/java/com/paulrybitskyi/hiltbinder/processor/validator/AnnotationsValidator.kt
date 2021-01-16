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

import com.paulrybitskyi.hiltbinder.processor.utils.castEach
import com.paulrybitskyi.hiltbinder.processor.validator.concrete.ComponentDuplicationValidator
import com.paulrybitskyi.hiltbinder.processor.validator.concrete.MapKeyPresenceValidator
import com.paulrybitskyi.hiltbinder.processor.validator.concrete.QualifierPresenceValidator
import com.paulrybitskyi.hiltbinder.processor.validator.concrete.ReturnTypeValidator
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

internal class AnnotationsValidator(
    private val returnTypeValidator: ReturnTypeValidator,
    private val componentDuplicationValidator: ComponentDuplicationValidator,
    private val mapKeyPresenceValidator: MapKeyPresenceValidator,
    private val qualifierPresenceValidator: QualifierPresenceValidator
) {


    fun validate(elements: Set<Element>) {
        val typeElements = elements.castEach<TypeElement>()

        for(typeElement in typeElements) {
            returnTypeValidator.validate(typeElement)
            componentDuplicationValidator.validate(typeElement)
            mapKeyPresenceValidator.validate(typeElement)
            qualifierPresenceValidator.validate(typeElement)
        }
    }


}