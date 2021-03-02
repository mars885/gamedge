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

package com.paulrybitskyi.gamedge.gamespot.api

import com.paulrybitskyi.gamedge.gamespot.api.articles.datastores.ArticlePublicationDateMapper
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.format.DateTimeParseException

internal class ArticlePublicationDateMapperTest {


    private lateinit var dateMapper: ArticlePublicationDateMapper


    @Before
    fun setup() {
        dateMapper = ArticlePublicationDateMapper()
    }


    @Test
    fun `Maps date successfully`() {
        assertEquals(
            1583188216000L,
            dateMapper.mapToTimestamp("2020-03-02 14:30:16")
        )
    }


    @Test(expected = DateTimeParseException::class)
    fun `Throws exception when providing empty date`() {
        dateMapper.mapToTimestamp("")
    }


    @Test(expected = DateTimeParseException::class)
    fun `Throws exception when providing blank date`() {
        dateMapper.mapToTimestamp("   ")
    }


}