/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.feature.news

import com.paulrybitskyi.gamedge.common.testing.DOMAIN_PAGINATION
import com.paulrybitskyi.gamedge.feature.news.data.DataArticle
import com.paulrybitskyi.gamedge.feature.news.domain.DomainArticle
import com.paulrybitskyi.gamedge.feature.news.domain.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.feature.news.domain.usecases.RefreshArticlesUseCase

internal val DOMAIN_ARTICLE = DomainArticle(
    id = 1,
    title = "title",
    lede = "lede",
    imageUrls = emptyMap(),
    publicationDate = 500L,
    siteDetailUrl = "site_detail_url",
)
internal val DOMAIN_ARTICLES = listOf(
    DOMAIN_ARTICLE.copy(id = 1),
    DOMAIN_ARTICLE.copy(id = 2),
    DOMAIN_ARTICLE.copy(id = 3),
)
internal val DATA_ARTICLE = DataArticle(
    id = 1,
    title = "title",
    lede = "lede",
    imageUrls = emptyMap(),
    publicationDate = 500L,
    siteDetailUrl = "site_detail_url",
)
internal val DATA_ARTICLES = listOf(
    DATA_ARTICLE.copy(id = 1),
    DATA_ARTICLE.copy(id = 2),
    DATA_ARTICLE.copy(id = 3),
)

internal val OBSERVE_ARTICLES_USE_CASE_PARAMS = ObserveArticlesUseCase.Params(DOMAIN_PAGINATION)
internal val REFRESH_ARTICLES_USE_CASE_PARAMS = RefreshArticlesUseCase.Params()
