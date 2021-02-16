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

package com.paulrybitskyi.gamedge.core.providers

import com.paulrybitskyi.gamedge.core.IgdbImageSize
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface ImageViewerGameUrlProvider {

    fun getArtworkImageUrls(game: Game): List<String>

    fun getCoverImageUrl(game: Game): String?

    fun getScreenshotImageUrls(game: Game): List<String>

}


@BindType
internal class ImageViewerGameUrlProviderImpl @Inject constructor(
    private val igdbImageUrlBuilder: IgdbImageUrlBuilder
) : ImageViewerGameUrlProvider {


    private companion object {

        private val IMAGE_SIZE = IgdbImageSize.HD

    }


    override fun getArtworkImageUrls(game: Game): List<String> {
        return igdbImageUrlBuilder
            .buildUrls(game.artworks, IgdbImageUrlBuilder.Config(IMAGE_SIZE))
    }


    override fun getCoverImageUrl(game: Game): String? {
        return game.cover?.let { cover ->
            igdbImageUrlBuilder.buildUrl(cover, IgdbImageUrlBuilder.Config(IMAGE_SIZE))
        }
    }


    override fun getScreenshotImageUrls(game: Game): List<String> {
        return igdbImageUrlBuilder.buildUrls(
            game.screenshots,
            IgdbImageUrlBuilder.Config(IMAGE_SIZE)
        )
    }


}