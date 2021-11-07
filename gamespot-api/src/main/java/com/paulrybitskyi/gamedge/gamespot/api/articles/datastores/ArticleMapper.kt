package com.paulrybitskyi.gamedge.gamespot.api.articles.datastores

import com.paulrybitskyi.gamedge.data.articles.DataArticle
import com.paulrybitskyi.gamedge.data.articles.DataImageType
import com.paulrybitskyi.gamedge.gamespot.api.articles.ApiArticle
import com.paulrybitskyi.gamedge.gamespot.api.articles.ApiImageType
import javax.inject.Inject

internal class ArticleMapper @Inject constructor(
    private val publicationDateMapper: ArticlePublicationDateMapper
) {

    fun mapToDataArticle(apiArticle: ApiArticle): DataArticle {
        return DataArticle(
            id = apiArticle.id,
            title = apiArticle.title,
            lede = apiArticle.lede,
            imageUrls = apiArticle.imageUrls.toDataImageUrls(),
            publicationDate = publicationDateMapper.mapToTimestamp(apiArticle.publicationDate),
            siteDetailUrl = apiArticle.siteDetailUrl
        )
    }

    private fun Map<ApiImageType, String>.toDataImageUrls(): Map<DataImageType, String> {
        return mapKeys {
            DataImageType.valueOf(it.key.name)
        }
    }
}

internal fun ArticleMapper.mapToDataArticles(apiArticles: List<ApiArticle>): List<DataArticle> {
    return apiArticles.map(::mapToDataArticle)
}
