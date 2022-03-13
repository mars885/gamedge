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

package com.paulrybitskyi.gamedge.database

import app.cash.turbine.test
import com.paulrybitskyi.gamedge.commons.testing.DATA_ARTICLES
import com.paulrybitskyi.gamedge.commons.testing.DATA_PAGINATION
import com.paulrybitskyi.gamedge.commons.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.database.articles.datastores.ArticleMapper
import com.paulrybitskyi.gamedge.database.articles.datastores.ArticlesDatabaseDataStore
import com.paulrybitskyi.gamedge.database.articles.datastores.mapToDatabaseArticles
import com.paulrybitskyi.gamedge.database.articles.tables.ArticlesTable
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class ArticlesDatabaseDataStoreTest {

    @MockK private lateinit var articlesTable: ArticlesTable

    private lateinit var articleMapper: ArticleMapper
    private lateinit var SUT: ArticlesDatabaseDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        articleMapper = ArticleMapper()
        SUT = ArticlesDatabaseDataStore(
            articlesTable = articlesTable,
            dispatcherProvider = FakeDispatcherProvider(),
            articleMapper = articleMapper
        )
    }

    @Test
    fun `Saves articles to table successfully`() {
        runTest {
            SUT.saveArticles(DATA_ARTICLES)

            coVerify {
                articlesTable.saveArticles(articleMapper.mapToDatabaseArticles(DATA_ARTICLES))
            }
        }
    }

    @Test
    fun `Emits articles successfully`() {
        runTest {
            val databaseArticles = articleMapper.mapToDatabaseArticles(DATA_ARTICLES)

            coEvery { articlesTable.observeArticles(any(), any()) } returns flowOf(databaseArticles)

            SUT.observeArticles(DATA_PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DATA_ARTICLES)
                awaitComplete()
            }
        }
    }
}
