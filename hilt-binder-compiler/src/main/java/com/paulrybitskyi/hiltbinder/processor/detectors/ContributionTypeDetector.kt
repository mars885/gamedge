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
import com.paulrybitskyi.hiltbinder.keys.MapClassKey
import com.paulrybitskyi.hiltbinder.keys.MapIntKey
import com.paulrybitskyi.hiltbinder.keys.MapLongKey
import com.paulrybitskyi.hiltbinder.keys.MapStringKey
import com.paulrybitskyi.hiltbinder.processor.model.ContributionType
import com.paulrybitskyi.hiltbinder.processor.model.MAP_KEY_TYPE_CANON_NAME
import com.paulrybitskyi.hiltbinder.processor.model.MapKeyType
import com.paulrybitskyi.hiltbinder.processor.model.StandardValue
import com.paulrybitskyi.hiltbinder.processor.utils.asTypeElement
import com.paulrybitskyi.hiltbinder.processor.utils.getType
import com.paulrybitskyi.hiltbinder.processor.utils.getTypeSafely
import com.paulrybitskyi.hiltbinder.processor.utils.hasAnnotation
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.KClass

internal class ContributionTypeDetector(
    private val elementUtils: Elements,
    private val typeUtils: Types
) {


    fun detectType(typeElement: TypeElement): ContributionType? {
        val mainAnnotation = typeElement.getAnnotation(BindType::class.java)

        return when(mainAnnotation.contributesTo) {
            BindType.Collection.NONE -> null
            BindType.Collection.SET -> ContributionType.Set
            BindType.Collection.MAP -> typeElement.createMapContributionType()
        }
    }


    private fun TypeElement.createMapContributionType(): ContributionType {
        val daggerMapKeyType = elementUtils.getType(MAP_KEY_TYPE_CANON_NAME)
        val mapKeyAnnotation = annotationMirrors.first {
            it.annotationType.asElement().hasAnnotation(daggerMapKeyType, typeUtils)
        }
        val keyType = (createStandardKeyType(mapKeyAnnotation) ?: MapKeyType.Custom(mapKeyAnnotation))

        return ContributionType.Map(keyType)
    }


    private fun TypeElement.createStandardKeyType(mapKeyAnnotation: AnnotationMirror): MapKeyType? {
        val mapKeyAnnotationType = mapKeyAnnotation.annotationType

        return when {
            mapKeyAnnotationType.isOfKeyType(MapIntKey::class) -> createIntValue()
            mapKeyAnnotationType.isOfKeyType(MapLongKey::class) -> createLongValue()
            mapKeyAnnotationType.isOfKeyType(MapStringKey::class) -> createStringValue()
            mapKeyAnnotationType.isOfKeyType(MapClassKey::class) -> createClassValue()
            else -> null
        }
        ?.let(MapKeyType::Standard)
    }


    private fun TypeMirror.isOfKeyType(clazz: KClass<*>): Boolean {
        return typeUtils.isSameType(this, elementUtils.getType(clazz.java.canonicalName))
    }


    private fun TypeElement.createIntValue(): StandardValue {
        return StandardValue.Int(getAnnotation(MapIntKey::class.java).value)
    }


    private fun TypeElement.createLongValue(): StandardValue {
        return StandardValue.Long(getAnnotation(MapLongKey::class.java).value)
    }


    private fun TypeElement.createStringValue(): StandardValue {
        return StandardValue.String(getAnnotation(MapStringKey::class.java).value)
    }


    private fun TypeElement.createClassValue(): StandardValue {
        val annotation = getAnnotation(MapClassKey::class.java)
        val classType = elementUtils.getTypeSafely(annotation::value)
        val typeElement = typeUtils.asTypeElement(classType)

        return StandardValue.Class(typeElement)
    }


}