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

package com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause.operators

import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause.ConditionBuilder
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause.WhereClauseBuilderFactory

internal class OperatorBuilderImpl(
    private val operator: Operator,
    private val whereClauseBuilderFactory: WhereClauseBuilderFactory
) : OperatorBuilder {


    private val conditionsBuilder = StringBuilder()


    override fun conditionBuilders(conditionBuilders: List<ConditionBuilder>) = apply {
        val operatorSymbolSeparator = operator.separator

        for(i in 0 until (conditionBuilders.size - 1)) {
            conditionsBuilder
                .append(conditionBuilders[i].buildWhereClause())
                .append(operatorSymbolSeparator)
        }

        conditionsBuilder.append(conditionBuilders.last().buildWhereClause())
    }


    private fun ConditionBuilder.buildWhereClause(): String {
        return whereClauseBuilderFactory.newBuilder().apply(this).build()
    }


    override fun build(): String {
        return conditionsBuilder.toString()
    }


}