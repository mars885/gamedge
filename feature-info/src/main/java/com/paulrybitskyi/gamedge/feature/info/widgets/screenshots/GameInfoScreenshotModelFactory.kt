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

package com.paulrybitskyi.gamedge.feature.info.widgets.screenshots

import com.paulrybitskyi.gamedge.core.factories.IgdbImageSize
import com.paulrybitskyi.gamedge.core.factories.IgdbImageUrlFactory
import com.paulrybitskyi.gamedge.domain.games.entities.Image
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GameInfoScreenshotModelFactory {
    fun createScreenshotModels(images: List<Image>): List<GameInfoScreenshotModel>
    fun createScreenshotModel(image: Image): GameInfoScreenshotModel?
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameInfoScreenshotModelFactoryImpl @Inject constructor(
    private val igdbImageUrlFactory: IgdbImageUrlFactory,
) : GameInfoScreenshotModelFactory {

    override fun createScreenshotModels(images: List<Image>): List<GameInfoScreenshotModel> {
        if (images.isEmpty()) return emptyList()

        return images.mapNotNull(::createScreenshotModel)
    }

    override fun createScreenshotModel(image: Image): GameInfoScreenshotModel? {
        val screenshotUrl = igdbImageUrlFactory.createUrl(
            image,
            IgdbImageUrlFactory.Config(IgdbImageSize.MEDIUM_SCREENSHOT),
        ) ?: return null

        return GameInfoScreenshotModel(
            id = image.id,
            url = screenshotUrl,
        )
    }
}
