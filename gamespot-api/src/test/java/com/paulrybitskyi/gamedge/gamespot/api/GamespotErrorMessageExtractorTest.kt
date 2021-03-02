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

import com.paulrybitskyi.gamedge.gamespot.api.commons.GamespotErrorMessageExtractor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class GamespotErrorMessageExtractorTest {


    private lateinit var errorExtractor: GamespotErrorMessageExtractor


    @Before
    fun setup() {
        errorExtractor = GamespotErrorMessageExtractor()
    }


    @Test
    fun `Extracts error message successfully`() {
        val responseBody = """
            {
              "error": "Invalid API Key",
              "limit": 0,
              "offset": 0,
              "number_of_page_results": 0,
              "number_of_total_results": 0,
              "status_code": 100,
              "results": []
            }
        """.trimIndent()

        assertEquals(
            "Invalid API Key",
            errorExtractor.extract(responseBody)
        )
    }


    @Test
    fun `Returns unknown error's message when response body is not json`() {
        assertNotEquals("", errorExtractor.extract("hello there"))
    }


    @Test
    fun `Returns unknown error's message when response body does not have message field`() {
        val responseBody = """
            {
              "limit": 0,
              "offset": 0,
              "number_of_page_results": 0,
              "number_of_total_results": 0,
              "status_code": 100,
              "results": []
            }
        """.trimIndent()

        assertNotEquals("", errorExtractor.extract(responseBody))
    }


}