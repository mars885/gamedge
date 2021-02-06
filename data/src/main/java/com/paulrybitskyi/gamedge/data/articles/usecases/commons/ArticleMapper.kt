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

package com.paulrybitskyi.gamedge.data.articles.usecases.commons

import com.paulrybitskyi.gamedge.data.articles.DataArticle
import com.paulrybitskyi.gamedge.data.articles.DataAssociation
import com.paulrybitskyi.gamedge.data.articles.DataImageType
import com.paulrybitskyi.gamedge.domain.articles.DomainArticle
import com.paulrybitskyi.gamedge.domain.articles.DomainAssociation
import com.paulrybitskyi.gamedge.domain.articles.DomainImageType
import javax.inject.Inject


internal class ArticleMapper @Inject constructor() {


    fun mapToDomainArticle(dataArticle: DataArticle): DomainArticle {
        return DomainArticle(
            id = dataArticle.id,
            title = dataArticle.title,
            lede = dataArticle.lede,
            imageUrls = dataArticle.imageUrls.toDomainImageUrls(),
            associations = dataArticle.associations.toDomainAssociations(),
            publishDate = dataArticle.publishDate,
            siteDetailUrl = dataArticle.siteDetailUrl
        )
    }


    private fun Map<DataImageType, String>.toDomainImageUrls(): Map<DomainImageType, String> {
        return mapKeys {
            DomainImageType.valueOf(it.key.name)
        }
    }


    private fun List<DataAssociation>.toDomainAssociations(): List<DomainAssociation> {
        return map {
            DomainAssociation(
                id = it.id,
                name = it.name
            )
        }
    }


    fun mapToDataArticle(domainArticle: DomainArticle): DataArticle {
        return DataArticle(
            id = domainArticle.id,
            title = domainArticle.title,
            lede = domainArticle.lede,
            imageUrls = emptyMap(),
            associations = domainArticle.associations.toDataAssociations(),
            publishDate = domainArticle.publishDate,
            siteDetailUrl = domainArticle.siteDetailUrl
        )
    }


    private fun List<DomainAssociation>.toDataAssociations(): List<DataAssociation> {
        return map {
            DataAssociation(
                id = it.id,
                name = it.name
            )
        }
    }


}


internal fun ArticleMapper.mapToDomainArticles(dataArticles: List<DataArticle>): List<DomainArticle> {
    return dataArticles.map(::mapToDomainArticle)
}