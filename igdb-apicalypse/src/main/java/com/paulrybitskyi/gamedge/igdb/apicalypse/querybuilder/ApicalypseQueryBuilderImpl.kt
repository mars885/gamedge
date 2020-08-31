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

package com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder

import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause.WhereClauseBuilder
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.whereclause.WhereClauseBuilderFactory

internal class ApicalypseQueryBuilderImpl(
    private val whereClauseBuilderFactory: WhereClauseBuilderFactory
) : ApicalypseQueryBuilder {


    private val queryBuilder = StringBuilder()


    override fun search(query: String) = apply {
        queryBuilder.append("search \"$query\";")
    }


    override fun select(fields: String) = apply {
        queryBuilder.append("fields $fields;")
    }


    override fun exclude(fields: String) = apply {
        queryBuilder.append("exclude $fields;")
    }


    override fun where(builder: WhereClauseBuilder.() -> Unit) = apply {
        queryBuilder
            .append("where ")
            .append(whereClauseBuilderFactory.newBuilder().apply(builder).build())
            .append(";")
    }


    override fun offset(offset: Int) = apply {
        queryBuilder.append("offset $offset;")
    }


    override fun limit(limit: Int) = apply {
        queryBuilder.append("limit $limit;")
    }


    override fun sortAsc(field: String) = apply {
        queryBuilder.append("sort $field asc;")
    }


    override fun sortDesc(field: String) = apply {
        queryBuilder.append("sort $field desc;")
    }


    override fun build(): String {
        return queryBuilder.toString()
    }


}