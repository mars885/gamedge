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

package com.paulrybitskyi.hiltbinder.processor.generator

import com.paulrybitskyi.hiltbinder.processor.model.BindingSchema
import com.paulrybitskyi.hiltbinder.processor.model.ContributionType
import com.paulrybitskyi.hiltbinder.processor.model.MapKeyType
import com.paulrybitskyi.hiltbinder.processor.utils.toAnnotationSpec
import com.paulrybitskyi.hiltbinder.processor.utils.toClassName
import com.paulrybitskyi.hiltbinder.processor.utils.toTypeName
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Modifier

internal class BindingMethodSpecFactory {


    fun create(bindingSchemas: List<BindingSchema>): List<MethodSpec> {
        return bindingSchemas.map {
            MethodSpec.methodBuilder(it.methodName)
                .addAnnotation(DAGGER_TYPE_BINDS)
                .addMultibindingAnnotationsIfExist(it.contributionType)
                .addQualifierAnnotationIfExists(it.qualifierAnnotation)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                .addParameter(it.paramType.toClassName(), it.paramName)
                .returns(it.returnType.toTypeName())
                .build()
        }
    }


    private fun MethodSpec.Builder.addMultibindingAnnotationsIfExist(
        contributionType: ContributionType?
    ): MethodSpec.Builder = apply {
        if(contributionType == null) return@apply

        when(contributionType) {
            is ContributionType.Set -> contributeToSet()
            is ContributionType.Map -> contributeToMap(contributionType.keyType)
        }
    }


    private fun MethodSpec.Builder.contributeToSet(): MethodSpec.Builder = apply {
        addAnnotation(DAGGER_TYPE_INTO_SET)
    }


    private fun MethodSpec.Builder.contributeToMap(keyType: MapKeyType): MethodSpec.Builder = apply {
        addAnnotation(DAGGER_TYPE_INTO_MAP)

        when(keyType) {
            is MapKeyType.Standard -> addAnnotation(keyType.value.toDaggerMapKeyAnnotationSpec())
            is MapKeyType.Custom -> addAnnotation(keyType.mirror.toAnnotationSpec())
        }
    }


    private fun MethodSpec.Builder.addQualifierAnnotationIfExists(
        qualifierAnnotation: AnnotationMirror?
    ): MethodSpec.Builder = apply {
        if(qualifierAnnotation == null) return@apply

        addAnnotation(qualifierAnnotation.toAnnotationSpec())
    }


}