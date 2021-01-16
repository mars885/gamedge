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

package com.paulrybitskyi.gamedge.igdb.api.commons.di

import com.paulrybitskyi.gamedge.igdb.api.commons.di.qualifiers.ErrorMessageExtractorQualifier
import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.ErrorMessageExtractor
import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.concrete.CompositeErrorMessageExtractor
import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.concrete.IgdbErrorMessageExtractor
import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.concrete.TwitchErrorMessageExtractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoSet

@Module(includes = [ErrorMessageExtractorsModule.MultibindingsModule::class])
@InstallIn(SingletonComponent::class)
internal interface ErrorMessageExtractorsModule {


    @Binds
    @ErrorMessageExtractorQualifier(ErrorMessageExtractorQualifier.Type.TWITCH)
    fun bindTwitchErrorMessageExtractor(extractor: TwitchErrorMessageExtractor): ErrorMessageExtractor


    @Binds
    @ErrorMessageExtractorQualifier(ErrorMessageExtractorQualifier.Type.IGDB)
    fun bindIgdbErrorMessageExtractor(extractor: IgdbErrorMessageExtractor): ErrorMessageExtractor


    @Binds
    fun bindCompositeErrorMessageExtractor(extractor: CompositeErrorMessageExtractor): ErrorMessageExtractor


    @Module
    @DisableInstallInCheck
    interface MultibindingsModule {

        @Binds
        @IntoSet
        fun bindTwitchErrorMessageExtractorToSet(
            @ErrorMessageExtractorQualifier(ErrorMessageExtractorQualifier.Type.TWITCH)
            errorMessageExtractor: ErrorMessageExtractor
        ): ErrorMessageExtractor

        @Binds
        @IntoSet
        fun bindIgdbErrorMessageExtractorToSet(
            @ErrorMessageExtractorQualifier(ErrorMessageExtractorQualifier.Type.IGDB)
            errorMessageExtractor: ErrorMessageExtractor
        ): ErrorMessageExtractor

    }


}