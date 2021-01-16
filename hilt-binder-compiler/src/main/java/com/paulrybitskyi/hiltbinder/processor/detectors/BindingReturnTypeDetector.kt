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

package com.paulrybitskyi.hiltbinder.processor.detectors

import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.hiltbinder.processor.model.OBJECT_TYPE_CANON_NAME
import com.paulrybitskyi.hiltbinder.processor.model.VOID_TYPE_CANON_NAME
import com.paulrybitskyi.hiltbinder.processor.utils.getType
import com.paulrybitskyi.hiltbinder.processor.utils.getTypeSafely
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class BindingReturnTypeDetector(
    private val elementUtils: Elements,
    private val typeUtils: Types
) {


    fun detectReturnType(typeElement: TypeElement): TypeMirror {
        val mainAnnotation = typeElement.getAnnotation(BindType::class.java)
        val returnType = (mainAnnotation.fetchReturnType() ?: typeElement.deduceReturnType())

        return returnType
    }


    private fun BindType.fetchReturnType(): TypeMirror? {
        return (if(hasReturnType()) elementUtils.getTypeSafely(::to) else null)
    }


    private fun BindType.hasReturnType(): Boolean {
        val toFieldType = elementUtils.getTypeSafely(::to)
        val voidType = elementUtils.getType(VOID_TYPE_CANON_NAME)

        return !typeUtils.isSameType(toFieldType, voidType)
    }


    private fun TypeElement.deduceReturnType(): TypeMirror {
        val hasSuperclass = (superclass != elementUtils.getType(OBJECT_TYPE_CANON_NAME))
        val hasInterfaces = interfaces.isNotEmpty()

        return (if(hasSuperclass && !hasInterfaces) superclass else interfaces.single())
    }


}