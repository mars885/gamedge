/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.igdb.api

import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProviderFactory
import com.paulrybitskyi.gamedge.igdb.api.adapters.AgeRatingCategoryAdapter
import com.paulrybitskyi.gamedge.igdb.api.adapters.AgeRatingTypeAdapter
import com.paulrybitskyi.gamedge.igdb.api.adapters.WebsiteCategoryAdapter
import com.paulrybitskyi.gamedge.igdb.api.querybuilder.IgdbApiQueryBuilder
import com.paulrybitskyi.gamedge.igdb.api.querybuilder.IgdbApiQueryBuilderImpl
import com.paulrybitskyi.gamedge.igdb.api.services.IgdbApiService
import com.paulrybitskyi.gamedge.igdb.api.utils.AuthorizationInterceptor
import com.paulrybitskyi.gamedge.igdb.api.utils.calladapter.ApiResultCallAdapterFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.ApicalypseQueryBuilderFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializer
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializerFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

internal object IgdbApiFactory {


    fun createIgdbApi(): IgdbApi {
        return IgdbApiImpl(
            igdbApiService = createIgdbApiService(),
            igdbApiQueryBuilder = createIgdbApiQueryBuilder()
        )
    }


    private fun createIgdbApiService(): IgdbApiService {
        return createRetrofit().create(IgdbApiService::class.java)
    }


    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(createOkHttpClient())
            .baseUrl(Constants.IGDB_API_BASE_URL + "/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(createMoshi()))
            .addCallAdapterFactory(ApiResultCallAdapterFactory())
            .build()
    }


    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                addInterceptor(createAuthorizationInterceptor())

                if(BuildConfig.DEBUG) {
                    addInterceptor(createHttpLoggingInterceptor())
                }
            }
            .build()
    }


    private fun createAuthorizationInterceptor(): AuthorizationInterceptor {
        return AuthorizationInterceptor(BuildConfig.IGDB_API_KEY)
    }


    private fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }
    }


    private fun createMoshi(): Moshi {
        return Moshi.Builder()
            .add(AgeRatingCategoryAdapter())
            .add(AgeRatingTypeAdapter())
            .add(WebsiteCategoryAdapter())
            .build()
    }


    private fun createIgdbApiQueryBuilder(): IgdbApiQueryBuilder {
        return IgdbApiQueryBuilderImpl(
            apicalypseQueryBuilderFactory = createApicalypseQueryBuilderFactory(),
            apicalypseSerializer = createApicalypseSerializer(),
            queryTimestampProvider = createQueryTimestampProvider()
        )
    }


    private fun createApicalypseQueryBuilderFactory(): ApicalypseQueryBuilderFactory {
        return ApicalypseQueryBuilderFactory()
    }


    private fun createApicalypseSerializer(): ApicalypseSerializer {
        return ApicalypseSerializerFactory().create()
    }


    private fun createQueryTimestampProvider(): QueryTimestampProvider {
        return QueryTimestampProviderFactory.create()
    }


}