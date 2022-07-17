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

import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.igdb.api.common.errorextractors.TwitchErrorMessageExtractor
import kotlinx.serialization.json.Json
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

internal class TwitchErrorMessageExtractorTest {

    private lateinit var SUT: TwitchErrorMessageExtractor

    @Before
    fun setup() {
        SUT = TwitchErrorMessageExtractor(Json)
    }

    @Test
    fun `Extracts twitch error message successfully`() {
        val responseBody = """
            {
                "status":403,
                "message": "invalid client secret"
            }
        """.trimIndent()

        assertThat(SUT.extract(responseBody)).isEqualTo("invalid client secret")
    }

    @Test
    fun `Throws exception when twitch response body is not json`() {
        assertThrows(IllegalStateException::class.java) {
            SUT.extract("hello there")
        }
    }

    @Test
    fun `Throws exception when twitch response body does not have message field`() {
        val responseBody = """
            {
                "status":403,
                "error": "invalid client secret"
            }
        """.trimIndent()

        assertThrows(IllegalStateException::class.java) {
            SUT.extract(responseBody)
        }
    }
}
