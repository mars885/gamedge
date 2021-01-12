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

package com.paulrybitskyi.gamedge.core.di

import com.paulrybitskyi.gamedge.core.providers.CustomTabsProvider
import com.paulrybitskyi.gamedge.core.urlopener.*
import com.paulrybitskyi.gamedge.core.urlopener.CustomTabUrlOpener
import com.paulrybitskyi.gamedge.core.urlopener.NativeAppUrlOpener
import com.paulrybitskyi.gamedge.core.urlopener.UrlOpenerFactoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
internal object UrlOpenersModule {


    @Qualifier
    annotation class UrlOpenerKey(val type: Type) {

        enum class Type {

            NATIVE_APP,
            CUSTOM_TAB,
            BROWSER

        }

    }


    @Provides
    @Singleton
    fun provideUrlOpenerFactory(
        @UrlOpenerKey(UrlOpenerKey.Type.NATIVE_APP) nativeAppUrlOpener: UrlOpener,
        @UrlOpenerKey(UrlOpenerKey.Type.CUSTOM_TAB) customTabUrlOpener: UrlOpener,
        @UrlOpenerKey(UrlOpenerKey.Type.BROWSER) browserUrlOpener: UrlOpener,
    ): UrlOpenerFactory {
        return UrlOpenerFactoryImpl(
            urlOpeners = listOf(
                nativeAppUrlOpener,
                customTabUrlOpener,
                browserUrlOpener
            )
        )
    }


    @Provides
    @UrlOpenerKey(UrlOpenerKey.Type.NATIVE_APP)
    fun provideNativeAppUrlOpener(): UrlOpener {
        return NativeAppUrlOpener()
    }


    @Provides
    @UrlOpenerKey(UrlOpenerKey.Type.CUSTOM_TAB)
    fun provideCustomTabUrlOpener(customTabsProvider: CustomTabsProvider): UrlOpener {
        return CustomTabUrlOpener(customTabsProvider)
    }


    @Provides
    @UrlOpenerKey(UrlOpenerKey.Type.BROWSER)
    fun provideBrowserUrlOpener(): UrlOpener {
        return BrowserUrlOpener()
    }


}