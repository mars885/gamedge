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

import com.paulrybitskyi.gamedge.igdb.api.commons.errorextractors.IgdbErrorMessageExtractor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class IgdbErrorMessageExtractorTest {


    private lateinit var SUT: IgdbErrorMessageExtractor


    @Before
    fun setup() {
        SUT = IgdbErrorMessageExtractor()
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
            SUT.extract(responseBody)
        )
    }


    @Test(expected = IllegalStateException::class)
    fun `Throws exception when igdb response body is not json`() {
        SUT.extract("hello there")
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

        SUT.extract(responseBody)
    }


}