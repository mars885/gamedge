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
import com.paulrybitskyi.hiltbinder.processor.model.HiltComponent
import com.paulrybitskyi.hiltbinder.processor.providers.MessageProvider
import com.paulrybitskyi.hiltbinder.processor.utils.HiltBinderException
import com.paulrybitskyi.hiltbinder.processor.utils.getType
import com.paulrybitskyi.hiltbinder.processor.utils.hasAnnotation
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class ComponentDuplicationValidator(
    private val messageProvider: MessageProvider,
    private val elementUtils: Elements,
    private val typeUtils: Types
) {


    fun validate(typeElement: TypeElement) {
        val mainAnnotation = typeElement.getAnnotation(BindType::class.java)
        val hasExplicitComponent = (mainAnnotation.installIn != BindType.Component.NONE)
        val hasAnyScopeAnnotation = HiltComponent.values().any {
            val componentScopeType = elementUtils.getType(it.scopeName)
            val hasScopeAnnotation = typeElement.hasAnnotation(componentScopeType, typeUtils)

            hasScopeAnnotation
        }

        if(hasExplicitComponent && hasAnyScopeAnnotation) {
            throw HiltBinderException(messageProvider.duplicatedComponentError(), typeElement)
        }
    }


}