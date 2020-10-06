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

package com.paulrybitskyi.gamedge.igdb.api.games.di

import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.igdb.api.BuildConfig
import com.paulrybitskyi.gamedge.igdb.api.auth.Authorizer
import com.paulrybitskyi.gamedge.igdb.api.commons.di.qualifiers.Endpoint
import com.paulrybitskyi.gamedge.igdb.api.games.Constants
import com.paulrybitskyi.gamedge.igdb.api.games.GamesEndpoint
import com.paulrybitskyi.gamedge.igdb.api.games.GamesEndpointImpl
import com.paulrybitskyi.gamedge.igdb.api.games.GamesService
import com.paulrybitskyi.gamedge.igdb.api.games.querybuilder.IgdbApiQueryBuilder
import com.paulrybitskyi.gamedge.igdb.api.games.querybuilder.IgdbApiQueryBuilderImpl
import com.paulrybitskyi.gamedge.igdb.api.games.serialization.AgeRatingCategoryAdapter
import com.paulrybitskyi.gamedge.igdb.api.games.serialization.AgeRatingTypeAdapter
import com.paulrybitskyi.gamedge.igdb.api.games.serialization.WebsiteCategoryAdapter
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.ApicalypseQueryBuilderFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializer
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializerFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal object GamesEndpointModule {


    @Singleton
    @Provides
    fun provideGamesEndpoint(
        gamesService: GamesService,
        igdbApiQueryBuilder: IgdbApiQueryBuilder,
        authorizer: Authorizer
    ): GamesEndpoint {
        return GamesEndpointImpl(
            gamesService = gamesService,
            igdbApiQueryBuilder = igdbApiQueryBuilder,
            authorizer = authorizer,
            clientId = BuildConfig.TWITCH_APP_CLIENT_ID
        )
    }


    @Provides
    fun provideGamesService(
        @Endpoint(Endpoint.Type.GAMES)
        retrofit: Retrofit
    ): GamesService {
        return retrofit.create(GamesService::class.java)
    }


    @Endpoint(Endpoint.Type.GAMES)
    @Provides
    fun provideRetrofit(
        retrofitBuilder: Retrofit.Builder,
        @Endpoint(Endpoint.Type.GAMES)
        moshi: Moshi
    ): Retrofit {
        return retrofitBuilder
            .baseUrl(Constants.IGDB_API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }


    @Endpoint(Endpoint.Type.GAMES)
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


    @Provides
    fun provideApicalypseSerializer(): ApicalypseSerializer {
        return ApicalypseSerializerFactory.create()
    }


}