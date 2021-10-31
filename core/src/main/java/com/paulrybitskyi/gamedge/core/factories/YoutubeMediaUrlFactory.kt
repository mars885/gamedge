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

package com.paulrybitskyi.gamedge.core.factories

import com.paulrybitskyi.gamedge.domain.games.entities.Video
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


enum class YoutubeThumbnailSize(internal val rawSize: String) {

    DEFAULT("default"), // 120x90
    MEDIUM("mqdefault"),    // 320x180
    HIGH("hqdefault"),  // 480x360
    STANDARD_DEFINITION("sddefault"),   // 640x480
    MAX("maxresdefault")    // 1052x592

}


interface YoutubeMediaUrlFactory {

    fun createThumbnailUrl(video: Video, size: YoutubeThumbnailSize): String?

    fun createVideoUrl(video: Video): String?

}


@BindType
internal class YoutubeMediaUrlFactoryImpl @Inject constructor() : YoutubeMediaUrlFactory {


    private companion object {

        private const val THUMBNAIL_URL_TEMPLATE = "https://img.youtube.com/vi/%s/%s.jpg"
        private const val VIDEO_URL_TEMPLATE = "https://youtu.be/%s"

    }


    override fun createThumbnailUrl(video: Video, size: YoutubeThumbnailSize): String? {
        if(video.id.isBlank()) return null

        return String.format(THUMBNAIL_URL_TEMPLATE, video.id, size.rawSize)
    }


    override fun createVideoUrl(video: Video): String? {
        if(video.id.isBlank()) return null

        return String.format(VIDEO_URL_TEMPLATE, video.id)
    }


}
