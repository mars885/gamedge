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

import com.paulrybitskyi.gamedge.domain.games.entities.Video


enum class YoutubeThumbnailSize(internal val rawSize: String) {

    DEFAULT("default"), // 120x90
    MEDIUM("mqdefault"),    // 320x180
    HIGH("hqdefault"),  // 480x360
    STANDARD_DEFINITION("sddefault"),   // 640x480
    MAX("maxresdefault")    // 1052x592

}


interface YoutubeMediaUrlBuilder {

    fun buildThumbnailUrl(video: Video, size: YoutubeThumbnailSize): String?

    fun buildVideoUrl(video: Video): String?

}


internal class YoutubeMediaUrlBuilderImpl : YoutubeMediaUrlBuilder {


    private companion object {

        private const val THUMBNAIL_URL_TEMPLATE = "https://img.youtube.com/vi/%s/%s.jpg"
        private const val VIDEO_URL_TEMPLATE = "https://youtu.be/%s"

    }


    override fun buildThumbnailUrl(video: Video, size: YoutubeThumbnailSize): String? {
        if(video.id.isBlank()) return null

        return String.format(THUMBNAIL_URL_TEMPLATE, video.id, size.rawSize)
    }


    override fun buildVideoUrl(video: Video): String? {
        if(video.id.isEmpty()) return null

        return String.format(VIDEO_URL_TEMPLATE, video.id)
    }


}