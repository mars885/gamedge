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

package com.paulrybitskyi.gamedge.igdb.api.games.entities

import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.Apicalypse
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.ApicalypseClass
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@ApicalypseClass
@JsonClass(generateAdapter = true)
internal data class Image(
    @Apicalypse(name = Schema.ID)
    @field:Json(name = Schema.ID)
    val id: String,
    @Apicalypse(name = Schema.WIDTH)
    @field:Json(name = Schema.WIDTH)
    val width: Int?,
    @Apicalypse(name = Schema.HEIGHT)
    @field:Json(name = Schema.HEIGHT)
    val height: Int?,
) {


    object Schema {

        const val ID = "image_id"
        const val WIDTH = "width"
        const val HEIGHT = "height"

    }


}