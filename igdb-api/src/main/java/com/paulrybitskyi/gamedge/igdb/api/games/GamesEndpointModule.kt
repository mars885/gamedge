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

package com.paulrybitskyi.gamedge.igdb.api.games

import com.paulrybitskyi.gamedge.commons.api.calladapter.ApiResultCallAdapterFactory
import com.paulrybitskyi.gamedge.igdb.api.BuildConfig
import com.paulrybitskyi.gamedge.igdb.api.auth.Authorizer
import com.paulrybitskyi.gamedge.igdb.api.commons.di.qualifiers.Endpoint
import com.paulrybitskyi.gamedge.igdb.api.commons.di.qualifiers.IgdbApi
import com.paulrybitskyi.gamedge.igdb.api.games.querybuilder.IgdbApiQueryBuilder
import com.paulrybitskyi.gamedge.igdb.api.games.serialization.*
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.ApicalypseQueryBuilderFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializer
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializerFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object GamesEndpointModule {


    @Provides
    @Singleton
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
    fun provideGamesService(@Endpoint(Endpoint.Type.GAMES) retrofit: Retrofit): GamesService {
        return retrofit.create(GamesService::class.java)
    }


    @Provides
    @Endpoint(Endpoint.Type.GAMES)
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @IgdbApi callAdapterFactory: ApiResultCallAdapterFactory,
        @Endpoint(Endpoint.Type.GAMES) moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Constants.IGDB_API_BASE_URL)
            .build()
    }


    @Provides
    @Endpoint(Endpoint.Type.GAMES)
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(CategoryAdapter())
            .add(AgeRatingCategoryAdapter())
            .add(AgeRatingTypeAdapter())
            .add(ReleaseDateCategoryAdapter())
            .add(WebsiteCategoryAdapter())
            .build()
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