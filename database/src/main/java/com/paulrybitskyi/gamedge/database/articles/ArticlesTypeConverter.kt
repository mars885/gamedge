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

package com.paulrybitskyi.gamedge.database.articles

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.paulrybitskyi.gamedge.core.JsonConverter
import com.paulrybitskyi.gamedge.database.articles.entities.ImageType
import com.paulrybitskyi.gamedge.database.commons.RoomTypeConverter
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@ProvidedTypeConverter
@BindType(contributesTo = BindType.Collection.SET)
internal class ArticlesTypeConverter @Inject constructor(
    private val jsonConverter: JsonConverter
) : RoomTypeConverter {


    @TypeConverter
    fun fromImageUrls(imageUrls: Map<ImageType, String>): String {
        return jsonConverter.toJson(imageUrls)
    }


    @TypeConverter
    fun toImageUrls(json: String): Map<ImageType, String> {
        return (jsonConverter.fromJson(json) ?: emptyMap())
    }


}