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

package com.paulrybitskyi.gamedge.image.loading

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import com.paulrybitskyi.commons.ktx.getSystemService
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ImageLoadingModule {

    @Provides
    @Singleton
    fun providePicasso(@ApplicationContext context: Context): Picasso {
        val activityManager = context.getSystemService<ActivityManager>()
        val cacheSizeInMegabytes = (activityManager.memoryClass / 2) // ~50% of the available heap

        @Suppress("MagicNumber")
        val cacheSizeInBytes = (1024 * 1024 * cacheSizeInMegabytes)

        return Picasso.Builder(context)
            .defaultBitmapConfig(Bitmap.Config.ARGB_8888)
            .memoryCache(LruCache(cacheSizeInBytes))
            .build()
    }
}
