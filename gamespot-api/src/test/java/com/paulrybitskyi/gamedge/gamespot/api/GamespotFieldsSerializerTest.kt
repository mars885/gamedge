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

import com.paulrybitskyi.gamedge.gamespot.api.commons.GamespotFieldsSerializer
import com.paulrybitskyi.gamedge.gamespot.api.commons.GamespotFieldsSerializerImpl
import com.squareup.moshi.Json
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class GamespotFieldsSerializerTest {


    private lateinit var serializer: GamespotFieldsSerializer


    @Before
    fun setup() {
        serializer = GamespotFieldsSerializerImpl()
    }


    @Test
    fun `Serializes simple entity successfully`() {
        data class Entity(
            @field:Json(name = "field1")
            val field1: Int,
            @field:Json(name = "field2")
            val field2: String,
            @field:Json(name = "field3")
            val field3: Double,
            @field:Json(name = "field4")
            val field4: Float,
            @field:Json(name = "field5")
            val field5: String,
            @field:Json(name = "field6")
            val field6: Float
        )

        assertEquals(
            "field1,field2,field3,field4,field5,field6",
            serializer.serializeFields(Entity::class.java)
        )
    }


    @Test
    fun `Serializes entity with no annotated fields successfully`() {
        data class Entity(
            val field1: Int,
            val field2: String,
            val field3: Double
        )

        assertEquals("", serializer.serializeFields(Entity::class.java))
    }


    @Test(expected = IllegalArgumentException::class)
    fun `Throws exception if name of field is empty`() {
        data class Entity(
            @field:Json(name = "")
            val field1: Int
        )

        serializer.serializeFields(Entity::class.java)
    }


    @Test(expected = IllegalArgumentException::class)
    fun `Throws exception if name of field is blank`() {
        data class Entity(
            @field:Json(name = "   ")
            val field1: Int
        )

        serializer.serializeFields(Entity::class.java)
    }


}