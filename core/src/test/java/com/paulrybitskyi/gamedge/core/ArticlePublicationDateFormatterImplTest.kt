/*
 * Copyright 2021 Paul Rybitskyi, oss@paulrybitskyi.com
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

import com.google.common.truth.Truth.assertThat
import com.paulrybitskyi.gamedge.common.testing.FakeLocaleProvider
import com.paulrybitskyi.gamedge.core.formatters.ArticlePublicationDateFormatterImpl
import com.paulrybitskyi.gamedge.core.formatters.RelativeDateFormatter
import com.paulrybitskyi.gamedge.core.providers.TimeFormat
import com.paulrybitskyi.gamedge.core.providers.TimeFormatProvider
import com.paulrybitskyi.gamedge.core.providers.TimeProvider
import com.paulrybitskyi.gamedge.core.utils.toMillis
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId

private const val RELATIVE_DATE = "relative_date"

internal class ArticlePublicationDateFormatterImplTest {

    @MockK private lateinit var relativeDateFormatter: RelativeDateFormatter
    @MockK private lateinit var timeProvider: TimeProvider
    @MockK private lateinit var timeFormatProvider: TimeFormatProvider

    private lateinit var SUT: ArticlePublicationDateFormatterImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = ArticlePublicationDateFormatterImpl(
            relativeDateFormatter = relativeDateFormatter,
            timeProvider = timeProvider,
            timeFormatProvider = timeFormatProvider,
            localeProvider = FakeLocaleProvider(),
        )

        every { relativeDateFormatter.formatRelativeDate(any()) } returns RELATIVE_DATE
    }

    @Test
    fun `Formats pub date in relative format`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val timestamp = currentTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatPublicationDate(timestamp)).isEqualTo(RELATIVE_DATE)
    }

    @Test
    fun `Formats pub date in absolute 24 hours format without year`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val timestamp = currentTime.minusDays(2).toMillis()

        every { timeProvider.getCurrentDateTime() } returns currentTime
        every { timeFormatProvider.getTimeFormat() } returns TimeFormat.TWENTY_FOUR_HOURS

        assertThat(SUT.formatPublicationDate(timestamp)).isEqualTo("Mar 2, 1:15")
    }

    @Test
    fun `Formats pub date in absolute 24 hours format with year`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val timestamp = currentTime.minusYears(1).toMillis()

        every { timeProvider.getCurrentDateTime() } returns currentTime
        every { timeFormatProvider.getTimeFormat() } returns TimeFormat.TWENTY_FOUR_HOURS

        assertThat(SUT.formatPublicationDate(timestamp)).isEqualTo("Mar 4, 2020, 1:15")
    }

    @Test
    fun `Formats pub date in absolute 12 hours format without year`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val timestamp = currentTime.minusDays(2).toMillis()

        every { timeProvider.getCurrentDateTime() } returns currentTime
        every { timeFormatProvider.getTimeFormat() } returns TimeFormat.TWELVE_HOURS

        assertThat(SUT.formatPublicationDate(timestamp)).isEqualTo("Mar 2, 1:15 AM")
    }

    @Test
    fun `Formats pub date in absolute 12 hours format with year`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val timestamp = currentTime.minusYears(1).toMillis()

        every { timeProvider.getCurrentDateTime() } returns currentTime
        every { timeFormatProvider.getTimeFormat() } returns TimeFormat.TWELVE_HOURS

        assertThat(SUT.formatPublicationDate(timestamp)).isEqualTo("Mar 4, 2020, 1:15 AM")
    }
}
