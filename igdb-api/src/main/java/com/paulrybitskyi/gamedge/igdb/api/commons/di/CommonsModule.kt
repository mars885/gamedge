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

package com.paulrybitskyi.gamedge.igdb.api.commons.di

import com.paulrybitskyi.gamedge.commons.api.ErrorMessageExtractor
import com.paulrybitskyi.gamedge.commons.api.addInterceptorAsFirstInChain
import com.paulrybitskyi.gamedge.commons.api.calladapter.ApiResultCallAdapterFactory
import com.paulrybitskyi.gamedge.igdb.api.auth.Authorizer
import com.paulrybitskyi.gamedge.igdb.api.commons.AuthorizationInterceptor
import com.paulrybitskyi.gamedge.igdb.api.commons.TwitchConstantsProvider
import com.paulrybitskyi.gamedge.igdb.api.commons.di.qualifiers.IgdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CommonsModule {


    @Provides
    @Singleton
    @IgdbApi
    fun provideOkHttpClient(
        okHttpClient: OkHttpClient,
        authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptorAsFirstInChain(authorizationInterceptor)
            .build()
    }


    @Provides
    @IgdbApi
    fun provideApiResultCallAdapterFactory(
        @IgdbApi errorMessageExtractor: ErrorMessageExtractor
    ): ApiResultCallAdapterFactory {
        return ApiResultCallAdapterFactory(errorMessageExtractor)
    }


    @Provides
    fun provideAuthorizationInterceptor(
        authorizer: Authorizer,
        twitchConstantsProvider: TwitchConstantsProvider
    ): AuthorizationInterceptor {
        return AuthorizationInterceptor(
            authorizer = authorizer,
            clientId = twitchConstantsProvider.clientId
        )
    }


}