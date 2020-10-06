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

package com.paulrybitskyi.gamedge.ui.info.mapping

import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.GameInfoVideoModel
import com.paulrybitskyi.gamedge.core.YoutubeMediaUrlBuilder
import com.paulrybitskyi.gamedge.core.YoutubeThumbnailSize
import com.paulrybitskyi.gamedge.domain.games.entities.Video


internal interface GameInfoVideoModelFactory {

    fun createVideoModels(videos: List<Video>): List<GameInfoVideoModel>

    fun createVideoModel(video: Video): GameInfoVideoModel?

}


internal class GameInfoVideoModelFactoryImpl(
    private val youtubeMediaUrlBuilder: YoutubeMediaUrlBuilder,
) : GameInfoVideoModelFactory {


    override fun createVideoModels(videos: List<Video>): List<GameInfoVideoModel> {
        if(videos.isEmpty()) return emptyList()

        return videos.mapNotNull(::createVideoModel)
    }


    override fun createVideoModel(video: Video): GameInfoVideoModel? {
        val thumbnailUrl = youtubeMediaUrlBuilder.buildThumbnailUrl(
            video,
            YoutubeThumbnailSize.MEDIUM
        )
        val videoUrl = youtubeMediaUrlBuilder.buildVideoUrl(video)

        if((thumbnailUrl == null) && (videoUrl == null)) return null

        return GameInfoVideoModel(
            thumbnailUrl = checkNotNull(thumbnailUrl),
            videoUrl = checkNotNull(videoUrl),
            title = video.name
        )
    }


}