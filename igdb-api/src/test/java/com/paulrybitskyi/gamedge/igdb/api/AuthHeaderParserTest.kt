/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

import com.paulrybitskyi.gamedge.igdb.api.auth.AuthHeaderParser
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.AuthorizationType
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

internal class AuthHeaderParserTest {

    private companion object {
        const val TOKEN = "token"
    }

    private lateinit var SUT: AuthHeaderParser

    @Before
    fun setup() {
        SUT = AuthHeaderParser()
    }

    @Test
    fun `Returns null when header string is empty`() {
        assertThat(SUT.parse("")).isNull()
    }

    @Test
    fun `Returns result with basic auth type`() {
        val expectedAuthType = AuthorizationType.BASIC
        val actualResult = SUT.parse("Basic $TOKEN")

        assertThat(actualResult).isNotNull
        assertThat(actualResult!!.type).isEqualTo(expectedAuthType)
        assertThat(actualResult.token).isEqualTo(TOKEN)
    }

    @Test
    fun `Returns result with bearer auth type`() {
        val expectedAuthType = AuthorizationType.BEARER
        val actualResult = SUT.parse("Bearer $TOKEN")

        assertThat(actualResult).isNotNull
        assertThat(actualResult!!.type).isEqualTo(expectedAuthType)
        assertThat(actualResult.token).isEqualTo(TOKEN)
    }
}
