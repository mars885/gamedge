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

package com.paulrybitskyi.gamedge.data.auth.di

import com.paulrybitskyi.gamedge.data.auth.datastores.commons.AuthDataStores
import com.paulrybitskyi.gamedge.data.auth.usecases.RefreshAuthUseCaseImpl
import com.paulrybitskyi.gamedge.data.auth.usecases.mappers.AuthMapper
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.domain.auth.usecases.RefreshAuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object UseCasesModule {


    @Provides
    @Singleton
    fun provideRefreshAuthUseCase(
        authDataStores: AuthDataStores,
        authMapper: AuthMapper,
        errorMapper: ErrorMapper
    ): RefreshAuthUseCase {
        return RefreshAuthUseCaseImpl(
            authDataStores = authDataStores,
            authMapper = authMapper,
            errorMapper = errorMapper
        )
    }


    @Provides
    fun provideAuthMapper(): AuthMapper {
        return AuthMapper()
    }


}