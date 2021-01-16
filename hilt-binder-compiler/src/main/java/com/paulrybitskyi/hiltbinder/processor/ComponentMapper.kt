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

package com.paulrybitskyi.hiltbinder.processor

import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.hiltbinder.processor.model.HiltComponent

internal class ComponentMapper {


    fun mapToHiltComponent(component: BindType.Component): HiltComponent {
        return when(component) {
            BindType.Component.SINGLETON -> HiltComponent.SINGLETON
            BindType.Component.ACTIVITY_RETAINED -> HiltComponent.ACTIVITY_RETAINED
            BindType.Component.SERVICE -> HiltComponent.SERVICE
            BindType.Component.ACTIVITY -> HiltComponent.ACTIVITY
            BindType.Component.VIEW_MODEL -> HiltComponent.VIEW_MODEL
            BindType.Component.FRAGMENT -> HiltComponent.FRAGMENT
            BindType.Component.VIEW -> HiltComponent.VIEW
            BindType.Component.VIEW_WITH_FRAGMENT -> HiltComponent.VIEW_WITH_FRAGMENT

            else -> throw IllegalArgumentException(
                "Cannot map the component ${component::class.qualifiedName} to a Hilt one."
            )
        }
    }


}