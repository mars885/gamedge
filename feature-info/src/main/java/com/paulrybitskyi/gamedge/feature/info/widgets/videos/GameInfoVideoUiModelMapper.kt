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

package com.paulrybitskyi.gamedge.feature.info.widgets.videos

import com.paulrybitskyi.gamedge.core.factories.YoutubeMediaUrlFactory
import com.paulrybitskyi.gamedge.core.factories.YoutubeThumbnailSize
import com.paulrybitskyi.gamedge.domain.games.entities.Video
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GameInfoVideoUiModelMapper {
    fun mapToUiModel(video: Video): GameInfoVideoUiModel?
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameInfoVideoUiModelMapperImpl @Inject constructor(
    private val youtubeMediaUrlFactory: YoutubeMediaUrlFactory,
) : GameInfoVideoUiModelMapper {

    override fun mapToUiModel(video: Video): GameInfoVideoUiModel? {
        val thumbnailUrl = youtubeMediaUrlFactory.createThumbnailUrl(
            video,
            YoutubeThumbnailSize.MEDIUM
        )
        val videoUrl = youtubeMediaUrlFactory.createVideoUrl(video)

        if ((thumbnailUrl == null) && (videoUrl == null)) return null

        return GameInfoVideoUiModel(
            id = video.id,
            thumbnailUrl = checkNotNull(thumbnailUrl),
            videoUrl = checkNotNull(videoUrl),
            title = video.name
        )
    }
}

internal fun GameInfoVideoUiModelMapper.mapToUiModels(
    videos: List<Video>,
): List<GameInfoVideoUiModel> {
    if (videos.isEmpty()) return emptyList()

    return videos.mapNotNull(::mapToUiModel)
}
