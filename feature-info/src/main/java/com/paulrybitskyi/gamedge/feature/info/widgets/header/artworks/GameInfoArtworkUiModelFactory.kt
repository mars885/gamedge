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

package com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks

import com.paulrybitskyi.gamedge.core.factories.IgdbImageSize
import com.paulrybitskyi.gamedge.core.factories.IgdbImageUrlFactory
import com.paulrybitskyi.gamedge.domain.games.entities.Image
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GameInfoArtworkUiModelFactory {
    fun createArtworkUiModel(image: Image): GameInfoArtworkUiModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameInfoArtworkUiModelFactoryImpl @Inject constructor(
    private val igdbImageUrlFactory: IgdbImageUrlFactory,
) : GameInfoArtworkUiModelFactory {

    override fun createArtworkUiModel(image: Image): GameInfoArtworkUiModel {
        return igdbImageUrlFactory.createUrl(
            image = image,
            config = IgdbImageUrlFactory.Config(IgdbImageSize.BIG_SCREENSHOT),
        )
        ?.let { url -> GameInfoArtworkUiModel.UrlImage(id = image.id, url = url) }
        ?: GameInfoArtworkUiModel.DefaultImage
    }
}

internal fun GameInfoArtworkUiModelFactory.createArtworkUiModels(
    images: List<Image>,
): List<GameInfoArtworkUiModel> {
    if (images.isEmpty()) return listOf(GameInfoArtworkUiModel.DefaultImage)

    return images.map(::createArtworkUiModel)
        .filterIsInstance<GameInfoArtworkUiModel.UrlImage>()
}
