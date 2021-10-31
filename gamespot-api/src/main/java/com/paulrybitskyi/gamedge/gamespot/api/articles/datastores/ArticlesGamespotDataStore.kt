package com.paulrybitskyi.gamedge.gamespot.api.articles.datastores

import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.gamedge.commons.api.ApiResult
import com.paulrybitskyi.gamedge.commons.api.ErrorMapper
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.articles.DataArticle
import com.paulrybitskyi.gamedge.data.articles.datastores.ArticlesRemoteDataStore
import com.paulrybitskyi.gamedge.data.commons.DataResult
import com.paulrybitskyi.gamedge.data.commons.Pagination
import com.paulrybitskyi.gamedge.gamespot.api.articles.ApiArticle
import com.paulrybitskyi.gamedge.gamespot.api.articles.ArticlesEndpoint
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class ArticlesGamespotDataStore @Inject constructor(
    private val articlesEndpoint: ArticlesEndpoint,
    private val dispatcherProvider: DispatcherProvider,
    private val articleMapper: ArticleMapper,
    private val errorMapper: ErrorMapper
) : ArticlesRemoteDataStore {


    override suspend fun getArticles(pagination: Pagination): DataResult<List<DataArticle>> {
        return articlesEndpoint
            .getArticles(pagination.offset, pagination.limit)
            .toDataStoreResult()
    }


    private suspend fun ApiResult<List<ApiArticle>>.toDataStoreResult(): DataResult<List<DataArticle>> {
        return withContext(dispatcherProvider.computation) {
            mapEither(articleMapper::mapToDataArticles, errorMapper::mapToDataError)
        }
    }


}
