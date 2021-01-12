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

package com.paulrybitskyi.gamedge.data.games.di.usecases

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.commons.GamesDataStores
import com.paulrybitskyi.gamedge.data.games.usecases.*
import com.paulrybitskyi.gamedge.data.games.usecases.commons.*
import com.paulrybitskyi.gamedge.domain.games.usecases.*
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshSimilarGamesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal object UseCasesModule {


    @Provides
    @Singleton
    fun provideGetGameUseCase(
        gamesLocalDataStore: GamesLocalDataStore,
        dispatcherProvider: DispatcherProvider,
        gameMapper: GameMapper
    ): GetGameUseCase {
        return GetGameUseCaseImpl(
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = dispatcherProvider,
            gameMapper = gameMapper
        )
    }


    @Provides
    @Singleton
    fun provideGetCompanyDevelopedGamesUseCase(
        refreshCompanyDevelopedGamesUseCase: RefreshCompanyDevelopedGamesUseCase,
        gamesLocalDataStore: GamesLocalDataStore,
        dispatcherProvider: DispatcherProvider,
        gameMapper: GameMapper,
        paginationMapper: PaginationMapper
    ): GetCompanyDevelopedGamesUseCase {
        return GetCompanyDevelopedGamesUseCaseImpl(
            refreshCompanyDevelopedGamesUseCase = refreshCompanyDevelopedGamesUseCase,
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = dispatcherProvider,
            gameMapper = gameMapper,
            paginationMapper = paginationMapper
        )
    }


    @Provides
    @Singleton
    fun provideGetSimilarGamesUseCase(
        refreshSimilarGamesUseCase: RefreshSimilarGamesUseCase,
        gamesLocalDataStore: GamesLocalDataStore,
        dispatcherProvider: DispatcherProvider,
        gameMapper: GameMapper,
        paginationMapper: PaginationMapper
    ): GetSimilarGamesUseCase {
        return GetSimilarGamesUseCaseImpl(
            refreshSimilarGamesUseCase = refreshSimilarGamesUseCase,
            gamesLocalDataStore = gamesLocalDataStore,
            dispatcherProvider = dispatcherProvider,
            gameMapper = gameMapper,
            paginationMapper = paginationMapper
        )
    }


    @Provides
    @Singleton
    fun provideSearchGamesUseCase(
        gamesDataStores: GamesDataStores,
        dispatcherProvider: DispatcherProvider,
        networkStateProvider: NetworkStateProvider,
        gameMapper: GameMapper,
        paginationMapper: PaginationMapper,
        errorMapper: ErrorMapper
    ): SearchGamesUseCase {
        return SearchGamesUseCaseImpl(
            gamesDataStores = gamesDataStores,
            dispatcherProvider = dispatcherProvider,
            networkStateProvider = networkStateProvider,
            gameMapper = gameMapper,
            paginationMapper = paginationMapper,
            errorMapper = errorMapper
        )
    }


    @Provides
    @Singleton
    fun provideToggleGameLikeStateUseCase(
        likedGamesLocalDataStore: LikedGamesLocalDataStore
    ): ToggleGameLikeStateUseCase {
        return ToggleGameLikeStateUseCaseImpl(likedGamesLocalDataStore)
    }


    @Provides
    fun provideGameMapper(): GameMapper {
        return GameMapper()
    }


    @Provides
    fun providePaginationMapper(): PaginationMapper {
        return PaginationMapper()
    }


}