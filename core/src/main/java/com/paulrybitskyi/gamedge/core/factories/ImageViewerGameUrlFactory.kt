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

import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


interface ImageViewerGameUrlFactory {

    fun createCoverImageUrl(game: Game): String?

    fun createArtworkImageUrls(game: Game): List<String>

    fun createScreenshotImageUrls(game: Game): List<String>

}


@BindType
internal class ImageViewerGameUrlFactoryImpl @Inject constructor(
    private val igdbImageUrlFactory: IgdbImageUrlFactory
) : ImageViewerGameUrlFactory {


    private companion object {

        private val IMAGE_SIZE = IgdbImageSize.HD

    }


    override fun createCoverImageUrl(game: Game): String? {
        return game.cover?.let { cover ->
            igdbImageUrlFactory.createUrl(cover, IgdbImageUrlFactory.Config(IMAGE_SIZE))
        }
    }


    override fun createArtworkImageUrls(game: Game): List<String> {
        return igdbImageUrlFactory
            .createUrls(game.artworks, IgdbImageUrlFactory.Config(IMAGE_SIZE))
    }


    override fun createScreenshotImageUrls(game: Game): List<String> {
        return igdbImageUrlFactory.createUrls(
            game.screenshots,
            IgdbImageUrlFactory.Config(IMAGE_SIZE)
        )
    }


}
