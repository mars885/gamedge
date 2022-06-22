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

import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.gamespot.api.commons.serialization.Gamespot
import com.paulrybitskyi.gamedge.gamespot.api.commons.serialization.GamespotFieldsSerializer
import com.paulrybitskyi.gamedge.gamespot.api.commons.serialization.GamespotFieldsSerializerImpl
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

internal class GamespotFieldsSerializerImplTest {

    private lateinit var SUT: GamespotFieldsSerializer

    @Before
    fun setup() {
        SUT = GamespotFieldsSerializerImpl()
    }

    @Test
    fun `Serializes simple entity successfully`() {
        data class Entity(
            @Gamespot("field1")
            val field1: Int,
            @Gamespot("field2")
            val field2: String,
            @Gamespot("field3")
            val field3: Double,
            @Gamespot("field4")
            val field4: Float,
            @Gamespot("field5")
            val field5: String,
            @Gamespot("field6")
            val field6: Float
        )

        assertThat(SUT.serializeFields(Entity::class.java))
            .isEqualTo("field1,field2,field3,field4,field5,field6")
    }

    @Test
    fun `Serializes entity with no annotated fields successfully`() {
        data class Entity(
            val field1: Int,
            val field2: String,
            val field3: Double
        )

        assertThat(SUT.serializeFields(Entity::class.java)).isEmpty()
    }

    @Test
    fun `Throws exception if name of field is empty`() {
        data class Entity(
            @Gamespot("")
            val field1: Int
        )

        assertThrows(IllegalArgumentException::class.java) {
            SUT.serializeFields(Entity::class.java)
        }
    }

    @Test
    fun `Throws exception if name of field is blank`() {
        data class Entity(
            @Gamespot("   ")
            val field1: Int
        )

        assertThrows(IllegalArgumentException::class.java) {
            SUT.serializeFields(Entity::class.java)
        }
    }
}
