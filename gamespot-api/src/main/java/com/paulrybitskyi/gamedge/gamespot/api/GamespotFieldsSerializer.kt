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

package com.paulrybitskyi.gamedge.gamespot.api

import com.paulrybitskyi.hiltbinder.BindType
import com.squareup.moshi.Json
import java.lang.reflect.Field


private const val FIELD_SEPARATOR = ", "



internal interface GamespotFieldsSerializer {

    fun serializeFields(clazz: Class<*>): String

}


@BindType
internal class GamespotFieldsSerializerImpl : GamespotFieldsSerializer {


    override fun serializeFields(clazz: Class<*>): String {
        return clazz.getJsonFieldNames().joinToString(FIELD_SEPARATOR)
    }


    private fun Class<*>.getJsonFieldNames(): List<String> {
        return buildList {
            for(declaredField in declaredFields) {
                if(!declaredField.hasJsonAnnotation()) continue

                add(declaredField.getJsonName())
            }
        }
    }


    private fun Field.hasJsonAnnotation(): Boolean {
        return isAnnotationPresent(Json::class.java)
    }


    private fun Field.getJsonName(): String {
        val jsonAnnotation = checkNotNull(getAnnotation(Json::class.java))
        val fieldName = jsonAnnotation.name

        return fieldName
    }


}