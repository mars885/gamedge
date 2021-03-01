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

package com.paulrybitskyi.gamedge.igdb.api

import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.CompositeErrorMessageExtractor
import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.IgdbErrorMessageExtractor
import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.TwitchErrorMessageExtractor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class ErrorExtractorsTest {


    private lateinit var twitchErrorExtractor: TwitchErrorMessageExtractor
    private lateinit var igdbErrorExtractor: IgdbErrorMessageExtractor
    private lateinit var compositeErrorExtractor: CompositeErrorMessageExtractor


    @Before
    fun setup() {
        twitchErrorExtractor = TwitchErrorMessageExtractor()
        igdbErrorExtractor = IgdbErrorMessageExtractor()
        compositeErrorExtractor = CompositeErrorMessageExtractor(
            setOf(
                twitchErrorExtractor,
                igdbErrorExtractor
            )
        )
    }


    @Test
    fun `Extracts twitch error message successfully`() {
        val responseBody = """
            {
                "status":403,
                "message": "invalid client secret"
            }
        """.trimIndent()

        assertEquals(
            "invalid client secret",
            twitchErrorExtractor.extract(responseBody)
        )
    }


    @Test(expected = IllegalStateException::class)
    fun `Throws exception when twitch response body is not json`() {
        twitchErrorExtractor.extract("hello there")
    }


    @Test(expected = IllegalStateException::class)
    fun `Throws exception when twitch response body does not have message field`() {
        val responseBody = """
            {
                "status":403,
                "error": "invalid client secret"
            }
        """.trimIndent()

        twitchErrorExtractor.extract(responseBody)
    }


    @Test
    fun `Extracts igdb error message successfully`() {
        val responseBody = """
            [
              {
                "title": "Syntax Error",
                "status": 400,
                "cause": "Missing `;` at end of query"
              }
            ]
        """.trimIndent()

        assertEquals(
            "Missing `;` at end of query",
            igdbErrorExtractor.extract(responseBody)
        )
    }


    @Test(expected = IllegalStateException::class)
    fun `Throws exception when igdb response body is not json`() {
        igdbErrorExtractor.extract("hello there")
    }


    @Test(expected = IllegalStateException::class)
    fun `Throws exception when igdb response body does not have message field`() {
        val responseBody = """
            [
              {
                "title": "Syntax Error",
                "status": 400
              }
            ]
        """.trimIndent()

        igdbErrorExtractor.extract(responseBody)
    }


    @Test
    fun `Extracts twitch error message from composite extractor successfully`() {
        val responseBody = """
            {
                "status":403,
                "message": "invalid client secret"
            }
        """.trimIndent()

        assertEquals(
            "invalid client secret",
            compositeErrorExtractor.extract(responseBody)
        )
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

        assertEquals(
            "Missing `;` at end of query",
            compositeErrorExtractor.extract(responseBody)
        )
    }


    @Test
    fun `Returns unknown error if composite extractor fails to extract message`() {
        val responseBody = """
            {
                "status":403,
                "error_message": "invalid client secret"
            }
        """.trimIndent()

        assertEquals(
            "Unknown Error: $responseBody",
            compositeErrorExtractor.extract(responseBody)
        )
    }


}