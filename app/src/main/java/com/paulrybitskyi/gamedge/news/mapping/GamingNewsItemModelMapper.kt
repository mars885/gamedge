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

package com.paulrybitskyi.gamedge.news.mapping

import com.paulrybitskyi.gamedge.commons.ui.widgets.news.GamingNewsItemModel
import com.paulrybitskyi.gamedge.core.formatters.ArticlePublicationDateFormatter
import com.paulrybitskyi.gamedge.domain.articles.entities.Article
import com.paulrybitskyi.gamedge.domain.articles.entities.ImageType
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface GamingNewsItemModelMapper {

    fun mapToGamingNewsItemModel(article: Article): GamingNewsItemModel

}


@BindType(installIn = BindType.Component.ACTIVITY_RETAINED)
internal class GamingNewsItemModelMapperImpl @Inject constructor(
    private val publicationDateFormatter: ArticlePublicationDateFormatter
): GamingNewsItemModelMapper {


    override fun mapToGamingNewsItemModel(article: Article): GamingNewsItemModel {
        return GamingNewsItemModel(
            id = article.id,
            imageUrl = article.imageUrls[ImageType.ORIGINAL],
            title = article.title,
            lede = article.lede,
            publicationDate = article.formatPublicationDate(),
            siteDetailUrl = article.siteDetailUrl
        )
    }


    private fun Article.formatPublicationDate(): String {
        return publicationDateFormatter.formatPublicationDate(publicationDate)
    }


}