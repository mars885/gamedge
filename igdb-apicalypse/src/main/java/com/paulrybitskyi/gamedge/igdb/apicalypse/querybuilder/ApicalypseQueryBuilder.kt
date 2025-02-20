/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

interface ApicalypseQueryBuilder {
    fun search(query: String): ApicalypseQueryBuilder
    fun select(fields: String): ApicalypseQueryBuilder
    fun selectAll(): ApicalypseQueryBuilder
    fun exclude(fields: String): ApicalypseQueryBuilder
    fun where(builder: WhereClauseBuilder.() -> Unit): ApicalypseQueryBuilder
    fun offset(offset: Int): ApicalypseQueryBuilder
    fun limit(limit: Int): ApicalypseQueryBuilder
    fun sortAsc(field: String): ApicalypseQueryBuilder
    fun sortDesc(field: String): ApicalypseQueryBuilder
    fun build(): String
}
