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

package com.paulrybitskyi.gamedge.feature.news.data.datastores.local

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.DATA_PAGINATION
import com.paulrybitskyi.gamedge.common.testing.FakeDispatcherProvider
import com.paulrybitskyi.gamedge.database.articles.tables.ArticlesTable
import com.paulrybitskyi.gamedge.feature.news.DATA_ARTICLES
import com.paulrybitskyi.gamedge.feature.news.data.datastores.local.database.ArticlesDatabaseDataStore
import com.paulrybitskyi.gamedge.feature.news.data.datastores.local.database.DbArticleMapper
import com.paulrybitskyi.gamedge.feature.news.data.datastores.local.database.mapToDatabaseArticles
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class ArticlesDatabaseDataStoreTest {

    @MockK private lateinit var articlesTable: ArticlesTable

    private lateinit var dbArticleMapper: DbArticleMapper
    private lateinit var SUT: ArticlesDatabaseDataStore

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        dbArticleMapper = DbArticleMapper()
        SUT = ArticlesDatabaseDataStore(
            articlesTable = articlesTable,
            dispatcherProvider = FakeDispatcherProvider(),
            dbArticleMapper = dbArticleMapper,
        )
    }

    @Test
    fun `Saves articles to table successfully`() {
        runTest {
            SUT.saveArticles(DATA_ARTICLES)

            coVerify {
                articlesTable.saveArticles(dbArticleMapper.mapToDatabaseArticles(DATA_ARTICLES))
            }
        }
    }

    @Test
    fun `Emits articles successfully`() {
        runTest {
            val databaseArticles = dbArticleMapper.mapToDatabaseArticles(DATA_ARTICLES)

            every { articlesTable.observeArticles(any(), any()) } returns flowOf(databaseArticles)

            SUT.observeArticles(DATA_PAGINATION).test {
                assertThat(awaitItem()).isEqualTo(DATA_ARTICLES)
                awaitComplete()
            }
        }
    }
}
