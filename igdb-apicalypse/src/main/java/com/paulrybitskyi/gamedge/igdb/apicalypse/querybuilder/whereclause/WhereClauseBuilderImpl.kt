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

import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause.operators.Operator
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause.operators.OperatorBuilderFactory

internal class WhereClauseBuilderImpl(
    private val operatorBuilderFactory: OperatorBuilderFactory
) : WhereClauseBuilder {


    private val clauseBuilder = StringBuilder()

    override val String.isTrue: WhereClauseBuilderImpl
        get() = this@WhereClauseBuilderImpl.apply { clauseBuilder.append("${this@isTrue} = true") }

    override val String.isFalse: WhereClauseBuilderImpl
        get() = this@WhereClauseBuilderImpl.apply { clauseBuilder.append("${this@isFalse} = false") }

    override val String.isNull: WhereClauseBuilderImpl
        get() = this@WhereClauseBuilderImpl.apply { clauseBuilder.append("${this@isNull} = null") }

    override val String.isNotNull: WhereClauseBuilderImpl
        get() = this@WhereClauseBuilderImpl.apply { clauseBuilder.append("${this@isNotNull} != null") }


    override fun String.isEqual(value: String) = this@WhereClauseBuilderImpl.apply {
        clauseBuilder.append("${this@isEqual} = $value")
    }


    override fun String.isNotEqual(value: String) = this@WhereClauseBuilderImpl.apply {
        clauseBuilder.append("${this@isNotEqual} != $value")
    }


    override fun String.isLargerThan(value: String) = this@WhereClauseBuilderImpl.apply {
        clauseBuilder.append("${this@isLargerThan} > $value")
    }


    override fun String.isLargerThanOrEqualTo(value: String) = this@WhereClauseBuilderImpl.apply {
        clauseBuilder.append("${this@isLargerThanOrEqualTo} >= $value")
    }


    override fun String.isSmallerThan(value: String) = this@WhereClauseBuilderImpl.apply {
        clauseBuilder.append("${this@isSmallerThan} < $value")
    }


    override fun String.isSmallerThanOrEqualTo(value: String) = this@WhereClauseBuilderImpl.apply {
        clauseBuilder.append("${this@isSmallerThanOrEqualTo} <= $value")
    }


    override fun String.containsAllOf(values: List<String>) = this@WhereClauseBuilderImpl.apply {
        values.joinToString(prefix = "[", postfix = "]")
            .also { clauseBuilder.append("${this@containsAllOf} = $it") }
    }


    override fun String.doesNotContainAllOf(values: List<String>) = this@WhereClauseBuilderImpl.apply {
        values.joinToString(prefix = "![", postfix = "]")
            .also { clauseBuilder.append("${this@doesNotContainAllOf} = $it") }
    }


    override fun String.containsAnyOf(values: List<String>) = this@WhereClauseBuilderImpl.apply {
        values.joinToString(prefix = "(", postfix = ")")
            .also { clauseBuilder.append("${this@containsAnyOf} = $it") }
    }


    override fun String.doesNotContainAnyOf(values: List<String>) = this@WhereClauseBuilderImpl.apply {
        values.joinToString(prefix = "!(", postfix = ")")
            .also { clauseBuilder.append("${this@doesNotContainAnyOf} = $it") }
    }


    override fun String.containsExclusivelyAllOf(values: List<String>) = this@WhereClauseBuilderImpl.apply {
        values.joinToString(prefix = "{", postfix = "}")
            .also { clauseBuilder.append("${this@containsExclusivelyAllOf} = $it") }
    }


    override fun and(vararg conditionBuilders: ConditionBuilder) = apply {
        clauseBuilder.append(Operator.AND.buildOperator(conditionBuilders.toList()))
    }


    override fun or(vararg conditionBuilders: ConditionBuilder) = apply {
        clauseBuilder.append(Operator.OR.buildOperator(conditionBuilders.toList()))
    }


    private fun Operator.buildOperator(conditionBuilders: List<ConditionBuilder>): String {
        return operatorBuilderFactory.newBuilder(this)
            .apply { conditionBuilders(conditionBuilders) }
            .build()
    }


    override fun build(): String {
        return clauseBuilder.toString()
    }


}