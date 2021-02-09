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

package com.paulrybitskyi.gamedge.database.articles.datastores

import com.paulrybitskyi.gamedge.data.articles.DataArticle
import com.paulrybitskyi.gamedge.database.articles.DatabaseArticle
import com.paulrybitskyi.gamedge.database.commons.JsonConverter
import javax.inject.Inject


internal class ArticleMapper @Inject constructor(private val jsonConverter: JsonConverter) {


    fun mapToDatabaseArticle(dataArticle: DataArticle): DatabaseArticle {
        return DatabaseArticle(
            id = dataArticle.id,
            title = dataArticle.title,
            lede = dataArticle.lede,
            imageUrls = dataArticle.imageUrls.toJson(),
            publicationDate = dataArticle.publicationDate,
            siteDetailUrl = dataArticle.siteDetailUrl
        )
    }


    fun mapToDataArticle(databaseArticle: DatabaseArticle): DataArticle {
        return DataArticle(
            id = databaseArticle.id,
            title = databaseArticle.title,
            lede = databaseArticle.lede,
            imageUrls = databaseArticle.imageUrls.fromJsonMap(),
            publicationDate = databaseArticle.publicationDate,
            siteDetailUrl = databaseArticle.siteDetailUrl
        )
    }


    private inline fun <reified T> T.toJson(): String {
        return jsonConverter.toJson(this)
    }


    private inline fun <reified K, reified V> String.fromJsonMap(): Map<K, V> {
        return (jsonConverter.fromJsonMap(this) ?: emptyMap())
    }


}


internal fun ArticleMapper.mapToDatabaseArticles(dataArticles: List<DataArticle>): List<DatabaseArticle> {
    return dataArticles.map(::mapToDatabaseArticle)
}


internal fun ArticleMapper.mapToDataArticles(databaseArticles: List<DatabaseArticle>): List<DataArticle> {
    return databaseArticles.map(::mapToDataArticle)
}