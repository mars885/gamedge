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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoSet

@InstallIn(ApplicationComponent::class)
@Module(includes = [ErrorMessageExtractorsModule.MultibindingSetModule::class])
internal object ErrorMessageExtractorsModule {


    @ErrorMessageExtractorQualifier(ErrorMessageExtractorQualifier.Type.TWITCH)
    @Provides
    fun provideTwitchErrorMessageExtractor(): ErrorMessageExtractor {
        return TwitchErrorMessageExtractor()
    }


    @ErrorMessageExtractorQualifier(ErrorMessageExtractorQualifier.Type.IGDB)
    @Provides
    fun provideIgdbErrorMessageExtractor(): ErrorMessageExtractor {
        return IgdbErrorMessageExtractor()
    }


    @Provides
    fun provideCompositeErrorMessageExtractor(
        errorMessageExtractors: Set<@JvmSuppressWildcards ErrorMessageExtractor>
    ): ErrorMessageExtractor {
        return CompositeErrorMessageExtractor(errorMessageExtractors)
    }


    @Module
    @DisableInstallInCheck
    internal object MultibindingSetModule {

        @IntoSet
        @Provides
        fun provideTwitchErrorMessageExtractor(
            @ErrorMessageExtractorQualifier(ErrorMessageExtractorQualifier.Type.TWITCH)
            errorMessageExtractor: ErrorMessageExtractor
        ): ErrorMessageExtractor {
            return errorMessageExtractor
        }

        @IntoSet
        @Provides
        fun provideIgdbErrorMessageExtractor(
            @ErrorMessageExtractorQualifier(ErrorMessageExtractorQualifier.Type.IGDB)
            errorMessageExtractor: ErrorMessageExtractor
        ): ErrorMessageExtractor {
            return errorMessageExtractor
        }

    }


}