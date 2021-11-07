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

package com.paulrybitskyi.gamedge.gamespot.api.commons.serialization

import com.paulrybitskyi.hiltbinder.BindType
import java.lang.reflect.Field
import javax.inject.Inject

private const val FIELD_SEPARATOR = ","

internal interface GamespotFieldsSerializer {
    fun serializeFields(clazz: Class<*>): String
}

@BindType
internal class GamespotFieldsSerializerImpl @Inject constructor() : GamespotFieldsSerializer {

    override fun serializeFields(clazz: Class<*>): String {
        return clazz.getGamespotFieldNames().joinToString(FIELD_SEPARATOR)
    }

    private fun Class<*>.getGamespotFieldNames(): List<String> {
        return declaredFields
            .filter { it.hasGamespotAnnotation() }
            .map { it.getGamespotName() }
    }

    private fun Field.hasGamespotAnnotation(): Boolean {
        return isAnnotationPresent(Gamespot::class.java)
    }

    private fun Field.getGamespotName(): String {
        val annotation = checkNotNull(getAnnotation(Gamespot::class.java))
        val fieldName = annotation.value

        if (fieldName.isBlank()) {
            throw IllegalArgumentException(
                "The field \"${name}\" of the class \"${declaringClass.simpleName}\" " +
                "is annotated with an invalid name \"$fieldName\""
            )
        }

        return fieldName
    }
}
