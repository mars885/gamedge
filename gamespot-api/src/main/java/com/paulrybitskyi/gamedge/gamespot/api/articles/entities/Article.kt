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

package com.paulrybitskyi.gamedge.gamespot.api.articles.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class Article(
    @field:Json(name = Schema.ID)
    val id: Int = -1,
    @field:Json(name = Schema.TITLE)
    val title: String = "",
    @field:Json(name = Schema.LEDE)
    val lede: String = "",
    @field:Json(name = Schema.IMAGE_URLS)
    val imageUrls: Map<ImageType, String> = mapOf(),
    @field:Json(name = Schema.PUBLICATION_DATE)
    val publicationDate: String = "",
    @field:Json(name = Schema.SITE_DETAIL_URL)
    val siteDetailUrl: String = ""
) {


    object Schema {

        const val ID = "id"
        const val TITLE = "title"
        const val LEDE = "lede"
        const val IMAGE_URLS = "image"
        const val PUBLICATION_DATE = "publish_date"
        const val SITE_DETAIL_URL = "site_detail_url"

    }


}