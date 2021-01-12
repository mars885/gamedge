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

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.commons.ObserveGamesUseCaseMappers
import com.paulrybitskyi.gamedge.data.games.usecases.likes.ObserveGameLikeStateUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.likes.ObserveLikedGamesUseCaseImpl
import com.paulrybitskyi.gamedge.data.games.usecases.likes.ToggleGameLikeStateUseCaseImpl
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveLikedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ToggleGameLikeStateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal object LikedGamesUseCasesModule {


    @Provides
    @Singleton
    fun provideGameLikeStateObservableUseCase(
        likedGamesLocalDataStore: LikedGamesLocalDataStore
    ): ObserveGameLikeStateUseCase {
        return ObserveGameLikeStateUseCaseImpl(likedGamesLocalDataStore)
    }


    @Provides
    @Singleton
    fun provideLikedGamesObservableUseCase(
        likedGamesLocalDataStore: LikedGamesLocalDataStore,
        dispatcherProvider: DispatcherProvider,
        mappers: ObserveGamesUseCaseMappers
    ): ObserveLikedGamesUseCase {
        return ObserveLikedGamesUseCaseImpl(
            likedGamesLocalDataStore = likedGamesLocalDataStore,
            dispatcherProvider = dispatcherProvider,
            mappers = mappers
        )
    }


    @Provides
    @Singleton
    fun provideToggleGameLikeStateUseCase(
        likedGamesLocalDataStore: LikedGamesLocalDataStore
    ): ToggleGameLikeStateUseCase {
        return ToggleGameLikeStateUseCaseImpl(likedGamesLocalDataStore)
    }


}