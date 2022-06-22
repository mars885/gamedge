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

package com.paulrybitskyi.gamedge.gamespot.api

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.commons.api.ErrorMapper
import com.paulrybitskyi.gamedge.commons.testing.API_ERROR_HTTP
import com.paulrybitskyi.gamedge.commons.testing.API_ERROR_NETWORK
import com.paulrybitskyi.gamedge.commons.testing.API_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.commons.testing.DATA_PAGINATION
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.gamespot.api.articles.ApiArticle
import com.paulrybitskyi.gamedge.gamespot.api.articles.ArticlesEndpoint
import com.paulrybitskyi.gamedge.gamespot.api.articles.datastores.ArticleMapper
import com.paulrybitskyi.gamedge.gamespot.api.articles.datastores.ArticlePublicationDateMapper
import com.paulrybitskyi.gamedge.gamespot.api.articles.datastores.ArticlesGamespotDataStore
import com.paulrybitskyi.gamedge.gamespot.api.articles.datastores.mapToDataArticles
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val API_ARTICLES = listOf(
    ApiArticle(publicationDate = "2020-03-02 12:14:16"),
    ApiArticle(publicationDate = "2020-03-02 12:14:16"),
    ApiArticle(publicationDate = "2020-03-02 12:14:16")
)

internal class ArticlesGamespotDataStoreTest {

    @MockK private lateinit var articlesEndpoint: ArticlesEndpoint

    private lateinit var articleMapper: ArticleMapper
    private lateinit var errorMapper: ErrorMapper
    private lateinit var SUT: ArticlesGamespotDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        articleMapper = ArticleMapper(ArticlePublicationDateMapper())
        errorMapper = ErrorMapper()
        SUT = ArticlesGamespotDataStore(
            articlesEndpoint = articlesEndpoint,
            dispatcherProvider = FakeDispatcherProvider(),
            articleMapper = articleMapper,
            errorMapper = errorMapper
        )
    }

    @Test
    fun `Returns articles successfully`() {
        runTest {
            coEvery { articlesEndpoint.getArticles(any(), any()) } returns Ok(API_ARTICLES)

            val result = SUT.getArticles(DATA_PAGINATION)

            assertThat(result.get())
                .isEqualTo(articleMapper.mapToDataArticles(API_ARTICLES))
        }
    }

    @Test
    fun `Returns http error when fetching articles`() {
        runTest {
            coEvery { articlesEndpoint.getArticles(any(), any()) } returns Err(API_ERROR_HTTP)

            val result = SUT.getArticles(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_HTTP))
        }
    }

    @Test
    fun `Returns network error when fetching articles`() {
        runTest {
            coEvery { articlesEndpoint.getArticles(any(), any()) } returns Err(API_ERROR_NETWORK)

            val result = SUT.getArticles(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_NETWORK))
        }
    }

    @Test
    fun `Returns unknown error when fetching articles`() {
        runTest {
            coEvery { articlesEndpoint.getArticles(any(), any()) } returns Err(API_ERROR_UNKNOWN)

            val result = SUT.getArticles(DATA_PAGINATION)

            assertThat(result.getError())
                .isEqualTo(errorMapper.mapToDataError(API_ERROR_UNKNOWN))
        }
    }
}
