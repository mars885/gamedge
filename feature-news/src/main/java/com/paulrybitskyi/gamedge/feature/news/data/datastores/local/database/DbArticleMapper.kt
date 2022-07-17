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

package com.paulrybitskyi.gamedge.feature.news.data.datastores.local.database

import com.paulrybitskyi.gamedge.database.articles.DatabaseArticle
import com.paulrybitskyi.gamedge.database.articles.DatabaseImageType
import com.paulrybitskyi.gamedge.feature.news.data.DataArticle
import com.paulrybitskyi.gamedge.feature.news.data.DataImageType
import javax.inject.Inject

internal class DbArticleMapper @Inject constructor() {

    fun mapToDatabaseArticle(dataArticle: DataArticle): DatabaseArticle {
        return DatabaseArticle(
            id = dataArticle.id,
            title = dataArticle.title,
            lede = dataArticle.lede,
            imageUrls = dataArticle.imageUrls.toDatabaseImageUrls(),
            publicationDate = dataArticle.publicationDate,
            siteDetailUrl = dataArticle.siteDetailUrl
        )
    }

    private fun Map<DataImageType, String>.toDatabaseImageUrls(): Map<DatabaseImageType, String> {
        return mapKeys {
            DatabaseImageType.valueOf(it.key.name)
        }
    }

    fun mapToDataArticle(databaseArticle: DatabaseArticle): DataArticle {
        return DataArticle(
            id = databaseArticle.id,
            title = databaseArticle.title,
            lede = databaseArticle.lede,
            imageUrls = databaseArticle.imageUrls.toDataImageUrls(),
            publicationDate = databaseArticle.publicationDate,
            siteDetailUrl = databaseArticle.siteDetailUrl
        )
    }

    private fun Map<DatabaseImageType, String>.toDataImageUrls(): Map<DataImageType, String> {
        return mapKeys {
            DataImageType.valueOf(it.key.name)
        }
    }
}

internal fun DbArticleMapper.mapToDatabaseArticles(dataArticles: List<DataArticle>): List<DatabaseArticle> {
    return dataArticles.map(::mapToDatabaseArticle)
}

internal fun DbArticleMapper.mapToDataArticles(databaseArticles: List<DatabaseArticle>): List<DataArticle> {
    return databaseArticles.map(::mapToDataArticle)
}
