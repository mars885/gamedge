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

package com.paulrybitskyi.hiltbinder.processor.factories

import com.paulrybitskyi.hiltbinder.processor.model.BindingSchema
import com.paulrybitskyi.hiltbinder.processor.model.HiltComponent
import com.paulrybitskyi.hiltbinder.processor.model.ModuleSchema
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

internal class ModuleSchemaFactory(
    private val fileInterfaceNameFactory: FileInterfaceNameFactory,
    private val elementUtils: Elements
) {


    fun createModuleSchema(
        packageName: String,
        component: HiltComponent,
        bindings: List<BindingSchema>
    ): ModuleSchema {
        return ModuleSchema(
            packageName = packageName,
            interfaceName = fileInterfaceNameFactory.createInterfaceName(component),
            componentType = component.toTypeElement(),
            bindings = bindings
        )
    }


    private fun HiltComponent.toTypeElement(): TypeElement {
        return elementUtils.getTypeElement(typeName)
    }


}