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
import com.paulrybitskyi.hiltbinder.processor.model.MAP_KEY_TYPE_CANON_NAME
import com.paulrybitskyi.hiltbinder.processor.providers.MessageProvider
import com.paulrybitskyi.hiltbinder.processor.utils.HiltBinderException
import com.paulrybitskyi.hiltbinder.processor.utils.getType
import com.paulrybitskyi.hiltbinder.processor.utils.hasAnnotation
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class MapKeyPresenceValidator(
    private val messageProvider: MessageProvider,
    private val elementUtils: Elements,
    private val typeUtils: Types
) {


    fun validate(typeElement: TypeElement) {
        val mainAnnotation = typeElement.getAnnotation(BindType::class.java)

        if(mainAnnotation.contributesTo != BindType.Collection.MAP) return

        val mapKeyType = elementUtils.getType(MAP_KEY_TYPE_CANON_NAME)
        val hasMapKeyAnnotation = typeElement.annotationMirrors.any {
            it.annotationType.asElement().hasAnnotation(mapKeyType, typeUtils)
        }

        if(!hasMapKeyAnnotation) {
            throw HiltBinderException(messageProvider.noMapKeyError(), typeElement)
        }
    }


}