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
import com.paulrybitskyi.gamedge.gamespot.api.common.GamespotErrorMessageExtractor
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

internal class GamespotErrorMessageExtractorTest {

    private lateinit var SUT: GamespotErrorMessageExtractor

    @Before
    fun setup() {
        SUT = GamespotErrorMessageExtractor(Json)
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

        assertThat(SUT.extract(responseBody)).isEqualTo("Invalid API Key")
    }

    @Test
    fun `Returns unknown error's message when response body is not json`() {
        assertThat(SUT.extract("hello there")).isNotEmpty()
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

        assertThat(SUT.extract(responseBody)).isNotEmpty()
    }
}
