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

package com.paulrybitskyi.gamedge.feature.category

import com.paulrybitskyi.gamedge.common.domain.games.ObservableGamesUseCase
import com.paulrybitskyi.gamedge.common.domain.games.RefreshableGamesUseCase
import com.paulrybitskyi.gamedge.feature.category.di.GamesCategoryKey.Type
import javax.inject.Inject
import javax.inject.Provider

internal class GamesCategoryUseCases @Inject constructor(
    private val observeGamesUseCasesMap: Map<Type, @JvmSuppressWildcards Provider<ObservableGamesUseCase>>,
    private val refreshGamesUseCasesMap: Map<Type, @JvmSuppressWildcards Provider<RefreshableGamesUseCase>>
) {

    fun getObservableUseCase(keyType: Type): ObservableGamesUseCase {
        return observeGamesUseCasesMap.getValue(keyType).get()
    }

    fun getRefreshableUseCase(keyType: Type): RefreshableGamesUseCase {
        return refreshGamesUseCasesMap.getValue(keyType).get()
    }
}
