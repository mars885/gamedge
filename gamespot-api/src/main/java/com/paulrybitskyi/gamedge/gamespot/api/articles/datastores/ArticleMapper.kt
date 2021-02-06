package com.paulrybitskyi.gamedge.gamespot.api.articles.datastores

import com.paulrybitskyi.gamedge.data.articles.DataArticle
import com.paulrybitskyi.gamedge.data.articles.DataAssociation
import com.paulrybitskyi.gamedge.data.articles.DataImageType
import com.paulrybitskyi.gamedge.gamespot.api.articles.ApiArticle
import com.paulrybitskyi.gamedge.gamespot.api.articles.ApiAssociation
import com.paulrybitskyi.gamedge.gamespot.api.articles.ApiImageType
import javax.inject.Inject


internal class ArticleMapper @Inject constructor(
    private val publishDateMapper: ArticlePublishDateMapper
) {


    fun mapToDataArticle(apiArticle: ApiArticle): DataArticle {
        return DataArticle(
            id = apiArticle.id,
            title = apiArticle.title,
            lede = apiArticle.lede,
            imageUrls = apiArticle.imageUrls.toDataImageUrls(),
            associations = apiArticle.associations.toDataAssociations(),
            publishDate = publishDateMapper.mapToTimestamp(apiArticle.publishDate),
            siteDetailUrl = apiArticle.siteDetailUrl
        )
    }


    private fun Map<ApiImageType, String>.toDataImageUrls(): Map<DataImageType, String> {
        return mapKeys {
            DataImageType.valueOf(it.key.name)
        }
    }


    private fun List<ApiAssociation>.toDataAssociations(): List<DataAssociation> {
        return map {
            DataAssociation(
                id = it.id,
                name = it.name
            )
        }
    }


}


internal fun ArticleMapper.mapToDataArticles(apiArticles: List<ApiArticle>): List<DataArticle> {
    return apiArticles.map(::mapToDataArticle)
}