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
import com.paulrybitskyi.hiltbinder.processor.ComponentMapper
import com.paulrybitskyi.hiltbinder.processor.model.HiltComponent
import com.paulrybitskyi.hiltbinder.processor.model.WITH_FRAGMENT_BINDINGS_TYPE_CANON_NAME
import com.paulrybitskyi.hiltbinder.processor.utils.getType
import com.paulrybitskyi.hiltbinder.processor.utils.hasAnnotation
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class HiltComponentDetector(
    private val componentMapper: ComponentMapper,
    private val elementUtils: Elements,
    private val typeUtils: Types
) {


    fun detectComponent(typeElement: TypeElement): HiltComponent {
        return detectFromScopeAnnotation(typeElement)
            ?: detectFromMainAnnotation(typeElement.getAnnotation(BindType::class.java))
            ?: returnDefaultComponent()
    }


    private fun detectFromScopeAnnotation(typeElement: TypeElement): HiltComponent? {
        if(hasViewWithFragmentComponent(typeElement)) {
            return HiltComponent.VIEW_WITH_FRAGMENT
        }

        return HiltComponent.values().firstOrNull {
            val componentScopeType = elementUtils.getType(it.scopeName)
            val hasScopeAnnotation = typeElement.hasAnnotation(componentScopeType, typeUtils)

            hasScopeAnnotation
        }
    }


    private fun hasViewWithFragmentComponent(typeElement: TypeElement): Boolean {
        val viewScopeType = elementUtils.getType(HiltComponent.VIEW.scopeName)
        val hasViewScope = typeElement.hasAnnotation(viewScopeType, typeUtils)

        val withFragmentBindingsType = elementUtils.getType(WITH_FRAGMENT_BINDINGS_TYPE_CANON_NAME)
        val hasWithFragmentBindingsType = typeElement.hasAnnotation(withFragmentBindingsType, typeUtils)

        return (hasViewScope && hasWithFragmentBindingsType)
    }


    private fun detectFromMainAnnotation(annotation: BindType): HiltComponent? {
        if(annotation.installIn == BindType.Component.NONE) return null

        return componentMapper.mapToHiltComponent(annotation.installIn)
    }


    private fun returnDefaultComponent(): HiltComponent {
        return HiltComponent.SINGLETON
    }


}