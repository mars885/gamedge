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

import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause.conditions.ConditionBuilderFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause.conditions.ConditionType

internal class WhereClauseBuilderImpl(
    private val conditionBuilderFactory: ConditionBuilderFactory
) : WhereClauseBuilder {


    private val clauseBuilder = StringBuilder()

    override val String.isTrue: WhereClauseBuilder
        get() {
            clauseBuilder.append("$this = true")
            return this@WhereClauseBuilderImpl
        }

    override val String.isFalse: WhereClauseBuilder
        get() {
            clauseBuilder.append("$this = false")
            return this@WhereClauseBuilderImpl
        }

    override val String.isNull: WhereClauseBuilder
        get() {
            clauseBuilder.append("$this = null")
            return this@WhereClauseBuilderImpl
        }

    override val String.isNotNull: WhereClauseBuilder
        get() {
            clauseBuilder.append("$this != null")
            return this@WhereClauseBuilderImpl
        }


    override fun String.isEqual(value: String): WhereClauseBuilder {
        clauseBuilder.append("$this = $value")
        return this@WhereClauseBuilderImpl
    }


    override fun String.isNotEqual(value: String): WhereClauseBuilder {
        clauseBuilder.append("$this != $value")
        return this@WhereClauseBuilderImpl
    }


    override fun String.isLargerThan(value: String): WhereClauseBuilder {
        clauseBuilder.append("$this > $value")
        return this@WhereClauseBuilderImpl
    }


    override fun String.isLargerThanOrEqualTo(value: String): WhereClauseBuilder {
        clauseBuilder.append("$this >= $value")
        return this@WhereClauseBuilderImpl
    }


    override fun String.isSmallerThan(value: String): WhereClauseBuilder {
        clauseBuilder.append("$this < $value")
        return this@WhereClauseBuilderImpl
    }


    override fun String.isSmallerThanOrEqualTo(value: String): WhereClauseBuilder {
        clauseBuilder.append("$this <= $value")
        return this@WhereClauseBuilderImpl
    }


    override fun String.containsAllOf(values: List<String>): WhereClauseBuilder {
        clauseBuilder.append("$this = ${values.joinToString(prefix = "[", postfix = "]")}")
        return this@WhereClauseBuilderImpl
    }


    override fun String.doesNotContainAllOf(values: List<String>): WhereClauseBuilder {
        clauseBuilder.append("$this = ${values.joinToString(prefix = "![", postfix = "]")}")
        return this@WhereClauseBuilderImpl
    }


    override fun String.containsAnyOf(values: List<String>): WhereClauseBuilder {
        clauseBuilder.append("$this = ${values.joinToString(prefix = "(", postfix = ")")}")
        return this@WhereClauseBuilderImpl
    }


    override fun String.doesNotContainAnyOf(values: List<String>): WhereClauseBuilder {
        clauseBuilder.append("$this = ${values.joinToString(prefix = "!(", postfix = ")")}")
        return this@WhereClauseBuilderImpl
    }


    override fun String.containsExclusivelyAllOf(values: List<String>): WhereClauseBuilder {
        clauseBuilder.append("$this = ${values.joinToString(prefix = "{", postfix = "}")}")
        return this@WhereClauseBuilderImpl
    }


    override infix fun and(condition: Condition) = apply {
        clauseBuilder.append(ConditionType.AND.buildCondition(condition))
    }


    override infix fun or(condition: Condition) = apply {
        clauseBuilder.append(ConditionType.OR.buildCondition(condition))
    }


    private fun ConditionType.buildCondition(condition: Condition): String {
        return conditionBuilderFactory.newBuilder(this)
            .condition(condition)
            .build()
    }


    override fun build(): String {
        return clauseBuilder.toString()
    }


}
