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

package com.paulrybitskyi.gamedge.database.tables

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.paulrybitskyi.gamedge.database.GamedgeDatabase
import com.paulrybitskyi.gamedge.database.articles.DatabaseArticle
import com.paulrybitskyi.gamedge.database.articles.tables.ArticlesTable
import com.paulrybitskyi.gamedge.database.utils.DATABASE_ARTICLES
import com.paulrybitskyi.gamedge.database.utils.createMemoryDb
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class ArticlesTableTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: GamedgeDatabase
    private lateinit var SUT: ArticlesTable


    @Before
    fun setup() {
        db = createMemoryDb()
        SUT = db.articlesTable
    }


    @Test
    fun saves_and_observes_sorted_articles() {
        runBlockingTest {
            SUT.saveArticles(DATABASE_ARTICLES)

            val expectedArticles = DATABASE_ARTICLES.sortedByDescending(DatabaseArticle::publicationDate)

            SUT.observeArticles(offset = 0, limit = DATABASE_ARTICLES.size).test {
                assertThat(expectItem()).isEqualTo(expectedArticles)
            }
        }
    }


    @After
    fun cleanup() {
        db.close()
    }


}