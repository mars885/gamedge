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

package com.paulrybitskyi.gamedge.core

import com.paulrybitskyi.gamedge.core.formatters.ArticlePublicationDateFormatterImpl
import com.paulrybitskyi.gamedge.core.formatters.RelativeDateFormatter
import com.paulrybitskyi.gamedge.core.providers.TimeFormat
import com.paulrybitskyi.gamedge.core.providers.TimeFormatProvider
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit


private const val RELATIVE_DATE = "relative_date"


internal class ArticlePublicationDateFormatterImplTest {


    private lateinit var timestampProvider: FakeTimestampProvider
    private lateinit var timeFormatProvider: FakeTimeFormatProvider
    private lateinit var SUT: ArticlePublicationDateFormatterImpl


    @Before
    fun setup() {
        timestampProvider = FakeTimestampProvider()
        timeFormatProvider = FakeTimeFormatProvider()
        SUT = ArticlePublicationDateFormatterImpl(
            relativeDateFormatter = FakeRelativeDateFormatter(),
            timestampProvider = timestampProvider,
            timeFormatProvider = timeFormatProvider
        )
    }


    @Test
    fun `Formats pub date in relative format`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            RELATIVE_DATE,
            SUT.formatPublicationDate(timestamp)
        )
    }


    @Test
    fun `Formats pub date in absolute 24 hours format without year`() {
        val originalTimestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val timestamp = (originalTimestamp - TimeUnit.DAYS.toMillis(2))

        timestampProvider.stubTimestamp = originalTimestamp
        timeFormatProvider.stubTimeFormat = TimeFormat.TWENTY_FOUR_HOURS

        assertEquals("Mar 2, 1:15", SUT.formatPublicationDate(timestamp))
    }


    @Test
    fun `Formats pub date in absolute 24 hours format with year`() {
        val originalTimestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val timestamp = (originalTimestamp - TimeUnit.DAYS.toMillis(365))

        timestampProvider.stubTimestamp = originalTimestamp
        timeFormatProvider.stubTimeFormat = TimeFormat.TWENTY_FOUR_HOURS

        assertEquals("Mar 4, 2020, 1:15", SUT.formatPublicationDate(timestamp))
    }


    @Test
    fun `Formats pub date in absolute 12 hours format without year`() {
        val originalTimestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val timestamp = (originalTimestamp - TimeUnit.DAYS.toMillis(2))

        timestampProvider.stubTimestamp = originalTimestamp
        timeFormatProvider.stubTimeFormat = TimeFormat.TWELVE_HOURS

        assertEquals("Mar 2, 1:15 AM", SUT.formatPublicationDate(timestamp))
    }


    @Test
    fun `Formats pub date in absolute 12 hours format with year`() {
        val originalTimestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val timestamp = (originalTimestamp - TimeUnit.DAYS.toMillis(365))

        timestampProvider.stubTimestamp = originalTimestamp
        timeFormatProvider.stubTimeFormat = TimeFormat.TWELVE_HOURS

        assertEquals("Mar 4, 2020, 1:15 AM", SUT.formatPublicationDate(timestamp))
    }


    private class FakeRelativeDateFormatter : RelativeDateFormatter {

        override fun formatRelativeDate(dateTime: LocalDateTime): String {
            return RELATIVE_DATE
        }

    }


    private class FakeTimestampProvider : TimestampProvider {

        var stubTimestamp = 0L

        override fun getUnixTimestamp(timeUnit: TimeUnit): Long {
            return stubTimestamp
        }

    }


    private class FakeTimeFormatProvider : TimeFormatProvider {

        var stubTimeFormat = TimeFormat.TWELVE_HOURS

        override fun getTimeFormat(): TimeFormat {
            return stubTimeFormat
        }

    }



}