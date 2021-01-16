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
import com.paulrybitskyi.hiltbinder.processor.model.OBJECT_TYPE_CANON_NAME
import com.paulrybitskyi.hiltbinder.processor.model.VOID_TYPE_CANON_NAME
import com.paulrybitskyi.hiltbinder.processor.providers.MessageProvider
import com.paulrybitskyi.hiltbinder.processor.utils.HiltBinderException
import com.paulrybitskyi.hiltbinder.processor.utils.asTypeElement
import com.paulrybitskyi.hiltbinder.processor.utils.getType
import com.paulrybitskyi.hiltbinder.processor.utils.getTypeSafely
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class ReturnTypeValidator(
    private val messageProvider: MessageProvider,
    private val elementUtils: Elements,
    private val typeUtils: Types
) {


    fun validate(typeElement: TypeElement) {
        val mainAnnotation = typeElement.getAnnotation(BindType::class.java)
        val bindingType = typeElement.asType()
        val returnType = (mainAnnotation.fetchReturnType() ?: typeElement.deduceReturnType())

        checkSubtypeRelation(bindingType, returnType)
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

        if(hasSuperclass && !hasInterfaces) return superclass
        if(!hasSuperclass && (interfaces.size == 1)) return interfaces.single()

        throw HiltBinderException(messageProvider.undefinedReturnTypeError(), this)
    }


    private fun checkSubtypeRelation(bindingType: TypeMirror, returnType: TypeMirror) {
        if(typeUtils.isSubtype(bindingType, returnType)) return

        val bindingTypeElement = typeUtils.asTypeElement(bindingType)
        val bindingTypeName = bindingTypeElement.qualifiedName.toString()
        val returnTypeName = typeUtils.asTypeElement(returnType).qualifiedName.toString()
        val errorMessage = messageProvider.noSubtypeRelationError(
            bindingTypeName = bindingTypeName,
            returnTypeName = returnTypeName
        )

        throw HiltBinderException(errorMessage, bindingTypeElement)
    }


}