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

package com.paulrybitskyi.gamedge.igdb.api.extractors

import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.CompositeErrorMessageExtractor
import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.IgdbErrorMessageExtractor
import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.TwitchErrorMessageExtractor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class CompositeErrorMessageExtractorTest {


    private lateinit var SUT: CompositeErrorMessageExtractor


    @Before
    fun setup() {
        SUT = CompositeErrorMessageExtractor(
            setOf(
                TwitchErrorMessageExtractor(),
                IgdbErrorMessageExtractor()
            )
        )
    }


    @Test
    fun `Extracts twitch error message from composite extractor successfully`() {
        val responseBody = """
            {
                "status":403,
                "message": "invalid client secret"
            }
        """.trimIndent()

        assertThat(SUT.extract(responseBody)).isEqualTo("invalid client secret")
    }


    @Test
    fun `Extracts igdb error message from composite extractor successfully`() {
        val responseBody = """
            [
              {
                "title": "Syntax Error",
                "status": 400,
                "cause": "Missing `;` at end of query"
              }
            ]
        """.trimIndent()

        assertThat(SUT.extract(responseBody)).isEqualTo("Missing `;` at end of query")
    }


    @Test
    fun `Returns unknown error if composite extractor fails to extract message`() {
        val responseBody = """
            {
                "status":403,
                "error_message": "invalid client secret"
            }
        """.trimIndent()

        assertThat(SUT.extract(responseBody)).isEqualTo("Unknown Error: $responseBody")
    }


}