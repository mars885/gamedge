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

import com.paulrybitskyi.gamedge.core.formatters.RelativeDateFormatterImpl
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

internal class RelativeDateFormatterTest {


    private lateinit var timestampProvider: FakeTimestampProvider
    private lateinit var stringProvider: FakeStringProvider
    private lateinit var SUT: RelativeDateFormatterImpl


    @Before
    fun setup() {
        timestampProvider = FakeTimestampProvider()
        stringProvider = FakeStringProvider()
        SUT = RelativeDateFormatterImpl(
            timestampProvider = timestampProvider,
            stringProvider = stringProvider
        )
    }


    @Test
    fun `Formats future date with year difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val yearDiff = 2L
        val futureTimestamp = (timestamp + TimeUnit.DAYS.toMillis(365 * yearDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "in $yearDiff years",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats future date with month difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val monthDiff = 3L
        val futureTimestamp = (timestamp + TimeUnit.DAYS.toMillis(31 * monthDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "in $monthDiff months",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats future date with day difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val dayDiff = 15L
        val futureTimestamp = (timestamp + TimeUnit.DAYS.toMillis(dayDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "in $dayDiff days",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats future date with hour difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val hourDiff = 5L
        val futureTimestamp = (timestamp + TimeUnit.HOURS.toMillis(hourDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "in $hourDiff hours",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats future date with minute difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val minuteDiff = 5L
        val futureTimestamp = (timestamp + TimeUnit.MINUTES.toMillis(minuteDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "in $minuteDiff minutes",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats future date with second difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val secondDiff = 5L
        val futureTimestamp = (timestamp + TimeUnit.SECONDS.toMillis(secondDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "in $secondDiff seconds",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats past date with year difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val yearDiff = 1L
        val futureTimestamp = (timestamp - TimeUnit.DAYS.toMillis(365 * yearDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "$yearDiff years ago",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats past date with month difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val monthDiff = 3L
        val futureTimestamp = (timestamp - TimeUnit.DAYS.toMillis(31 * monthDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "$monthDiff months ago",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats past date with day difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val dayDiff = 15L
        val futureTimestamp = (timestamp - TimeUnit.DAYS.toMillis(dayDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "$dayDiff days ago",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats past date with hour difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val hourDiff = 5L
        val futureTimestamp = (timestamp - TimeUnit.HOURS.toMillis(hourDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "$hourDiff hours ago",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats past date with minute difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val minuteDiff = 5L
        val futureTimestamp = (timestamp - TimeUnit.MINUTES.toMillis(minuteDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "$minuteDiff minutes ago",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    @Test
    fun `Formats past date with second difference correctly`() {
        val timestamp = 1614813308317L  // March 4th, 2021 at 1:15 AM
        val secondDiff = 5L
        val futureTimestamp = (timestamp - TimeUnit.SECONDS.toMillis(secondDiff))

        timestampProvider.stubTimestamp = timestamp

        assertEquals(
            "$secondDiff seconds ago",
            SUT.formatRelativeDate(futureTimestamp.toLocalDateTime())
        )
    }


    private fun Long.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(this),
            ZoneId.systemDefault()
        )
    }


    private class FakeTimestampProvider : TimestampProvider {

        var stubTimestamp = 0L

        override fun getUnixTimestamp(timeUnit: TimeUnit): Long {
            return stubTimestamp
        }

    }


    private class FakeStringProvider : StringProvider {

        override fun getString(id: Int, vararg args: Any): String {
            return ""
        }

        override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String {
            return when(id) {
                R.plurals.future_relative_timestamp_year -> "in $quantity years"
                R.plurals.future_relative_timestamp_month -> "in $quantity months"
                R.plurals.future_relative_timestamp_day -> "in $quantity days"
                R.plurals.future_relative_timestamp_hour -> "in $quantity hours"
                R.plurals.future_relative_timestamp_minute -> "in $quantity minutes"
                R.plurals.future_relative_timestamp_second -> "in $quantity seconds"

                R.plurals.past_relative_timestamp_year -> "$quantity years ago"
                R.plurals.past_relative_timestamp_month -> "$quantity months ago"
                R.plurals.past_relative_timestamp_day -> "$quantity days ago"
                R.plurals.past_relative_timestamp_hour -> "$quantity hours ago"
                R.plurals.past_relative_timestamp_minute -> "$quantity minutes ago"
                R.plurals.past_relative_timestamp_second -> "$quantity seconds ago"

                else -> throw IllegalStateException()
            }
        }

    }


}