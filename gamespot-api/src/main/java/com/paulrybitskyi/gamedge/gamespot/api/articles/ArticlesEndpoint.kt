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

package com.paulrybitskyi.gamedge.gamespot.api.articles

import com.github.michaelbull.result.map
import com.paulrybitskyi.gamedge.commons.api.ApiResult
import com.paulrybitskyi.gamedge.gamespot.api.articles.entities.Article
import com.paulrybitskyi.gamedge.gamespot.api.commons.GamespotQueryParamsBuilder
import com.paulrybitskyi.gamedge.gamespot.api.commons.QUERY_PARAM_LIMIT
import com.paulrybitskyi.gamedge.gamespot.api.commons.QUERY_PARAM_OFFSET
import com.paulrybitskyi.gamedge.gamespot.api.commons.Response
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton


internal interface ArticlesEndpoint {

    suspend fun getArticles(offset: Int, limit: Int): ApiResult<List<Article>>

}


@BindType
@Singleton
internal class ArticlesEndpointImpl @Inject constructor(
    private val articlesService: ArticlesService,
    private val queryParamsBuilder: GamespotQueryParamsBuilder
) : ArticlesEndpoint {


    override suspend fun getArticles(offset: Int, limit: Int): ApiResult<List<Article>> {
        val queryParams = queryParamsBuilder.buildArticlesQueryParams {
            put(QUERY_PARAM_OFFSET, offset.toString())
            put(QUERY_PARAM_LIMIT, limit.toString())
        }

        return articlesService.getArticles(queryParams)
            .map(Response<Article>::results)
    }


}