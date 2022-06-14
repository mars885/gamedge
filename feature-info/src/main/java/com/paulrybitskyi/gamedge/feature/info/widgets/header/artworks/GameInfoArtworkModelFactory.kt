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

internal interface GameInfoArtworkModelFactory {
    fun createArtworkModels(images: List<Image>): List<GameInfoArtworkModel>
    fun createArtworkModel(image: Image): GameInfoArtworkModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameInfoArtworkModelFactoryImpl @Inject constructor(
    private val igdbImageUrlFactory: IgdbImageUrlFactory,
) : GameInfoArtworkModelFactory {

    override fun createArtworkModels(images: List<Image>): List<GameInfoArtworkModel> {
        if (images.isEmpty()) return listOf(GameInfoArtworkModel.DefaultImage)

        return images.map(::createArtworkModel)
            .filterIsInstance<GameInfoArtworkModel.UrlImage>()
    }

    override fun createArtworkModel(image: Image): GameInfoArtworkModel {
        return igdbImageUrlFactory.createUrl(
            image = image,
            config = IgdbImageUrlFactory.Config(IgdbImageSize.BIG_SCREENSHOT),
        )
        ?.let { url -> GameInfoArtworkModel.UrlImage(id = image.id, url = url) }
        ?: GameInfoArtworkModel.DefaultImage
    }
}
