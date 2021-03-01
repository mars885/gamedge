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

package com.paulrybitskyi.gamedge.igdb.apicalypse

import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializerFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.Apicalypse
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.ApicalypseClass
import org.junit.Assert.*
import org.junit.Test

internal class ApicalypseSerializerTest {


    private val serializer = ApicalypseSerializerFactory.create()


    @Test
    fun `Serializes simple entity successfully`() {
        @ApicalypseClass
        data class Entity(
            @Apicalypse(name = "field1")
            val field1: Int,
            @Apicalypse(name = "field2")
            val field2: String,
            @Apicalypse(name = "field3")
            val field3: Double,
            @Apicalypse(name = "field4")
            val field4: Float,
            @Apicalypse(name = "field5")
            val field5: String,
            @Apicalypse(name = "field6")
            val field6: Float
        )

        assertEquals(
            "field1, field2, field3, field4, field5, field6",
            serializer.serialize(Entity::class.java)
        )
    }


    @Test(expected = IllegalArgumentException::class)
    fun `Throws exception if entity does not have @ApicalypseClass annotation`() {
        data class Entity(
            val field1: Int,
            val field2: Float
        )

        serializer.serialize(Entity::class.java)
    }


    @Test(expected = IllegalArgumentException::class)
    fun `Throws exception if name of field is empty`() {
        @ApicalypseClass
        data class Entity(
            @Apicalypse(name = "")
            val field1: Int
        )

        serializer.serialize(Entity::class.java)
    }


    @Test(expected = IllegalArgumentException::class)
    fun `Throws exception if name of field is blank`() {
        @ApicalypseClass
        data class Entity(
            @Apicalypse(name = "   ")
            val field1: Int
        )

        serializer.serialize(Entity::class.java)
    }


    @Test
    fun `Does not serialize fields that are not annotated with @Apicalypse annotation`() {
        @ApicalypseClass
        data class Entity(
            @Apicalypse(name = "field1")
            val field1: Int,
            val field2: Float
        )

        assertEquals(
            "field1",
            serializer.serialize(Entity::class.java)
        )
    }


    @Test
    fun `Serializes parent entity that contains child entities successfully`() {
        @ApicalypseClass
        data class Child1(
            @Apicalypse(name = "field1")
            val field1: Int
        )

        @ApicalypseClass
        data class Child2(
            @Apicalypse(name = "field1")
            val field1: Int,
            @Apicalypse(name = "field2")
            val field2: String
        )

        @ApicalypseClass
        data class Child3(
            @Apicalypse(name = "field1")
            val field1: Int,
            @Apicalypse(name = "field2")
            val field2: String,
            @Apicalypse(name = "field3")
            val field3: Double
        )

        @ApicalypseClass
        data class Parent(
            @Apicalypse(name = "parent")
            val parent: Int,
            @Apicalypse(name = "child1")
            val child1: Child1,
            @Apicalypse(name = "child2")
            val child2: Child2,
            @Apicalypse(name = "child3")
            val child3: Child3
        )

        assertEquals(
            "parent, child1.field1, child2.field1, child2.field2, child3.field1, child3.field2, child3.field3",
            serializer.serialize(Parent::class.java)
        )
    }


    @Test
    fun `Serializes parent entity with deeply nested child entities successfully`() {
        @ApicalypseClass
        data class Grandchild1(
            @Apicalypse(name = "field1")
            val field1: Int
        )

        @ApicalypseClass
        data class Grandchild2(
            @Apicalypse(name = "field1")
            val field1: String,
            @Apicalypse(name = "field2")
            val field2: String
        )

        @ApicalypseClass
        data class Grandchild3(
            @Apicalypse(name = "field1")
            val field1: String,
            @Apicalypse(name = "field2")
            val field2: String,
            @Apicalypse(name = "field3")
            val field3: String
        )

        @ApicalypseClass
        data class Child1(
            @Apicalypse(name = "kid1")
            val child1: Grandchild1
        )

        @ApicalypseClass
        data class Child2(
            @Apicalypse(name = "kid1")
            val child1: Grandchild1,
            @Apicalypse(name = "kid2")
            val child2: Grandchild2
        )

        @ApicalypseClass
        data class Child3(
            @Apicalypse(name = "kid1")
            val child1: Grandchild1,
            @Apicalypse(name = "kid2")
            val child2: Grandchild2,
            @Apicalypse(name = "kid3")
            val child3: Grandchild3
        )

        @ApicalypseClass
        data class Parent(
            @Apicalypse(name = "parent")
            val parent: Int,
            @Apicalypse(name = "child1")
            val child1: Child1,
            @Apicalypse(name = "child2")
            val child2: Child2,
            @Apicalypse(name = "child3")
            val child3: Child3
        )

        assertEquals(
            "parent, child1.kid1.field1, child2.kid1.field1, child2.kid2.field1, child2.kid2.field2, " +
            "child3.kid1.field1, child3.kid2.field1, child3.kid2.field2, child3.kid3.field1, " +
            "child3.kid3.field2, child3.kid3.field3",
            serializer.serialize(Parent::class.java)
        )
    }


    @Test
    fun `Serializes entity with list collection type`() {
        @ApicalypseClass
        data class Entity(
            @Apicalypse(name = "field1")
            val field1: Int,
            @Apicalypse(name = "field2")
            val field2: List<String>
        )

        assertEquals(
            "field1, field2",
            serializer.serialize(Entity::class.java)
        )
    }


    @Test
    fun `Serializes entity with set collection type`() {
        @ApicalypseClass
        data class Entity(
            @Apicalypse(name = "field1")
            val field1: Int,
            @Apicalypse(name = "field2")
            val field2: Set<String>
        )

        assertEquals(
            "field1, field2",
            serializer.serialize(Entity::class.java)
        )
    }


    @Test(expected = IllegalStateException::class)
    fun `Throws exception when serializing non collection generic type`() {
        @ApicalypseClass
        data class Entity(
            @Apicalypse(name = "field1")
            val field1: Int,
            @Apicalypse(name = "field2")
            val field2: Map<String, String>
        )

        serializer.serialize(Entity::class.java)
    }


}