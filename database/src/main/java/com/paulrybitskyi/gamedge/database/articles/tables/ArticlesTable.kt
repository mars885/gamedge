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

package com.paulrybitskyi.gamedge.database.articles.tables

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paulrybitskyi.gamedge.database.articles.entities.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesTable {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticles(articles: List<Article>)

    @Query(
        """
        SELECT * FROM ${Article.Schema.TABLE_NAME}
        ORDER BY ${Article.Schema.PUBLICATION_DATE} DESC
        LIMIT :offset, :limit
        """
    )
    fun observeArticles(offset: Int, limit: Int): Flow<List<Article>>
}
