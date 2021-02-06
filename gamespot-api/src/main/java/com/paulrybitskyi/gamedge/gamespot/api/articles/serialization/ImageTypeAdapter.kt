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

package com.paulrybitskyi.gamedge.gamespot.api.articles.serialization

import com.paulrybitskyi.gamedge.gamespot.api.articles.entities.ImageType
import com.paulrybitskyi.gamedge.gamespot.api.articles.entities.ImageType.Companion.asImageType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

internal class ImageTypeAdapter {


    @FromJson
    fun fromJson(imageType: String): ImageType {
        return imageType.asImageType()
    }


    @ToJson
    fun toJson(imageType: ImageType): String {
        return imageType.value
    }


}