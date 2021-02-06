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

package com.paulrybitskyi.gamedge.gamespot.api.di

import com.paulrybitskyi.gamedge.gamespot.api.*
import com.paulrybitskyi.gamedge.gamespot.api.serialization.ImageTypeAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal object ArticlesEndpointModule {


    @Provides
    fun provideArticlesService(@Endpoint(Endpoint.Type.ARTICLES) retrofit: Retrofit): ArticlesService {
        return retrofit.create(ArticlesService::class.java)
    }


    @Provides
    @Endpoint(Endpoint.Type.ARTICLES)
    fun provideRetrofit(
        retrofitBuilder: Retrofit.Builder,
        @Endpoint(Endpoint.Type.ARTICLES)
        moshi: Moshi
    ): Retrofit {
        return retrofitBuilder
            .baseUrl(Constants.GAMESPOT_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }


    @Provides
    @Endpoint(Endpoint.Type.ARTICLES)
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(ImageTypeAdapter())
            .build()
    }


    @Provides
    fun provideGamespotQueryParamsBuilder(
        gamespotFieldsSerializer: GamespotFieldsSerializer
    ): GamespotQueryParamsBuilder {
        return GamespotQueryParamsBuilderImpl(
            gamespotFieldsSerializer = gamespotFieldsSerializer,
            apiKey = BuildConfig.GAMESPOT_API_KEY
        )
    }


}