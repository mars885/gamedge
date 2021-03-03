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

package com.paulrybitskyi.gamedge.core

import com.paulrybitskyi.gamedge.core.factories.IgdbImageExtension
import com.paulrybitskyi.gamedge.core.factories.IgdbImageSize
import com.paulrybitskyi.gamedge.core.factories.IgdbImageUrlFactory
import com.paulrybitskyi.gamedge.core.factories.IgdbImageUrlFactoryImpl
import com.paulrybitskyi.gamedge.domain.games.entities.Image
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val IMAGE = Image(
    id = "id",
    width = 500,
    height = 1000
)


internal class IgdbImageUrlFactoryTest {


    private lateinit var SUT: IgdbImageUrlFactoryImpl


    @Before
    fun setup() {
        SUT = IgdbImageUrlFactoryImpl()
    }


    @Test
    fun `Creates image urls without retina size correctly`() {
        for(imageExtension in IgdbImageExtension.values()) {
            for(igdbImageSize in IgdbImageSize.values()) {
                val config = IgdbImageUrlFactory.Config(
                    size = igdbImageSize,
                    extension = imageExtension
                )

                val expectedUrl = String.format(
                    "https://images.igdb.com/igdb/image/upload/t_%s/%s.%s",
                    config.size.rawSize,
                    IMAGE.id,
                    config.extension.rawExtension
                )

                assertEquals(expectedUrl, SUT.createUrl(IMAGE, config))
            }
        }
    }


    @Test
    fun `Creates image urls with retina size correctly`() {
        for(imageExtension in IgdbImageExtension.values()) {
            for(igdbImageSize in IgdbImageSize.values()) {
                val config = IgdbImageUrlFactory.Config(
                    size = igdbImageSize,
                    extension = imageExtension,
                    withRetinaSize = true
                )

                val expectedUrl = String.format(
                    "https://images.igdb.com/igdb/image/upload/t_%s/%s.%s",
                    (config.size.rawSize + "_2x"),
                    IMAGE.id,
                    config.extension.rawExtension
                )

                assertEquals(expectedUrl, SUT.createUrl(IMAGE, config))
            }
        }
    }


    @Test
    fun `Returns null when image id is blank while creating image url`() {
        val config = IgdbImageUrlFactory.Config(
            size = IgdbImageSize.BIG_COVER
        )

        assertNull(SUT.createUrl(IMAGE.copy(id = "   "), config))
    }


}