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

package com.paulrybitskyi.gamedge.igdb.api.auth

import com.paulrybitskyi.gamedge.common.api.asConverterFactory
import com.paulrybitskyi.gamedge.common.api.calladapter.ApiResultCallAdapterFactory
import com.paulrybitskyi.gamedge.igdb.api.common.TwitchConstantsProvider
import com.paulrybitskyi.gamedge.igdb.api.common.di.qualifiers.Endpoint
import com.paulrybitskyi.gamedge.igdb.api.common.di.qualifiers.IgdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal object AuthEndpointModule {

    @Provides
    @Singleton
    fun provideAuthEndpoint(
        authService: AuthService,
        twitchConstantsProvider: TwitchConstantsProvider
    ): AuthEndpoint {
        return AuthEndpointImpl(
            authService = authService,
            clientId = twitchConstantsProvider.clientId,
            clientSecret = twitchConstantsProvider.clientSecret
        )
    }

    @Provides
    fun provideAuthService(@Endpoint(Endpoint.Type.AUTH) retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Endpoint(Endpoint.Type.AUTH)
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @IgdbApi callAdapterFactory: ApiResultCallAdapterFactory,
        json: Json,
        twitchConstantsProvider: TwitchConstantsProvider
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(json.asConverterFactory())
            .baseUrl(twitchConstantsProvider.apiBaseUrl)
            .build()
    }
}
