/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.igdb.apicalypse.serialization

import com.paulrybitskyi.gamedge.igdb.apicalypse.Constants
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.Apicalypse
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.ApicalypseClass
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.fieldserializers.FieldSerializer
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.fieldserializers.FieldSerializerFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.fieldserializers.concrete.StubFieldSerializer
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

internal class ApicalypseSerializerImpl : ApicalypseSerializer {


    override fun serialize(clazz: Class<*>): String {
        return clazz
            .fieldSerializers()
            .joinToString(
                Constants.FIELD_SEPARATOR,
                transform = FieldSerializer::serialize
            )
    }


    private fun Class<*>.fieldSerializers(parentName: String = ""): List<FieldSerializer> {
        if(!shouldBeSerialized()) return listOf()

        return buildList {
            for(field in declaredFields) {
                add(field.serializer(parentName))
            }
        }
    }


    private fun Class<*>.shouldBeSerialized(): Boolean {
        return isAnnotationPresent(ApicalypseClass::class.java)
    }


    private fun Field.serializer(parentName: String): FieldSerializer {
        if(!shouldBeSerialized()) return StubFieldSerializer

        val apicalypseAnnotation = checkNotNull(getAnnotation(Apicalypse::class.java))
        val fieldName = apicalypseAnnotation.name
        val childSerializers = childSerializers(parentName = fieldName)

        return FieldSerializerFactory.create(parentName, fieldName, childSerializers)
    }


    private fun Field.shouldBeSerialized(): Boolean {
        return isAnnotationPresent(Apicalypse::class.java)
    }


    private fun Field.childSerializers(parentName: String): List<FieldSerializer> {
        if(type.typeParameters.isEmpty()) return type.fieldSerializers(parentName)
        if(type.typeParameters.size > 1) return listOf()

        val parameterizedType = (genericType as ParameterizedType)
        val typeArgument = parameterizedType.actualTypeArguments.first()
        val typeClass = (typeArgument as Class<*>)

        return typeClass.fieldSerializers(parentName)
    }


}