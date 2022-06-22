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

import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.gamespot.api.articles.datastores.ArticlePublicationDateMapper
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.time.format.DateTimeParseException

internal class ArticlePublicationDateMapperTest {

    private lateinit var SUT: ArticlePublicationDateMapper

    @Before
    fun setup() {
        SUT = ArticlePublicationDateMapper()
    }

    @Test
    fun `Maps date successfully`() {
        assertThat(SUT.mapToTimestamp("2020-03-02 14:30:16")).isEqualTo(1583188216000L)
    }

    @Test
    fun `Throws exception when providing empty date`() {
        assertThrows(DateTimeParseException::class.java) {
            SUT.mapToTimestamp("")
        }
    }

    @Test
    fun `Throws exception when providing blank date`() {
        assertThrows(DateTimeParseException::class.java) {
            SUT.mapToTimestamp("   ")
        }
    }
}
