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

package com.paulrybitskyi.gamedge.data.articles.usecases.commons.throttling

import com.paulrybitskyi.gamedge.domain.games.commons.Pagination
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface ArticlesRefreshingThrottlerKeyBuilder {

    fun buildArticlesKey(pagination: Pagination): String

}


@BindType
internal class ArticlesRefreshingThrottlerKeyBuilderImpl @Inject constructor() : ArticlesRefreshingThrottlerKeyBuilder {


    override fun buildArticlesKey(pagination: Pagination): String {
        return "articles | offset: ${pagination.offset} | limit: ${pagination.limit}"
    }


}