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

package com.paulrybitskyi.gamedge.gamespot.api.common.di

import com.paulrybitskyi.gamedge.common.api.ErrorMessageExtractor
import com.paulrybitskyi.gamedge.common.api.addInterceptorAsFirstInChain
import com.paulrybitskyi.gamedge.common.api.calladapter.ApiResultCallAdapterFactory
import com.paulrybitskyi.gamedge.gamespot.api.common.GamespotConstantsProvider
import com.paulrybitskyi.gamedge.gamespot.api.common.GamespotQueryParamsFactory
import com.paulrybitskyi.gamedge.gamespot.api.common.GamespotQueryParamsFactoryImpl
import com.paulrybitskyi.gamedge.gamespot.api.common.UserAgentInterceptor
import com.paulrybitskyi.gamedge.gamespot.api.common.serialization.GamespotFieldsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
internal object CommonsModule {

    @Provides
    @Singleton
    @GamespotApi
    fun provideOkHttpClient(
        okHttpClient: OkHttpClient,
        userAgentInterceptor: UserAgentInterceptor
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptorAsFirstInChain(userAgentInterceptor)
            .build()
    }

    @Provides
    @GamespotApi
    fun provideApiResultCallAdapterFactory(
        @GamespotApi errorMessageExtractor: ErrorMessageExtractor
    ): ApiResultCallAdapterFactory {
        return ApiResultCallAdapterFactory(errorMessageExtractor)
    }

    @Provides
    fun provideGamespotQueryParamsBuilder(
        gamespotFieldsSerializer: GamespotFieldsSerializer,
        gamespotConstantsProvider: GamespotConstantsProvider
    ): GamespotQueryParamsFactory {
        return GamespotQueryParamsFactoryImpl(
            gamespotFieldsSerializer = gamespotFieldsSerializer,
            apiKey = gamespotConstantsProvider.apiKey
        )
    }
}
