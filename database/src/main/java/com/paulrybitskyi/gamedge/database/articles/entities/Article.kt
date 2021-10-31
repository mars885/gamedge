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

package com.paulrybitskyi.gamedge.database.articles.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = Article.Schema.TABLE_NAME,
    primaryKeys = [Article.Schema.ID],
    indices = [
        Index(Article.Schema.PUBLICATION_DATE)
    ]
)
internal data class Article(
    @ColumnInfo(name = Schema.ID) val id: Int,
    @ColumnInfo(name = Schema.TITLE) val title: String,
    @ColumnInfo(name = Schema.LEDE) val lede: String,
    @ColumnInfo(name = Schema.IMAGE_URLS) val imageUrls: Map<ImageType, String>,
    @ColumnInfo(name = Schema.PUBLICATION_DATE) val publicationDate: Long,
    @ColumnInfo(name = Schema.SITE_DETAIL_URL) val siteDetailUrl: String
) {


    object Schema {

        const val TABLE_NAME = "articles"
        const val ID = "id"
        const val TITLE = "title"
        const val LEDE = "lede"
        const val IMAGE_URLS = "image_urls"
        const val PUBLICATION_DATE = "publication_date"
        const val SITE_DETAIL_URL = "site_detail_url"

    }


}
