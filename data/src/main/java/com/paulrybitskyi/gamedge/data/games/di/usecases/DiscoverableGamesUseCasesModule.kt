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

package com.paulrybitskyi.gamedge.data.games.di.usecases

import com.paulrybitskyi.gamedge.data.games.usecases.discovery.*
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DiscoverableGamesUseCasesModule {

    @Binds
    fun bindObserveComingSoonGamesUseCaseImpl(useCase: ObserveComingSoonGamesUseCaseImpl): ObserveComingSoonGamesUseCase

    @Binds
    fun bindRefreshComingSoonGamesUseCaseImpl(useCase: RefreshComingSoonGamesUseCaseImpl): RefreshComingSoonGamesUseCase

    @Binds
    fun bindObserveMostAnticipatedGamesUseCaseImpl(useCase: ObserveMostAnticipatedGamesUseCaseImpl): ObserveMostAnticipatedGamesUseCase

    @Binds
    fun bindRefreshMostAnticipatedGamesUseCaseImpl(useCase: RefreshMostAnticipatedGamesUseCaseImpl): RefreshMostAnticipatedGamesUseCase

    @Binds
    fun bindObservePopularGamesUseCaseImpl(useCase: ObservePopularGamesUseCaseImpl): ObservePopularGamesUseCase

    @Binds
    fun bindRefreshPopularGamesUseCaseImpl(useCase: RefreshPopularGamesUseCaseImpl): RefreshPopularGamesUseCase

    @Binds
    fun bindObserveRecentlyReleasedGamesUseCaseImpl(useCase: ObserveRecentlyReleasedGamesUseCaseImpl): ObserveRecentlyReleasedGamesUseCase

    @Binds
    fun bindRefreshRecentlyReleasedGamesUseCaseImpl(useCase: RefreshRecentlyReleasedGamesUseCaseImpl): RefreshRecentlyReleasedGamesUseCase

    @Binds
    fun bindRefreshAllDiscoverableGamesUseCaseImpl(useCase: RefreshAllDiscoverableGamesUseCaseImpl): RefreshAllDiscoverableGamesUseCase

}