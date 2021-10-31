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

package com.paulrybitskyi.gamedge.feature.info.widgets.main

import com.paulrybitskyi.gamedge.commons.ui.widgets.categorypreview.GamesCategoryPreviewItemModel
import com.paulrybitskyi.gamedge.feature.info.widgets.companies.GameCompanyModel
import com.paulrybitskyi.gamedge.feature.info.widgets.links.GameLinkItemModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.header.GameHeaderImageModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.header.artworks.GameArtworkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoCompanyModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoLinkModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.GameInfoVideoModel
import com.paulrybitskyi.gamedge.feature.info.widgets.main.model.games.GameInfoRelatedGameModel
import com.paulrybitskyi.gamedge.feature.info.widgets.videos.GameVideoModel


internal fun List<GameHeaderImageModel>.mapToGameArtworkModels(): List<GameArtworkModel> {
    return map { imageModel ->
        when(imageModel) {
            is GameHeaderImageModel.DefaultImage -> GameArtworkModel.DefaultImage
            is GameHeaderImageModel.UrlImage -> GameArtworkModel.UrlImage(imageModel.url)
        }
    }
}


internal fun List<GameInfoVideoModel>.mapToVideoModels(): List<GameVideoModel> {
    return map {
        GameVideoModel(
            thumbnailUrl = it.thumbnailUrl,
            videoUrl = it.videoUrl,
            title = it.title
        )
    }
}


internal fun GameVideoModel.mapToInfoVideoModel(): GameInfoVideoModel {
    return GameInfoVideoModel(
        thumbnailUrl = thumbnailUrl,
        videoUrl = videoUrl,
        title = title
    )
}


internal fun List<GameInfoCompanyModel>.mapToCompanyModels(): List<GameCompanyModel> {
    return map {
        GameCompanyModel(
            logoViewSize = it.logoViewSize,
            logoImageSize = it.logoImageSize,
            logoUrl = it.logoUrl,
            websiteUrl = it.websiteUrl,
            name = it.name,
            roles = it.roles
        )
    }
}


internal fun GameCompanyModel.mapToInfoCompanyModel(): GameInfoCompanyModel {
    return GameInfoCompanyModel(
        logoViewSize = logoViewSize,
        logoImageSize = logoImageSize,
        logoUrl = logoUrl,
        websiteUrl = websiteUrl,
        name = name,
        roles = roles
    )
}


internal fun List<GameInfoLinkModel>.mapToLinkModels(): List<GameLinkItemModel> {
    return map {
        GameLinkItemModel(
            id = it.id,
            text = it.text,
            iconId = it.iconId,
            payload = it.payload
        )
    }
}


internal fun GameLinkItemModel.mapToInfoLinkModel(): GameInfoLinkModel {
    return GameInfoLinkModel(
        id = id,
        text = text,
        iconId = iconId,
        payload = payload
    )
}


internal fun List<GameInfoRelatedGameModel>.mapToCategoryModels(): List<GamesCategoryPreviewItemModel> {
    return map {
        GamesCategoryPreviewItemModel(
            id = it.id,
            title = it.title,
            coverUrl = it.coverUrl
        )
    }
}


internal fun GamesCategoryPreviewItemModel.mapToInfoRelatedGameModel(): GameInfoRelatedGameModel {
    return GameInfoRelatedGameModel(
        id = id,
        title = title,
        coverUrl = coverUrl
    )
}
