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

package com.paulrybitskyi.gamedge.core

import com.paulrybitskyi.gamedge.domain.games.entities.Image
import com.paulrybitskyi.gamedge.domain.games.entities.ImageSize


private const val IMAGE_URL_TEMPLATE = "https://images.igdb.com/igdb/image/upload/t_%s/%s.jpg"
private const val IMAGE_TYPE_RETINA_EXTENSION = "_2x"


interface ImageUrlBuilder {


    data class Config(
        val size: ImageSize,
        val withRetinaSize: Boolean = false
    )


    fun buildUrl(image: Image, config: Config): String?


}


internal class ImageUrlBuilderImpl : ImageUrlBuilder {


    override fun buildUrl(image: Image, config: ImageUrlBuilder.Config): String? {
        if(image.id.isBlank()) return null

        return String.format(IMAGE_URL_TEMPLATE, constructType(config), image.id)
    }


    private fun constructType(config: ImageUrlBuilder.Config): String {
        return buildString {
            append(config.size.rawSize)

            if(config.withRetinaSize) {
                append(IMAGE_TYPE_RETINA_EXTENSION)
            }
        }
    }


}