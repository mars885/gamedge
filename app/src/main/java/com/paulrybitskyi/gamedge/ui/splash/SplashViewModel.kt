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

package com.paulrybitskyi.gamedge.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.core.utils.onError
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.domain.auth.usecases.RefreshAuthUseCase
import com.paulrybitskyi.gamedge.domain.commons.extensions.execute
import com.paulrybitskyi.gamedge.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.utils.ErrorMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

internal class SplashViewModel @ViewModelInject constructor(
    private val refreshAuthUseCase: RefreshAuthUseCase,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) : BaseViewModel() {


    fun init() {
        viewModelScope.launch {
            initInternal()
        }
    }


    private suspend fun initInternal() {
        return refreshAuth()
            .onCompletion { onInitializationFinished(it) }
            .onError { logger.error(logTag, "Failed to initialize.", it) }
            .collect()
    }


    private suspend fun refreshAuth(): Flow<*> {
        return refreshAuthUseCase.execute()
            .resultOrError()
    }


    private fun onInitializationFinished(error: Throwable?) {
        if(error != null) {
            dispatchCommand(GeneralCommand.ShowLongToast(errorMapper.mapToMessage(error)))
            route(SplashRoute.Exit)
            return
        }

        route(SplashRoute.Dashboard)
    }


}