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

package com.paulrybitskyi.gamedge.data.auth.usecases

import com.paulrybitskyi.gamedge.core.utils.mapResult
import com.paulrybitskyi.gamedge.data.auth.datastores.commons.AuthDataStores
import com.paulrybitskyi.gamedge.data.auth.usecases.mappers.AuthMapper
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.data.commons.utils.onEachSuccess
import com.paulrybitskyi.gamedge.domain.auth.entities.OauthCredentials
import com.paulrybitskyi.gamedge.domain.auth.usecases.RefreshAuthUseCase
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class RefreshAuthUseCaseImpl @Inject constructor(
    private val authDataStores: AuthDataStores,
    private val authMapper: AuthMapper,
    private val errorMapper: ErrorMapper
) : RefreshAuthUseCase {


    override suspend fun execute(params: Unit): Flow<DomainResult<OauthCredentials>> {
        return flow {
            if(authDataStores.local.isExpired()) {
                emit(authDataStores.remote.getOauthCredentials())
            }
        }
        .onEachSuccess(authDataStores.local::saveOauthCredentials)
        .mapResult(authMapper::mapToDomainOauthCredentials, errorMapper::mapToDomainError)
    }


}