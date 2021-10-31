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
import app.cash.turbine.test
import com.paulrybitskyi.gamedge.database.articles.DatabaseArticle
import com.paulrybitskyi.gamedge.database.articles.tables.ArticlesTable
import com.paulrybitskyi.gamedge.database.commons.di.DatabaseModule
import com.paulrybitskyi.gamedge.database.utils.DATABASE_ARTICLES
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
internal class ArticlesTableTest {


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val executorRule = InstantTaskExecutorRule()

    @Inject lateinit var SUT: ArticlesTable


    @Module(includes = [TestDatabaseModule::class])
    @InstallIn(SingletonComponent::class)
    class TestModule


    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun saves_and_observes_sorted_articles() {
        runBlockingTest {
            SUT.saveArticles(DATABASE_ARTICLES)

            val expectedArticles = DATABASE_ARTICLES.sortedByDescending(DatabaseArticle::publicationDate)

            SUT.observeArticles(offset = 0, limit = DATABASE_ARTICLES.size).test {
                assertThat(awaitItem()).isEqualTo(expectedArticles)
            }
        }
    }


}
