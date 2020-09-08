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

package com.paulrybitskyi.gamedge.igdb.api.di

import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.igdb.api.*
import com.paulrybitskyi.gamedge.igdb.api.Constants
import com.paulrybitskyi.gamedge.igdb.api.IgdbApi
import com.paulrybitskyi.gamedge.igdb.api.IgdbApiImpl
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
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal object ApiModule {


    @Singleton
    @Provides
    fun provideIgdbApi(
        igdbApiService: IgdbApiService,
        igdbApiQueryBuilder: IgdbApiQueryBuilder
    ): IgdbApi {
        return IgdbApiImpl(
            igdbApiService = igdbApiService,
            igdbApiQueryBuilder = igdbApiQueryBuilder
        )
    }


    @Provides
    fun provideIgdbApiService(retrofit: Retrofit): IgdbApiService {
        return retrofit.create(IgdbApiService::class.java)
    }


    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.IGDB_API_BASE_URL + "/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiResultCallAdapterFactory())
            .build()
    }


    @Provides
    fun provideOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor,
        httpLoggingInterceptor: Provider<HttpLoggingInterceptor>
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                addInterceptor(authorizationInterceptor)

                if(BuildConfig.DEBUG) {
                    addInterceptor(httpLoggingInterceptor.get())
                }
            }
            .build()
    }


    @Provides
    fun provideAuthorizationInterceptor(): AuthorizationInterceptor {
        return AuthorizationInterceptor(BuildConfig.IGDB_API_KEY)
    }


    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }
    }


    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(AgeRatingCategoryAdapter())
            .add(AgeRatingTypeAdapter())
            .add(WebsiteCategoryAdapter())
            .build()
    }


    @Provides
    fun provideIgdbApiQueryBuilder(
        apicalypseQueryBuilderFactory: ApicalypseQueryBuilderFactory,
        apicalypseSerializer: ApicalypseSerializer,
        queryTimestampProvider: QueryTimestampProvider
    ): IgdbApiQueryBuilder {
        return IgdbApiQueryBuilderImpl(
            apicalypseQueryBuilderFactory = apicalypseQueryBuilderFactory,
            apicalypseSerializer = apicalypseSerializer,
            queryTimestampProvider = queryTimestampProvider
        )
    }


    @Provides
    fun provideApicalypseQueryBuilderFactory(): ApicalypseQueryBuilderFactory {
        return ApicalypseQueryBuilderFactory
    }


}