/*
 * Copyright 2021 Paul Rybitskyi, oss@paulrybitskyi.com
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

import com.paulrybitskyi.gamedge.common.api.ApiResult
import com.paulrybitskyi.gamedge.gamespot.api.articles.entities.ApiArticle
import com.paulrybitskyi.gamedge.gamespot.api.common.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

internal interface ArticlesService {

    @GET("articles")
    suspend fun getArticles(
        @QueryMap queryParams: Map<String, String>,
    ): ApiResult<Response<ApiArticle>>
}
