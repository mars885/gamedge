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

import com.paulrybitskyi.gamedge.core.factories.YoutubeMediaUrlFactoryImpl
import com.paulrybitskyi.gamedge.core.factories.YoutubeThumbnailSize
import com.paulrybitskyi.gamedge.domain.games.DomainVideo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val DOMAIN_VIDEO = DomainVideo(
    id = "id",
    name = "name"
)


internal class YoutubeMediaUrlFactoryImplTest {


    private lateinit var SUT: YoutubeMediaUrlFactoryImpl


    @Before
    fun setup() {
        SUT = YoutubeMediaUrlFactoryImpl()
    }


    @Test
    fun `Creates thumbnail image urls correctly`() {
        for(ytThumbnailSize in YoutubeThumbnailSize.values()) {
            assertEquals(
                "https://img.youtube.com/vi/${DOMAIN_VIDEO.id}/${ytThumbnailSize.rawSize}.jpg",
                SUT.createThumbnailUrl(DOMAIN_VIDEO, ytThumbnailSize)
            )
        }
    }


    @Test
    fun `Returns null when video id is blank while creating thumbnail image url`() {
        assertNull(SUT.createThumbnailUrl(DOMAIN_VIDEO.copy(id = "   "), YoutubeThumbnailSize.MEDIUM))
    }


    @Test
    fun `Creates video urls correctly`() {
        for(ytThumbnailSize in YoutubeThumbnailSize.values()) {
            assertEquals(
                "https://youtu.be/${DOMAIN_VIDEO.id}",
                SUT.createVideoUrl(DOMAIN_VIDEO)
            )
        }
    }


    @Test
    fun `Returns null when video id is blank while creating video url`() {
        assertNull(SUT.createVideoUrl(DOMAIN_VIDEO.copy(id = "   ")))
    }


}