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
import com.paulrybitskyi.gamedge.core.providers.TimeProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month

internal class RelativeDateFormatterImplTest {


    @MockK private lateinit var timeProvider: TimeProvider

    private lateinit var stringProvider: FakeStringProvider
    private lateinit var SUT: RelativeDateFormatterImpl


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        stringProvider = FakeStringProvider()
        SUT = RelativeDateFormatterImpl(
            timeProvider = timeProvider,
            stringProvider = stringProvider
        )
    }


    @Test
    fun `Formats future date with year difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val yearDiff = 2L
        val futureTime = currentTime.plusYears(yearDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(futureTime)).isEqualTo("in $yearDiff years")
    }


    @Test
    fun `Formats future date with month difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val monthDiff = 3L
        val futureTime = currentTime.plusMonths(monthDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(futureTime)).isEqualTo("in $monthDiff months")
    }


    @Test
    fun `Formats future date with day difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val dayDiff = 15L
        val futureTime = currentTime.plusDays(dayDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(futureTime)).isEqualTo("in $dayDiff days")
    }


    @Test
    fun `Formats future date with hour difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val hourDiff = 5L
        val futureTime = currentTime.plusHours(hourDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(futureTime)).isEqualTo("in $hourDiff hours")
    }


    @Test
    fun `Formats future date with minute difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val minuteDiff = 5L
        val futureTime = currentTime.plusMinutes(minuteDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(futureTime)).isEqualTo("in $minuteDiff minutes")
    }


    @Test
    fun `Formats future date with second difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val secondDiff = 5L
        val futureTime = currentTime.plusSeconds(secondDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(futureTime)).isEqualTo("in $secondDiff seconds")
    }


    @Test
    fun `Formats past date with year difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val yearDiff = 1L
        val pastTime = currentTime.minusYears(yearDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(pastTime)).isEqualTo("$yearDiff years ago")
    }


    @Test
    fun `Formats past date with month difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val monthDiff = 3L
        val pastTime = currentTime.minusMonths(monthDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(pastTime)).isEqualTo("$monthDiff months ago")
    }


    @Test
    fun `Formats past date with day difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val dayDiff = 15L
        val pastTime = currentTime.minusDays(dayDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(pastTime)).isEqualTo("$dayDiff days ago")
    }


    @Test
    fun `Formats past date with hour difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val hourDiff = 5L
        val pastTime = currentTime.minusHours(hourDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(pastTime)).isEqualTo("$hourDiff hours ago")
    }


    @Test
    fun `Formats past date with minute difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val minuteDiff = 5L
        val pastTime = currentTime.minusMinutes(minuteDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(pastTime)).isEqualTo("$minuteDiff minutes ago")
    }


    @Test
    fun `Formats past date with second difference correctly`() {
        val currentTime = LocalDateTime.of(2021, Month.MARCH, 4, 1, 15) // March 4th, 2021 at 1:15 AM
        val secondDiff = 5L
        val pastTime = currentTime.minusSeconds(secondDiff)

        every { timeProvider.getCurrentDateTime() } returns currentTime

        assertThat(SUT.formatRelativeDate(pastTime)).isEqualTo("$secondDiff seconds ago")
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
