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

package com.paulrybitskyi.gamedge.gamespot.api.commons

import com.paulrybitskyi.gamedge.gamespot.api.articles.entities.Article
import com.paulrybitskyi.gamedge.gamespot.api.commons.serialization.GamespotFieldsSerializer

internal interface GamespotQueryParamsFactory {

    fun createArticlesQueryParams(
        action: MutableMap<String, String>.() -> Unit
    ): Map<String, String>
}

internal class GamespotQueryParamsFactoryImpl(
    private val gamespotFieldsSerializer: GamespotFieldsSerializer,
    private val apiKey: String
) : GamespotQueryParamsFactory {

    private companion object {

        private const val ARTICLE_FIELD_CATEGORIES = "categories"
        private const val ARTICLE_CATEGORY_ID_GAMES = 18

        private const val COMPLEX_QUERY_VALUE_TEMPLATE = "%s:%s"
    }

    private val articleEntityFields by lazy {
        gamespotFieldsSerializer.serializeFields(Article::class.java)
    }

    override fun createArticlesQueryParams(
        action: MutableMap<String, String>.() -> Unit
    ): Map<String, String> {
        return createGeneralQueryParams {
            put(QUERY_PARAM_FIELDS, articleEntityFields)
            put(
                QUERY_PARAM_FILTER,
                createFilterQueryParamValue(ARTICLE_FIELD_CATEGORIES, ARTICLE_CATEGORY_ID_GAMES)
            )
            put(
                QUERY_PARAM_SORT,
                createSortQueryParamValue(Article.Schema.PUBLICATION_DATE, SortOrder.DESC)
            )

            action()
        }
    }

    private fun createFilterQueryParamValue(field: String, value: Any): String {
        return String.format(COMPLEX_QUERY_VALUE_TEMPLATE, field, value)
    }

    private fun createSortQueryParamValue(field: String, order: SortOrder): String {
        return String.format(COMPLEX_QUERY_VALUE_TEMPLATE, field, order.rawOrder)
    }

    private fun createGeneralQueryParams(
        action: MutableMap<String, String>.() -> Unit
    ): Map<String, String> {
        return buildMap {
            put(QUERY_PARAM_API_KEY, apiKey)
            put(QUERY_PARAM_FORMAT, ResponseFormat.JSON.rawFormat)

            action()
        }
    }
}
