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

package com.paulrybitskyi.gamedge.data.di

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.data.usecases.mappers.EntityMapper
import com.paulrybitskyi.gamedge.data.usecases.mappers.PaginationMapper
import com.paulrybitskyi.gamedge.data.usecases.observers.ObserveComingSoonGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.usecases.observers.ObserveMostAnticipatedGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.usecases.observers.ObservePopularGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.usecases.observers.ObserveRecentlyReleasedGamesUseCaseImpl
import com.paulrybitskyi.gamedge.domain.usecases.games.observers.ObserveComingSoonGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.games.observers.ObserveMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.games.observers.ObservePopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.games.observers.ObserveRecentlyReleasedGamesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal object ObservableUseCasesModule {


    @Singleton
    @Provides
    fun provideComingSoonGamesObservableUserCase(
        gamesDatabaseDataStore: GamesDatabaseDataStore,
        dispatcherProvider: DispatcherProvider,
        paginationMapper: PaginationMapper,
        entityMapper: EntityMapper
    ): ObserveComingSoonGamesUseCase {
        return ObserveComingSoonGamesUseCaseImpl(
            gamesDatabaseDataStore = gamesDatabaseDataStore,
            dispatcherProvider = dispatcherProvider,
            paginationMapper = paginationMapper,
            entityMapper = entityMapper
        )
    }


    @Singleton
    @Provides
    fun provideMostAnticipatedGamesObservableUseCase(
        gamesDatabaseDataStore: GamesDatabaseDataStore,
        dispatcherProvider: DispatcherProvider,
        paginationMapper: PaginationMapper,
        entityMapper: EntityMapper
    ): ObserveMostAnticipatedGamesUseCase {
        return ObserveMostAnticipatedGamesUseCaseImpl(
            gamesDatabaseDataStore = gamesDatabaseDataStore,
            dispatcherProvider = dispatcherProvider,
            paginationMapper = paginationMapper,
            entityMapper = entityMapper
        )
    }


    @Singleton
    @Provides
    fun providePopularGamesObservableUseCase(
        gamesDatabaseDataStore: GamesDatabaseDataStore,
        dispatcherProvider: DispatcherProvider,
        paginationMapper: PaginationMapper,
        entityMapper: EntityMapper
    ): ObservePopularGamesUseCase {
        return ObservePopularGamesUseCaseImpl(
            gamesDatabaseDataStore = gamesDatabaseDataStore,
            dispatcherProvider = dispatcherProvider,
            paginationMapper = paginationMapper,
            entityMapper = entityMapper
        )
    }


    @Singleton
    @Provides
    fun provideRecentlyReleasedGamesObservableUseCase(
        gamesDatabaseDataStore: GamesDatabaseDataStore,
        dispatcherProvider: DispatcherProvider,
        paginationMapper: PaginationMapper,
        entityMapper: EntityMapper
    ): ObserveRecentlyReleasedGamesUseCase {
        return ObserveRecentlyReleasedGamesUseCaseImpl(
            gamesDatabaseDataStore = gamesDatabaseDataStore,
            dispatcherProvider = dispatcherProvider,
            paginationMapper = paginationMapper,
            entityMapper = entityMapper
        )
    }


}