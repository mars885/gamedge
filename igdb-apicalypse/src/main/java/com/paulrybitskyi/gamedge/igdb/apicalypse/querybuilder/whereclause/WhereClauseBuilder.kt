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

package com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause

interface WhereClauseBuilder {

    val String.isTrue: WhereClauseBuilder

    val String.isFalse: WhereClauseBuilder

    val String.isNull: WhereClauseBuilder

    val String.isNotNull: WhereClauseBuilder

    fun String.isEqual(value: String): WhereClauseBuilder

    fun String.isNotEqual(value: String): WhereClauseBuilder

    fun String.isLargerThan(value: String): WhereClauseBuilder

    fun String.isLargerThanOrEqualTo(value: String): WhereClauseBuilder

    fun String.isSmallerThan(value: String): WhereClauseBuilder

    fun String.isSmallerThanOrEqualTo(value: String): WhereClauseBuilder

    fun String.containsAllOf(values: List<String>): WhereClauseBuilder

    fun String.doesNotContainAllOf(values: List<String>): WhereClauseBuilder

    fun String.containsAnyOf(values: List<String>): WhereClauseBuilder

    fun String.doesNotContainAnyOf(values: List<String>): WhereClauseBuilder

    fun String.containsExclusivelyAllOf(values: List<String>): WhereClauseBuilder

    infix fun and(condition: Condition): WhereClauseBuilder

    infix fun or(condition: Condition): WhereClauseBuilder

    fun build(): String

}