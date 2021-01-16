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

import com.paulrybitskyi.gamedge.ui.info.mapping.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface GameInfoModule {

    @Binds
    fun bindGameInfoUiStateFactory(factory: GameInfoUiStateFactoryImpl): GameInfoUiStateFactory

    @Binds
    fun bindGameInfoModelFactoryImpl(factory: GameInfoModelFactoryImpl): GameInfoModelFactory

    @Binds
    fun bindGameInfoHeaderModelFactoryImpl(factory: GameInfoHeaderModelFactoryImpl): GameInfoHeaderModelFactory

    @Binds
    fun bindGameInfoVideoModelFactoryImpl(factory: GameInfoVideoModelFactoryImpl): GameInfoVideoModelFactory

    @Binds
    fun bindGameInfoDetailsModelFactoryImpl(factory: GameInfoDetailsModelFactoryImpl): GameInfoDetailsModelFactory

    @Binds
    fun bindGameInfoLinkModelFactoryImpl(factory: GameInfoLinkModelFactoryImpl): GameInfoLinkModelFactory

    @Binds
    fun bindGameInfoCompanyModelFactoryImpl(factory: GameInfoCompanyModelFactoryImpl): GameInfoCompanyModelFactory

    @Binds
    fun bindGameInfoOtherCompanyGamesModelFactoryImpl(factory: GameInfoOtherCompanyGamesModelFactoryImpl): GameInfoOtherCompanyGamesModelFactory

    @Binds
    fun bindGameInfoSimilarGamesModelFactoryImpl(factory: GameInfoSimilarGamesModelFactoryImpl): GameInfoSimilarGamesModelFactory

}