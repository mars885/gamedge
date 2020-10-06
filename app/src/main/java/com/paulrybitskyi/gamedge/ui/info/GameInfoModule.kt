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

package com.paulrybitskyi.gamedge.ui.info

import android.content.Context
import com.paulrybitskyi.gamedge.core.*
import com.paulrybitskyi.gamedge.core.formatters.GameAgeRatingFormatter
import com.paulrybitskyi.gamedge.core.formatters.GameCategoryFormatter
import com.paulrybitskyi.gamedge.core.formatters.GameRatingFormatter
import com.paulrybitskyi.gamedge.core.formatters.GameReleaseDateFormatter
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.core.YoutubeMediaUrlBuilder
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.games.usecases.*
import com.paulrybitskyi.gamedge.domain.games.usecases.observers.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.ui.info.mapping.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object GameInfoModule {


    @Provides
    fun provideGameInfoUseCases(
        getGameUseCase: GetGameUseCase,
        observeGameLikeStateUseCase: ObserveGameLikeStateUseCase,
        toggleGameLikeStateUseCase: ToggleGameLikeStateUseCase,
        getCompanyDevelopedGamesUseCase: GetCompanyDevelopedGamesUseCase,
        getSimilarGamesUseCase: GetSimilarGamesUseCase,
    ): GameInfoUseCases {
        return GameInfoUseCases(
            getGameUseCase = getGameUseCase,
            observeGameLikeStateUseCase = observeGameLikeStateUseCase,
            toggleGameLikeStateUseCase = toggleGameLikeStateUseCase,
            getCompanyDevelopedGamesUseCase = getCompanyDevelopedGamesUseCase,
            getSimilarGamesUseCase = getSimilarGamesUseCase
        )
    }


    @Provides
    fun provideGameInfoUiStateFactory(
        modelFactory: GameInfoModelFactory
    ): GameInfoUiStateFactory {
        return GameInfoUiStateFactoryImpl(
            modelFactory = modelFactory
        )
    }


    @Provides
    fun provideGameInfoModelFactory(
        headerModelFactory: GameInfoHeaderModelFactory,
        videoModelFactory: GameInfoVideoModelFactory,
        detailsModelFactory: GameInfoDetailsModelFactory,
        linkModelFactory: GameInfoLinkModelFactory,
        companyModelFactory: GameInfoCompanyModelFactory,
        otherCompanyGamesModelFactory: GameInfoOtherCompanyGamesModelFactory,
        similarGamesModelFactory: GameInfoSimilarGamesModelFactory,
        igdbImageUrlBuilder: IgdbImageUrlBuilder
    ): GameInfoModelFactory {
        return GameInfoModelFactoryImpl(
            headerModelFactory = headerModelFactory,
            videoModelFactory = videoModelFactory,
            detailsModelFactory = detailsModelFactory,
            linkModelFactory = linkModelFactory,
            companyModelFactory = companyModelFactory,
            otherCompanyGamesModelFactory = otherCompanyGamesModelFactory,
            similarGamesModelFactory = similarGamesModelFactory,
            igdbImageUrlBuilder = igdbImageUrlBuilder
        )
    }


    @Provides
    fun provideGameInfoHeaderModelFactory(
        igdbImageUrlBuilder: IgdbImageUrlBuilder,
        releaseDateFormatter: GameReleaseDateFormatter,
        ratingFormatter: GameRatingFormatter,
        likeCountCalculator: GameLikeCountCalculator,
        ageRatingFormatter: GameAgeRatingFormatter,
        categoryFormatter: GameCategoryFormatter
    ): GameInfoHeaderModelFactory {
        return GameInfoHeaderModelFactoryImpl(
            igdbImageUrlBuilder = igdbImageUrlBuilder,
            releaseDateFormatter = releaseDateFormatter,
            ratingFormatter = ratingFormatter,
            likeCountCalculator = likeCountCalculator,
            ageRatingFormatter = ageRatingFormatter,
            categoryFormatter = categoryFormatter
        )
    }


    @Provides
    fun provideGameInfoVideoModelFactory(
        youtubeMediaUrlBuilder: YoutubeMediaUrlBuilder
    ): GameInfoVideoModelFactory {
        return GameInfoVideoModelFactoryImpl(youtubeMediaUrlBuilder)
    }


    @Provides
    fun provideGameInfoDetailsModelFactory(): GameInfoDetailsModelFactory {
        return GameInfoDetailsModelFactoryImpl()
    }


    @Provides
    fun provideGameInfoLinkModelFactory(
        websiteNameRetriever: WebsiteNameRetriever,
        websiteIconRetriever: WebsiteIconRetriever
    ): GameInfoLinkModelFactory {
        return GameInfoLinkModelFactoryImpl(
            websiteNameRetriever = websiteNameRetriever,
            websiteIconRetriever = websiteIconRetriever
        )
    }


    @Provides
    fun provideGameInfoCompanyModelFactory(
        @ApplicationContext context: Context,
        igdbImageUrlBuilder: IgdbImageUrlBuilder,
        stringProvider: StringProvider
    ): GameInfoCompanyModelFactory {
        return GameInfoCompanyModelFactoryImpl(
            context = context,
            igdbImageUrlBuilder = igdbImageUrlBuilder,
            stringProvider = stringProvider
        )
    }


    @Provides
    fun provideGameInfoOtherCompanyGamesModelFactory(
        stringProvider: StringProvider,
        igdbImageUrlBuilder: IgdbImageUrlBuilder
    ): GameInfoOtherCompanyGamesModelFactory {
        return GameInfoOtherCompanyGamesModelFactoryImpl(
            igdbImageUrlBuilder = igdbImageUrlBuilder,
            stringProvider = stringProvider
        )
    }


    @Provides
    fun provideGameInfoSimilarGamesModelFactory(
        stringProvider: StringProvider,
        igdbImageUrlBuilder: IgdbImageUrlBuilder,
    ): GameInfoSimilarGamesModelFactory {
        return GameInfoSimilarGamesModelFactoryImpl(
            stringProvider = stringProvider,
            igdbImageUrlBuilder = igdbImageUrlBuilder
        )
    }


}